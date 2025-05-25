package renderer;

import lighting.LightSource;
import primitives.*;
import scene.Scene;

import geometries.Intersectable. Intersection;

import java.util.List;

import static primitives.Util.isZero;

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
    public boolean preprocessIntersection(Intersection intersection, Vector rayDirection) {
        // Set the ray direction and normal at the intersection point
        intersection.rayDirection = rayDirection.normalize();
        intersection.normalAtPoint = intersection.geometry.getNormal(intersection.point);
        // Calculate the dot product of the normal and ray direction
        intersection.nDotV = intersection.normalAtPoint.dotProduct(rayDirection);
        if(isZero(intersection.nDotV)) {
            // If the dot product is zero, the ray is perpendicular to the surface
            return false;
        }
        // If the ray is not perpendicular, return true
        return true;
    }
    public boolean setLightSource(Intersection intersection, LightSource lightSource) {
        // Set the light source and calculate the direction and dot product
        intersection.lightSource = lightSource;
        intersection.lightDirection = lightSource.getL(intersection.point).normalize();
        intersection.nDotL= intersection.normalAtPoint.dotProduct(intersection.lightDirection);
        if (isZero(intersection.nDotL)) {
            // If the dot product is zero, the light source is perpendicular to the surface
            return false;
        }
        // If the light source is not perpendicular, return true
        return true;
    }

    private Color calcColorLocalEffects(Intersection intersection) {
        Double3 kd = calcDiffusive(intersection);
        Double3 ks = calcSpecular(intersection);
        Color lightIntensity = intersection.lightSource.getIntensity(intersection.point);
        //We need to check something? ---
        return lightIntensity.scale(kd.add(ks));
    }

    private Double3 calcDiffusive(Intersection intersection) {
        return intersection.material.kD.scale(Math.abs(intersection.nDotL));
    }
    private Double3 calcSpecular(Intersection intersection) {
        Vector r = intersection.lightDirection.subtract(
                intersection.normalAtPoint.scale(2 * intersection.nDotL)).normalize();

        double minusVR = -intersection.rayDirection.dotProduct(r);
        if (minusVR <= 0) return Double3.ZERO;

        return intersection.material.kS.scale(
                Math.pow(minusVR, intersection.material.nSh));
    }

}
