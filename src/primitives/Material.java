package primitives;


/**
 * The Material class represents the properties of a material in a 3D scene.
 * It includes coefficients for ambient, diffuse, and specular reflection,
 * as well as shininess, transparency, and reflection coefficients.
 */
public class Material {

    /**
     * Ambient reflection coefficient
     */
    public Double3 kA = Double3.ONE;

    /**
     * Diffuse reflection coefficient
     */
    public Double3 kD = Double3.ZERO;

    /**
     * Specular reflection coefficient
     */
    public Double3 kS = Double3.ZERO;

    /**
     * The transparency coefficient.
     */
    public Double3 kT = Double3.ZERO;

    /**
     * The reflection coefficient.
     */
    public Double3 kR = Double3.ZERO;

    /**
     * The shininess coefficient for specular reflection.
     */
    public int nShininess = 0;


    /**
     * Sets the ambient reflection coefficient.
     *
     * @param kA the ambient reflection coefficient to set
     * @return this Material instance for chaining
     */
    public Material setKA(Double3 kA) {
        this.kA = kA;
        return this;
    }

    /**
     * Sets the ambient coefficient of the material.
     *
     * @param kA the ambient coefficient as a Double3 object
     * @return the current Material object (for method chaining)
     */

    public Material setKA(double kA) {
        this.kA = new Double3(kA);
        return this;
    }

    /**
     * Sets the ambient coefficient of the material.
     *
     * @param kD the ambient coefficient as a Double3 object
     * @return the current Material object (for method chaining)
     */

    public Material setKD(Double3 kD) {
        this.kD = kD;
        return this;
    }

    /**
     * Sets the diffuse coefficient of the material.
     *
     * @param kD the diffuse coefficient as a double
     * @return the current Material object (for method chaining)
     * @throws IllegalArgumentException if kD is negative
     */
    public Material setKD(double kD) {
        this.kD = new Double3(kD);
        return this;
    }

    /**
     * Sets the specular coefficient of the material.
     *
     * @param kS the specular coefficient as a Double3 object
     * @return the current Material object (for method chaining)
     */
    public Material setKS(Double3 kS) {
        this.kS = kS;
        return this;
    }

    /**
     * Sets the specular coefficient of the material.
     *
     * @param kS the specular coefficient as a double
     * @return the current Material object (for method chaining)
     * @throws IllegalArgumentException if kS is negative
     */
    public Material setKS(double kS) {
        this.kS = new Double3(kS);
        return this;
    }

    /**
     * Sets the shininess coefficient of the material.
     *
     * @param nSh the shininess coefficient, must be non-negative
     * @return the current Material object (for method chaining)
     * @throws IllegalArgumentException if nSh is negative
     */
    public Material setShininess(int nSh) {
        if (nSh < 0) {
            throw new IllegalArgumentException("Shininess value cannot be negative");
        }
        this.nShininess = nSh;
        return this;
    }

    /**
     * Sets the transparency coefficient of the material.
     *
     * @param kt the transparency coefficient as a Double3 object
     * @return the current Material object (for method chaining)
     */

    public Material setKT(double kt) {
        this.kT = new Double3(kt);
        return this;
    }

    /**
     * Sets the transparency coefficient of the material.
     *
     * @param kt the transparency coefficient as a Double3 object
     * @return the current Material object (for method chaining)
     */
    public Material setKT(Double3 kt) {
        this.kT = kt;
        return this;
    }

    /**
     * Sets the reflection coefficient.
     *
     * @param kr the reflection coefficient to set
     * @return this Material instance for method chaining
     */
    public Material setKR(Double3 kr) {
        this.kR = kr;
        return this;
    }


    /**
     * Sets the reflection coefficient.
     *
     * @param kr the reflection coefficient as a double
     * @return this Material instance for method chaining
     */
    public Material setKR(double kr) {
        this.kR = new Double3(kr);
        return this;
    }
}
