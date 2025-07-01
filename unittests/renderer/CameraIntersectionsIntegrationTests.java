package renderer;

import geometries.Geometry;
import geometries.Plane;
import geometries.Sphere;
import geometries.Triangle;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration tests for Camera ray intersections with geometries.
 */
public class CameraIntersectionsIntegrationTests {
    /**
     * defoult constractor
     */
    public CameraIntersectionsIntegrationTests() {
    }

    /**
     * Helper method to test the number of intersection points between a geometry and rays constructed by a camera.
     * The method casts rays through a view plane defined by the given resolution (nX by nY),
     * counts all intersection points with the specified geometry, and compares the total to the expected amount.
     *
     * @param camera         the camera that constructs the rays
     * @param geometry       the geometry to test intersections with
     * @param expectedAmount the expected number of intersection points
     * @param nX             the number of columns (pixels in width) in the view plane
     * @param nY             the number of rows (pixels in height) in the view plane
     */
    private void amountOfIntersections(Camera camera, Geometry geometry, int expectedAmount, int nX, int nY) {
        int intersections = 0;
        for (int j = 0; j < nY; j++) {
            for (int i = 0; i < nX; i++) {
                List<Point> intersectionsList = geometry.findIntersections(camera.constructRay(nX, nY, i, j));
                intersections += intersectionsList != null ? intersectionsList.size() : 0;
            }
        }
        assertEquals(expectedAmount, intersections, "Wrong amount of intersections");
    }


    /**
     * Integration tests for Camera ray intersections with spheres.
     * {@link geometries.Sphere#findIntersections(Ray)}
     */
    @Test
    void testSphereIntersection() {
        Camera cam1 = Camera.getBuilder()
                .setLocation(Point.ZERO)
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0)) // vTo, vUp
                .setVpSize(3, 3)
                .setVpDistance(1)
                .setResolution(3, 3)
                .build();

        Camera cam2 = Camera.getBuilder()
                .setLocation(new Point(0, 0, 0.5))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpSize(3, 3)
                .setVpDistance(1)
                .setResolution(3, 3)
                .build();

        // TC01: 2 intersections
        amountOfIntersections(cam1, new Sphere(1, new Point(0, 0, -3)), 2, 3, 3);

        // TC02: 18 intersections
        amountOfIntersections(cam2, new Sphere(2.5, new Point(0, 0, -2.5)), 18, 3, 3);

        // TC03: 10 intersections
        amountOfIntersections(cam2, new Sphere(2, new Point(0, 0, -2)), 10, 3, 3);

        // TC04: 9 intersections
        amountOfIntersections(cam2, new Sphere(4, new Point(0, 0, -1)), 9, 3, 3);

        // TC05: 0 intersections
        amountOfIntersections(cam2, new Sphere(0.5, new Point(0, 0, 1)), 0, 3, 3);
    }

    /**
     * Integration tests for Camera ray intersections with plane.
     * {@link geometries.Plane#findIntersections(Ray)}
     */
    @Test
    void testPlaneIntersection() {
        Camera cam = Camera.getBuilder()
                .setLocation(new Point(0, 0, 0.5))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpSize(3, 3)
                .setVpDistance(1)
                .setResolution(3, 3)
                .build();

        // TC01: 9 intersections
        amountOfIntersections(cam, new Plane(new Point(0, 0, -1), new Vector(0, 0, -1)), 9, 3, 3);

        // TC02: 9 intersections
        amountOfIntersections(cam, new Plane(new Point(0, 0, -1), new Vector(0, 1, -10)), 9, 3, 3);

        // TC03: 6 intersections
        amountOfIntersections(cam, new Plane(new Point(0, 0, -1), new Vector(0, -1, -1)), 6, 3, 3);
    }

    /**
     * Integration tests for Camera ray intersections with triangles.
     * {@link geometries.Triangle#findIntersections(Ray)}
     */
    @Test
    void testTriangleIntersection() {
        Camera cam = Camera.getBuilder()
                .setLocation(new Point(0, 0, 0.5))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpSize(3, 3)
                .setVpDistance(1)
                .setResolution(3, 3)
                .build();

        // TC01: 1 intersection
        amountOfIntersections(cam,
                new Triangle(new Point(0, 1, -2), new Point(1, -1, -2), new Point(-1, -1, -2)), 1, 3, 3);

        // TC02: 2 intersections
        amountOfIntersections(cam,
                new Triangle(new Point(0, 20, -2), new Point(1, -1, -2), new Point(-1, -1, -2)), 2, 3, 3);
    }
}
