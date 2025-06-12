package geometries;

import primitives.*;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import static primitives.Util.alignZero;
import static primitives.Util.isZero;

import java.util.List;
/**
 * Class Box represents an axis-aligned rectangular box (cuboid)
 * in 3D space, defined by two opposite corner points.
 * The box is composed of 6 rectangular faces (Polygons).
 *
 * @author Naomie Gronstein
 */
public class Box extends Geometry{
    private final Double3 min;
    private final Double3 max;
    private final List<Polygon> faces;

    public Box(Double3 p1, Double3 p2) {
        if (p1.d1()==p2.d1() ||
            p1.d2()==p2.d2() ||
            p1.d3()==p2.d3()) {
            throw new IllegalArgumentException("Points must not be aligned on the same axis");
        }
        this.min = new Double3( Math.min(p1.d1(),p2.d1()),Math.min(p1.d2(),p2.d2()),Math.min(p1.d3(),p2.d3()));
        this.max = new Double3( Math.max(p1.d1(),p2.d1()),Math.max(p1.d2(),p2.d2()),Math.max(p1.d3(),p2.d3()));
        // Create the 6 faces of the box
        faces = List.of(
                // Left (x = minX) – counter-clockwise when looking from x < 0
                new Polygon(
                        new Point(min.d1(), min.d2(), max.d3()),
                        new Point(min.d1(), max.d2(), max.d3()),
                        new Point(min.d1(), max.d2(), min.d3()),
                        new Point(min.d1(), min.d2(), min.d3())
                ),
                // Right (x = maxX) – counter-clockwise when looking from x > 0
                new Polygon(
                        new Point(max.d1(), min.d2(), min.d3()),
                        new Point(max.d1(), max.d2(), min.d3()),
                        new Point(max.d1(), max.d2(), max.d3()),
                        new Point(max.d1(), min.d2(), max.d3())
                ),
                // Bottom (y = minY) – counter-clockwise from y < 0
                new Polygon(
                        new Point(min.d1(), min.d2(), max.d3()),
                        new Point(max.d1(), min.d2(), max.d3()),
                        new Point(max.d1(), min.d2(), min.d3()),
                        new Point(min.d1(), min.d2(), min.d3())
                ),
                // Top (y = maxY) – counter-clockwise from y > 0
                new Polygon(
                        new Point(min.d1(), max.d2(), max.d3()),
                        new Point(max.d1(), max.d2(), max.d3()),
                        new Point(max.d1(), max.d2(), min.d3()),
                        new Point(min.d1(), max.d2(), min.d3())
                ),
                // Front (z = minZ) – counter-clockwise from z < 0
                new Polygon(
                        new Point(min.d1(), min.d2(), min.d3()),
                        new Point(min.d1(), max.d2(), min.d3()),
                        new Point(max.d1(), max.d2(), min.d3()),
                        new Point(max.d1(), min.d2(), min.d3())
                ),
                // Back (z = maxZ) – counter-clockwise from z > 0
                new Polygon(
                        new Point(max.d1(), min.d2(), max.d3()),
                        new Point(max.d1(), max.d2(), max.d3()),
                        new Point(min.d1(), max.d2(), max.d3()),
                        new Point(min.d1(), min.d2(), max.d3())
                )
        );

    }


    @Override
    public Vector getNormal(Point point) {
        for (Polygon face : faces) {
            if (face.isOn(point)) {
                return face.getNormal(point);
            }
        }
        throw new IllegalArgumentException("Point is not on the surface of the box");
    }

    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
        List<Intersection> intersections = null;
        List<Point> uniquePoints = new LinkedList<>();
        for (Polygon face : faces) {
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

        return intersections;    }
}
