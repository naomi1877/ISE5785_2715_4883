package renderer;

import primitives.*;
import scene.Scene;

import geometries.Intersectable. Intersection;

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
//    public Color traceRay(Ray ray) {
//        var intersections = scene.geometries.findGeoIntersections(ray);
//        if (intersections == null) return scene.background;
//        GeoPoint closestPoint = ray.findClosestGeoPoint(intersections);
//        return calcColor(closestPoint, ray.getDirection());
//    }
    @Override
    public Color traceRay(Ray ray) {
        List<Intersection> intersections = scene.geometries.calculateIntersections( ray);
        if (intersections == null || intersections.isEmpty()) {
            return scene.background;
        }
        Intersection closestPoint = ray.findClosestIntersection( intersections);
        return calcColor(closestPoint);
    }

    /**
     * Calculate the color at the intersection point.
     * @param intersection the intersection point
     * @return the color at the intersection point
     */
    private Color calcColor(Intersection intersection) {

        Color emission = intersection.geometry.getEmission();

        // Get the ambient light coefficient
        Double3 kA = intersection.geometry.getMaterial().kA;

        // Calculate the ambient light color = environment light * kA
        Color ambient = scene.ambientLight.getIntensity().scale(kA);
        // Add the emission color
        return emission.add(ambient);
    }
}