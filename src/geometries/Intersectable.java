package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;

public abstract class Intersectable {
    /**
     * This method finds the intersections of a ray with the geometry.
     * @param ray
     * @return a list of intersection points
     */
   public abstract List<Point> findIntersections(Ray ray);
}
