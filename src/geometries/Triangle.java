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
     * @param vertices the three vertices of the triangle
     * @throws IllegalArgumentException if the number of points is not 3
     * or the points do not define a valid triangle
     */
    public Triangle(Point... vertices) {
        super(vertices);
    }
    @Override
    List<Point> findIntersections(Ray ray) {

        Vector edge1 = vertices.get(1).subtract(vertices.get(0));
        Vector edge2 = vertices.get(2).subtract(vertices.get(0));
        Vector h = ray.getDirection().crossProduct(edge2);
        double a = alignZero(edge1.dotProduct(h));

        if (isZero(a)) {
            return null;
        }

        double f = 1 / a;
        Vector s = ray.getP0().subtract(vertices.get(0));
        double u = f * alignZero(s.dotProduct(h));

        if (u <= 0 || u >= 1) {
            return null;
        }

        Vector q = s.crossProduct(edge1);
        double v = f * ray.getDirection().dotProduct(q);

        if (v <= 0 || u + v >= 1) {
            return null;
        }

        double t = alignZero(f * edge2.dotProduct(q));

        if (t <= 0) {
            return null;
        }

        return List.of(ray.getP0().add(ray.getDirection().scale(t)));
    }

    }

