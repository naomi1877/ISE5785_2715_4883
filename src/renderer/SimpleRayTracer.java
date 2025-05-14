package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

import java.util.List;

/**
 * A basic ray tracer that returns the ambient light color at the intersection point,
 * or background color if there's no intersection.
 */
public class SimpleRayTracer extends RayTracerBase {
    /**
     * Constructor
     *
     * @param scene the scene to trace rays through
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    /**
     * Traces a ray through the scene and returns the color at the intersection point.
     * If there are no intersections, it returns the background color.
     *
     * @param ray the ray to trace
     * @return the color at the intersection point or background color
     */
    @Override
    public Color traceRay(Ray ray) {
        List<Point> intersections = scene.geometries.findIntersections(ray);
        if (intersections == null || intersections.isEmpty()) {
            return scene.background;
        }
        Point closestPoint = ray.findClosestPoint(intersections);
        return calcColor(closestPoint);
    }

    /**
     * Calculate the color at the intersection point.
     *
     * @param point the intersection point
     * @return the color at the intersection point
     */
    private Color calcColor(Point point) {
        return scene.ambientLight.getIntensity();
    }
}