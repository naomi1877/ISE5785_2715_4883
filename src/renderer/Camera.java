package renderer;

import primitives.*;
import primitives.Vector;
import scene.Scene;

import java.util.*;
import java.util.stream.IntStream;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * The Camera class represents a camera in a 3D scene.
 * It includes properties such as position, direction vectors, and viewport size.
 */
public class Camera implements Cloneable {
    /**
     * The image writer used to create the image.
     */
    private static ImageWriter imageWriter;
    /**
     * The "to" direction vector of the camera.
     */
    private Vector vTo;
    /**
     * The "up" direction vector of the camera.
     */
    private Vector vUp;
    /**
     * The "right" direction vector of the camera.
     */
    private Vector vRight;
    /**
     * The position of the camera.
     */
    private Point p0;
    /**
     * The center point of the viewport.
     */
    private Point pCenter;
    /**
     * The width of the viewport.
     */
    private double width = 0.0;
    /**
     * The height of the viewport.
     */
    private double height = 0.0;
    /**
     * The distance from the camera to the viewport.
     */
    private double distance = 0.0;
    /**
     * The horizontal resolution of the image.
     */
    private int nX = 1;
    /**
     * The vertical resolution of the image.
     */
    private int nY = 1;
    /**
     * The ray tracer used to trace rays in the scene.
     */
    private RayTracerBase rayTracer;
    /**
     * Default constructor for the Camera class.
     */

    private boolean AA_FLAG = false;

    private boolean ASS_FLAG = false;

    private int AA_GRID_SIZE = 9;

    /** Amount of threads to use fore rendering image by the camera */
    private int threadsCount = 0;
    /**
     * Amount of threads to spare for Java VM threads:<br>
     * Spare threads if trying to use all the cores
     */
    private static final int SPARE_THREADS = 2;
    /**
     * Debug print interval in seconds (for progress percentage)<br>
     * if it is zero - there is no progress output
     */
    private double printInterval = 0;
    /**
     * Pixel manager for supporting:
     * <ul>
     * <li>multi-threading</li>
     * <li>debug print of progress percentage in Console window/tab</li>
     * </ul>
     */
    private PixelManager pixelManager;

    private Camera() {}

    /**
     * Returns a new instance of the {@code Builder} for constructing a {@code Camera} object.
     *
     * @return a new Builder instance
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    /**
     * Returns the forward direction vector of the camera (vTo).
     *
     * @return the vector pointing in the direction the camera is facing
     */
    public Vector getVTo() {
        return vTo;
    }

    /**
     * Returns the upward direction vector of the camera (vUp).
     *
     * @return the vector pointing upward relative to the camera
     */
    public Vector getVUp() {
        return vUp;
    }

    /**
     * Returns the rightward direction vector of the camera (vRight),
     * which is orthogonal to both vTo and vUp.
     *
     * @return the vector pointing to the right side of the camera
     */
    public Vector getVRight() {
        return vRight;
    }

    /**
     * Returns the position of the camera in 3D space (p0).
     *
     * @return the origin point of the camera
     */
    public Point getP0() {
        return p0;
    }

    /**
     * Returns the center point of the view plane (pCenter),
     * which is located at a fixed distance from the camera in the direction of vTo.
     *
     * @return the center point of the view plane
     */
    public Point getPCenter() {
        return pCenter;
    }

    /**
     * Returns the width of the view plane.
     *
     * @return the width of the view plane
     */
    public double getWidth() {
        return width;
    }

    /**
     * Returns the height of the view plane.
     *
     * @return the height of the view plane
     */
    public double getHeight() {
        return height;
    }

