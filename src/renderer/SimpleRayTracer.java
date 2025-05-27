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
        return calcColor(closestPoint,ray);
    }

    /**
     * Calculate the color at the intersection point.
     * @param intersection the intersection point
     * @return the color at the intersection point
     */
    private Color calcColor(Intersection intersection, Ray ray) {
       if(!preprocessIntersection(intersection,ray.getDirection()))
           //if the point is not illuminated at all
            return Color.BLACK;
        return calcColorLocalEffects(intersection);
    }


    public boolean preprocessIntersection(Intersection intersection, Vector rayDirection) {
        // Set the ray direction and normal at the intersection point
        intersection.toViewer = rayDirection.normalize();
        intersection.normalAtPoint = intersection.geometry.getNormal(intersection.point);
        // Calculate the dot product of the normal and ray direction
        intersection.nDotV = intersection.normalAtPoint.dotProduct(rayDirection);
        // If the dot product is zero, the ray is perpendicular to the surface, and if not - return true
        return !isZero(intersection.nDotV);
    }

    public boolean setLightSource(Intersection intersection, LightSource lightSource) {
        // Set the light source and calculate the direction and dot product
        intersection.lightSource = lightSource;
        intersection.toLight = lightSource.getL(intersection.point).normalize();
        intersection.nDotL= intersection.normalAtPoint.dotProduct(intersection.toLight);
        // If the dot product is zero, the light source is perpendicular to the surface, and if not - return true
        if (alignZero(intersection.nDotL * intersection.nDotV) <= 0) return false;
        return true;
    }

    private Color calcColorLocalEffects(Intersection intersection) {

        Color color = intersection.geometry.getEmission();

        for (LightSource light : scene.lights) {
            if (!setLightSource(intersection, light)) continue;

            Color lightIntensity = light.getIntensity(intersection.point);
            //diffusive
            Double3 kd = calcDiffusive(intersection);
            //specular
            Double3 ks = calcSpecular(intersection);
            Double3 combinedEffect = kd.add(ks);
            Color scaleLight= lightIntensity.scale(combinedEffect);
            color = color.add(scaleLight);
        }
        return color;
    }

    private Double3 calcDiffusive(Intersection intersection) {
        //kD * |n·l|
        return intersection.material.kD.scale(Math.abs(intersection.nDotL));
    }

    private Double3 calcSpecular(Intersection intersection) {
        Vector r = intersection.toLight.subtract(
                intersection.normalAtPoint.scale(2 * intersection.nDotL)).normalize();

        double minusVR = -intersection.toViewer.dotProduct(r);
        if (minusVR <= 0) return Double3.ZERO;

        //kS * pow(..., shininess)
        return intersection.material.kS.scale(
                Math.pow(minusVR, intersection.material.nShininess));
    }

}
