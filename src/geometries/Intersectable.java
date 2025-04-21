package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;

public abstract class Intersectable {

    abstract List<Point> findIntersections(Ray ray);
}
