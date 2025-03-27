package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

public class Cylinder extends Tube {
    final double hight;

    public Cylinder(double radius, Ray axis, double hight) {
        super(radius, axis);
        this.hight = hight;
    }

    @Override
    public Vector getNormal(Point p1) {
        return null;
    }
}

