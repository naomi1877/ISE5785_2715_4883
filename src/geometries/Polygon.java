package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * This class represents a Polygon, a 2D shape in 3D Cartesian coordinates.
 * It extends the Geometry class and is defined by a list of vertices and a plane.
 * A polygon can have any number of vertices, but must be convex.
 */
public class Polygon extends Geometry {
    /**
     * List of vertices of the polygon.
     */
    protected final List<Point> vertices;

    /**
     * The plane in which the polygon lies.
     */
    protected final Plane plane;

    /**
     * The number of vertices in the polygon.
     */
    private final int size;

    /**
     * Constructor to initialize a Polygon object with a list of vertices.
     * The list must be ordered by edge path and the polygon must be convex.
     *
     * @param vertices list of vertices ordered by edge path
     * @throws IllegalArgumentException if the vertices are invalid
     */
    public Polygon(Point... vertices) {
        if (vertices.length < 3)
            throw new IllegalArgumentException("A polygon can't have less than 3 vertices");
        this.vertices = List.of(vertices);
        size = vertices.length;

        // Generate the plane based on the first three vertices
        plane = new Plane(vertices[0], vertices[1], vertices[2]);
        if (size == 3) return;

        Vector n = plane.getNormal(null);
        Vector edge1 = vertices[size - 1].subtract(vertices[size - 2]);
        Vector edge2 = vertices[0].subtract(vertices[size - 1]);

        boolean positive = edge1.crossProduct(edge2).dotProduct(n) > 0;
        for (var i = 1; i < size; ++i) {
            if (!isZero(vertices[i].subtract(vertices[0]).dotProduct(n)))
                throw new IllegalArgumentException("All vertices of a polygon must lay in the same plane");
            edge1 = edge2;
            edge2 = vertices[i].subtract(vertices[i - 1]);
            if (positive != (edge1.crossProduct(edge2).dotProduct(n) > 0))
                throw new IllegalArgumentException("All vertices must be ordered and the polygon must be convex");
        }
    }

    @Override
    public Vector getNormal(Point point) {
        return plane.getNormal(null);
    }


    //    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
        // Check if the ray intersects the plane
        List<Intersection> planeIntersections = plane.calculateIntersections(ray);
        if (planeIntersections == null) {
            // No intersection with the plane
            return null;
        }

        Point intersectionPoint = planeIntersections.getFirst().point;

        // Check if the intersection point is inside the polygon
        Vector v1, v2;
        for (int i = 0; i < size; ++i) {
            Point p1 = vertices.get(i);
            Point p2 = vertices.get((i + 1) % size);
            if (intersectionPoint.equals(p1) || intersectionPoint.equals(p2)) continue;
            try {
                v1 = p1.subtract(intersectionPoint);
                v2 = p2.subtract(intersectionPoint);
            } catch (IllegalArgumentException e) {
                continue;
            }
            Vector cross;
            try {
                cross = v1.crossProduct(v2);
            } catch (IllegalArgumentException e) {
                // Points are collinear, skip this edge
                continue;
            }

            if (alignZero(cross.dotProduct(plane.getNormal(null))) <= 0) {
                // Intersection point is outside the polygon
                return null;
            }
        }

        return List.of(new Intersection(this, intersectionPoint));


    }

    /**
     * Checks if a given point is on the polygon.
     *
     * @param point the point to check
     * @return true if the point is on the polygon, false otherwise
     */
    public boolean isOn(Point point) {
        // Step 1: Check if point is on the same plane
        Vector n = this.getNormal(point);
        // using one of the polygon's base points
        Vector qToP = point.subtract(this.vertices.getFirst());
        double distance = alignZero(n.dotProduct(qToP));
        if (!isZero(distance)) {
            // point is not on the plane
            return false;
        }
        // Step 2: Check if the point is inside the polygon
        int size = this.vertices.size();
        Vector v1, v2;
        for (int i = 0; i < size; ++i) {
            try {
                v1 = this.vertices.get(i).subtract(point);
                v2 = this.vertices.get((i + 1) % size).subtract(point);
            } catch (IllegalArgumentException e) {
                // in case the point is on a vertex – skip
                continue;
            }
            try {
                Vector cross = v1.crossProduct(v2);
                if (alignZero(cross.dotProduct(n)) < 0) {
                    return false; // point is outside
                }
            } catch (IllegalArgumentException e) {
                // points are collinear, skip this edge
                return false;
            }
        }
        // point is on the plane and inside the polygon
        return true;
    }


}
