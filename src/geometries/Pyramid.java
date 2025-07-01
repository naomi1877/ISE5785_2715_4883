package geometries;

import primitives.*;

import java.util.LinkedList;
import java.util.List;

public class Pyramid extends Geometry {
    private final Point top;
    private final Polygon base;
    private final List<Triangle> sideFaces = new LinkedList<>();
    private final Geometries components = new Geometries();

    public Pyramid(Point top, Polygon base) {
        this.top = top;
        this.base = base;

        List<Point> vertices = base.vertices;
        if (vertices.size() < 3)
            throw new IllegalArgumentException("Base must have at least 3 vertices");

        // saving the contour of the piramid base
        for (int i = 0; i < vertices.size(); i++) {
            Point p1 = vertices.get(i);
            Point p2 = vertices.get((i + 1) % vertices.size());
            sideFaces.add(new Triangle(top, p1, p2));
        }

        // adding the triangles and base to the components
        components.add(base);
        components.add(sideFaces.toArray(new Triangle[0]));
    }

    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
        return components.calculateIntersections(ray);
    }

    @Override
    public Vector getNormal(Point point) {
        if (base.isOn(point)) return base.getNormal(point);
        for (Triangle t : sideFaces)
            if (t.isOn(point)) return t.getNormal(point);
        return null;
    }

    @Override
    public Geometry setEmission(Color emission) {
        base.setEmission(emission);
        for (Triangle t : sideFaces) t.setEmission(emission);
        return this;
    }

    @Override
    public Geometry setMaterial(Material material) {
        base.setMaterial(material);
        for (Triangle t : sideFaces) t.setMaterial(material);
        return this;
    }

}
