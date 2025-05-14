package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Geometries extends Intersectable {
    /**
     * A collection of intersectable shapes.
     */
    private final List<Intersectable> shapes = new LinkedList<>();

    /**
     * Default constructor to initialize an empty Geometries object.
     */
    public Geometries() {
    }

    /**
     * Constructor to initialize a Geometries object with a collection of intersectable shapes.
     *
     * @param geometries a collection of intersectable shapes
     */
    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    public void add(Intersectable... geometries) {
        Collections.addAll(shapes, geometries);
    }


    @Override
    public List<Point> findIntersections(Ray ray) {
        List<Point> intersections = new LinkedList<>();
        for (Intersectable shape : shapes) {
            List<Point> shapeIntersections = shape.findIntersections(ray);
            if (shapeIntersections != null) {
                intersections.addAll(shapeIntersections);
            }
        }
        if (intersections.isEmpty()) {
            return null;
        }
        return intersections;
    }

}