    /**
     * Returns the distance from the camera to the view plane.
     *
     * @return the distance from the camera to the view plane
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Constructs a ray through a specific pixel in the viewport
     *
     * @param nX the number of pixels in the X direction
     * @param nY the number of pixels in the Y direction
     * @param j  the column index of the pixel
     * @param i  the row index of the pixel
     * @return a list of points representing the ray
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
        //calculate the size of each pixel
        double pixelWidth = width / nX;
        double pixelHeight = height / nY;

        //calculate the center of the view plane
        Point pCenter = p0.add(vTo.scale(distance));


        //calculate the center of the pixel
        double xJ = (j - (nX - 1) / 2.0) * pixelWidth;
        double yI = -1 * (i - (nY - 1) / 2.0) * pixelHeight;

        Point pIJ = pCenter;
        //calculate the current pixel
        if (!isZero(yI)) pIJ = pIJ.add(vUp.scale(yI));
        if (!isZero(xJ)) pIJ = pIJ.add(vRight.scale(xJ));

        //return the ray from the camera position to the pixel center
        return new Ray(p0, pIJ.subtract(p0).normalize());//normalize or not?
    }

    /**
     * Casts a ray through a specific pixel in the viewport and writes the color to the image.
     *
     * @param nx the number of pixels in the X direction
     * @param ny the number of pixels in the Y direction
     * @param i  the row index of the pixel
     * @param j  the column index of the pixel
     */
    private void castRay(int nx, int ny, int i, int j) {
        Ray ray = constructRay(nx, ny, j, i);
        Color color = rayTracer.traceRay(ray);
        imageWriter.writePixel(j, i, color);
        // If multithreading is enabled, notify the pixel manager that the pixel is done
        if (pixelManager != null)
            pixelManager.pixelDone();
    }

    /**
     *
     * @param nX
     * @param nY
     * @param j
     * @param i
     * @return
     */
    public List<Ray> constructRaysJ(int nX, int nY, int j, int i) {

        List<Ray> rays = new ArrayList<>();
        Random random = new Random();
        double Ry = height / nY;
        double Rx = width / nX;
        double stepY = Ry / AA_GRID_SIZE;
        double stepX = Rx / AA_GRID_SIZE;

        for (int subI = 0; subI < AA_GRID_SIZE; subI++) {
            for (int subJ = 0; subJ < AA_GRID_SIZE; subJ++) {
                double jitterY = random.nextDouble() % AA_GRID_SIZE; // Random offset in Y direction
                double jitterX = random.nextDouble() % AA_GRID_SIZE; // Random offset in X direction

                double offsetI = (subI + jitterY) * stepY;
                double offsetJ = (subJ + jitterX) * stepX;
                double Yi = -(i - (nY - 1) / 2d) * Ry + offsetI;
                double Xj = (j - (nX - 1) / 2d) * Rx + offsetJ;
                Point pIJ = p0;
                if (!isZero(Xj)) pIJ = pIJ.add(vRight.scale(Xj));
                if (!isZero(Yi)) pIJ = pIJ.add(vUp.scale(Yi));
                pIJ = pIJ.add(vTo.scale(distance)); // pIJ is the center of the pixel in the view plane

                Ray ray = new Ray(p0, pIJ.subtract(p0).normalize());
                rays.add(ray);
            }
        }
        return rays;

    }

    /**
     * Casts multiple rays through a single pixel using jittered or super-sampling,
     * averages their colors, and writes the final color to the image.
     * through slightly different sub-pixel locations (e.g., using jittering or grid sampling).
     * It collects the color for each ray, averages the result, and sets the pixel color accordingly.</p>
     *
     * @param nX total number of pixels in the image width (columns)
     * @param nY total number of pixels in the image height (rows)
     * @param j the pixel's horizontal index (column number, X-axis)
     * @param i the pixel's vertical index (row number, Y-axis)
     */
    private void castRayJ(int nX, int nY, int j, int i) {

        List<Ray> rays = constructRaysJ(nX, nY, j, i);
        Color color = Color.BLACK;
        for (Ray ray : rays) {
            color = color.add(rayTracer.traceRay(ray));
        }
        color = color.scale(1d / rays.size());
        imageWriter.writePixel(j, i, color);
    }


    private void castRayASS(int nX, int nY, int j, int i) {
        double pixelWidth = width / nX;
        double pixelHeight = height / nY;

        // חישוב מרכז הפיקסל
        double xJ = (j - (nX - 1) / 2.0) * pixelWidth;
        double yI = -1 * (i - (nY - 1) / 2.0) * pixelHeight;

        Point pCenter = this.p0.add(vTo.scale(distance));
        if (!isZero(xJ)) pCenter = pCenter.add(vRight.scale(xJ));
        if (!isZero(yI)) pCenter = pCenter.add(vUp.scale(yI));

        Color color = adaptiveSuperSampling(pCenter, pixelWidth, pixelHeight, 4); // רמת עומק התחלתית
        imageWriter.writePixel(j, i, color);

        if (pixelManager != null)
            pixelManager.pixelDone();
    }


