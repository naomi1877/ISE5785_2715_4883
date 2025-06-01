package primitives;

public class Material {
    public Double3 kA = Double3.ONE;

    public Double3 kD = Double3.ZERO;

    public Double3 kS = Double3.ZERO;

    public int nShininess = 0;
    public Double3 kT= Double3.ZERO;
    public Double3 kR= Double3.ZERO;

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
        this.nShininess = nSh;
        return this;
    }

    public Material setKT(double kt) {
        this.kT=new Double3(kt);
        return this;
    }

    public Material setKT(Double3 kt) {
        this.kT = kt;
        return this;
    }

    public Material setKR(Double3 kr) {
        this.kR = kr;
        return this;
    }

    public Material setKR(double kr) {
        this.kR = new Double3(kr);
        return this;
    }
}
