package renderer;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.List;
import java.util.MissingResourceException;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * The Camera class represents a camera in a 3D scene.
 * It includes properties such as position, direction vectors, and viewport size.
 */
public class Camera implements Cloneable {

    private Camera() {}

    private Vector vTo; // The "to" direction vector of the camera
    private Vector vUp; // The "up" direction vector of the camera
    private Vector vRight; // The "right" direction vector of the camera
    private Point p0; // The position of the camera
    private Point pCenter; // The center point of the viewport
    private double width = 0.0; // The width of the viewport
    private double height = 0.0; // The height of the viewport
    private double distance = 0.0; // The distance from the camera to the viewport
    private int nX = 1; // horizontal resolution
    private int nY = 1; // vertical resolution

    // Getters for camera properties
    public Vector getVTo() {
        return vTo;
    }

    public Vector getVUp() {
        return vUp;
    }

    public Vector getVRight() {
        return vRight;
    }

    public Point getP0() {
        return p0;
    }

    public Point getPCenter() { return pCenter; }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getDistance() {
        return distance;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    /**
     * Constructs a ray through a specific pixel in the viewport.
     *
     * @param nX the number of pixels in the X direction
     * @param nY the number of pixels in the Y direction
     * @param j the column index of the pixel
     * @param i the row index of the pixel
     * @return a list of points representing the ray
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
        //calculate the size of each pixel
        double pixelWidth = width/nX;
        double pixelHeight = height/nY;

        //calculate the center of the view plane
        Point pCenter = p0.add(vTo.scale(distance));

        //calculate the center of the pixel
        double xJ = (j - (nX - 1) / 2.0) * pixelWidth;
        double yI = -1* (i - (nY - 1) / 2.0) * pixelHeight;
        Point pIJ = pCenter;
        if (!isZero(yI)) pIJ = pIJ.add(vUp.scale(yI));
        if (!isZero(xJ)) pIJ = pIJ.add(vRight.scale(xJ));

        //return the ray from the camera position to the pixel center
        return new Ray(p0, pIJ.subtract(p0).normalize());//normalize or not?
    }

    /**
     * Builder class for constructing a Camera object.
     */
    public static class Builder {
        // The camera instance being built
        private final Camera camera = new Camera();
        /**
         * Sets the location of the camera.
         * @param p0 the position of the camera
         * @return the Builder instance
         */
        public Builder setLocation(Point p0) {
            camera.p0 = p0;
            return this;
        }
        /**
         * Sets the direction vectors of the camera.
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
         * @param target the point the camera looks at
         * @return the Builder instance
         */
        public Builder setDirection(Point target) {
//            return setDirection(target, Vector.AXIS_Y);
            if (camera.p0 == null)
                throw new IllegalArgumentException("Camera position (p0) must be set before setting direction using a target point.");
            // Calculate vTo from p0 to target
            camera.vTo = target.subtract(camera.p0).normalize();
            camera.vRight = camera.vTo.crossProduct(Vector.AXIS_Y).normalize();
            camera.vUp = camera.vRight.crossProduct(camera.vTo).normalize();
//            camera.vUp = camera.vTo.crossProduct(Vector.AXIS_Y).normalize();
            return this;
        }
        /**
         * Sets the direction of the camera towards a target point with a specified up vector.
         * @param target2 the target point
         * @param vUp the up vector
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
         * @param width the width of the viewport
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
                throw new MissingResourceException(MISSING,CLASS_NAME,"distance must be set");

            if (camera.width == 0)
                throw new MissingResourceException(MISSING,CLASS_NAME,"view plane width must be positive");

            if (camera.height == 0)
                throw new MissingResourceException(MISSING,CLASS_NAME,"view plane height must be positive");

            if (camera.nX == 0)
                throw new MissingResourceException(MISSING,CLASS_NAME,"nX (horizontal resolution) must be positive");

            if (camera.nY == 0)
                throw new MissingResourceException(MISSING,CLASS_NAME,"nY (vertical resolution) must be positive");

            // Normalize vTo and vUp to ensure correct computation of vRight
            camera.vTo = camera.vTo.normalize();
            camera.vUp = camera.vUp.normalize();

            // Compute vRight = vTo x vUp
            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();

            // Return a cloned camera
            try {
                return (Camera) camera.clone();
            }
            catch (CloneNotSupportedException e) {
                throw new RuntimeException("Failed to clone Camera object", e);
            }
        }
    }
}