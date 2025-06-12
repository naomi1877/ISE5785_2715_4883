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
     * The radius at the base of the cone.
     */
    protected final double radius2;

    /**
     * Constructor to initialize a Cone object with a radius, an axis, and a height.
     * The cone is defined by its base radius (radius1), the apex radius (radius2), the axis (Ray), and the height.
     *
     * @param radius1 the radius at the apex of the cone
     * @param radius2 the radius at the base of the cone
     * @param axis    the axis of the cone (Ray)
     * @param height  the height of the cone
     */
    public Cone(double radius1, double radius2, Ray axis, double height) {
        super(radius1, axis, height);
        this.radius2 = radius2;
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

        Point p0 = axis.getHead(); // קודקוד הקונוס
        Vector v = axis.getDirection();
        Point r0 = ray.getHead();
        Vector dir = ray.getDirection();

        Vector deltaP = r0.subtract(p0);
        double cos2 = (radius2 * radius2) / (height * height);

        // הגנה מפני יצירת Vector אפס
        if (isZero(v.lengthSquared()) || isZero(dir.lengthSquared()) || isZero(deltaP.lengthSquared()))
            return null;

        double dv = dir.dotProduct(v);
        double dpv = deltaP.dotProduct(v);

        double lenD2 = dir.lengthSquared();
        double lenDP2 = deltaP.lengthSquared();

        double a = lenD2 - cos2 * dv * dv;
        double b = 2 * (dir.dotProduct(deltaP) - cos2 * dv * dpv);
        double c = lenDP2 - cos2 * dpv * dpv;

        double discriminant = alignZero(b * b - 4 * a * c);
        if (discriminant < 0 || isZero(a)) return null;

        double sqrtD = Math.sqrt(discriminant);
        double t1 = alignZero((-b + sqrtD) / (2 * a));
        double t2 = alignZero((-b - sqrtD) / (2 * a));

        for (double t : new double[]{t1, t2}) {
            if (t > 0) {
                Point p = ray.getPoint(t);
                double hProj;
                if (p.equals(p0)) {
                    hProj = 0;
                } else {
                    hProj = alignZero(v.dotProduct(p.subtract(p0)));
                }
                if (hProj >= 0 && hProj <= height) {
                    intersections.add(new Intersection(this, p));
                }
            }
        }
        // Check for intersection with the base of the cone
        Point baseCenter = axis.getPoint(height);
        double nv = v.dotProduct(dir);
        if (!isZero(nv)) {
            double tBase = alignZero(v.dotProduct(baseCenter.subtract(r0)) / nv);
            if (tBase > 0) {
                Point p = ray.getPoint(tBase);
                if (alignZero(p.distanceSquared(baseCenter) - radius2 * radius2) <= 0) {
                    intersections.add(new Intersection(this, p));
                }
            }
        }
        //remove duplicates
        intersections.removeIf(intersection -> intersections.stream()
                .anyMatch(other -> other != intersection && other.point.equals(intersection.point)));

        return intersections.isEmpty() ? null : intersections;
    }

}
