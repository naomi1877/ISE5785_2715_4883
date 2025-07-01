package primitives;

import geometries.Intersectable.Intersection;

import java.util.List;

import static primitives.Util.isZero;

/**
 * Represents a ray in 3-dimensional space.
 * A ray has a starting point (head) and a direction.
 */
public class Ray {
    /**
     * A small constant used to adjust the head position of the ray
     * to avoid precision issues such as self-intersection.
     */
    private static final double DELTA = 0.1;
    /**
     * The starting point of the ray.
     */
    private final Point head;
    /**
     * The direction of the ray.
     */
    private final Vector direction;

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
     * Constructs a Ray with a given starting point (head), direction, and a normal vector.
     * If the direction is perpendicular (or nearly perpendicular) to the normal,
     * the starting point is slightly adjusted along the normal by a small constant {@code DELTA}
     * to avoid precision issues such as self-intersection.
     *
     * @param head      the starting point of the ray
     * @param direction the direction vector of the ray (will be normalized)
     * @param normal    the normal vector used to adjust the starting point if needed
     */
    public Ray(Point head, Vector direction, Vector normal) {
        // If the direction is perpendicular to the normal, adjust the head position slightly
        double t = direction.dotProduct(normal);
        this.direction = direction.normalize();
        this.head = head.add(normal.scale(t < 0 ? -DELTA : DELTA));
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
    public Vector getNormalVec() {
        return direction.normalize();
    }


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

    /**
     * Finds the closest intersection from a list of intersections relative to a reference point.
     *
     * @param list the list of intersections to search
     * @return the closest {@code Intersection} object to the reference point {@code head}, or {@code null} if the list is empty or null
     */
    public Intersection findClosestIntersection(List<Intersection> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
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
