package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * DirectionalLight class represents a directional light source in a 3D scene.
 * It extends the Light class and implements the LightSource interface.
 * The light has a constant direction and intensity, independent of the position in space.
 */
public class DirectionalLight extends Light implements LightSource {
    /**
     * The direction of the light.
     */
    private final Vector direction;

    /**
     * Constructor for DirectionalLight
     *
     * @param intensity the intensity of the light
     * @param direction the direction of the light
     */
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        //if the direction is zero, we throw an exception
        if (direction == null || direction.equals(Vector.ZERO)) {
            throw new IllegalArgumentException("Direction cannot be null or zero vector");
        }
        this.direction = direction.normalize();
    }

    @Override
    public Color getIntensity(Point p) {
        // All time the same intensity for directional light
        return getIntensity();
    }

    @Override
    public Vector getL(Point p) {
        // Directional light always has the same direction
        return direction;
    }

    @Override
    public double getDistance(Point point) {
        return Double.POSITIVE_INFINITY; // Directional light is considered to be infinitely far away
    }
}
