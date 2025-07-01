package geometries;

import org.junit.jupiter.api.Test;
import primitives.Double3;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoxTest {

    Box box = new Box(
            new Double3(0, 0, 0),
            new Double3(2, 2, 2)
    );

    @Test
    void testBoxIntersectionsAndNormals() {
        // === Normal tests on face centers ===

        // Point on left face
        Point p = new Point(0, 1.0, 1.0);

        Vector normal = box.getNormal(p);
        assertEquals(new Vector(-1, 0, 0), normal, "Incorrect normal on left face");

        // Point on top face
        p = new Point(1, 2, 1);
        normal = box.getNormal(p);
        assertEquals(new Vector(0, 1, 0), normal, "Incorrect normal on top face");

        // Point on front face
        p = new Point(1, 1, 0);
        normal = box.getNormal(p);
        assertEquals(new Vector(0, 0, -1), normal, "Incorrect normal on front face");

        // Point on a corner of the box
        Point corner = new Point(0, 0, 0);
        normal = box.getNormal(corner);
        // Any one of the adjacent face normals is valid here
        assertTrue(
                normal.equals(new Vector(-1, 0, 0)) ||
                        normal.equals(new Vector(0, -1, 0)) ||
                        normal.equals(new Vector(0, 0, -1)),
                "Normal at corner point is not one of the expected directions"
        );

        // === Ray-Box intersection tests ===

        // Ray entering and exiting the box
        Ray ray1 = new Ray(new Point(-1, 1, 1), new Vector(1, 0, 0));
        List<Intersectable.Intersection> inter1 = box.calculateIntersectionsHelper(ray1);
        assertNotNull(inter1, "Expected intersections for ray through box");
        assertEquals(2, inter1.size(), "Expected two intersection points");
        assertTrue(inter1.stream().anyMatch(i -> i.point.equals(new Point(0, 1, 1))), "Missing entry point at x=0");
        assertTrue(inter1.stream().anyMatch(i -> i.point.equals(new Point(2, 1, 1))), "Missing exit point at x=2");

        // Ray starting inside the box
        Ray ray2 = new Ray(new Point(1, 1, 1), new Vector(0, 1, 0));
        List<Intersectable.Intersection> inter2 = box.calculateIntersectionsHelper(ray2);
        assertNotNull(inter2, "Expected one intersection from inside");
        assertEquals(1, inter2.size(), "Expected one intersection from inside");

        // Ray missing the box completely
        Ray ray3 = new Ray(new Point(3, 3, 3), new Vector(1, 0, 0));
        List<Intersectable.Intersection> inter3 = box.calculateIntersectionsHelper(ray3);
        assertNull(inter3, "Expected no intersection for ray that misses the box");

        // Ray starting on the surface of the box
        Ray ray4 = new Ray(new Point(0, 1, 1), new Vector(1, 0, 0));
        List<Intersectable.Intersection> inter4 = box.calculateIntersectionsHelper(ray4);
        assertEquals(1, inter4.size(), "Expected one intersection on surface");


        // === Parallel rays outside the box ===
        // Parallel to X axis, above the box
        Ray ray6 = new Ray(new Point(0, 3, 1), new Vector(1, 0, 0));
        assertNull(box.calculateIntersectionsHelper(ray6), "Parallel ray above box should not intersect");

        // Parallel to Y axis, behind the box
        Ray ray7 = new Ray(new Point(1, 0, -1), new Vector(0, 1, 0));
        assertNull(box.calculateIntersectionsHelper(ray7), "Parallel ray behind box should not intersect");

        // Parallel to Z axis, to the right of the box
        Ray ray8 = new Ray(new Point(3, 1, 0), new Vector(0, 0, 1));
        assertNull(box.calculateIntersectionsHelper(ray8), "Parallel ray to the right should not intersect");

        // On the plane of top face, but outside in X direction
        Ray ray9 = new Ray(new Point(3, 2, 1), new Vector(1, 0, 0));
        assertNull(box.calculateIntersectionsHelper(ray9), "Ray on plane but outside box should not intersect");
    }

    @Test
    void testRayIntersection_InsideToOutside() {
        Ray ray = new Ray(new Point(1, 1, 1), new Vector(1, 0, 0));
        List<Intersectable.Intersection> intersections = box.calculateIntersectionsHelper(ray);
        assertNotNull(intersections, "Expected intersection when ray originates inside the box");
        assertEquals(1, intersections.size(), "Expected one intersection point");
        assertEquals(new Point(2, 1, 1), intersections.getFirst().point, "Wrong intersection point");
    }

    @Test
    void testRayIntersection_Miss() {
        Ray ray = new Ray(new Point(3, 3, 3), new Vector(1, 1, 1));
        List<Intersectable.Intersection> intersections = box.calculateIntersectionsHelper(ray);
        assertNull(intersections, "Expected no intersection when ray misses the box");
    }

    @Test
    void testRayIntersection_OutsideThroughBox() {
        Ray ray = new Ray(new Point(-1, 1, 1), new Vector(1, 0, 0));
        List<Intersectable.Intersection> intersections = box.calculateIntersectionsHelper(ray);
        assertNotNull(intersections, "Expected intersections when ray passes through box");
        assertEquals(2, intersections.size(), "Expected two intersection points");

        assertTrue(intersections.get(0).point.equals(new Point(0, 1, 1)) ||
                intersections.get(1).point.equals(new Point(0, 1, 1)), "Missing entry point");
        assertTrue(intersections.get(0).point.equals(new Point(2, 1, 1)) ||
                intersections.get(1).point.equals(new Point(2, 1, 1)), "Missing exit point");
    }
}