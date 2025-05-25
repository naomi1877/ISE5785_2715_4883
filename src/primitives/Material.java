package primitives;

public class Material {
    public Double3 kA = Double3.ONE;

    public Double3 kD = Double3.ZERO;

    public Double3 kS = Double3.ZERO;

    public int nSh = 0;

    public Material setKA(Double3 kA) {
        this.kA = kA;
        return this;
    }


    public Material setKA(double kA) {
        this.kA = new Double3(kA);
        return this;
    }

    public Material setKD(Double3 kD) {
        this.kD = kD;
        return this;
    }
    public Material setKD(double kD) {
        this.kD = new Double3(kD);
        return this;
    }
    public Material setKS(Double3 kS) {
        this.kS = kS;
        return this;
    }
    public Material setKS(double kS) {
        this.kS = new Double3(kS);
        return this;
    }
    public Material setShininess(int nSh) {
        if (nSh < 0) {
            throw new IllegalArgumentException("Shininess value cannot be negative");
        }
        this.nSh = nSh;
        return this;
    }
}