    /**
     * Renders the image by tracing rays through each pixel in the viewport.
     * @return the Camera object for method chaining
     */
    /**
     * Render the image
     *
     * @return the camera object
     */

    public Camera renderImage() {
        if (imageWriter == null || rayTracer == null)
            throw new IllegalArgumentException("Missing imageWriter or rayTracer");

        System.out.println("Multithreading mode: " +
                (threadsCount == 0 ? "Single thread" :
                        threadsCount == -1 ? "Parallel stream" :
                                threadsCount + " raw threads"));

        // Initialize the pixel manager if multithreading is enabled
        if (threadsCount != 0)
            pixelManager = new PixelManager(nY, nX, printInterval);

        // Calculate the center point of the viewport
        return switch (threadsCount) {
            case 0 -> renderImageNoThreads();   // no multithreading
            case -1 -> renderImageStream();     // parallel stream
            default -> renderImageRawThreads(); // raw threads
        };
    }


    /**
     * Prints a grid on the image.
     *
     * @param interval the interval between grid lines
     * @param color    the color of the grid lines
     * @return the Camera object for method chaining
     */
    public Camera printGrid(int interval, Color color) {
        for (int i = 0; i < imageWriter.nY(); i++) {
            for (int j = 0; j < imageWriter.nX(); j++) {
                if (i % interval == 0 || j % interval == 0) {
                    imageWriter.writePixel(j, i, color);
                }
            }
        }
        return this;
    }

    /**
     * Writes the image to a file.
     *
     * @param name the name of the image file
     */
    public void writeToImage(String name) {
        imageWriter.writeToImage(name);
    }
    /**
     * Render image using multi-threading by parallel streaming
     * @return the camera object itself
     */
    private Camera renderImageStream() {
        IntStream.range(0, nY).parallel()
                .forEach(i -> IntStream.range(0, nX).parallel()
                        .forEach(j -> {
                            if (ASS_FLAG)
                                castRayASS(nX, nY, j, i);
                            else if (AA_FLAG)
                                castRayJ(nX, nY, j, i);
                            else
                                castRay(nX, nY, j, i);

                        }));
        return this;
    }

    /**
     * Render image without multi-threading
     * @return the camera object itself
     */
    private Camera renderImageNoThreads() {
        for (int i = 0; i < nY; ++i)
            for (int j = 0; j < nX; ++j)
                if (ASS_FLAG)
                    castRayASS(nX, nY, j, i);
                else if (AA_FLAG)
                    castRayJ(nX, nY, j, i);
                else
                    castRay(nX, nY, j, i);

        return this;
    }

    /**
     * Render image using multi-threading by creating and running raw threads
     * @return the camera object itself
     */
    private Camera renderImageRawThreads() {
        var threads = new LinkedList<Thread>();
        for (int i = 0; i < threadsCount; i++) {
            threads.add(new Thread(() -> {
                PixelManager.Pixel pixel;
                while ((pixel = pixelManager.nextPixel()) != null) {
                    if (ASS_FLAG)
                        castRayASS(nX, nY, pixel.col(), pixel.row());
                    else if (AA_FLAG)
                        castRayJ(nX, nY, pixel.col(), pixel.row());
                    else
                        castRay(nX, nY, pixel.col(), pixel.row());

                }
            }));
        }
        for (var thread : threads) thread.start();
        try {
            for (var thread : threads) thread.join();
        } catch (InterruptedException ignored) {}
        return this;
    }


    /**
     * Builder class for constructing a Camera object.
     */
    public static class Builder {
        /**
         * The Camera object being built.
         */
        private final Camera camera = new Camera();

        /**
         * Default constructor for the Builder class.
         */
        public Builder() {
        }

        /**
         * Sets the location of the camera.
         *
         * @param p0 the position of the camera
         * @return the Builder instance
         */
        public Builder setLocation(Point p0) {
            camera.p0 = p0;
            return this;
        }

