package renderer;

import geometries.Intersectable.Intersection;
import lighting.LightSource;
import primitives.Color;
import primitives.Double3;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * A basic ray tracer that returns the ambient light color at the intersection point,
 * or background color if there's no intersection.
 */
public class SimpleRayTracer extends RayTracerBase {

    /**
     * Maximum number of color levels for the ray tracer
     */
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    /**
     * Minimum color level for the ray tracer
     */
    private static final double MIN_CALC_COLOR_K = 0.001;
    /**
     * Initial value for the ambient light intensity
     */
    private static final Double3 INITIAL_K = Double3.ONE;


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
        if (intersections == null)
            return scene.background;
        Intersection closestIntersection = ray.findClosestIntersection(intersections);
        return closestIntersection == null ? scene.background : calcColor(closestIntersection, ray);
    }


    /**
     * Find the closest intersection point for the given ray.
     * This method calculates all intersections with the scene geometries
     * and returns the closest one.
     *
     * @param ray the ray to trace
     * @return the closest intersection point, or null if no intersection is found
     */
    private Intersection findClosestIntersection(Ray ray) {
        var intersections = scene.geometries.calculateIntersections(ray);
        return ray.findClosestIntersection(intersections);
    }

    /**
     * Calculate the color at the intersection point.
     * If the intersection is not illuminated, return black.
     * Otherwise, calculate the local and global effects at the intersection point.
     *
     * @param intersection the intersection point
     * @param ray          the ray that intersects with the geometry
     * @return the color at the intersection point
     */
    private Color calcColor(Intersection intersection, Ray ray) {
        if (!preprocessIntersection(intersection, ray.getDirection()))
            return Color.BLACK;
        Color color = scene.ambientLight.getIntensity().scale(intersection.material.kA);
        color = color.add(calcColor(intersection, MAX_CALC_COLOR_LEVEL, INITIAL_K.d1())); // קריאה לרקורסיה עם רמות
        return color;
    }


    /**
     * Calculate the color at the intersection point recursively.
     * This method calculates the local effects and global effects (reflection and refraction)
     * at the intersection point, up to a maximum recursion level.
     *
     * @param intersection the intersection point
     * @param level        the current recursion level
     * @param k            the scaling factor for the color
     * @return the color at the intersection point
     */
    private Color calcColor(Intersection intersection, int level, double k) {
        Color color = calcColorLocalEffects(intersection);
        if (level == 1)
            return color;
        return color.add(calcGlobalEffects(intersection, level, new Double3(k)));
    }


    private Color calcGlobalEffects(Intersection intersection, int level, Double3 k) {
        return calcColorGlobalEffect(constructRefractedRay(intersection), level, k, intersection.material.kT)
                .add(calcColorGlobalEffect(constructReflectedRay(intersection), level, k, intersection.material.kR));
    }


    private Color calcColorGlobalEffect(Ray ray, int level, Double3 k, Double3 kx) {
        Double3 kkx = k.product(kx);
        if (kkx.lowerThan(MIN_CALC_COLOR_K))
            return Color.BLACK;
        Intersection intersection = findClosestIntersection(ray);
        if (intersection == null)
            return scene.background.scale(kx);
        return preprocessIntersection(intersection, ray.getDirection())
                ? calcColor(intersection, level - 1, kkx.d1()).scale(kx)
                : Color.BLACK;
    }


    /**
     * Preprocess the intersection point by setting the ray direction and normal at the intersection point.
     * If the dot product of the normal and ray direction is zero, it means the ray is perpendicular to the surface.
     *
     * @param intersection the intersection point
     * @param rayDirection the direction of the ray
     * @return true if the intersection is valid, false otherwise
     */
    public boolean preprocessIntersection(Intersection intersection, Vector rayDirection) {
        if (intersection.geometry == null) return false;
        intersection.toViewer = rayDirection;
        intersection.normalAtPoint = intersection.geometry.getNormal(intersection.point);
        intersection.nDotV = intersection.toViewer.dotProduct(intersection.normalAtPoint);
        return !isZero(intersection.nDotV);
    }

    /**
     * Set the light source for the intersection point and calculate the direction to the light source.
     * If the dot product of the normal and light direction is zero, it means the light source is perpendicular to the surface.
     *
     * @param intersection the intersection point
     * @param lightSource  the light source
     * @return true if the light source is valid, false otherwise
     */
    public boolean setLightSource(Intersection intersection, LightSource lightSource) {
        intersection.lightSource = lightSource;
        intersection.toLight = lightSource.getL(intersection.point).normalize();
        intersection.nDotL = intersection.normalAtPoint.dotProduct(intersection.toLight);
        return alignZero(intersection.nDotL) * intersection.nDotV > 0;
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
            if (!setLightSource(intersection, lightSource)) continue;
            Double3 ktr = transparency(intersection);
            if (ktr.lowerThan(MIN_CALC_COLOR_K)) continue;
            Color lightIntensity = lightSource.getIntensity(intersection.point).scale(ktr);
            Color diffuse = lightIntensity.scale(calcDiffusive(intersection));
            Color specular = lightIntensity.scale(calcSpecular(intersection));
            color = color.add(diffuse, specular);
        }
        return color;
    }

    /**
     * Calculate the diffuse component of the lighting at the given intersection point.
     * This is computed as the material's diffuse coefficient (kD) scaled by the
     * absolute value of the dot product between the surface normal and the light direction.
     *
     * @param intersection the intersection data containing material properties and lighting info
     * @return the diffuse light intensity as a {@code Double3} representing RGB color components
     */
    private Double3 calcDiffusive(Intersection intersection) {
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

        return intersection.material.kS.scale(
                Math.pow(minusVR, intersection.material.nShininess));
    }

    /**
     * Calculate the transparency factor at the intersection point
     *
     * @param intersection the intersection
     * @return the transparency factor
     */
    private Double3 transparency(Intersection intersection) {
        Vector lightDirection = intersection.toLight.scale(-1);
        Ray shadowRay = new Ray(intersection.point, lightDirection, intersection.normalAtPoint);
        List<Intersection> shadowIntersections = scene.geometries.calculateIntersections(shadowRay);

        if (shadowIntersections == null || shadowIntersections.isEmpty()) return Double3.ONE;

        Double3 ktr = Double3.ONE;
        double lightDistance = intersection.lightSource.getDistance(intersection.point);

        for (Intersection hit : shadowIntersections) {
            if (alignZero(hit.point.distance(intersection.point) - lightDistance) < 0) {
                ktr = ktr.product(hit.geometry.getMaterial().kT);
                if (ktr.lowerThan(MIN_CALC_COLOR_K)) return Double3.ZERO;
            }
        }

        return ktr;
    }

    /**
     * Construct a reflected ray from the intersection point
     *
     * @param intersection the intersection
     * @return the reflected ray
     */
    private Ray constructReflectedRay(Intersection intersection) {
        Vector n = intersection.normalAtPoint;
        return new Ray(intersection.point, intersection.toViewer.subtract(n.scale(2 * intersection.toViewer.dotProduct(n))), n);
    }

    /**
     * Construct a refracted ray from the intersection point
     *
     * @param intersection the intersection
     * @return the refracted ray
     */
    private Ray constructRefractedRay(Intersection intersection) {
        return new Ray(intersection.point, intersection.toViewer, intersection.normalAtPoint);
    }

}
