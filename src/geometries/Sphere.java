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
     *
     * @param radius the radius of the sphere
     * @param center the center of the sphere (Point)
     */
    public Sphere(double radius, Point center) {
        super(radius);
        this.center = center;
    }

    /**
     * Gets the normal vector of the Sphere at a specific point.
     *
     * @param p1 the point on the surface of the sphere
     * @return null (currently not implemented)
     */
    @Override
    public Vector getNormal(Point p1) {
        return null;
    }
}
