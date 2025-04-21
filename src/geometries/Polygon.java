package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import java.util.List;
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

        Vector n = plane.getNormal(null);//
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
        return plane.getNormal(null);//
    }

    @Override
    List<Point> findIntersections(Ray ray) {
        // Check if the ray intersects the plane
        List<Point> planeIntersections = plane.findIntersections(ray);
        if (planeIntersections == null)
            return null; // No intersection with the plane

        // Check if the intersection point is inside the polygon
        Point intersectionPoint = planeIntersections.get(0);
        Vector v = intersectionPoint.subtract(vertices.get(0));
        Vector edge1 = vertices.get(1).subtract(vertices.get(0));
        Vector edge2 = vertices.get(size - 1).subtract(vertices.get(0));
        boolean positive = edge1.crossProduct(v).dotProduct(plane.getNormal(null)) > 0;
        for (int i = 1; i < size; ++i) {
            edge1 = edge2;
            edge2 = vertices.get(i).subtract(vertices.get(i - 1));
            v = intersectionPoint.subtract(vertices.get(i));
            if (positive != (edge1.crossProduct(v).dotProduct(plane.getNormal(null)) > 0))
                return null; // The intersection point is outside the polygon
        }
        // The intersection point is inside the polygon
        return List.of(intersectionPoint);
    }
}
