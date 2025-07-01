package lighting;

import primitives.Color;
import primitives.Double3;
import primitives.Point;
import primitives.Vector;


/**
 * PointLight class represents a point light source in a 3D scene.
 * It extends the Light class and implements the LightSource interface.
 * The light has a position in space and its intensity decreases with distance.
 */
public class PointLight extends Light implements LightSource {
    /**
     * The position of the point light in 3D space.
     */
    private final Point position;

    /**
     * Attenuation factors for the point light.
     * kC - constant attenuation
     */
    private Double3 kC = Double3.ONE;
    /**
     * kL - linear attenuation
     */
    private Double3 kL = Double3.ZERO;
    /**
     * kQ - quadratic attenuation
     */
    private Double3 kQ = Double3.ZERO;

    /**
     * Constructor for PointLight
     *
     * @param intensity the intensity of the point light
     * @param position  the position of the point light in 3D space
     */
    public PointLight(Color intensity, Point position) {
        super(intensity);
        this.position = position;
    }

    /**
     * Sets the position of the point light.
     *
     * @param kC the constant attenuation factor
     * @return the current PointLight object (for method chaining)
     */
    public PointLight setKC(Double3 kC) {
        this.kC = kC;
        return this;
    }

    /**
     * Sets the constant attenuation factor of the point light.
     *
     * @param kC the constant attenuation factor as a double
     * @return the current PointLight object (for method chaining)
     */
    public PointLight setKC(double kC) {
        this.kC = new Double3(kC);
        return this;
    }

    /**
     * Sets the linear attenuation factor of the point light.
     *
     * @param kL the linear attenuation factor
     * @return the current PointLight object (for method chaining)
     */
    public PointLight setKL(Double3 kL) {
        this.kL = kL;
        return this;
    }

    /**
     * Sets the linear attenuation factor of the point light.
     *
     * @param kL the linear attenuation factor as a double
     * @return the current PointLight object (for method chaining)
     */
    public PointLight setKL(double kL) {
        this.kL = new Double3(kL);
        return this;
    }

    /**
     * Sets the quadratic attenuation factor of the point light.
     *
     * @param kQ the quadratic attenuation factor
     * @return the current PointLight object (for method chaining)
     */
    public PointLight setKQ(double kQ) {
        this.kQ = new Double3(kQ);
        return this;
    }

    /**
     * Sets the quadratic attenuation factor of the point light.
     *
     * @param kQ the quadratic attenuation factor as a Double3 object
     * @return the current PointLight object (for method chaining)
     */
    public PointLight setKQ(Double3 kQ) {
        this.kQ = kQ;
        return this;
    }


    @Override
    public Color getIntensity(Point p) {
        double d = position.distance(p);
        // Calculate the attenuation factor
        Double3 attenuation = (kC.add(kL.scale(d)).add(kQ.scale(d * d)));
        // If the attenuation factor is zero, return the intensity directly
        if (attenuation.equals(Double3.ZERO)) {
            return intensity;
        }
        // Calculate because there is no reduction for double number in the intensity
        Double3 invAttenuation = Double3.ONE.reduce(attenuation.d1());
        // Return the intensity scaled by the inverse of the attenuation factor
        return intensity.scale(invAttenuation);
    }

    @Override
    public Vector getL(Point p) {
        //if the point is the same as the position of the light, return a zero vector
        if (p.equals(position)) {
            return null;
        }
        Vector direction = p.subtract(position);
        return direction.normalize();
    }

    @Override
    public double getDistance(Point point) {
        return position.distance(point);
    }
}
