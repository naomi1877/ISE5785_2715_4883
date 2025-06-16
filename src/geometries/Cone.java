package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

public class Cone extends Cylinder{

    /**
     * Constructor to initialize a Cone object with a radius, an axis, and a height.
     * The cone is defined by its base radius (radius1), the apex radius (radius2), the axis (Ray), and the height.
     * @param axis    the axis of the cone (Ray)
     * @param height  the height of the cone
     */
    public Cone(double radius, Ray axis, double height) { //double radius2,
        super(radius, axis, height);
        //this.radius2 = radius2;
    }

    @Override
    public Vector getNormal(Point point) {
        // Calculate the normal vector at the point on the cone's surface
        // This is a simplified version; actual implementation may vary based on the cone's geometry
        Point apex = axis.getHead();
        Vector axisDirection = axis.getDirection();
        Vector toPoint = point.subtract(apex);
        double t = axisDirection.dotProduct(toPoint);
        Vector normal = toPoint.subtract(axisDirection.scale(t)).normalize();
        return normal;
    }
@Override
protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
    List<Intersection> intersections = new LinkedList<>();

    Point apex = axis.getHead();         // קודקוד הקונוס
    Vector axisDir = axis.getDirection(); // כיוון הקונוס
    Point rayOrigin = ray.getHead();      // ראש הקרן
    Vector rayDir = ray.getDirection();   // כיוון הקרן

    Vector deltaP = rayOrigin.subtract(apex);

    // חישוב יחס: tan^2(theta) = (r/h)^2
    double k = radius / height;
    double k2 = k * k;

    // הצבה למשוואה הריבועית At^2 + Bt + C = 0
    double dv = rayDir.dotProduct(axisDir);
    double deltaPv = deltaP.dotProduct(axisDir);

    Vector vPerp = rayDir.subtract(axisDir.scale(dv));
    Vector deltaPPerp = deltaP.subtract(axisDir.scale(deltaPv));

    double A = vPerp.lengthSquared() - k2 * dv * dv;
    double B = 2 * (vPerp.dotProduct(deltaPPerp) - k2 * dv * deltaPv);
    double C = deltaPPerp.lengthSquared() - k2 * deltaPv * deltaPv;

    double discriminant = alignZero(B * B - 4 * A * C);
    if (discriminant < 0 || isZero(A)) {
        return null;
    }

    double sqrtD = Math.sqrt(discriminant);
    double t1 = alignZero((-B - sqrtD) / (2 * A));
    double t2 = alignZero((-B + sqrtD) / (2 * A));

    for (double t : new double[]{t1, t2}) {
        if (t > 0) {
            Point p = ray.getPoint(t);
            double h = alignZero(p.subtract(apex).dotProduct(axisDir));
            if (h >= 0 && h <= height) {
                intersections.add(new Intersection(this, p));
            }
        }
    }

    // חישוב חיתוך עם בסיס הקונוס (עיגול בגובה height)
    Point baseCenter = apex.add(axisDir.scale(height));
    double nv = axisDir.dotProduct(rayDir);
    if (!isZero(nv)) {
        double tBase = alignZero(axisDir.dotProduct(baseCenter.subtract(rayOrigin)) / nv);
        if (tBase > 0) {
            Point p = ray.getPoint(tBase);
            if (alignZero(p.distanceSquared(baseCenter) - radius * radius) <= 0) {
                intersections.add(new Intersection(this, p));
            }
        }
    }

    return intersections.isEmpty() ? null : intersections;
}

}
