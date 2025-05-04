package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;

public abstract class Intersectable {
    /**
     * This method finds the intersections of a ray with the geometry.
     * @param ray the ray to check for intersections
     * @return a list of intersection points, or null if there are no intersections
     */
    abstract List<Point> findIntersections(Ray ray);
}
