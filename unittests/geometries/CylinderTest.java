package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link geometries.Cylinder} class.
 *
 * @author uthor Naomie Gronstein and Efrat Brinkman.
 */
class CylinderTest {
    /**
     * default constructor
     */
    public CylinderTest() {
    }

    /**
     * Vector v1 with components (0, 0, -1).
     */
    private final Vector v1 = new Vector(0, 0, -1);

    /**
     * Vector v2 with components (0, 0, 1).
     */
    private final Vector v2 = new Vector(0, 0, 1);

    /**
     * Cylinder created with a ray starting at "Point.ZERO" and direction v2 with radius 1 and height 1.
     */
    private final Cylinder cylinder = new Cylinder(1, new Ray(Point.ZERO, v2), 1);

    /**
     * Test method for {@link geometries.Cylinder} constructor.
     */
    @Test
    void testConstructor() {
        // ============ Valid Case ================
        assertDoesNotThrow(() -> new Cylinder(1, new Ray(Point.ZERO, new Vector(0, 0, 1)), 5),
                "Constructor failed with valid input");

        // ============ Invalid Cases =============

        // Case 1: Radius is zero
        assertThrows(IllegalArgumentException.class,
                () -> new Cylinder(0, new Ray(Point.ZERO, new Vector(0, 0, 1)), 5),
                "Constructor should throw exception for radius = 0");

        // Case 2: Radius is negative
        assertThrows(IllegalArgumentException.class,
                () -> new Cylinder(-1, new Ray(Point.ZERO, new Vector(0, 0, 1)), 5),
                "Constructor should throw exception for negative radius");

        // Case 3: Height is zero
        assertThrows(IllegalArgumentException.class,
                () -> new Cylinder(1, new Ray(Point.ZERO, new Vector(0, 0, 1)), 0),
                "Constructor should throw exception for height = 0");

        // Case 4: Height is negative
        assertThrows(IllegalArgumentException.class,
                () -> new Cylinder(1, new Ray(Point.ZERO, new Vector(0, 0, 1)), -2),
                "Constructor should throw exception for negative height");

        // Case 5: Direction vector is zero
        assertThrows(IllegalArgumentException.class,
                () -> new Cylinder(1, new Ray(Point.ZERO, new Vector(0, 0, 0)), 2),
                "Constructor should throw exception for zero direction vector");
    }

    /**
     * Test method for {@link geometries.Cylinder#getNormal(Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: the point is on the top outer surface of the cylinder
        assertEquals(new Vector(0, 1, 0),
                cylinder.getNormal(new Point(0, 1, 0.5)),
                "Bad normal to cylinder1");

        // TC02: the point is on the bottom outer surface of the cylinder
        assertEquals(v1, cylinder.getNormal(new Point(0, 0.5, 0)),
                "Bad normal to cylinder2");

        // TC03: the point is on the side outer surface of the cylinder
        assertEquals(v2, cylinder.getNormal(new Point(0, 0.5, 1)),
                "Bad normal to cylinder3");

        // =============== Boundary Values Tests ==================
        // TC04: the point is on the top edge of the cylinder;
        assertEquals(v2, cylinder.getNormal(new Point(0, 1, 1)),
                "Bad normal to cylinder4");

        // TC05: the point is on the bottom edge of the cylinder
        assertEquals(v1, cylinder.getNormal(new Point(0, 1, 0)),
                "Bad normal to cylinder5");

        // TC06: the point is in the middle bottom outer surface of the cylinder
        assertEquals(v1, cylinder.getNormal(Point.ZERO),
                "Bad normal to cylinder6");

        // TC07: the point is in the middle top edge of the cylinder
        assertEquals(v2, cylinder.getNormal(new Point(0, 0, 1)),
                "Bad normal to cylinder7");
    }

}