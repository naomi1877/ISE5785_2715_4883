package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

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
     *
     * @param radius the radius of the tube
     * @param axis   the axis of the tube (Ray)
     */
    public Tube(double radius, Ray axis) {
        super(radius);
        this.axis = axis;
    }

    @Override
    public Vector getNormal(Point point) {
        //Calculate the projection of the point onto the cylinder axis.
        //Obtain the angle between the cylinder vector and the vector generated from the head point in the direction of the given point.
        double angleValue = (axis.getDirection()).dotProduct(point.subtract(axis.getHead()));
        //Calculating the center point of the Tube closest to the given point.
        Point centerTubePoint = axis.getPoint(angleValue);
        //return the normal - calculated like the Sphere's normal.
        return (point.subtract(centerTubePoint)).normalize();
    }

    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
        //Tube is infinite, so we need to calculate the intersection with the tube's axis
        Point p0 = axis.getHead();
        Vector v = axis.getDirection();
        //axisHeadToRayHead: the vector from the tube's axis to the ray's head
        Vector u = ray.getHead().subtract(p0);
        //Calculate the projection of the vector from the ray's head to the tube's axis onto the tube's axis
        double impoU = v.dotProduct(u);       //impoU: the projection of the vector from the tube's axis to the ray's head onto the tube's axis
        double impoRay = v.dotProduct(ray.getDirection());        //impoRay: the projection of the ray's direction onto the tube's axis
        //Check if the ray is parallel to the tube's axis
        if (isZero(ray.getDirection().crossProduct(v).lengthSquared())) {
            //The ray is parallel to the tube's axis
            return null;
        }
        //ΔP⊥: component of u orthogonal to v
        Vector vPerpendicular = isZero(impoU) ? u : u.subtract(v.scale(impoU));
        // component of ray orthogonal to v
        Vector rayPerpendicular = isZero(impoRay) ? ray.getDirection() : ray.getDirection().subtract(v.scale(impoRay));

        //At^2 +Bt+C=0 : the equation of the intersection between the ray and the tube
        //Quadratic coefficients for the intersection equation
        double a = rayPerpendicular.lengthSquared();
        double b = alignZero(2 * rayPerpendicular.dotProduct(vPerpendicular));
        double c = alignZero(vPerpendicular.lengthSquared() - radius * radius);

        // Calculate the delta
        double delta = alignZero((b * b) - (4 * a * c));
        if (delta < 0) {//|| isZero(delta)
            return null; // No intersection
        }
        // Calculate the intersection points
        double deltaSqrt = Math.sqrt(delta);
        double t1 = alignZero((-b - deltaSqrt) / (2 * a));
        double t2 = alignZero((-b + deltaSqrt) / (2 * a));
        // If t1 and t2 are both positive, return both intersection points
        if (t1 > 0 && t2 > 0) {
            //If both intersection points are positive, return both points
            //Sort the points by distance from the ray's head
            if (t1 > t2) {
                double temp = t1;
                t1 = t2;
                t2 = temp;
            }
            return List.of(new Intersection(this, ray.getPoint(t1)), new Intersection(this, ray.getPoint(t2)));
        }
        // If one of the intersection points is zero, return the other point
        if (t1 > 0) {
            return List.of(new Intersection(this, ray.getPoint(t1)));
        }
        if (t2 > 0) {
            return List.of(new Intersection(this, ray.getPoint(t2)));
        }
        return null; // No intersection
    }
}
