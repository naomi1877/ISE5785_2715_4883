package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PyramidTest {

    @Test
    void testPyramidIntersectionAndNormal() {
        Polygon base = new Polygon(
                new Point(0, 0, 0),
                new Point(2, 0, 0),
                new Point(1, 2, 0)
        );


        Point top = new Point(1, 2, 3);
        Pyramid pyramid = new Pyramid(top, base);


        Ray ray = new Ray(new Point(1, 0.5, 5), new Vector(0, 0, -1));
        List<Intersectable.Intersection> intersections = pyramid.calculateIntersectionsHelper(ray);
        System.out.println(intersections);
        assertNotNull(intersections, "Expected intersections");
        assertEquals(2, intersections.size(), "Should intersect side and base");


        Point basePoint = new Point(1, 1, 0);
        Vector baseNormal = pyramid.getNormal(basePoint);
        assertEquals(new Vector(0, 0, 1), baseNormal, "Incorrect normal on base");

    }
}
