package lighting;

import primitives.Color;
import primitives.Point;

/**
 * Abstract class Light is the basic class representing a light source in the 3D space.
 */
abstract class Light {
    /**
     * the intensity of the light
     */
    protected final Color intensity;

    /**
     * Constructor for a Light object receiving a Color.
     *
     * @param intensity the intensity of the light
     */
    protected Light(Color intensity) {
        this.intensity = intensity;
    }

    /**
     * Returns the base intensity of the color.
     *
     * @return the color intensity
     */
    public Color getIntensity() {
        return intensity;
    }

    /**
     * Abstract method to get the color intensity at a specific point.
     *
     * @param p the point to calculate intensity at
     * @return the color intensity at point p
     */
    public abstract Color getIntensity(Point p);

}