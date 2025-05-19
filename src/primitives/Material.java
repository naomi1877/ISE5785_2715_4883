package primitives;

public class Material {
    public Double3 kA = Double3.ZERO;

    public Material setKa(Double3 kD) {
        this.kA = kA;
        return this;
    }

    public Material setKa(double kA) {
        this.kA = new Double3(kA);
        return this;
    }
}
