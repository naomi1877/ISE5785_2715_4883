package lighting;

import primitives.Color;

/**
 * Ambient light class represents the ambient light in the scene.
 * It is a type of light that is present everywhere in the scene and does not
 * have a specific direction.
 * The intensity of the ambient light is represented by a color.
 */
public class AmbientLight {
    private  final Color intensity;

    public static final AmbientLight NONE = new AmbientLight(Color.BLACK);

    /**
     * Constructor for AmbientLight
     * @param intensity the intensity of the ambient light
     */
    public AmbientLight(Color intensity ) {
        this.intensity = intensity;

    }

    public  Color getIntensity() {
        return intensity;
    }

}
