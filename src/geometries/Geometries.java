package geometries;

import primitives.Ray;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * The Geometries class represents a collection of geometric shapes.
 * It implements the Composite design pattern by allowing to treat multiple {@link Intersectable} objects as a single object.
 * This class enables combining multiple geometries and finding their intersections collectively.
 */
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

    /**
     * Constructor to initialize a Geometries object with a collection of intersectable shapes.
     *
     * @param geometries a collection of intersectable shapes
     */
    public void add(Intersectable... geometries) {
        Collections.addAll(shapes, geometries);
    }


    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray) {//, double distanceToLight
        List<Intersection> intersections = new LinkedList<>();
        for (Intersectable shape : shapes) {
            List<Intersection> shapeIntersections = shape.calculateIntersections(ray);//distanceToLight
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
