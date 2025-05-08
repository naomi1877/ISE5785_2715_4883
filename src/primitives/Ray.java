package primitives;

import java.util.List;
import java.util.Objects;

import static primitives.Util.isZero;

/**
 * Represents a ray in 3-dimensional space.
 * A ray has a starting point (head) and a direction.
 */
public class Ray {
    final Point head;
    final Vector direction;

    /**
     * Getter for head
     * @return head.
     */
    public Point getHead() {
        return head;
    }
    /**
     * Getter for direction
     * @return direction.
     */
    public Vector getDirection() {
        return direction;
    }

    /**
     * Constructs a ray with a given starting point and direction.
     * @param head The starting point of the ray.
     * @param direction The direction vector of the ray.
     */
    public Ray(Point head, Vector direction) {
        this.head = head;
        this.direction = direction.normalize();
    }

    @Override
    public String toString() {
        return "Ray: head=" + head.toString() + " direction=" + direction.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return (o instanceof Ray other)
                && this.head.equals(other.head)
                && this.direction.equals(other.direction);
    }


    /**
     * Returns a point on the ray at a distance t from the head.
     * @param t The distance from the head to the point.
     * @return The point on the ray at distance t from the head.
     */
    public Point getPoint(double t){
        if (isZero(t)) {
            return head;
        }
        return head.add(direction.scale(t));
    }

    /**
     * Finds the closest point to the ray from a list of points.
     * @param points The list of points to search.
     * @return The closest point to the ray.
     */
    public Point findClosestPoint(List<Point> points){
        if (points == null || points.isEmpty()) {
            return null;
        }
        Point closestPoint = points.get(0);
        // Initialize the closest point to the first point in the list
        double minDistance = head.distanceSquared(closestPoint);

        for (Point p: points){
            double distance = head.distanceSquared(p);
            if (distance < minDistance) {
                closestPoint = p;
                minDistance = distance;
            }
        }
        return closestPoint;
    }


}
