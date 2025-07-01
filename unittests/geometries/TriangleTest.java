package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link geometries.Triangle} class.
 *
 * @author uthor Naomie Gronstein and Efrat Brinkman.
 */
class TriangleTest {
    /**
     * default constructor
     */
    public TriangleTest() {
    }

    /**
     * A point at the origin (0,0,0).
     */
    Point p1 = new Point(0, 0, 0);

    /**
     * A point located at (-1,0,0) on the X-axis.
     */
    Point p2 = new Point(-1, 0, 0);


    /**
     * A point located at (1,-1,0) in the XY plane.
     */
    Point p3 = new Point(1, -1, 0);

    /**
     * A point located at (1,1,0) in the XY plane.
     */
    Point p4 = new Point(1, 1, 0);

    /**
     * A vector pointing along the positive Z-axis.
     */
    Vector v1 = new Vector(0, 0, 1);

    /**
     * A vector pointing along the negative Z-axis.
     */
    Vector v2 = new Vector(0, 0, -1);

    /**
     * Test method for {@link geometries.Triangle} constructor.
     */
    @Test
    void testConstructor() {
        // ============ Valid Case ================
        assertDoesNotThrow(() -> new Triangle(p1, p2, p4),
                "Constructor failed with valid triangle points");

        // ============ Invalid Cases =============

        // Case 1: Less than 3 points
        assertThrows(IllegalArgumentException.class,
                () -> new Triangle(p1, p2),
                "Constructor should throw exception for less than 3 points");

        // Case 2: More than 3 points
        assertThrows(IllegalArgumentException.class,
                () -> new Triangle(p1, p2, p3, p4),
                "Constructor should throw exception for more than 3 points");

        // Case 3: Colinear points (on the X-axis)
        Point b = new Point(1, 0, 0);
        Point c = new Point(2, 0, 0);

        assertThrows(IllegalArgumentException.class,
                () -> new Triangle(p1, b, c),
                "Constructor should throw exception for colinear points");
    }

    /**
     * Test method for {@link geometries.Triangle#getNormal(Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests =================
        // TC01: Simple test
        Vector normal = new Triangle(p2, p3, p4).getNormal(p1);
        assertTrue(normal.equals(v1) || normal.equals(v2), "getNormal() wrong result");

    }

    /**
     * Test method for {@link geometries.Triangle#findIntersections(primitives.Ray)}.
     */
    @Test
    void testFindIntersections() {

        Triangle triangle = new Triangle(new Point(1, 1, 0), p3, p2);
        // ============ Equivalence Partitions Tests ==============
        // TC01: the intersection point is inside the triangle
        assertEquals(1, triangle.findIntersections(
                        new Ray(new Point(1.8, 1.8, 1), new Vector(-1, -1, -1))).size(),
                "Failed to find the intersection point when the intersection point is inside the triangle");

        // TC02: the intersection point is outside the triangle and against an edge
        assertNull(triangle.findIntersections(
                        new Ray(new Point(0.5, 2, 1), v1)),
                "Failed to find the intersection point when the intersection point is outside the triangle and against an edge");

        // TC03: the intersection point is outside the triangle and against a vertex
        assertNull(triangle.findIntersections(
                        new Ray(new Point(2, 2, 1), v1)),
                "Failed to find the intersection point when the intersection point is outside the triangle and against an edge");

        // ================= Boundary Values Tests =================
        // TC04: the intersection point is on the edge of the triangle
        assertNull(triangle.findIntersections(
                        new Ray(new Point(0.5, 1, -1), v2)),
                "Failed to find the intersection point when the intersection point is on the edge of the triangle");

        // TC05: the intersection point is on the vertex of the triangle
        assertNull(triangle.findIntersections(
                        new Ray(new Point(1, 1, 1), new Vector(0, 0, -1))),
                "Failed to find the intersection point when the intersection point is on the vertex of the triangle");

        // TC06: the intersection point is outside the triangle but in the path of the edge
        assertNull(triangle.findIntersections(
                        new Ray(new Point(2, 1, -1), v2)),
                "Failed to find the intersection point when the intersection point is outside the triangle but in the path of the edge");

        // ================= external Tests =================
        // TC07: the triangle is in an angle
        Triangle triangle2 = new Triangle(p3, p2, p1);
        assertNull(triangle2.findIntersections(
                        new Ray(new Point(-1, -1, -1), new Vector(1, 1, 1))),
                "Intersection on a triangle's vertex should return null");
    }

}