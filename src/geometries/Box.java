package geometries;

import primitives.*;

import java.util.List;

/**
 * Class Box represents an axis-aligned rectangular box (cuboid)
 * in 3D space, defined by two opposite corner points.
 * The box is composed of 6 rectangular faces (Polygons).
 *
 * @author Naomie Gronstein and Efrat Brinkman
 */
public class Box extends Geometry {
    private final Double3 min;
    private final Double3 max;
    private final List<Polygon> sideFaces;
    private final Geometries components = new Geometries();

    public Box(Double3 p1, Double3 p2) {
        if (p1.d1() == p2.d1() ||
                p1.d2() == p2.d2() ||
                p1.d3() == p2.d3()) {
            throw new IllegalArgumentException("Points must not be aligned on the same axis");
        }
        this.min = new Double3(Math.min(p1.d1(), p2.d1()), Math.min(p1.d2(), p2.d2()), Math.min(p1.d3(), p2.d3()));
        this.max = new Double3(Math.max(p1.d1(), p2.d1()), Math.max(p1.d2(), p2.d2()), Math.max(p1.d3(), p2.d3()));
        this.sideFaces = List.of( // Left (x = minX) – counter-clockwise when looking from x < 0
                new Polygon(
                        new Point(min.d1(), min.d2(), max.d3()),
                        new Point(min.d1(), max.d2(), max.d3()),
                        new Point(min.d1(), max.d2(), min.d3()),
                        new Point(min.d1(), min.d2(), min.d3())
                ),
                // Right (x = maxX) – counter-clockwise when looking from x > 0
                new Polygon(
                        new Point(max.d1(), min.d2(), min.d3()),
                        new Point(max.d1(), max.d2(), min.d3()),
                        new Point(max.d1(), max.d2(), max.d3()),
                        new Point(max.d1(), min.d2(), max.d3())
                ),
                // Bottom (y = minY) – counter-clockwise from y < 0
                new Polygon(
                        new Point(min.d1(), min.d2(), max.d3()),
                        new Point(max.d1(), min.d2(), max.d3()),
                        new Point(max.d1(), min.d2(), min.d3()),
                        new Point(min.d1(), min.d2(), min.d3())
                ),
                // Top (y = maxY) – counter-clockwise from y > 0
                new Polygon(
                        new Point(min.d1(), max.d2(), max.d3()),
                        new Point(max.d1(), max.d2(), max.d3()),
                        new Point(max.d1(), max.d2(), min.d3()),
                        new Point(min.d1(), max.d2(), min.d3())
                ),
                // Front (z = minZ) – counter-clockwise from z < 0
                new Polygon(
                        new Point(min.d1(), min.d2(), min.d3()),
                        new Point(min.d1(), max.d2(), min.d3()),
                        new Point(max.d1(), max.d2(), min.d3()),
                        new Point(max.d1(), min.d2(), min.d3())
                ),
                // Back (z = maxZ) – counter-clockwise from z > 0
                new Polygon(
                        new Point(max.d1(), min.d2(), max.d3()),
                        new Point(max.d1(), max.d2(), max.d3()),
                        new Point(min.d1(), max.d2(), max.d3()),
                        new Point(min.d1(), min.d2(), max.d3())
                ));
        // Create the 6 faces of the box
        components.add(sideFaces.toArray(new Polygon[0]));
    }

    @Override
    public Vector getNormal(Point point) {
        for (Polygon face : sideFaces) {
            if (face.isOn(point)) {
                return face.getNormal(point);
            }
        }
    return null;
    }

    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
        return components.calculateIntersections(ray);
    }

    @Override
    public Geometry setEmission(Color emission) {
        for (Polygon p : sideFaces) p.setEmission(emission);
        return this;
    }

    @Override
    public Geometry setMaterial(Material material) {
        for (Polygon p : sideFaces) p.setMaterial(material);
        return this;
    }
}
