package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.isZero;

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


    @Override
    public Vector getNormal(Point point) {
        return (point.subtract(center)).normalize();
    }


    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
        Vector v = ray.getDirection();
        Point p = ray.getHead();

        // Check if the ray's head is at the center of the sphere
        if (p.equals(center))
            return List.of(new Intersection(this, ray.getPoint(radius)));

        // Calculate the vector from the ray's head to the sphere's center

        Vector u = center.subtract(ray.getHead());
        double tm = v.dotProduct(u);
        double d = Math.sqrt(u.lengthSquared() - tm * tm);
        double th = Math.sqrt(radius * radius - d * d);

        // Check if the ray intersects the sphere
        if (d >= radius) {
            return null; // No intersection
        }
        // Calculate the intersection points
        double t1 = tm - th;
        double t2 = tm + th;


        // If t1 and t2 are both positive, return both intersection points
        if (t1 > 0 && t2 > 0) {
            // If t1 and t2 are equal, return only one point
            if (isZero(t1 - t2)) {
                return List.of(new Intersection(this, ray.getPoint(t1)));
            }

            // If both intersection points are positive, return both points
            return List.of(new Intersection(this, ray.getPoint(t1)), new Intersection(this, ray.getPoint(t2)));
        }

        // If t1 is positive, return t1
        if (t1 > 0) {
            return List.of(new Intersection(this, ray.getPoint(t1)));
        }
        // If t2 is positive, return t2
        if (t2 > 0) {
            return List.of(new Intersection(this, ray.getPoint(t2)));
        }
        // If both t1 and t2 are negative, return null
        return null;

    }
}
