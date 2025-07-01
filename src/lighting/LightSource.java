package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Interface LightSource represents a light source in a 3D space.
 * It provides methods to get the intensity and direction of the light at a specific point.
 */
public interface LightSource {
    /**
     * Get the intensity of the light source at a specific point.
     *
     * @param p the point in space where the intensity is needed
     * @return the color representing the intensity of the light at that point
     */
    Color getIntensity(Point p);

    /**
     * Get the direction of the light source at a specific point.
     *
     * @param p the point in space where the direction is needed
     * @return the direction vector of the light source at that point
     */
    Vector getL(Point p);


    /**
     * Returns the distance from the origin to the given point.
     *
     * @param point the target point
     * @return the distance as a double value
     */
    double getDistance(Point point);

}
