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
        //todo
        return null;
    }

//    private Color calcColor(Point point) {
//        return scene.ambientLight.getIntensity();
//    }
//    private Color calcColor(Intersection point, Vector direction) {
//        Color color = point.geometry.getEmission().add(scene.ambientLight.getIntensity()); // Ia * ka + Ie
//        Vector n = point.geometry.getNormal(point.point);
//
//        //store the values of the material
//        int nShininess = point.geometry.getMaterial().nShininess;
//        Double3 kD = point.geometry.getMaterial().kD;
//        Double3 kS = point.geometry.getMaterial().kS;
//
//        double nv = Util.alignZero(n.dotProduct(direction));
//        if (nv == 0d) return Color.BLACK;
//
//        // Calculate the color of the point by adding the diffusive and specular components
//        for (var lightSource : scene.lights) {
//            Vector l = lightSource.getL(point.point).normalize();
//            double nl = n.dotProduct(l);
//
//            if (Util.alignZero(nl * nv) > 0) { // Only if nl and nv have the same sign
//                Color lightIntensity = lightSource.getIntensity(point.point);
//                color = color.add(calcDiffusive(kD, nl, lightIntensity))
//                        .add(calcSpecular(kS, l, n, nl, direction, nShininess, lightIntensity));
//            }
//        }
//
//        return color;
//    }
}