package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * This class represents a Tube, a 3D shape defined by a radius and an axis (a Ray).
 * It extends the RadialGeometry class to inherit the radius property.
 * The Tube class does not currently implement the getNormal method.
 *
 */
public class Tube extends RadialGeometry {
    /**
     * The axis of the tube, represented by a Ray.
     */
    protected final Ray axis;

    /**
     * Constructor to initialize a Tube object with a radius and an axis.
     *
     * @param radius the radius of the tube
     * @param axis the axis of the tube (Ray)
     */
    public Tube(double radius, Ray axis) {
        super(radius);
        this.axis = axis;
    }

    /**
     * Gets the normal vector of the Tube at a specific point.
     *
     * @param p1 the point on the surface of the tube
     * @return null (currently not implemented)
     */
    @Override
    public Vector getNormal(Point p1) {
        return null;
    }
}
