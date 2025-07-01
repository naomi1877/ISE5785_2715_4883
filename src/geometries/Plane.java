package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;

/**
 * This class represents a Plane in 3D space, defined by a point and a normal vector.
 * It extends the Geometry class.
 */
public class Plane extends Geometry {
    /**
     * A point on the plane.
     */
    protected final Point q;

    /**
     * The normal vector of the plane.
     */
    protected final Vector normal;

    /**
     * Constructor to initialize a Plane object with three points
     *
     * @param p1 the first point defining the plane
     * @param p2 the second point defining the plane
     * @param p3 the third point defining the plane
     */
    public Plane(Point p1, Point p2, Point p3) {
        super();
        q = p1;
        Vector v1 = p2.subtract(p1);
        Vector v2 = p3.subtract(p1);
        normal = v1.crossProduct(v2).normalize();
    }

    /**
     * Constructor to initialize a Plane object with a point and a normal vector.
     *
     * @param q      the point on the plane
     * @param normal the normal vector of the plane
     */
    public Plane(Point q, Vector normal) {
        this.q = q;
        this.normal = normal.normalize();
    }


    @Override
    public Vector getNormal(Point unused) {
        return normal;
    }


    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray) {

        // Check if the ray is parallel to the plane
        double denominator = alignZero(normal.dotProduct(ray.getDirection()));

        if (denominator == 0) {
            // The ray is parallel to the plane
            return null;
        }
        if (q.equals(ray.getHead())) {
            // The ray starts on the plane
            return null;
        }
        // Calculate the numerator for the intersection point
        Vector q0 = q.subtract(ray.getHead());
        double numerator = alignZero(normal.dotProduct(q0));
        // Calculate the t parameter for the intersection point
        double t = alignZero(numerator / denominator);
        if (t <= 0) {
            // The intersection point is behind the ray's head
            return null;
        }

        // Calculate the intersection point
        Point intersectionPoint = ray.getPoint(t);
        return List.of(new Intersection(this, intersectionPoint));
    }

}

