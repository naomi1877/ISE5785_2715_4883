package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.ArrayList;
import java.util.LinkedList;
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
     * @param height  the height of the cylinder
     */
    public Cylinder(double radius, Ray axis, double height) {
        super(radius, axis);
        if (isZero(height) || height < 0) {
            throw new IllegalArgumentException("Height cannot be zero");
        }
        else this.height = height;
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
//@Override
//public Vector getNormal(Point point) {
//    Vector v = point.subtract(axis.getHead());
//    double projection = axis.getDirection().dotProduct(v);
//
//    if (isZero(projection)) {
//        return axis.getDirection().scale(-1);
//    }
//    if (isZero(projection - hight)) {
//        return axis.getDirection();
//    }
//    return super.getNormal(point);
//}

//    @Override
//    protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
//        //Cylinder is a tube with a limited height with two bases
//        //Calculate the vector from the ray's head to the tube's axis
//        Vector u = axis.getHead().subtract(ray.getHead());
//        double tm = ray.getDirection().dotProduct(u);
//        double d = Math.sqrt(u.lengthSquared() - tm * tm);
//        if (d > radius) {
//            return null; // No intersection
//        }
//        double th = Math.sqrt(radius * radius - d * d);
//        //Check if the ray intersects the tube
//        if (isZero(th)) {
//            return List.of(new Intersection(this, ray.getPoint(tm)));
//        }
//        // Calculate the intersection points
//        double t1 = tm - th;
//        double t2 = tm + th;
//        // If t1 and t2 are both positive, return both intersection points
//        if (t1 > 0 && t2 > 0) {
//            // If both intersection points are positive, return both points
//            Point p1 = ray.getPoint(t1);
//            Point p2 = ray.getPoint(t2);
//            // Check if the intersection points are within the height of the cylinder
//            if (isZero(p1.subtract(axis.getHead()).dotProduct(axis.getDirection())) ||
//                    isZero(p2.subtract(axis.getHead()).dotProduct(axis.getDirection()))) {
//                return List.of(new Intersection(this, p1), new Intersection(this, p2));
//            }
//            return null; // No intersection within the height
//        }
//        // If one of the intersection points is zero, return the other point
//        if (!isZero(t1)) {
//            Point p1 = ray.getPoint(t1);
//            // Check if the intersection point is within the height of the cylinder
//            if (isZero(p1.subtract(axis.getHead()).dotProduct(axis.getDirection()))) {
//                return List.of(new Intersection(this, p1));
//            }
//            return null; // No intersection within the height
//        }
//        // If both intersection points are negative, return null
//        if (!isZero(t2)) {
//            Point p2 = ray.getPoint(t2);
//            // Check if the intersection point is within the height of the cylinder
//            if (isZero(p2.subtract(axis.getHead()).dotProduct(axis.getDirection()))) {
//                return List.of(new Intersection(this, p2));
//            }
//            return null; // No intersection within the height
//        }
//        // If both intersection points are negative, return null
//        return null;
//    }
//@Override
//protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
//    List<Intersection> intersections = new LinkedList<>();
//
//    // חישוב החיתוכים עם המעטפת (tube)
//    List<Intersection> tubeIntersections = super.calculateIntersectionsHelper(ray);
//    if (tubeIntersections != null) {
//        intersections.addAll(tubeIntersections);
//    }
//
//    Vector axisDir = axis.getDirection();
//    Point p0 = axis.getHead(); // בסיס תחתון
//    Point pTop = axis.getPoint(height); // בסיס עליון
//
//    // חישוב חיתוך עם הבסיס התחתון
//    double nv = alignZero(axisDir.dotProduct(ray.getDirection()));
//    if (!isZero(nv)) {
//        double t = alignZero(axisDir.dotProduct(p0.subtract(ray.getHead())) / nv);
//        if (t > 0) {
//            Point p = ray.getPoint(t);
//            if (alignZero(p.distanceSquared(p0) - radius * radius) <= 0) {
//                intersections.add(new Intersection(this, p));
//            }
//        }
//    }
//
//    // חישוב חיתוך עם הבסיס העליון
//    nv = alignZero(axisDir.dotProduct(ray.getDirection()));
//    if (!isZero(nv)) {
//        double t = alignZero(axisDir.dotProduct(pTop.subtract(ray.getHead())) / nv);
//        if (t > 0) {
//            Point p = ray.getPoint(t);
//            if (alignZero(p.distanceSquared(pTop) - radius * radius) <= 0) {
//                intersections.add(new Intersection(this, p));
//            }
//        }
//    }
//
//    return intersections.isEmpty() ? null : intersections;
//}
//
//    @Override
//    protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
//        List<Intersection> intersections = new LinkedList<>();
//
//        // חיתוך עם המעטפת (tube) דרך המחלקה האב
//        List<Intersection> tubeIntersections = super.calculateIntersectionsHelper(ray);
//        if (tubeIntersections != null) {
//            intersections.addAll(tubeIntersections);
//        }
//
//        Vector v = axis.getDirection();
//        Point p0 = axis.getHead(); // מרכז הבסיס התחתון
//        Point p1 = axis.getPoint(height); // מרכז הבסיס העליון
//
//        // בסיס תחתון
//        double nvBottom = alignZero(v.dotProduct(ray.getDirection()));
//        if (!isZero(nvBottom)) {
//            double t = alignZero(v.dotProduct(p0.subtract(ray.getHead())) / nvBottom);
//            if (t > 0) {
//                Point p = ray.getPoint(t);
//                if (alignZero(p.distanceSquared(p0) - radius * radius) <= 0) {
//                    intersections.add(new Intersection(this, p));
//                }
//            }
//        }
//
//        // בסיס עליון
//        double nvTop = alignZero(v.dotProduct(ray.getDirection()));
//        if (!isZero(nvTop)) {
//            double t = alignZero(v.dotProduct(p1.subtract(ray.getHead())) / nvTop);
//            if (t > 0) {
//                Point p = ray.getPoint(t);
//                if (alignZero(p.distanceSquared(p1) - radius * radius) <= 0) {
//                    intersections.add(new Intersection(this, p));
//                }
//            }
//        }
//
//        return intersections.isEmpty() ? null : intersections;
////    }
//@Override
//protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
//    List<Intersection> intersections = new LinkedList<>();
//
//    // חיתוך עם מעטפת הגליל (Tube)
//    List<Intersection> tubeIntersections = super.calculateIntersectionsHelper(ray);
//    if (tubeIntersections != null) {
//        intersections.addAll(tubeIntersections);
//    }
//
//    Vector axisDir = axis.getDirection();
//    Point base1 = axis.getHead();             // בסיס תחתון
//    Point base2 = axis.getPoint(height);      // בסיס עליון
//
//    Point rayStart = ray.getHead();
//    Vector rayDir = ray.getDirection();
//
//    // חישוב חיתוך עם בסיס תחתון
//    Vector base1ToRay = base1.subtract(rayStart);
//    double nv1 = axisDir.dotProduct(rayDir);
//
//    if (!isZero(nv1)) {
//        double t = alignZero(axisDir.dotProduct(base1ToRay) / nv1);
//        if (t > 0) {
//            Point p = ray.getPoint(t);
//            if (alignZero(p.distanceSquared(base1) - radius * radius) <= 0) {
//                intersections.add(new Intersection(this, p));
//            }
//        }
//    }
//
//    // חישוב חיתוך עם בסיס עליון
//    Vector base2ToRay = base2.subtract(rayStart);
//    double nv2 = axisDir.dotProduct(rayDir);
//
//    if (!isZero(nv2)) {
//        double t = alignZero(axisDir.dotProduct(base2ToRay) / nv2);
//        if (t > 0) {
//            Point p = ray.getPoint(t);
//            if (alignZero(p.distanceSquared(base2) - radius * radius) <= 0) {
//                intersections.add(new Intersection(this, p));
//            }
//        }
//    }
//
//    return intersections.isEmpty() ? null : intersections;
//}
@Override
protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
    List<Intersection> intersections = super.calculateIntersectionsHelper(ray);
    if (intersections != null) {
        // נוודא שנוכל להסיר מתוך הרשימה
        intersections = new ArrayList<>(intersections);

        // סינון לפי גובה הגליל
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

    // חישוב חיתוך עם בסיס תחתון
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

    // חישוב חיתוך עם בסיס עליון
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