package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the {@link primitives.Point} class.
 */
class PointTests {
    /**
     * The point p0 with coordinates (1, 2, 3).
     */
    private final Point p0 = new Point(1, 2, 3);

    /**
     * The vector v0 with components (1, 2, 3).
     */
    private final Vector v0 = new Vector(1, 2, 3);

    /**
     * A small delta value used for comparison in floating-point operations.
     */
    private final double DELTA = 0.00001;

    /**
     * Default constructor for the PointTests class.
     * Initializes the test class for testing the Point class functionality.
     */
    public PointTests() {
        // Default constructor
    }

    /**
     * Test method for {@link primitives.Point#subtract(primitives.Point)}.
     */
    @Test
    void testSubtract() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Simple test
        assertEquals(v0, new Point(2, 4, 6).subtract(p0), "Subtract() wrong result");

        // =============== Boundary Values Tests =================
        // TC11: Subtracting the same point
        assertThrows(IllegalArgumentException.class, () -> p0.subtract(p0), "Subtract() for subtracting the same point does not throw an exception");
    }

    /**
     * Test method for {@link primitives.Point#add(primitives.Vector)}.
     */
    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Simple test
        assertEquals(new Point(2, 4, 6), p0.add(v0), "Add() wrong result");

        // =============== Boundary Values Tests =================
        // TC11: Adding the zero vector
        assertEquals(Point.ZERO, p0.add(new Vector(-1, -2, -3)), "Add() for adding the zero vector does not return the same point");
    }

    /**
     * Test method for {@link primitives.Point#distanceSquared(primitives.Point)}.
     */
    @Test
    void testDistanceSquared() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Calculate the squared distance between two points
        assertEquals(11, p0.distanceSquared(new Point(2, 3, 6)), DELTA, "Calculate the squared distance between two points does not work correctly");

        // ============ Boundary Values Tests ==================
        // TC02: Calculate the squared distance between two points
        assertEquals(0, p0.distanceSquared(p0), DELTA, "Calculate the squared distance between two points does not work correctly");
    }

    /**
     * Test method for {@link primitives.Point#distance(primitives.Point)}.
     */
    @Test
    void testDistance() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Calculate the distance between two points
        assertEquals(8, p0.distance(new Point(1, 2, -5)), DELTA, "Calculate the distance between two points does not work correctly");

        // ============ Boundary Values Tests ==================
        // TC02: Calculate the distance between two points
        assertEquals(0, p0.distance(p0), DELTA, "Calculate the distance between two points does not work correctly");
    }
}