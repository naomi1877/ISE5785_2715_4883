package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 * This class represents a Tube, a 3D shape defined by a radius and an axis (a Ray).
 * It extends the RadialGeometry class to inherit the radius property.
 * The Tube class does not currently implement the getNormal method.
 */
public class Tube extends RadialGeometry {
    /**
     * The axis of the tube, represented by a Ray.
     */
    protected final Ray axis;

    /**
     * Constructor to initialize a Tube object with a radius and an axis.
     * @param radius the radius of the tube
     * @param axis the axis of the tube (Ray)
     */
    public Tube(double radius, Ray axis) {
        super(radius);
        this.axis = axis;
    }

    /**
     * Calculates the normal vector to the surface of the infinite tube at a given point.
     * The normal is computed as the vector from the closest point on the tube's axis to the given point.
     * @param point the point on the surface of the tube
     * @return the normalized normal vector at the given point
     */
    @Override
    public Vector getNormal(Point point) {
        //Calculate the projection of the point onto the cylinder axis.
        //Obtain the angle between the cylinder vector and the vector generated from the head point in the direction of the given point.
        double angleValue=(axis.getDirection()).dotProduct(point.subtract(axis.getHead()));
        //Calculating the center point of the Tube closest to the given point.
        Point centerTubePoint=(axis.getHead()).add(axis.getDirection().scale(angleValue));
        //return the normal - calculated like the Sphere's normal.
        return (point.subtract(centerTubePoint)).normalize();
    }

    @Override
    List<Point> findIntersections(Ray ray) {
        return List.of();
    }
}