        /**
         * Sets the direction vectors of the camera.
         *
         * @param vTo the "to" direction vector
         * @param vUp the "up" direction vector
         * @return the Builder instance
         * @throws IllegalArgumentException if vTo and vUp are not orthogonal
         */
        public Builder setDirection(Vector vTo, Vector vUp) {
            // Ensure vTo and vUp are orthogonal
            if (!isZero(vTo.dotProduct(vUp)))
                throw new IllegalArgumentException("vTo and vUp must be orthogonal");
            // Normalize and set the direction vectors
            camera.vUp = vUp.normalize();
            camera.vTo = vTo.normalize();
            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();
            return this;
        }

        /**
         * Sets the direction of the camera towards a target point.
         * If vTo is parallel to Vector.AXIS_Y, a different up vector (AXIS_Z) is used to avoid zero vector in cross product.
         *
         * @param target the point the camera looks at
         * @return the Builder instance
         */
        public Builder setDirection(Point target) {
            //  return setDirection(target, Vector.AXIS_Y);
            if (camera.p0 == null)
                throw new IllegalArgumentException("Camera position (p0) must be set before setting direction using a target point.");
            // Calculate vTo from p0 to target
            camera.vTo = target.subtract(camera.p0).normalize();
            Vector vRight = camera.vTo.crossProduct(Vector.AXIS_Y).normalize();
            camera.vUp = vRight.crossProduct(camera.vTo).normalize();
            // camera.vUp = camera.vTo.crossProduct(Vector.AXIS_Y).normalize();
            return this;
        }

        /**
         * Sets the direction of the camera towards a target point with a specified up vector.
         *
         * @param target2 the target point
         * @param vUp     the up vector
         * @return the Builder instance
         */
        public Builder setDirection(Point target2, Vector vUp) {

            if (camera.p0 == null)
                throw new IllegalArgumentException("Camera position (p0) must be set before setting direction using a target point.");

            Vector vTo = target2.subtract(camera.p0);
            if (vTo.equals(Vector.ZERO)) // או שימוש ב-Util.isZero אם את משתמשת בווקטור שאינו סטטי
                throw new IllegalArgumentException("Target point must not be the same as camera position (creates zero direction vector)");

            camera.vTo = vTo.normalize();

            Vector vRight = camera.vTo.crossProduct(vUp);
            if (vRight.equals(Vector.ZERO))
                throw new IllegalArgumentException("Up vector cannot be parallel to direction vector");

            vRight = vRight.normalize();
            camera.vUp = vRight.crossProduct(camera.vTo).normalize();

            return this;
        }

        /**
         * Sets the size of the viewport.
         *
         * @param width  the width of the viewport
         * @param height the height of the viewport
         * @return the Builder instance
         * @throws IllegalArgumentException if width or height is not positive
         */
        public Builder setVpSize(double width, double height) {
            // Ensure width and height are positive
            if (alignZero(width) <= 0 || alignZero(height) <= 0)
                throw new IllegalArgumentException("Width and height must be positive");
            // Set the viewport size
            camera.width = width;
            camera.height = height;
            return this;
        }

        /**
         * Sets the distance from the camera to the viewport.
         *
         * @param distance the distance to the viewport
         * @return the Builder instance
         * @throws IllegalArgumentException if distance is not positive
         */
        public Builder setVpDistance(double distance) {
            // Ensure distance is positive
            if (alignZero(distance) <= 0)
                throw new IllegalArgumentException("Distance must be positive");
            // Set the distance
            camera.distance = distance;
            return this;
        }

        /**
         * Sets the resolution of the camera.
         *
         * @param nX the horizontal resolution
         * @param nY the vertical resolution
         * @return the Builder instance
         */
        public Builder setResolution(int nX, int nY) {
            // Ensure nX and nY are positive
            if (nX <= 0 || nY <= 0)
                throw new IllegalArgumentException("Resolution must be positive");
            camera.nX = nX;
            camera.nY = nY;
            // Create a new ImageWriter with the specified resolution
            camera.imageWriter = new ImageWriter(nX, nY);
            return this;
        }

        /**
         * Sets the ray tracer for the camera.
         *
         * @param scene the scene to be rendered
         * @param type  the type of ray tracer
         * @return the Builder instance
         */

