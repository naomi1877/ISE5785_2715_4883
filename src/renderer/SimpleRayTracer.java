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
        return calcColor(closestPoint, ray);
    }

    /**
     * Calculate the color at the intersection point.
     *
     * @param
     * @return the color at the intersection point
     */
    private Color calcColor(Intersection intersection, Ray ray) {

        Color emission = intersection.geometry.getEmission();

        Double3 kA = intersection.geometry.getMaterial().kA;

        Color ambient = scene.ambientLight.getIntensity().scale(kA);

        return emission.add(ambient);
    }


}

