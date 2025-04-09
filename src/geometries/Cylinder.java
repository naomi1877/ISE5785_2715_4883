package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import static primitives.Util.isZero;

/**
 * This class represents a Cylinder, a 3D shape defined by a radius, an axis (Ray), and a height.
 * It extends the Tube class.
 * The Cylinder class does not currently implement the getNormal method.
 * @ Author Naomie Gronstein && Efrat Brinkman.
 */
public class Cylinder extends Tube {
    /**
     * The height of the cylinder.
     */
    final double hight;

    /**
     * Constructor to initialize a Cylinder object with a radius, an axis, and a height.
     * @param radius the radius of the cylinder
     * @param axis the axis of the cylinder (Ray)
     * @param hight the height of the cylinder
     */
    public Cylinder(double radius, Ray axis, double hight) {
        super(radius, axis);
        this.hight = hight;
    }

    /**
     * Calculates the normal vector to the surface of the finite cylinder at a given point.
     * <p>
     * - If the point lies on one of the bases (top or bottom), the normal is the direction of the axis (or its opposite).<br>
     * - If the point lies on the lateral surface, the normal is computed as in the Tube.
     * @param point the point on the surface of the cylinder
     * @return the normalized normal vector at the given point
     */
    @Override
    public Vector getNormal(Point point) {
        // Vector from bottom base to the point
        Vector v = point.subtract(axis.getHead());
        // Projection of v on the axis direction
        double projection = axis.getDirection().dotProduct(v);

        // If point is on the bottom base
        if (isZero(projection)) {
            return axis.getDirection().scale(-1); // opposite to direction
        }
        // If point is on the top base
        if (isZero(projection - hight)) {
            return axis.getDirection();
        }
        // Otherwise, the point is on the side → delegate to Tube
        return super.getNormal(point);
    }
}
