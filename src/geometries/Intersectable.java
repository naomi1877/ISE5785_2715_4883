package geometries;

import lighting.LightSource;
import primitives.Material;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 * The Intersectable class represents a geometric object that can be intersected by rays.
 * It serves as an abstract base class for all geometries that can be intersected.
 * This class defines the method for finding intersections with rays.
 */
public abstract class Intersectable {
    /**
     * Default constructor for the Intersectable class.
     */
    public Intersectable() {
    }

    /**
     * This method finds the intersections of a ray with the geometry.
     *
     * @param ray the ray to check for intersections
     * @return a list of intersection points
     */
    public final List<Point> findIntersections(Ray ray) {
        var list = calculateIntersections(ray);
        return list == null ? null : list.stream().map(intersection -> intersection.point).toList();
    }

    /**
     * This method calculates the intersections of a ray with the geometry.
     * It is a public method that calls the protected abstract method to perform the actual calculation.
     *
     * @param ray the ray to check for intersections
     * @return a list of Intersection objects representing the intersection points
     */
    public final List<Intersection> calculateIntersections(Ray ray) {
        return calculateIntersectionsHelper(ray);
    }

    /**
     * This is a protected abstract method that must be implemented by subclasses to calculate intersections.
     * It is called by the public method calculateIntersections.
     *
     * @param ray the ray to check for intersections
     * @return a list of Intersection objects representing the intersection points
     */
    protected abstract List<Intersection> calculateIntersectionsHelper(Ray ray);


    /**
     * Represents an intersection point between a ray and a geometry.
     */
    public static class Intersection {
        /**
         * The intersected geometry
         */
        public final Geometry geometry;

        /**
         * The point of intersection
         */
        public final Point point;

        /**
         * Material at the intersection
         */
        public final Material material;

        /**
         * Vector pointing toward the viewer
         */
        public Vector toViewer;

        /**
         * Normal vector at the intersection point
         */
        public Vector normalAtPoint;

        /**
         * Dot product of normal and viewer vectors
         */
        public double nDotV;

        /**
         * The light source affecting the point
         */
        public LightSource lightSource;

        /**
         * Vector pointing toward the light source
         */
        public Vector toLight;

        /**
         * Dot product of normal and light direction vectors
         */
        public double nDotL;

        /**
         * Constructor for the Intersection class.
         * Initializes the intersection with a geometry, point, and material.
         *
         * @param geometry the geometry of the intersection
         * @param point    the point of intersection
         */

        public Intersection(Geometry geometry, Point point) {
            if (geometry != null)
                this.material = geometry.getMaterial();
            else
                this.material = null;
            this.geometry = geometry;
            this.point = point;

        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Intersection other)) return false;
            return this.geometry == other.geometry && this.point.equals(other.point);
        }

        @Override
        public String toString() {
            return "Intersection{" +
                    "geometry=" + geometry +
                    ", point=" + point +
                    '}';
        }
    }
}
