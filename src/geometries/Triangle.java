package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;

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

        // Check if the ray's head is inside the triangle
        Vector edge1 = vertices.get(1).subtract(vertices.get(0));
        Vector edge2 = vertices.get(2).subtract(vertices.get(0));
        Vector h = ray.getDirection().crossProduct(edge2);
        // Calculate the determinant
        double a = alignZero(edge1.dotProduct(h));

        // If the determinant is close to zero, the ray is parallel to the triangle
        if (a == 0) {
            return null;
        }

        // Calculate the distance from the ray's head to the triangle
        double f = 1 / a;
        Vector s = ray.getHead().subtract(vertices.get(0));
        double u = f * alignZero(s.dotProduct(h));

        // Check if the intersection point is inside the triangle
        // If u is less than 0 or greater than 1, the intersection point is outside the triangle
        if (u <= 0 || u >= 1) {
            return null;
        }

        Vector q = s.crossProduct(edge1);
        double v = f * alignZero(ray.getDirection().dotProduct(q));

        // If v is less than 0 or u + v is greater than 1, the intersection point is outside the triangle
        if (v <= 0 || u + v >= 1) {
            return null;
        }

        double t = alignZero(f * edge2.dotProduct(q));

        // If t is less than or equal to 0, the intersection point is behind the ray's head
        if (t <= 0) {
            return null;
        }

        // Calculate the intersection point
        return List.of(new Intersection(this, ray.getPoint(t)));
    }
}

