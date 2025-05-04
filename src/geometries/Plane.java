package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

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
     * @param q the point on the plane
     * @param normal the normal vector of the plane
     */
    public Plane(Point q, Vector normal) {
        this.q = q;
        this.normal = normal.normalize();
    }

    /**
     * Calculates the normal vector to the plane at a given point.
     * The normal is constant for the entire plane.
     *
     * @param unused the point on the plane (not used in this implementation)
     * @return the normalized normal vector of the plane
     */
@Override
    public Vector getNormal(Point unused) {
        return normal;
    }

    /**
     * Calculates the intersection points between a ray and the plane.
     * The method checks if the ray is parallel to the plane and calculates the intersection point if not.
     *
     * @param ray the ray to check for intersections with the plane
     * @return a list of intersection points, or null if there are no intersections
     */
    @Override
    List<Point> findIntersections(Ray ray) {

        // Check if the ray is parallel to the plane
        double denominator = normal.dotProduct(ray.getDirection());

        if (isZero(denominator)) {
            return null; // The ray is parallel to the plane
        }

        if(q.equals(ray.getHead())){
            return null; // The ray starts on the plane
        }
        // Calculate the numerator for the intersection point
        double numerator = alignZero(normal.dotProduct(q.subtract(ray.getHead())));
        // Calculate the t parameter for the intersection point
        double t = alignZero(numerator / denominator);
        if (t <= 0) {
            return null; // The intersection point is behind the ray's head
        }

        // Calculate the intersection point
        Point intersectionPoint = ray.getHead().add(ray.getDirection().scale(t));
        return List.of(intersectionPoint);
    }
}
