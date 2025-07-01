package geometries;

/**
 * This abstract class represents a Radial Geometry, which is a 3D shape that has a radius.
 * It is extended by shapes like Tube and Sphere.
 */
public abstract class RadialGeometry extends Geometry {
    /**
     * The radius of the radial geometry.
     */
    protected final double radius;

    /**
     * Constructor to initialize a RadialGeometry object with a radius.
     *
     * @param radius the radius of the geometry
     */
    protected RadialGeometry(double radius) {
        if (radius <= 0) {
            throw new IllegalArgumentException("Radius must be positive.");
        }
        this.radius = radius;
    }
}
