package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link geometries.Tube} class.
 *
 * @author uthor Naomie Gronstein and Efrat Brinkman.
 */
class TubeTest {
    /**
     * A cylindrical tube with its axis along the Z-axis and a radius of 1.
     */
    private final Tube tube = new Tube(1, new Ray(Point.ZERO, new Vector(0, 0, 1)));

    /**
     * defult constractur
     */
    public TubeTest() {
    }

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

    /**
     * Test method for {@link geometries.Tube#findIntersections(primitives.Ray)}.
     */
    @Test
    void testFindIntersections() {
        //create a infinite tube that does not sit on the coordinate center for the tests
        //Building unit tests, at least 40 different relevant tests on the next tube (tube2):
        Tube tube2 = new Tube(1, new Ray(new Point(1, 1, 1), new Vector(0, 0, 1)));
        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray's line is outside the tube (0 points)
        assertNull(tube2.findIntersections(new Ray(new Point(-1, 0, 0), new Vector(-1, -1, 1))), "Ray's line out of tube");
        // TC02: Ray starts before and crosses the tube (2 points)
        List<Point> result1 = tube2.findIntersections(new Ray(new Point(0, 0, 1), new Vector(1, 1, 1)));
        assertNotNull(result1, "Can't be empty list");
        assertEquals(2, result1.size(), "Wrong number of points");
        assertEquals(List.of(new Point(0.2928932188134525, 0.2928932188134525, 1.2928932188134525), new Point(1.7071067811865475, 1.7071067811865475, 2.7071067811865475)), result1, "Ray crosses tube");
        // TC03: Ray starts inside the tube (1 point)
        // assertEquals(List.of(new Point(), tube2.findIntersections(new  Ray(new Point(1.5, 1, 2), new Vector(1, 0.5, 0)), "Ray starts inside the tube");
    }

}