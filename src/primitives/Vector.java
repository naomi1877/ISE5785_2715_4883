

package primitives;

public class Vector extends Point {

    public Vector(double x, double y, double z) {
        this(new Double3(x, y, z));
    }

    public Vector(Double3 xyz) {
        super(xyz);
        if (xyz.equals(Double3.ZERO)) {
            throw new IllegalArgumentException("Vector cannot be Vector(0, 0, 0)");
        }
    }

    public double lengthSquared() {
        return xyz.d1() * xyz.d1() + xyz.d2() * xyz.d2() + xyz.d3() * xyz.d3();
    }

    public double length() {
        return Math.sqrt(lengthSquared());
    }

    public Vector normalize() {
        double len = length();
        return new Vector(xyz.scale(1.0 / len));
    }

    public Vector crossProduct(Vector u) {
        double xx = xyz.d2() * u.xyz.d3() - xyz.d3() * u.xyz.d2();
        double yy = xyz.d3() * u.xyz.d1() - xyz.d1() * u.xyz.d3();
        double zz = xyz.d1() * u.xyz.d2() - xyz.d2() * u.xyz.d1();

        return new Vector(xx, yy, zz);
    }

    public double dotProduct(Vector u) {
        double xx = xyz.d1() * u.xyz.d1();
        double yy = xyz.d2() * u.xyz.d2();
        double zz = xyz.d3() * u.xyz.d3();
        return xx + yy + zz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return(o instanceof Vector vector)&& xyz.equals(vector.xyz) ;
    }

    public Vector add(Vector other) {
        return new Vector(xyz.add(other.xyz));
    }

    @Override
    public String toString() {
        return xyz.toString();
    }

    public Vector scale(double rhs) {
        return new Vector(xyz.scale(rhs));
    }

}


