package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Pyramid extends Geometry {
    private final Point top;
    private final Polygon base;
    private final List<Triangle> sideFaces;

    /**
     * Constructs a pyramid from an apex point and a base polygon.
     *
     * @param top the top point of the pyramid
     * @param base the base polygon
     */
    public Pyramid(Point top, Polygon base) {
        this.top = top;
        this.base = base;
        // Create the side triangles (faces) from apex and base edges
        sideFaces = new ArrayList<>();
        List<Point> baseVertices = base.vertices;

        if (baseVertices.size() < 3)
            throw new IllegalArgumentException("Base must have at least 3 vertices");

        for (int i = 0; i < baseVertices.size(); i++) {
            Point p1 = baseVertices.get(i);
            Point p2 = baseVertices.get((i + 1) % baseVertices.size());
            sideFaces.add(new Triangle(top, p1, p2));
        }
    }

    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
        List<Intersection> intersections = null;
        List<Point> uniquePoints = new LinkedList<>();
        //check intersection with the base:
        List<Intersection> baseIntersections = base.calculateIntersectionsHelper(ray);
        if (baseIntersections != null) {
            intersections = new LinkedList<>();
            for (Intersection i : baseIntersections) {
                if (!uniquePoints.contains(i.point)) {
                    uniquePoints.add(i.point);
                    intersections.add(i);
                }
            }
        }

        for (Triangle face : sideFaces) {
            List<Intersection> faceIntersections = face.calculateIntersectionsHelper(ray);
            if (faceIntersections != null) {
                if (intersections == null)
                    intersections = new LinkedList<>();
                //if the intersect point is on a corner, and already exists in the list, skip it
                for (Intersection intersection : faceIntersections) {
                    Point point = intersection.point;
                    if (!uniquePoints.contains(point)) {
                        uniquePoints.add(point);
                        intersections.add(intersection);
                    }
                }
            }
        }

        return intersections;
    }

    @Override
    public Vector getNormal(Point point) {
        if (base.isOn(point)) {
            return base.getNormal(point);
        }
        for (Triangle face : sideFaces) {
            if (face.getNormal(point) != null && face.isOn(point)) {
                return face.getNormal(point);
            }
        }
        throw new IllegalArgumentException("Point is not on the surface of the pyramid");
    }
}
