package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * This class represents a Cylinder, a 3D shape defined by a radius, an axis (Ray), and a height.
 * It extends the Tube class.
 *
 * The Cylinder class does not currently implement the getNormal method.
 *
 * @author Dan Zilberstein
 */
public class Cylinder extends Tube {
    /**
     * The height of the cylinder.
     */
    final double hight;

    /**
     * Constructor to initialize a Cylinder object with a radius, an axis, and a height.
     *
     * @param radius the radius of the cylinder
     * @param axis the axis of the cylinder (Ray)
     * @param hight the height of the cylinder
     */
    public Cylinder(double radius, Ray axis, double hight) {
        super(radius, axis);
        this.hight = hight;
    }


    @Override
    public Vector getNormal(Point p1) {
        return null;
    }
}
