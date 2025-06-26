package primitives;

/**
 * Represents a point in 3-dimensional space.
 * This class provides operations to add vectors to points, subtract points, and calculate distances.
 */
public class Point {

    /**
     * A constant representing the point (0, 0, 0).
     */
    public static final Point ZERO = new Point(0.0, 0.0, 0.0);

    /**
     * The coordinates of the point represented as a {@link Double3} object.
     */
    protected final Double3 xyz;

    /**
     * Constructs a point from the given x, y, and z coordinates.
     *
     * @param x The x-coordinate of the point.
     * @param y The y-coordinate of the point.
     * @param z The z-coordinate of the point.
     */
    public Point(double x, double y, double z) {
        xyz = new Double3(x, y, z);
    }

    /**
     * Constructs a point from the given {@link Double3} object representing the x, y, and z coordinates.
     *
     * @param xyz The {@link Double3} object representing the point's coordinates.
     */
    public Point(Double3 xyz) {
        this.xyz = xyz;
    }

    /**
     * Returns a vector representing the difference between this point and another point.
     *
     * @param other The other point.
     * @return A vector representing the difference between the two points.
     */
    public Vector subtract(Point other) {
        return new Vector(xyz.subtract(other.xyz));
    }

    /**
     * Adds a vector to this point and returns the resulting point.
     *
     * @param vector The vector to add to the point.
     * @return A new point representing the result of the addition.
     */
    public Point add(Vector vector) {
        return new Point(xyz.add(vector.xyz));
    }

    /**
     * Returns the squared distance between this point and another point.
     *
     * @param other The other point.
     * @return The squared distance between the two points.
     */
    public double distanceSquared(Point other) {
        double x2x1 = xyz.d1() - other.xyz.d1();
        double y2y1 = xyz.d2() - other.xyz.d2();
        double z2z1 = xyz.d3() - other.xyz.d3();
        return x2x1 * x2x1 + y2y1 * y2y1 + z2z1 * z2z1;
    }

    /**
     * Returns the distance between this point and another point.
     *
     * @param other The other point.
     * @return The distance between the two points.
     */
    public double distance(Point other) {
        return Math.sqrt(distanceSquared(other));
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point other)) return false;
        return xyz.equals(other.xyz);
    }

    @Override
    public int hashCode() {
        return xyz.hashCode();
    }


    @Override
    public String toString() {
        return xyz.toString();
    }


    public double getX() {

        return xyz.d1();
    }

    public double getY() {
        return xyz.d2();
    }


    public double getZ() {
        return xyz.d3();
    }

}
