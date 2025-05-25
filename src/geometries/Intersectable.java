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

    public final List<Intersection> calculateIntersections(Ray ray) {
        return calculateIntersectionsHelper(ray);
    }

    /**
     * This method is an abstract method that must be implemented by subclasses to calculate intersections : NVI
     * @param ray the ray to check for intersections
     * @return a list of intersection points
     */
    protected abstract List<Intersection> calculateIntersectionsHelper(Ray ray);


    public static class Intersection {
        //Fixed fields: geometry, point, and material
        public final Geometry geometry;
        public final Point point;
        public final Material material;

        //Direction from camera to intersection point (v)
        public Vector toViewer;
        //The normal vector at the intersection point (n)
        public Vector normalAtPoint;
        //Dot product of the normal vector and the ray direction vector (n·v)
        public double nDotV;

        //Light source at the intersection point
        public LightSource lightSource;
        //Direction from light source to intersection point
        public Vector toLight;
        //Dot product of the normal vector and the light direction vector.
        public double nDotL;


        public Intersection(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
            this.material = geometry.getMaterial();
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
