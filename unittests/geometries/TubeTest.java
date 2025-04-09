package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link geometries.Tube} class.
 * @ Author Naomie Gronstein && Efrat Brinkman.
 */
class TubeTest {
    /**
     * A cylindrical tube with its axis along the Z-axis and a radius of 1.
     */
    private final Tube tube = new Tube(1,new Ray(Point.ZERO, new Vector(0, 0, 1)));

    /**
     * Test method for {@link geometries.Tube} constructor.
     */
    @Test
    void testConstructor() {
        // ============ Valid Case ================
        assertDoesNotThrow(() -> new Tube(1, new Ray(Point.ZERO, new Vector(0, 0, 1))),
                "Constructor failed with valid input");

        // ============ Invalid Cases =============

        // Case 1: Radius is zero
        assertThrows(IllegalArgumentException.class,
                () -> new Tube(0, new Ray(Point.ZERO, new Vector(0, 0, 1))),
                "Constructor should throw exception for radius = 0");

        // Case 2: Radius is negative
        assertThrows(IllegalArgumentException.class,
                () -> new Tube(-1, new Ray(Point.ZERO, new Vector(0, 0, 1))),
                "Constructor should throw exception for negative radius");

        // Case 3: Direction vector is zero
        assertThrows(IllegalArgumentException.class,
                () -> new Tube(1, new Ray(Point.ZERO, new Vector(0, 0, 0))),
                "Constructor should throw exception for zero direction vector");
    }

    /**
     * Test method for {@link geometries.Tube#getNormal(Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple test for the getNormal function
        assertEquals(new Vector(1, 0, 0), tube.getNormal(new Point(1, 0, 1)), "Bad normal to tube");

        // =============== Boundary Values Tests =================
        // TC02: (p-p0) is orthogonal to the axis of the tube
        assertEquals(new Vector(1, 0, 0), tube.getNormal(new Point(1, 0, 0)), "Bad normal to tube");

    }
}