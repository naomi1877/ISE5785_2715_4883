package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link geometries.Sphere} class.
 *
 * @author uthor Naomie Gronstein and Efrat Brinkman.
 */
class SphereTest {
    /**
     * default constructor
     */
    public SphereTest() {
    }

    /**
     * Vector v1 with components (1, 0, 0).
     */
    Vector v1 = new Vector(1, 0, 0);

    /**
     * Point p1 with coordinates (0, 0, 0).
     */
    Point p1 = new Point(0, 0, 0);

    /**
     * Point p2 with coordinates (1, 0, 0).
     */
    Point p2 = new Point(1, 0, 0);
    /**
     * A point used in some tests
     */
    private final Point p100 = new Point(1, 0, 0);

    /**
     * Test method for {@link geometries.Sphere#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests =================
        // TC01: Simple test
        assertEquals(v1.normalize(), new Sphere(1, p1).getNormal(p2), "getNormal() wrong result");
        // =============== Boundary Values Tests =================
        // TC02: Test for a point on the sphere
        Sphere sphere = new Sphere(1, new Point(0, 0, 0));
        Point p = new Point(0, 0, 1);
        Vector expectedNormal = new Vector(0, 0, 1);
        Vector normal = sphere.getNormal(p);
        assertEquals(expectedNormal, normal, "Bad normal to sphere");

    }


    /**
     * Test method for {@link geometries.Sphere#findIntersections(primitives.Ray)}.
     */
    @Test
    public void testFindIntersections() {
        Sphere sphere = new Sphere(1d, p100);

        final Point gp1 = new Point(0.0651530771650466, 0.355051025721682, 0);
        final Point gp2 = new Point(1.53484692283495, 0.844948974278318, 0);
        final var exp = List.of(gp1, gp2);

        final Vector v310 = new Vector(3, 1, 0);
        final Vector v110 = new Vector(1, 1, 0);

        final Point p01 = new Point(-1, 0, 0);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray's line is outside the sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(p01, v110)), "Ray's line out of sphere");

        // TC02: Ray starts before and crosses the sphere (2 points)
        final var result1 = sphere.findIntersections(new Ray(p01, v310));
        assertNotNull(result1, "Can't be empty list");
        assertEquals(2, result1.size(), "Wrong number of points");
        assertEquals(exp, result1, "Ray crosses sphere");

        // TC03: Ray starts inside the sphere (1 point)
        Ray ray03 = new Ray(new Point(1.2, 0.8, 0), new Vector(1, 0, 0));
        var result03 = sphere.findIntersections(ray03);
        assertNotNull(result03, "Ray starts inside the sphere");
        assertEquals(1, result03.size(), "Wrong number of points");

        // TC04: Ray starts after the sphere (0 points)
        Ray ray04 = new Ray(new Point(3, 1, 0), new Vector(1, 0, 0));
        assertNull(sphere.findIntersections(ray04), "Ray starts after the sphere");

        // =============== Boundary Values Tests ==================
        // Group 1: Ray's line crosses the sphere (but not the center)
        // TC11: Ray starts at sphere and goes inside (1 point)
        Ray ray11 = new Ray(new Point(2, 0, 0), new Vector(-1, 1, 0));
        var result11 = sphere.findIntersections(ray11);
        assertNotNull(result11);
        assertEquals(1, result11.size());

        // TC12: Ray starts at sphere and goes outside (0 points)
        Ray ray12 = new Ray(new Point(2, 0, 0), new Vector(1, 0, 0));
        assertNull(sphere.findIntersections(ray12));

        // Group 2: Ray's line goes through the center
        // TC21: Ray starts before the sphere (2 points)
        Ray ray21 = new Ray(new Point(-1, 0, 0), new Vector(1, 0, 0));
        var result21 = sphere.findIntersections(ray21);
        assertNotNull(result21);
        assertEquals(2, result21.size());

        // TC22: Ray starts at sphere and goes inside (1 point)
        Ray ray22 = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0));
        var result22 = sphere.findIntersections(ray22);
        assertNotNull(result22);
        assertEquals(1, result22.size());

        // TC23: Ray starts inside (1 point)
        Ray ray23 = new Ray(new Point(1, 0.5, 0), new Vector(0, 1, 0));
        var result23 = sphere.findIntersections(ray23);
        assertNotNull(result23);
        assertEquals(1, result23.size());

        // TC24: Ray starts at the center (1 point)
        Ray ray24 = new Ray(new Point(1, 0, 0), new Vector(0, 1, 0));
        var result24 = sphere.findIntersections(ray24);
        assertNotNull(result24);
        assertEquals(1, result24.size());

        // TC25: Ray starts at sphere and goes outside (0 points)
        Ray ray25 = new Ray(new Point(0, 0, 0), new Vector(-1, 0, 0));
        assertNull(sphere.findIntersections(ray25));

        // TC26: Ray starts after sphere (0 points)
        Ray ray26 = new Ray(new Point(3, 0, 0), new Vector(1, 0, 0));
        assertNull(sphere.findIntersections(ray26));

        // Group 3: Ray is tangent to the sphere (0 points all)
        // TC31: Ray starts before the tangent point
        Ray ray31 = new Ray(new Point(0, 1, 0), new Vector(1, 0, 0));
        assertNull(sphere.findIntersections(ray31));

        // TC32: Ray starts at the tangent point
        Ray ray32 = new Ray(new Point(1, 1, 0), new Vector(1, 0, 0));
        assertNull(sphere.findIntersections(ray32));

        // TC33: Ray starts after the tangent point
        Ray ray33 = new Ray(new Point(2, 1, 0), new Vector(1, 0, 0));
        assertNull(sphere.findIntersections(ray33));

        // Group 4: Special cases
        // TC41: Ray is outside sphere, ray is orthogonal to ray-to-center line
        Ray ray41 = new Ray(new Point(0, 2, 0), new Vector(0, 0, 1));
        assertNull(sphere.findIntersections(ray41));

        // TC42: Ray starts inside, orthogonal to direction toward center
        Ray ray42 = new Ray(new Point(1, 0.5, 0), new Vector(0, 0, 1));
        var result42 = sphere.findIntersections(ray42);
        assertNotNull(result42);
        assertEquals(1, result42.size());

    }
}