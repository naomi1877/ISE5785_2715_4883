package geometries;

public abstract class RadialGeometry extends Geometry {
 protected final double radius;

    protected RadialGeometry(double radius) {
        this.radius = radius;
    }
}
