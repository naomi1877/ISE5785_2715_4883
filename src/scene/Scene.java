package scene;

import geometries.Geometries;
import lighting.AmbientLight;
import lighting.LightSource;
import primitives.Color;

import java.util.LinkedList;
import java.util.List;

/**
 * Scene class represents a 3D scene with a name, background color, ambient light,
 * and geometries.
 * It is used to define the properties of the scene that will be rendered.
 * It is PDS - Plain Data Structure
 */
public class Scene {
    /**
     * The name of the scene.
     */
    public String name;
    /**
     * The background color of the scene.
     */
    public Color background = Color.BLACK;
    /**
     * The ambient light in the scene.
     */
    public AmbientLight ambientLight = AmbientLight.NONE;
    /**
     * The geometries in the scene.
     */
    public Geometries geometries = new Geometries();
    /**
     * The list of light sources in the scene.
     */
    public List<LightSource> lights = new LinkedList<>();

    /**
     * Constructor for Scene
     *
     * @param name the name of the scene
     */
    public Scene(String name) {
        this.name = name;
    }

    /**
     * Sets the background color of the scene.
     *
     * @param background the background color to set
     * @return the current Scene object (for method chaining)
     */
    public Scene setBackground(Color background) {
        this.background = background;
        return this;
    }

    /**
     * Sets the ambient light of the scene.
     *
     * @param ambientLight The {@link AmbientLight} object representing the ambient light in the scene.
     * @return Scene
     */
    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    /**
     * Sets the geometries of the scene.
     *
     * @param geometries The {@link Geometries} object containing the shapes to include in the scene.
     * @return The current {@link Scene} instance (for method chaining).
     */
    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }

    /**
     * Adds a light source to the scene.
     *
     * @param light the light source to be added to the scene
     * @return the current {@code Scene} object, to allow method chaining
     */
    public Scene addLight(LightSource light) {
        lights.add(light);
        return this;
    }
}