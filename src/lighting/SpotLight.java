package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

import static primitives.Util.alignZero;

/**
 * SpotLight class represents a spotlight in a 3D scene.
 * It extends PointLight to include a direction for the light beam.
 * The intensity of the light at a point is scaled by the cosine of the angle
 * between the light's direction and the vector from the light to the point.
 */
public class SpotLight extends PointLight{

    private final Vector direction;

    /**
     * The narrow beam factor of the spotlight.
     */
    private double narrowBeam = 1;



    /**
     * Sets the narrow beam factor of the spotlight.
     *
     * @param intensity the intensity of the light
     */

    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalize();
    }
@Override
    public SpotLight setKC(double kC) {
        super.setKC(kC);
        return this;
    }
@Override
    public SpotLight setKL(double kL) {
        super.setKL(kL);
        return this;
    }

@Override
    public SpotLight setKQ(double kQ) {
        super.setKQ(kQ);
        return this;
    }
    /**
     * Setter for the direction of the spotlight
     *
     * @return the direction vector of the spotlight
     */

    public SpotLight setNarrowBeam(double nb) {
        this.narrowBeam = nb;
        return this;
    }
    @Override
    public Color getIntensity(Point p) {
        Vector l = getL(p);
        if (l==null) {
            return Color.BLACK; // If the point is not reachable, return black
        }
        double dirVectorsCosine = alignZero(direction.dotProduct(l));
        double factor = Math.max(0, dirVectorsCosine);
        return super.getIntensity(p).scale(factor);
    }

}
