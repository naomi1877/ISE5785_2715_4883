package geometries;

import primitives.Point;
import primitives.Vector;

public class Plane extends Geometry {
    protected final Point p;
    protected final Vector normal;

    public Plane(Point p1, Point p2, Point p3) {
        super();
        p = p1;
        normal = null;
    }

    public Plane(Point p, Vector normal) {
        this.p = p;
        this.normal = normal.normalize();
    }

    public Vector getNormal(Point unused) {
        return normal;
    }
}
