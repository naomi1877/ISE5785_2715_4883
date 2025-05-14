package renderer;

import primitives.*;
import scene.Scene;
//import java.awt.Color;

import java.util.List;
import java.util.MissingResourceException;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * The Camera class represents a camera in a 3D scene.
 * It includes properties such as position, direction vectors, and viewport size.
 */
public class Camera implements Cloneable {

    /**
     * The default constructor for the Camera class.
     * It initializes the camera with default values.
     */
    private Camera() {
    }

    /**
     * The "to" direction vector of the camera.
     * This vector indicates the direction in which the camera is pointing.
     */
    private Vector vTo;
    /**
     * The "up" direction vector of the camera.
     * This vector indicates the upward direction of the camera.
     */
    private Vector vUp;
    /**
     * The "right" direction vector of the camera.
     * This vector is perpendicular to both vTo and vUp, forming a right-handed coordinate system.
     */
    private Vector vRight;
    /**
     * The position of the camera in 3D space.
     */
    private Point p0;
    /**
     * The center point of the camera's view.
     * This point is used to calculate the rays that will be cast through the viewport.
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
    private int nX = 1; // horizontal resolution
    /**
     * The vertical resolution of the image.
     */
    private int nY = 1; // vertical resolution
    /**
     * The image writer used to create the image.
     */
    private static ImageWriter imageWriter;
    /**
     * The ray tracer used to trace rays through the scene.
     */
    private RayTracerBase rayTracer;



    /**
     * Getter for the camera's position.
     *
     * @return the position of the camera.
     */
    public Point getP0() {
        return p0;
    }

    /**
     * Getter for the camera's "to" direction vector.
     *
     * @return the "to" direction vector of the camera.
     */
    public Point getPCenter() {
        return pCenter;
    }

    /**
     * Getter for the camera's "up" direction vector.
     *
     * @return the "up" direction vector of the camera.
     */
    public double getWidth() {
        return width;
    }

    /**
     * Getter for the camera's "right" direction vector.
     *
     * @return the "right" direction vector of the camera.
     */
    public double getHeight() {
        return height;
    }

    /**
     * Getter for the camera's "right" direction vector.
     *
     * @return the "right" direction vector of the camera.
     */
    public double getDistance() {
        return distance;
    }

    /**
     *
     * @return buildetr
     */
    public static Builder getBuilder() {
        return new Builder();
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
        if (!isZero(yI)) pIJ = pIJ.add(vUp.scale(yI));
        if (!isZero(xJ)) pIJ = pIJ.add(vRight.scale(xJ));

        //return the ray from the camera position to the pixel center
        return new Ray(p0, pIJ.subtract(p0).normalize());//normalize or not?
    }

    /**
     *
     * @param nx
     * @param ny
     * @param i
     * @param j
     */
    private void castRay(int nx, int ny, int i, int j) {
        Ray ray = constructRay(nx, ny, j, i);
        Color color = rayTracer.traceRay(ray);
        imageWriter.writePixel(j, i, color);
    }

    /**
     * Renders the image by tracing rays through each pixel in the viewport.
     * @return the Camera object for method chaining
     */
    /**
     * Render the image
     *
     * @return the Camera object for method chaining
     */
    public Camera renderImage() {
        if (imageWriter == null || rayTracer == null)
            throw new IllegalArgumentException("Missing imageWriter or rayTracer");
        int y = imageWriter.nY();
        int x = imageWriter.nX();
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                castRay(x, y, i, j);
            }
        }
        return this;
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
     * Builder class for constructing a Camera object.
     */
    public static class Builder {
        // The camera instance being built
        private final Camera camera = new Camera();

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
            camera.vRight = camera.vTo.crossProduct(Vector.AXIS_Y).normalize();
            camera.vUp = camera.vRight.crossProduct(camera.vTo).normalize();
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
            //if camera position not set yet, cannot set direction
            if (camera.p0 == null)
                throw new IllegalArgumentException("Camera position (p0) must be set before setting direction using a target point.");
            // Calculate the direction vector from the camera position to the target point
            camera.vTo = target2.subtract(camera.p0).normalize();
            camera.vRight = camera.vTo.crossProduct(vUp).normalize();
            // if(!isZero(camera.vTo.dotProduct(camera.vRight)))
            //   throw new IllegalArgumentException("vUp and vTo must be orthogonal");
            //fix the up vector because it may not be orthogonal to vTo;
            camera.vUp = camera.vRight.crossProduct(camera.vTo).normalize();
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

            if ((camera.vTo.dotProduct(camera.vUp)) != 0)
                throw new IllegalArgumentException("vTo and vUp must be orthogonal");

            if (camera.distance == 0)
                throw new MissingResourceException(MISSING, CLASS_NAME, "distance must be set");

            if (camera.width == 0)
                throw new MissingResourceException(MISSING, CLASS_NAME, "view plane width must be positive");

            if (camera.height == 0)
                throw new MissingResourceException(MISSING, CLASS_NAME, "view plane height must be positive");
//מיותר לא?
            if (camera.nX == 0)
                throw new MissingResourceException(MISSING, CLASS_NAME, "nX (horizontal resolution) must be positive");
//מיותר?
            if (camera.nY == 0)
                throw new MissingResourceException(MISSING, CLASS_NAME, "nY (vertical resolution) must be positive");

            //if i checked the ray tracer here at some test then it will be null
            // if (camera.rayTracer == null)
            //  throw new MissingResourceException(MISSING, CLASS_NAME, "rayTracer");

            // Normalize vTo and vUp to ensure correct computation of vRight
            camera.vTo = camera.vTo.normalize();
            camera.vUp = camera.vUp.normalize();

            // Compute vRight = vTo x vUp
            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();

            // Return a cloned camera
            try {
                return (Camera) camera.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException("Failed to clone Camera object", e);
            }
        }

    }
}