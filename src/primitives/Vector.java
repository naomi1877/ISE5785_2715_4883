package primitives;

/**
 * Represents a vector in 3-dimensional space.
 * This class extends the {@link Point} class and adds vector-specific operations like cross product, dot product, and normalization.
 */
public class Vector extends Point {

    /**
     * A constant representing the zero vector (0, 0, 0).
     */
    public static final Vector AXIS_Z = new Vector(0, 0, 1);

    /**
     * A constant representing the unit vector along the Y-axis (0, 1, 0).
     */
    public static final Vector AXIS_Y = new Vector(0, 1, 0);

    /**
     * A constant representing the unit vector along the X-axis (1, 0, 0).
     */
    public static final Vector AXIS_X = new Vector(1, 0, 0);

    /**
     * Constructs a vector from the given x, y, and z coordinates.
     *
     * @param x The x-coordinate of the vector.
     * @param y The y-coordinate of the vector.
     * @param z The z-coordinate of the vector.
     */
    public Vector(double x, double y, double z) {
        this(new Double3(x, y, z));
    }

    /**
     * Constructs a vector from the given {@link Double3} object representing the x, y, and z coordinates.
     *
     * @param xyz The {@link Double3} object representing the vector's coordinates.
     * @throws IllegalArgumentException If the vector is a zero vector (0, 0, 0).
     */
    public Vector(Double3 xyz) {
        super(xyz);
        if (xyz.equals(Double3.ZERO)) {
            throw new IllegalArgumentException("Vector cannot be Vector(0, 0, 0)");
        }
    }

    /**
     * Returns the squared length of the vector.
     *
     * @return The squared length of the vector.
     */
    public double lengthSquared() {
        return xyz.d1() * xyz.d1() + xyz.d2() * xyz.d2() + xyz.d3() * xyz.d3();
    }

    /**
     * Returns the length (magnitude) of the vector.
     *
     * @return The length of the vector.
     */
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    /**
     * Normalizes the vector to a unit vector (length of 1).
     *
     * @return A new normalized vector.
     */
    public Vector normalize() {
        double len = length();
        return new Vector(xyz.scale(1.0 / len));
    }

    /**
     * Returns the cross product of this vector with another vector.
     *
     * @param u The other vector.
     * @return A new vector representing the cross product of this vector and the input vector.
     */
    public Vector crossProduct(Vector u) {
        double xx = xyz.d2() * u.xyz.d3() - xyz.d3() * u.xyz.d2();
        double yy = xyz.d3() * u.xyz.d1() - xyz.d1() * u.xyz.d3();
        double zz = xyz.d1() * u.xyz.d2() - xyz.d2() * u.xyz.d1();

        return new Vector(xx, yy, zz);
    }

    /**
     * Returns the dot product of this vector with another vector.
     *
     * @param u The other vector.
     * @return The dot product of this vector and the input vector.
     */
    public double dotProduct(Vector u) {
        double xx = xyz.d1() * u.xyz.d1();
        double yy = xyz.d2() * u.xyz.d2();
        double zz = xyz.d3() * u.xyz.d3();

        return xx + yy + zz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector vector)) return false;
        return xyz.equals(vector.xyz);
    }

    /**
     * Adds this vector to another vector and returns the resulting vector.
     *
     * @param other The other vector to add.
     * @return A new vector representing the sum of the two vectors.
     */
    public Vector add(Vector other) {
        return new Vector(xyz.add(other.xyz));
    }


    /**
     * Scales this vector by a scalar value and returns the resulting vector.
     *
     * @param rhs The scalar value to multiply the vector by.
     * @return A new vector representing the scaled vector.
     */
    public Vector scale(double rhs) {
        return new Vector(xyz.scale(rhs));
    }
}
