package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * This is an abstract class representing a general 3D geometry object.
 * Any geometry must implement the method to return its normal vector at a specific point.
 */
public abstract class Geometry {

    /**
     * Gets the normal vector of the geometry at a specific point.
     *
     * @param p1 the point on the geometry's surface
     * @return the normal vector of the geometry at the point
     */
    public abstract Vector getNormal(Point p1);
}
