package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;


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

        return List.of();
        //To do
    }
}
