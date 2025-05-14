package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

/**
 * Abstract class for ray tracing
 */
public abstract class RayTracerBase {
    /**
     * The scene to trace rays through
     */
    protected final Scene scene;

    /**
     * Constructor
     *
     * @param scene the scene to trace rays through
     */
    protected RayTracerBase(Scene scene) {
        this.scene = scene;
    }

    /**
     * Trace a single ray and return the color it sees
     *
     * @param ray the ray to trace
     * @return the color seen by the ray
     */
    public abstract Color traceRay(Ray ray);

}