        public Builder setRayTracer(Scene scene, RayTracerType type) {
            switch (type) {
                case SIMPLE:
                    camera.rayTracer = new SimpleRayTracer(scene);
                    break;
                default:
                    camera.rayTracer = new SimpleRayTracer(null);
                    break;
            }
            return this;
        }

        /**
         * Set the image writer
         *
         * @param imageWriter the image writer
         * @return the camera builder
         */
        public Builder setImageWriter(ImageWriter imageWriter) {
            camera.imageWriter = imageWriter;
            return this;
        }
        /**
         * Sets the pixel manager for the camera.
         *
         * @return the Builder instance
         */
        public Builder setMultithreading(int threads) {
            if (threads < -3)
                throw new IllegalArgumentException("Multithreading parameter must be -2 or higher");
            if (threads == -2) {
                int cores = Runtime.getRuntime().availableProcessors() - SPARE_THREADS;
                camera.threadsCount = cores <= 2 ? 1 : cores;
            } else {
                camera.threadsCount = threads;
            }
            return this;
        }

        /**
         * Sets the pixel manager for the camera.
         *
         * @return the Builder instance
         */
        public Builder setDebugPrint(double interval) {
            if (interval < 0)
                throw new IllegalArgumentException("interval parameter must be non-negative");
            camera.printInterval = interval;
            return this;
        }
        public Builder setAAFlag(boolean flag) {
            camera.AA_FLAG = flag;
            return this;
        }

        public Builder setASSFlag(boolean flag) {
            camera.ASS_FLAG = flag;
            return this;
        }

        /**
         * Builds the Camera object after validating all necessary fields.
         *
         * @return a cloned Camera object with computed right vector
         * @throws MissingResourceException if any required field is missing
         * @throws IllegalArgumentException if any value is invalid
         */
        public Camera build() {
            final String MISSING = "Missing render data";
            final String CLASS_NAME = "Camera";

            if (camera.p0 == null)
                throw new MissingResourceException(MISSING, CLASS_NAME, "p0");

            if (camera.vTo == null)
                throw new MissingResourceException(MISSING, CLASS_NAME, "vTo");

            if (camera.vUp == null)
                throw new MissingResourceException(MISSING, CLASS_NAME, "vUp");

            if (camera.distance == 0)
                throw new MissingResourceException(MISSING, CLASS_NAME, "distance must be set");

            if (camera.width == 0)
                throw new MissingResourceException(MISSING, CLASS_NAME, "view plane width must be positive");

            if (camera.height == 0)
                throw new MissingResourceException(MISSING, CLASS_NAME, "view plane height must be positive");
            //??
            if (camera.nX <= 0)
                throw new MissingResourceException(MISSING, CLASS_NAME, "nX (horizontal resolution) must be positive");
            //??
            if (camera.nY <= 0)
                throw new MissingResourceException(MISSING, CLASS_NAME, "nY (vertical resolution) must be positive");

            //check if imageWriter is null
            if (camera.rayTracer == null)
                camera.rayTracer = new SimpleRayTracer(null);

            // Normalize vTo and vUp to ensure correct computation of vRight
            camera.vTo = camera.vTo.normalize();
            camera.vUp = camera.vUp.normalize();

            // Compute vRight = vTo x vUp
            camera.vRight = camera.vTo.crossProduct(camera.vUp);

            // Return a cloned camera
            try {
                return (Camera) camera.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException("Failed to clone Camera object", e);
            }
        }

    }

