package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

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

    /**
     * Calculates the intersection points between a ray and the cylinder.
     * The method uses the quadratic formula to find the intersection points.
     * @param ray the ray to check for intersections with the cylinder
     * @return a list of intersection points, or null if there are no intersections
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        //Cylinder is a tube with a limited height with two bases
        //Calculate the vector from the ray's head to the tube's axis
        Vector u = axis.getHead().subtract(ray.getHead());
        double tm = ray.getDirection().dotProduct(u);
        double d = Math.sqrt(u.lengthSquared() - tm * tm);
        if (d > radius) {
            return null; // No intersection
        }
        double th = Math.sqrt(radius * radius - d * d);
        //Check if the ray intersects the tube
        if (isZero(th)) {
            return List.of(ray.getPoint(tm));
        }
        // Calculate the intersection points
        double t1 = tm - th;
        double t2 = tm + th;
        // If t1 and t2 are both positive, return both intersection points
        if (t1 > 0 && t2 > 0) {
            // If both intersection points are positive, return both points
            Point p1 = ray.getPoint(t1);
            Point p2 = ray.getPoint(t2);
            // Check if the intersection points are within the height of the cylinder
            if (isZero(p1.subtract(axis.getHead()).dotProduct(axis.getDirection())) ||
                    isZero(p2.subtract(axis.getHead()).dotProduct(axis.getDirection()))) {
                return List.of(p1, p2);
            }
            return null; // No intersection within the height
        }
        // If one of the intersection points is zero, return the other point
        if (!isZero(t1)) {
            Point p1 = ray.getPoint(t1);
            // Check if the intersection point is within the height of the cylinder
            if (isZero(p1.subtract(axis.getHead()).dotProduct(axis.getDirection()))) {
                return List.of(p1);
            }
            return null; // No intersection within the height
        }
        // If both intersection points are negative, return null
        if (!isZero(t2)) {
            Point p2 = ray.getPoint(t2);
            // Check if the intersection point is within the height of the cylinder
            if (isZero(p2.subtract(axis.getHead()).dotProduct(axis.getDirection()))) {
                return List.of(p2);
            }
            return null; // No intersection within the height
        }
        // If both intersection points are negative, return null
        return null;
    }
}