package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link geometries.Triangle} class.
 * @ Author Naomie Gronstein && Efrat Brinkman.
 */
class TriangleTest {
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
}