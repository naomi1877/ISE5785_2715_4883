package lighting;

import primitives.Color;
import primitives.Point;

/**
 * Ambient light class represents the ambient light in the scene.
 * It is a type of light that is present everywhere in the scene and does not
 * have a specific direction.
 * The intensity of the ambient light is represented by a color.
 */
public class AmbientLight extends Light {
    /**
     * A constant representing no ambient light (black).
     */
    public static final AmbientLight NONE = new AmbientLight(Color.BLACK);

    /**
     * Constructor for AmbientLight
     *
     * @param intensity the intensity of the ambient light
     */
    public AmbientLight(Color intensity) {
        super(intensity);

    }

    @Override
    public Color getIntensity(Point p) {
        return intensity;
    }

}

