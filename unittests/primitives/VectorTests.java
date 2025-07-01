package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.isZero;

/**
 * Unit tests for the {@link primitives.Vector} class.
 */
class VectorTests {
    /**
     * Error tolerance used for floating-point comparisons.
     */
    private final double DELTA = 0.00001;

    /**
     * A point in 3D space used for testing.
     */
    private final Point p1 = new Point(1, 2, 3);

    /**
     * A vector representing a point in 3D space.
     */
    private final Vector v1 = new Vector(1, 2, 3);

    /**
     * Another test vector with both positive and negative components.
     */
    private final Vector v2 = new Vector(4, 2, -3);

    /**
     * A vector that is the negation of v1.
     */
    private final Vector v3 = new Vector(-1, -2, -3);

    /**
     * Unit vector along the X-axis.
     */
    private final Vector v4 = new Vector(1, 0, 0);

    /**
     * Unit vector along the Y-axis.
     */
    private final Vector v5 = new Vector(0, 1, 0);

    /**
     * Unit vector along the Z-axis.
     */
    private final Vector v6 = new Vector(0, 0, 1);

    /**
     * Another test vector with mixed components.
     */
    private final Vector v7 = new Vector(-1, 2, -2);

    /**
     * Another test vector with mixed components.
     */
    private final Vector v8 = new Vector(2, 1, 1);

    /**
     * A private and final field representing a 3D vector.
     * The vector's values are (1.0, 2.0, 3.0).
     */
    private final Double3 d1 = new Double3(1, 2, 3);

    /**
     * Constructor for VectorTests class.
     * Contains test cases for vector initialization, covering equivalence partitions and boundary values.
     */
    public VectorTests() {
        //============ Equivalence Partitions Tests ==============
        // TC01: Correct vector initialization using individual components.
        assertEquals(v1, new Vector(1, 2, 3), "Wrong vector initialization with individual components.");

        // TC02: Correct vector initialization using a Double3 object.
        assertEquals(v1, new Vector(d1), "Wrong vector initialization with a Double3 object.");

        // =============== Boundary Values Tests ==================
        // TC03: Zero vector initialization (should throw IllegalArgumentException).
        assertThrows(IllegalArgumentException.class, () -> new Vector(0, 0, 0), "Zero vector initialization should throw IllegalArgumentException.");

        // TC04: Zero vector initialization using a Double3 object (should throw IllegalArgumentException).
        assertThrows(IllegalArgumentException.class, () -> new Vector(new Double3(0, 0, 0)), "Zero vector initialization with Double3 should throw IllegalArgumentException.");
    }

    /**
     * Test method for {@link primitives.Vector#lengthSquared()}.
     */
    @Test
    void testLengthSquared() {
        //============ Equivalence Partitions Tests ==============
        // TC01: Correct vector
        assertEquals(14, v1.lengthSquared(), DELTA, "Wrong result of length squared");

        // =============== Boundary Values Tests ==================
        // TC02: positive and negative vector
        assertEquals(9, v7.lengthSquared(), DELTA, "Wrong result of length squared");
    }

    /**
     * Test method for {@link primitives.Vector#normalize()}.
     */
    @Test
    void testNormalize() {
        //============ Equivalence Partitions Tests ==============
        // TC01: Correct vector
        assertEquals(1, v1.normalize().length(), "Wrong result of normalization");

        // =============== Boundary Values Tests ==================
        // TC02: zero vector
        Vector v = new Vector(1, 4, 7);
        assertThrows(IllegalArgumentException.class, () -> v.normalize().crossProduct(v), "Normalized a zero vector");

        //TC03: Correct vector normalized is the same vector
        assertEquals(v4, v4.normalize(), "Wrong result of normalization");

        //TC04: v.normalized*v bigger than 0
        assertTrue(v1.normalize().dotProduct(v1) > 0, "Normalized vector is not parallel to the original one");
    }

    /**
     * Test method for {@link primitives.Vector#length()}.
     */
    @Test
    void testLength() {
        //============ Equivalence Partitions Tests ==============
        // TC01: Correct vector
        assertEquals(3, new Vector(1, -2, -2).length(), DELTA, "Wrong result of length");

        // =============== Boundary Values Tests ==================
        // TC02: positive and negative vector
        assertEquals(3, v7.length(), DELTA, "Wrong result of length");
    }

    /**
     * Test method for {@link primitives.Vector#dotProduct(Vector)}.
     */
    @Test
    void testDotProduct() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Correct vector
        assertEquals(-1, v1.dotProduct(v2), DELTA, "Wrong result of dot product");

        // =============== Boundary Values Tests ==================
        // TC02: one of the vectors is the singularity vector
        assertEquals(1, v1.dotProduct(v4), DELTA, "Dot product with singularity vector");

        // TC03: dot product with zero vector
        assertEquals(0, v4.dotProduct(v6), DELTA, "Dot product with zero vector");
    }

    /**
     * Test method for {@link primitives.Vector#crossProduct(Vector)}.
     */
    @Test
    void testCrossProduct() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Correct vector
        assertEquals(v4, v5.crossProduct(v6), "Wrong result of cross product");
        //check dot product with the result
        assertTrue(isZero(v6.dotProduct(v4)), "Cross product is not orthogonal to its operands");
        assertTrue(isZero(v5.dotProduct(v4)), "Cross product is not orthogonal to its operands");

        // =============== Boundary Values Tests ==================
        Vector vr = v1.crossProduct(v8);

        // TC02: the vectors are parallel
        assertThrows(IllegalArgumentException.class, () -> v1.crossProduct(new Vector(2, 4, 6)), "did not constructed a zero vector");

        // TC03: the vector are equal
        assertThrows(IllegalArgumentException.class, () -> v1.crossProduct(v1), "did not constructed a zero vector");

        // TC04: the cross product is not orthogonal to its operands
        assertTrue(isZero(vr.dotProduct(v1)) && isZero(vr.dotProduct(v8)), "Cross product is not orthogonal to its operands");

        // TC05: the vectors are the same length but not parallel
        assertEquals(new Vector(1, 7, -5), v1.crossProduct(new Vector(3, 1, 2)), "Wrong result of cross product");
    }

    /**
     * Test method for {@link primitives.Vector#scale(double)}.
     */
    @Test
    void testScale() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Correct vector
        assertEquals(new Vector(3, 6, 9), v1.scale(3), "Wrong result of scaling a vector");

        // =============== Boundary Values Tests ==================
        // TC02: scaling by zero
        assertThrows(IllegalArgumentException.class, () -> v1.scale(0), "Constructed a zero vector");
    }

    /**
     * Test method for {@link primitives.Vector#add(primitives.Vector)}.
     */
    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Correct vector
        assertEquals(new Vector(5, 4, 0), v1.add(v2), "Wrong result of adding two vectors");

        // =============== Boundary Values Tests ==================
        // TC02: result is zero vector
        assertThrows(IllegalArgumentException.class, () -> v1.add(v3), "Constructed a zero vector");
    }

    /**
     * Test method for {@link primitives.Vector#subtract(primitives.Point)}.
     */
    @Test
    void testSubtract() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Subtract two points
        assertEquals(v1, new Point(2, 4, 6).subtract(p1), "Subtract two points does not work correctly");

        // ============ Boundary Values Tests ==================
        // TC02: Subtract equal points
        assertThrows(IllegalArgumentException.class, () -> p1.subtract(p1), "Subtract equal points does not work correctly");
    }
}