package primitives;

import geometries.Intersectable;
import geometries.Intersectable.Intersection;

import java.util.List;

import static primitives.Util.isZero;

/**
 * Represents a ray in 3-dimensional space.
 * A ray has a starting point (head) and a direction.
 */
public class Ray {
    /**
     * The starting point of the ray.
     */
    final Point head;
    /**
     * The direction of the ray.
     */
    final Vector direction;

    /**
     * Constructs a ray with a given starting point and direction.
     *
     * @param head      The starting point of the ray.
     * @param direction The direction vector of the ray.
     */
    public Ray(Point head, Vector direction) {
        this.head = head;
        this.direction = direction.normalize();
    }

    /**
     * Getter for head
     *
     * @return head.
     */
    public Point getHead() {
        return head;
    }

    /**
     * Getter for direction
     *
     * @return direction.
     */
    public Vector getDirection() {
        return direction;
    }

    /**
     * Returns the normalized direction vector of the ray.
     *
     * @return the normalized direction vector of the ray
     */
    public Vector getNormalVector() {
        return direction.normalize();
    }


    /**
     * Returns the normalized direction vector of the ray.
     *
     * @return the normalized direction vector of the ray
     */
    public Vector getNormalVec() {return direction.normalize();}



    @Override
    public String toString() {
        return "Ray: head=" + head.toString() + " direction=" + direction.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ray other)) return false;
        return this.head.equals(other.head) && this.direction.equals(other.direction);
    }

    /**
     * Returns a point on the ray at a distance t from the head.
     *
     * @param t The distance from the head to the point.
     * @return The point on the ray at distance t from the head.
     */
    public Point getPoint(double t) {
        if (isZero(t)) {
            return head;
        }
        return head.add(direction.scale(t));
    }

    /**
     * Finds the closest point to the ray from a list of points.
     *
     * @param points The list of points to search.
     * @return The closest point to the ray.
     */
    public Point findClosestPoint(List<Point> points) {
        if (points == null || points.isEmpty()) {
            return null;
        } else
            return findClosestIntersection(points.stream().map(p -> new Intersection(null, p)).toList()).point;
    }


    public Intersection findClosestIntersection(List<Intersection> list) {
        Intersection closest = null;
        double minDistance = Double.POSITIVE_INFINITY;
        for (Intersection point : list) {
            double distance = head.distance(point.point);
            if (distance < minDistance) {
                minDistance = distance;
                closest = point;
            }
        }
        return closest;
    }

}
