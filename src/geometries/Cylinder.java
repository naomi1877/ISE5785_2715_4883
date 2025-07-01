package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.ArrayList;
import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * This class represents a Cylinder, a 3D shape defined by a radius, an axis (Ray), and a height.
 * It extends the Tube class.
 * The Cylinder class does not currently implement the getNormal method.
 *
 * @author uthor Naomie Gronstein and Efrat Brinkman.
 */
public class Cylinder extends Tube {
    /**
     * The height of the cylinder.
     */
    final double height;

    /**
     * Constructor to initialize a Cylinder object with a radius, an axis, and a height.
     *
     * @param radius the radius of the cylinder
     * @param axis   the axis of the cylinder (Ray)
     * @param height the height of the cylinder
     */
    public Cylinder(double radius, Ray axis, double height) {
        super(radius, axis);
        if (isZero(height) || height < 0) {
            throw new IllegalArgumentException("Height cannot be zero");
        } else this.height = height;
    }

    @Override
    public Vector getNormal(Point point) {
        if (point.equals(axis.getHead()))//////////////////////////////////////////////////;//
            return axis.getNormalVec().scale(-1);
        double t = axis.getNormalVec().dotProduct(point.subtract(axis.getHead()));
        if (t == height) {
            return axis.getNormalVec();
        }
        if (t == 0) {
            return axis.getNormalVec().scale(-1);
        } else {
            return super.getNormal(point);
        }
    }

    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
        List<Intersection> intersections = super.calculateIntersectionsHelper(ray);
        if (intersections != null) {

            intersections = new ArrayList<>(intersections);

            //filter intersections based on the height of the cylinder
            intersections.removeIf(intersection -> {
                double projection = axis.getDirection()
                        .dotProduct(intersection.point.subtract(axis.getHead()));
                return projection < 0 || projection > height;
            });
        } else {
            intersections = new ArrayList<>();
        }

        Vector v = axis.getDirection();
        Point base1 = axis.getHead();
        Point base2 = axis.getPoint(height);
        Vector rayDir = ray.getDirection();
        Point rayStart = ray.getHead();

        // Calculate intersection with the curved surface of the cylinder
        double nv1 = v.dotProduct(rayDir);
        if (!isZero(nv1)) {
            double t1 = alignZero(v.dotProduct(base1.subtract(rayStart)) / nv1);
            if (t1 > 0) {
                Point p = ray.getPoint(t1);
                if (alignZero(p.distanceSquared(base1) - radius * radius) <= 0) {
                    intersections.add(new Intersection(this, p));
                }
            }
        }

        // Calculate intersection with the other base of the cylinder
        double nv2 = v.dotProduct(rayDir);
        if (!isZero(nv2)) {
            double t2 = alignZero(v.dotProduct(base2.subtract(rayStart)) / nv2);
            if (t2 > 0) {
                Point p = ray.getPoint(t2);
                if (alignZero(p.distanceSquared(base2) - radius * radius) <= 0) {
                    intersections.add(new Intersection(this, p));
                }
            }
        }

        return intersections.isEmpty() ? null : intersections;
    }

}