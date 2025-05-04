package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Geometries extends Intersectable{
    /**
     * A collection of intersectable shapes.
     */
    private final List<Intersectable> shapes =new LinkedList<>();

    public Geometries() {
    }

    /**
     * Constructor to initialize a Geometries object with a collection of intersectable shapes.
     * @param geometries
     */
    public  Geometries(Intersectable... geometries){
        add(geometries);
    }

    public void add(Intersectable... geometries){
        Collections.addAll(shapes, geometries);
    }

    /**
     * Adds a collection of intersectable shapes to the Geometries object.
     * @param geometries the collection of intersectable shapes to add
     */
    @Override
    List<Point> findIntersections(Ray ray) {
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
