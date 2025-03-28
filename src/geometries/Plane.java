package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * This class represents a Plane in 3D space, defined by a point and a normal vector.
 * It extends the Geometry class.
 */
public class Plane extends Geometry {
    /**
     * A point on the plane.
     */
    protected final Point p;

    /**
     * The normal vector of the plane.
     */
    protected final Vector normal;

    /**
     * Constructor to initialize a Plane object with three points.
     *
     * @param p1 the first point defining the plane
     * @param p2 the second point defining the plane
     * @param p3 the third point defining the plane
     */
    public Plane(Point p1, Point p2, Point p3) {
        super();
        p = p1;
        normal = null; // Not implemented yet
    }

    /**
     * Constructor to initialize a Plane object with a point and a normal vector.
     *
     * @param p the point on the plane
     * @param normal the normal vector of the plane
     */
    public Plane(Point p, Vector normal) {
        this.p = p;
        this.normal = normal.normalize();
    }

    /**
     * Gets the normal vector of the Plane.
     *
     * @param unused unused point (not used)
     * @return the normal vector of the plane
     */
    public Vector getNormal(Point unused) {
        return normal;
    }
}
