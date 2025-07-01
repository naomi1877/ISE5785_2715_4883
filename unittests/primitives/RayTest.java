package primitives;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit tests for the {@link primitives.Ray} class.
 *
 * @author uthor Naomie Gronstein and Efrat Brinkman.
 */
class RayTest {
    /**
     * default constructor
     */
    public RayTest() {
    }

    /**
     * A point used in some tests.
     */
    private final Point p1 = new Point(1, 2, 3);
    /**
     * A point used in some tests.
     */
    private final Ray ray = new Ray(p1, new Vector(1, 0, 0));

    /**
     * Test method for {@link primitives.Ray#equals(Object)}.
     */
    @Test
    void getPoint() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: t is a negative number
        assertEquals(new Point(-1, 2, 3), ray.getPoint(-2), "Bad getPoint with negative t");

        // TC02: t is a positive number
        assertEquals(new Point(3, 2, 3), ray.getPoint(2), "Bad getPoint with positive t");

        // =============== Boundary Values Tests =================
        // TC03: t is zero
        assertEquals(p1, ray.getPoint(0), "Bad getPoint with t=0");
    }

    /**
     * Test method for {@link primitives.Ray#findClosestPoint(List)}.
     */
    @Test
    void testFindClosestPoint() {
        Ray ray = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0));
        //=========== Equivalence Partitions Tests ==============
        // TC01: closest point is in the middle of the list
        List<Point> points = List.of(new Point(2, 0, 0), new Point(1, 0, 0), new Point(3, 0, 0));
        assertEquals(new Point(1, 0, 0), ray.findClosestPoint(points), "Bad findClosestPoint with closest point in the middle of the list");

        //============ Boundary Values Tests =================
        //BV01: null list
        assertNull(ray.findClosestPoint(null), "Bad findClosestPoint with null list");
        //BV02: closest point is the first point in the list
        points = List.of(new Point(1, 0, 0), new Point(2, 0, 0), new Point(3, 0, 0));
        assertEquals(new Point(1, 0, 0), ray.findClosestPoint(points), "Bad findClosestPoint with closest point is the first point in the list");
        //BV03: closest point is the last point in the list
        points = List.of(new Point(2, 0, 0), new Point(3, 0, 0), new Point(1, 0, 0));
        assertEquals(new Point(1, 0, 0), ray.findClosestPoint(points), "Bad findClosestPoint with closest point is the last point in the list");
    }
}
