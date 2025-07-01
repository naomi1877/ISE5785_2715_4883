package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * This class represents a Triangle, a polygon with exactly three vertices.
 * It extends the Polygon class.
 */

public class Triangle extends Polygon {

    /**
     * Constructs a triangle from three vertices.
     * The vertices must be ordered and define a valid triangle (non-collinear).
     *
     * @param vertices the three vertices of the triangle
     * @throws IllegalArgumentException if the number of points is not 3
     *                                  or the points do not define a valid triangle
     */
    public Triangle(Point... vertices) {
        super(vertices);
    }

    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
        Point p0 = ray.getHead();
        Vector dir = ray.getDirection();

        Point v0 = vertices.get(0);
        Point v1 = vertices.get(1);
        Point v2 = vertices.get(2);

        Vector edge1 = v1.subtract(v0);
        Vector edge2 = v2.subtract(v0);

        // if the ray's head is one of the triangle's vertices, return the intersection point
        double dp = edge2.dotProduct(dir);
        //if the vectors are parallel, the cross product is zero
        double edgeLen = edge2.lengthSquared();
        double dirLen = dir.lengthSquared();

        double parallelCheck = edgeLen * dirLen - dp * dp;
        if (isZero(parallelCheck)) {
            // parallel → cross product would be zero, no intersection
            return null;
        }

        Vector h = dir.crossProduct(edge2);
        double a = alignZero(edge1.dotProduct(h));
        if (isZero(a)) return null;

        double f = 1.0 / a;
        Vector s = p0.subtract(v0);
        double u = f * alignZero(s.dotProduct(h));
        if (u <= 0 || u >= 1) return null;

        Vector q = s.crossProduct(edge1);
        double v = f * alignZero(dir.dotProduct(q));
        if (v <= 0 || u + v >= 1) return null;

        double t = alignZero(f * edge2.dotProduct(q));
        if (t <= 0) return null;

        return List.of(new Intersection(this, ray.getPoint(t)));
    }
}

