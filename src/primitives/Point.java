
package primitives;

import java.util.Objects;

public class Point {


    public static final Point ZERO = new Point(0.0, 0.0, 0.0);
    protected final Double3 xyz;

    public Point(double x, double y, double z) {
        xyz = new Double3(x, y, z);
    }

    public Point(Double3 xyz) {
        this.xyz = xyz;
    }

    public Vector subtract(Point other) {
        return new Vector(xyz.subtract(other.xyz));
    }

    public Point add(Vector vector) {
        return new Point(xyz.add(vector.xyz));
    }

    public double distanceSquared(Point other) {
        double x2x1 = xyz.d1() - other.xyz.d1();
        double y2y1 = xyz.d2() - other.xyz.d2();
        double z2z1 = xyz.d3() - other.xyz.d3();
        return x2x1 * x2x1 + y2y1 * y2y1 + z2z1 * z2z1;
    }

    public double distance(Point other) {
        return Math.sqrt(distanceSquared(other));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return  true;
        return (o instanceof Point point) && xyz.equals(point.xyz);
    }

    @Override
    public int hashCode() {
        return xyz.hashCode();
    }

    @Override
    public String toString() {
        return xyz.toString();
    }
}
