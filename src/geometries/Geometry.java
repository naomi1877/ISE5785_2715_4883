package geometries;

import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;

/**
 * This is an abstract class representing a general 3D geometry object.
 * Any geometry must implement the method to return its normal vector at a specific point.
 */
public abstract class Geometry extends Intersectable {

    /**
     * The emission color of the geometry, which is the color emitted by the geometry itself.
     * Default value is black (no emission).
     */

    protected Color emission = Color.BLACK;

    /**
     * The material of the geometry, which defines its surface properties such as reflectivity, transparency, etc.
     * Default value is a new Material instance with default properties.
     */
    private Material material = new Material();


    /**
     * Default constructor for the Geometry class.
     */
    public Geometry() {
    }

    /**
     * Gets the emission color of the geometry.
     *
     * @return the emission color
     */
    public Color getEmission() {
        return emission;
    }

    /**
     * Sets the emission color of the geometry.
     *
     * @param emission the color to set as the emission color
     * @return the geometry instance with the updated emission color
     */
    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }

    /**
     * Gets the normal vector of the geometry at a specific point.
     *
     * @param p1 the point on the geometry's surface
     * @return the normal vector of the geometry at the point
     */
    public abstract Vector getNormal(Point p1);

    /**
     * Get the material of the geometry
     *
     * @return the material of the geometry
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Set the material of the geometry
     *
     * @param material the material to set
     * @return the geometry
     */
    public Geometry setMaterial(Material material) {
        this.material = material;
        return this;
    }
}
