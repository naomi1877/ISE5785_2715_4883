package geometries;

import primitives.Point;
import primitives.Ray;

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
    public abstract List<Point> findIntersections(Ray ray);
}
