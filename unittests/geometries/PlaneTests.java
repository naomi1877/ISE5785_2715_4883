package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link geometries.Plane} class.
 *
 * @author uthor Naomie Gronstein and Efrat Brinkman.
 */
class PlaneTests {
    /**
     * default constructor
     */
    public PlaneTests() {

    }

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

    /**
     * Test method for {@link geometries.Plane#findIntersections(primitives.Ray)}.
     */
    @Test
    void testFindIntersections() {
        // =============== Equivalence Partitions Tests ===============

        // EP01: Ray intersects the plane
        Plane plane = new Plane(new Point(0, 0, 1), new Vector(0, 0, 1));
        Ray ray = new Ray(new Point(0, 0, 0), new Vector(0, 0, 2));

        assertEquals(List.of(new Point(0, 0, 1)), plane.findIntersections(ray), "EP01: Ray should intersect the plane");

        // EP02: Ray does not intersect the plane
        ray = new Ray(new Point(0, 0, 2), new Vector(0, 0, 1));
        assertNull(plane.findIntersections(ray), "EP02: Ray does not intersect the plane (going away)");

        // =============== Boundary Values Tests ===============

        // BVA01: Ray is parallel and lies on the plane
        plane = new Plane(new Point(0, 0, 1), new Vector(0, 1, 0));
        ray = new Ray(new Point(0, 0, 1), new Vector(1, 0, 0));
        assertNull(plane.findIntersections(ray), "BVA01: Ray lies on the plane - no intersection");

        // BVA02: Ray is parallel and not on the plane
        ray = new Ray(new Point(0, 0, 2), new Vector(1, 0, 0));
        assertNull(plane.findIntersections(ray), "BVA02: Ray is parallel and not on the plane");

        // BVA03: Ray is orthogonal and starts before the plane
        plane = new Plane(new Point(0, 0, 1), new Vector(0, 0, 1));
        ray = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        assertEquals(List.of(new Point(0, 0, 1)), plane.findIntersections(ray), "BVA03: Orthogonal ray should intersect");

        // BVA04: Ray is orthogonal and starts on the plane
        ray = new Ray(new Point(0, 0, 1), new Vector(0, 0, 1));
        assertNull(plane.findIntersections(ray), "BVA04: Orthogonal ray starts on the plane - no intersection");

        // BVA05: Ray is orthogonal and starts after the plane
        ray = new Ray(new Point(0, 0, 2), new Vector(0, 0, 1));
        assertNull(plane.findIntersections(ray), "BVA05: Orthogonal ray after the plane - no intersection");

        // BVA06: Ray starts exactly at q (the reference point of the plane) - no intersection
        Point q = new Point(1, 1, 1);
        plane = new Plane(q, new Vector(0, 0, 1));
        ray = new Ray(q, new Vector(1, 1, 1));
        assertNull(plane.findIntersections(ray), "BVA06: Ray starts at q - no intersection");


        // BVA07: Ray starts on the plane (not from q), not parallel nor orthogonal
        plane = new Plane(new Point(0, 0, 0), new Vector(0, 1, 0));
        Point p0 = new Point(1, 0, 1); // on the plane since y = 0
        ray = new Ray(p0, new Vector(1, 1, 0));
        assertNull(plane.findIntersections(ray), "BVA07: Ray starts on the plane - no intersection");


    }

}