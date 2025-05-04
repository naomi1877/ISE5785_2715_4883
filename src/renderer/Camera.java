package renderer;

import primitives.Point;
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
    private Point pcenter; // The center point of the viewport
    private double width = 0.0; // The width of the viewport
    private double height = 0.0; // The height of the viewport
    private double distance = 0.0; // The distance from the camera to the viewport

    /**
     * Returns a new Builder instance for constructing a Camera object.
     *
     * @return a Builder instance
     */
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
    public List<Point> constructRay(int nX, int nY, int j, int i) {
        // todo
        return null;
    }

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

    public Point getPcenter() {
        return pcenter;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getDistance() {
        return distance;
    }

    /**
     * Builder class for constructing a Camera object.
     */
    public static class Builder {
        private final Camera camera = new Camera();

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
            camera.vRight = camera.vTo.crossProduct(camera.vUp);
            return this;
        }
        /**
         * Sets the direction of the camera towards a target point.
         *
         * @param target the target point
         * @return the Builder instance
         */

        public Builder setDirection(Point target) {
            //todo
            return this;
        }

        public Builder setDirection(Point target2, Vector vUp) {
            //todo
            return this;
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
         * Sets the size of the viewport.
         *
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
         * Build and returns the constructed Camera object.
         * This method ensure immutability by cloning the camera instance.
         *
         * @return the constructed Camera object, or null if cloning fails
         */

        public Camera build() {
            //todo
            final String className = "Camera";
            final String description = "values not set: ";


            if(camera.vTo == null)
                throw new MissingResourceException(description, className, "vTo");
            if(camera.vUp == null)
                throw new MissingResourceException(description, className, "vUp");
            if(camera.p0 == null)
                throw new MissingResourceException(description, className, "p0");
            if(camera.pcenter == null)
                throw new MissingResourceException(description, className, "pcenter");
            if(camera.width ==0.0)
                throw new MissingResourceException(description, className, "width");
            if(camera.height == 0.0)
                throw new MissingResourceException(description, className, "height");
            if(camera.distance == 0.0)
                throw new MissingResourceException(description, className, "distance");
            if (camera.vRight == null)
                camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();


            if (!Util.isZero(camera.vTo.dotProduct(camera.vRight)) ||
                    !Util.isZero(camera.vTo.dotProduct(camera.vUp)) ||
                    !Util.isZero(camera.vRight.dotProduct(camera.vUp)))
                throw new IllegalArgumentException("vTo, vUp and vRight must be orthogonal");

            if (camera.vTo.length() != 1 || camera.vUp.length() != 1 || camera.vRight.length() != 1)
                throw new IllegalArgumentException("vTo, vUp and vRight must be normalized");

            if (camera.width <= 0 || camera.height <= 0)
                throw new IllegalArgumentException("width and height must be positive");

            if (camera.distance <= 0)
                throw new IllegalArgumentException("distance from camera to view must be positive");

            try {
                // Clone the camera to ensure immutability
                return (Camera) camera.clone();
            } catch (CloneNotSupportedException e) {
                return null;
            }
        }

        public Builder setResolution(int nx, int ny) {
            return this;
        }

    }
}