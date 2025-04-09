package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link geometries.Sphere} class.
 * @ Author Naomie Gronstein && Efrat Brinkman.
 */
class SphereTest {

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
     * Test method for {@link geometries.Sphere#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests =================
        // TC01: Simple test
        assertEquals(v1.normalize(), new Sphere(1, p1).getNormal(p2), "getNormal() wrong result");
    }
}