    /**
     * This function implements Adaptive Anti-Aliasing using recursive subdivision.
     * It samples the center and 4 corners of a pixel and compares their colors.
     * If the colors differ significantly, the pixel is divided into 4 subpixels and processed recursively.
     * Otherwise, the average color is returned.
     */
//    private Color adaptiveSuperSampling(Point center, double pixelWidth, double pixelHeight, int level) {
//        if (level == 0) {
//            Ray ray = new Ray(p0, center.subtract(p0).normalize());
//            return rayTracer.traceRay(ray);
//        }
//
//        double halfWidth = pixelWidth / 2.0;
//        double halfHeight = pixelHeight / 2.0;
//
//        Point topLeft     = center.add(vRight.scale(-halfWidth)).add(vUp.scale(halfHeight));
//        Point topRight    = center.add(vRight.scale(halfWidth)).add(vUp.scale(halfHeight));
//        Point bottomLeft  = center.add(vRight.scale(-halfWidth)).add(vUp.scale(-halfHeight));
//        Point bottomRight = center.add(vRight.scale(halfWidth)).add(vUp.scale(-halfHeight));
//
//        Color cCenter     = rayTracer.traceRay(new Ray(p0, center.subtract(p0).normalize()));
//        Color cTopLeft    = rayTracer.traceRay(new Ray(p0, topLeft.subtract(p0).normalize()));
//        Color cTopRight   = rayTracer.traceRay(new Ray(p0, topRight.subtract(p0).normalize()));
//        Color cBottomLeft = rayTracer.traceRay(new Ray(p0, bottomLeft.subtract(p0).normalize()));
//        Color cBottomRight= rayTracer.traceRay(new Ray(p0, bottomRight.subtract(p0).normalize()));
//
//        if (isColorCloseEnough(cCenter, cTopLeft) &&
//                isColorCloseEnough(cCenter, cTopRight) &&
//                isColorCloseEnough(cCenter, cBottomLeft) &&
//                isColorCloseEnough(cCenter, cBottomRight)) {
//
//            return cCenter.add(cTopLeft, cTopRight, cBottomLeft, cBottomRight).scale(0.2);
//        }
//
//        Color avg = Color.BLACK;
//
//        avg = avg.add(adaptiveSuperSampling(topLeft,     halfWidth, halfHeight, level - 1));
//        avg = avg.add(adaptiveSuperSampling(topRight,    halfWidth, halfHeight, level - 1));
//        avg = avg.add(adaptiveSuperSampling(bottomLeft,  halfWidth, halfHeight, level - 1));
//        avg = avg.add(adaptiveSuperSampling(bottomRight, halfWidth, halfHeight, level - 1));
//
//        return avg.scale(0.25);
//    }
    private Color adaptiveSuperSampling(Point center, double pixelWidth, double pixelHeight, int level) {
        if (level == 0) {
            return rayTracer.traceRay(new Ray(p0, center.subtract(p0).normalize()));
        }

        double halfWidth = pixelWidth / 2.0;
        double halfHeight = pixelHeight / 2.0;
        Random random = new Random();

        Color totalColor = rayTracer.traceRay(new Ray(p0, center.subtract(p0).normalize()));
        int count = 1; // התחל מ־1 עבור המרכז

        // מיקומי הרבעים
        double[] xSigns = {-1, 1, -1, 1};
        double[] ySigns = {1, 1, -1, -1};

        for (int k = 0; k < 4; k++) {
            double xSign = xSigns[k];
            double ySign = ySigns[k];

            double randX = random.nextDouble() * halfWidth / 2;
            double randY = random.nextDouble() * halfHeight / 2;

            Point samplePoint = center
                    .add(vRight.scale(xSign * (halfWidth / 2 + randX)))
                    .add(vUp.scale(ySign * (halfHeight / 2 + randY)));

            Color sampleColor = rayTracer.traceRay(new Ray(p0, samplePoint.subtract(p0).normalize()));

            if (isColorCloseEnough(totalColor, sampleColor)) {
                totalColor = totalColor.add(sampleColor);
                count++;
            } else {
                // קריאה רקורסיבית באותו רבע פיקסל
                Point quarterCenter = center
                        .add(vRight.scale(xSign * halfWidth / 2))
                        .add(vUp.scale(ySign * halfHeight / 2));

                Color recursiveColor = adaptiveSuperSampling(quarterCenter, halfWidth, halfHeight, level - 1);
                totalColor = totalColor.add(recursiveColor);
                count++;
            }
        }

        return totalColor.scale(1.0 / count);
    }

    private boolean isColorCloseEnough(Color c1, Color c2) {
        final double THRESHOLD = 0.1;

        Double3 diff = c1.getRgb().subtract(c2.getRgb());
        return Math.abs(diff.d1()) < THRESHOLD &&
                Math.abs(diff.d2()) < THRESHOLD &&
                Math.abs(diff.d3()) < THRESHOLD;
    }


}