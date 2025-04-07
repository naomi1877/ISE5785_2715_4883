package geometries;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import primitives.*;

/**
 * Unit tests for the {@link geometries.Plane} class.
 */
class PlaneTests {
    /**
     * Point p1 with coordinates (0, 1, 1).
     */
    private final Point p1 = new Point(0, 1, 1);

    /**
     * Point p2 with coordinates (0, 0, 2).
     */
    private final Point p2 = new Point(0, 0, 2);

    /**
     * Point p3 with coordinates (0, 0, 1).
     */
    private final Point p3 = new Point(0, 0, 1);

    /**
     * Point p4 with coordinates (0, 0, 0).
     */
    private final Point p4 = new Point(0, 0, 0);

    /**
     * Vector v1 with components (0, 1, 0).
     */
    Vector v1 = new Vector(0, 1, 0);

    /**
     * Vector v2 with components (0, -1, 0).
     */
    Vector v2 = new Vector(0, -1, 0);


    /**
     * Default constructor for the PlaneTests class.
     * Initializes the test class for testing the Plane class functionality.
     */
    public PlaneTests() {
        // Default constructor
    }

    /**
     * Test method for {@link geometries.Plane#Plane(primitives.Point, primitives.Point, primitives.Point)}.
     */
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Simple test
        assertDoesNotThrow(() -> new Plane(p1, new Point(0, 1, 0), p3), "Failed to create a proper plane");

        // =============== Boundary Values Tests =================
        // TC02: Test for a plane that the points are on the same line

        // Test case where all three points are collinear (on the same line)
        assertThrows(IllegalArgumentException.class, () -> new Plane(new Point(1, 0, 0), new Point(2, 0, 0), new Point(3, 0, 0)), "Failed to throw an exception when creating a plane with points on the same line");
        // Test case where all three points are the same
        assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p1, p1), "Failed to throw an exception when creating a plane with points on the same line");
        // Test case where the first two points are the same
        assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p1, p2), "Failed to throw an exception when creating a plane with points on the same line");
        // Test case where the last two points are the same
        assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p2, p2), "Failed to throw an exception when creating a plane with points on the same line");
        // Test case where the first and the last points are the same
        assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p2, p1), "Failed to throw an exception when creating a plane with points on the same line");
    }

    /**
     * Test method for {@link geometries.Plane#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests =================
        // TC01: Simple test
        Vector normal = new Plane(new Point(1, 0, 0), new Point(-1, 0, 0), new Point(1, 0, 1)).getNormal(p4);
        assertTrue(normal.equals(v1) || normal.equals(v2), "Failed to get the normal vector of the plane");
    }
}