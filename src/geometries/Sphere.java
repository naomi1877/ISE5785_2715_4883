package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * This class represents a Sphere, a 3D shape defined by a center and a radius.
 * It extends the RadialGeometry class to inherit the radius property.
 * The Sphere class does not currently implement the getNormal method.
 */
public class Sphere extends RadialGeometry {
    /**
     * The center of the sphere, represented by a Point.
     */
    final Point center;

    /**
     * Constructor to initialize a Sphere object with a radius and a center.
     * @param radius the radius of the sphere
     * @param center the center of the sphere (Point)
     */
    public Sphere(double radius, Point center) {
        super(radius);
        this.center = center;
    }

    /**
     * Calculates the normal vector to the surface of the sphere at a given point.
     * The normal is the normalized vector from the center of the sphere to the point.
     * @param point the point on the surface of the sphere
     * @return the normalized normal vector at the given point
     */
    @Override
    public Vector getNormal(Point point) {
        return (point.subtract(center)).normalize();
    }
}
