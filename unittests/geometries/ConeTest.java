package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ConeTest {

    @Test
    void testConeRayHitsBothBases() {

        Ray axis = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));

        Cone cone = new Cone(3, axis, 10);

        Ray ray = new Ray(new Point(0, 0, 15), new Vector(0, 0, -1));

        List<Intersectable.Intersection> intersections = cone.calculateIntersectionsHelper(ray);
        System.out.println(intersections);
        assertNotNull(intersections, "Expected intersections with cone");
        assertEquals(2, intersections.size(), "Ray should intersect both bases");

    }


}