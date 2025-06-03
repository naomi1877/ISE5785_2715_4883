package renderer;

import lighting.LightSource;
import primitives.*;
import scene.Scene;

import geometries.Intersectable. Intersection;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * A basic ray tracer that returns the ambient light color at the intersection point,
 * or background color if there's no intersection.
 */
public class SimpleRayTracer extends RayTracerBase {
    /**
     * Delta value for shadow rays
     */
    private static final double DELTA = 0.1;


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
        var intersections = scene.geometries.calculateIntersections(ray);
        return intersections == null
                ? scene.background
                : calcColor(ray.findClosestIntersection(intersections), ray);
    }

    /**
     * Calculate the color at the intersection point.
     * If the intersection is not illuminated, return black.
     * Otherwise, calculate the local effects (diffuse and specular) at the intersection point.
     *
     * @param intersection the intersection point
     * @param ray  the ray that intersects with the geometry
     * @return the color at the intersection point
     */

    private Color calcColor(Intersection intersection, Ray ray) {
        if(!preprocessIntersection(intersection,ray.getDirection()))
            //if the point is not illuminated at all
            return Color.BLACK;
        return calcColorLocalEffects(intersection);
    }
    /**
     * Check if the intersection point is unshaded by any geometry.
     * This method casts a shadow ray towards the light source and checks for intersections.
     * If there are no intersections before reaching the light source, the point is considered unshaded.
     *
     * @param intersection the intersection point
     * @return true if the point is unshaded, false otherwise
     */
    private boolean unshaded(Intersection intersection) {
        Vector lightDirection = intersection.toLight.scale(-1);
        Vector delta = intersection.normalAtPoint.scale(intersection.nDotV < 0 ? DELTA : -DELTA);
        Ray lightRay = new Ray(intersection.point.add(delta), lightDirection);
        double lightDistance = intersection.lightSource.getDistance(intersection.point);
        List<Intersection> intersections = scene.geometries.calculateIntersections(lightRay);
        if (intersections == null) return true;
        for (Intersection hit : intersections) {
            double hitDistance = hit.point.distance(intersection.point);
            if (hitDistance < lightDistance - DELTA) {
                return false;
            }
        }
        return true;
    }
    /**
     * Preprocess the intersection point by setting the ray direction and normal at the intersection point.
     * If the dot product of the normal and ray direction is zero, it means the ray is perpendicular to the surface.
     * @param intersection the intersection point
     * @param rayDirection the direction of the ray
     * @return true if the intersection is valid, false otherwise
     */
    public boolean preprocessIntersection(Intersection intersection, Vector rayDirection) {
        // Set the ray direction and normal at the intersection point
        intersection.toViewer = rayDirection.normalize();
        intersection.normalAtPoint = intersection.geometry.getNormal(intersection.point);
        // Calculate the dot product of the normal and ray direction
        intersection.nDotV = intersection.normalAtPoint.dotProduct(rayDirection);
        // If the dot product is zero, the ray is perpendicular to the surface, and if not - return true
        return !isZero(intersection.nDotV);
    }
    /**
     * Set the light source for the intersection point and calculate the direction to the light source.
     * If the dot product of the normal and light direction is zero, it means the light source is perpendicular to the surface.
     * @param intersection the intersection point
     * @param lightSource the light source
     * @return true if the light source is valid, false otherwise
     */
    public boolean setLightSource(Intersection intersection, LightSource lightSource) {
        // Set the light source and calculate the direction and dot product
        intersection.lightSource = lightSource;
        intersection.toLight = lightSource.getL(intersection.point).normalize();
        intersection.nDotL= intersection.normalAtPoint.dotProduct(intersection.toLight);
        // If the dot product is zero, the light source is perpendicular to the surface, and if not - return true
        if (alignZero(intersection.nDotL * intersection.nDotV) <= 0) return false;
        return true;
    }

    /**
     * Calculate local lighting effects at an intersection
     *
     * @param intersection the intersection
     * @return the color from local lighting effects
     */
        private Color calcColorLocalEffects(Intersection intersection) {
            Color color = intersection.geometry.getEmission();
            for (LightSource lightSource : scene.lights) {
                if (!setLightSource(intersection, lightSource) || !unshaded(intersection)) continue;
                Color lightIntensity = lightSource.getIntensity(intersection.point);
                Color diffuse = lightIntensity.scale(calcDiffusive(intersection));
                Color specular = lightIntensity.scale(calcSpecular(intersection));
                color = color.add(diffuse, specular);
            }
            return color;
        }

    /**
 * Calculates the diffuse component of the lighting at the given intersection point.
            *
            * This is computed as the material's diffuse coefficient (kD) scaled by the
            * absolute value of the dot product between the surface normal and the light direction.
            *
            * @param intersection the intersection data containing material properties and lighting info
 * @return the diffuse light intensity as a {@code Double3} representing RGB color components
 */

    private Double3 calcDiffusive(Intersection intersection) {
        //kD * |n·l|
        return intersection.material.kD.scale(Math.abs(intersection.nDotL));
    }

    /**
     * Calculates the specular component of the lighting at the given intersection point.
     * This is computed as the material's specular coefficient (kS) scaled by the
     * power of the dot product between the reflected light direction and the viewer direction,
     * raised to the material's shininess factor (nShininess).
     *
     * @param intersection the intersection data containing material properties and lighting info
     * @return the specular light intensity as a {@code Double3} representing RGB color components
     */
    private Double3 calcSpecular(Intersection intersection) {
        Vector r = intersection.toLight.subtract(
                intersection.normalAtPoint.scale(2 * intersection.nDotL)).normalize();

        double minusVR = -intersection.toViewer.dotProduct(r);
        if (minusVR <= 0) return Double3.ZERO;

        //kS * pow(..., shininess)
        return intersection.material.kS.scale(
                Math.pow(minusVR, intersection.material.nShininess));
    }
    /**
     * Check if the intersection is unshaded by any geometry
     *
     * @param intersection the intersection
     * @return true if unshaded, false otherwise
     */

}
