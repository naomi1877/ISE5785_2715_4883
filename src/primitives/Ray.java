package primitives;

import java.util.Objects;

/**
 * Represents a ray in 3-dimensional space.
 * A ray has a starting point (head) and a direction.
 */
public class Ray {
    final Point head;
    final Vector direction;

    /**
     * Constructs a ray with a given starting point and direction.
     *
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

    @Override
    public int hashCode() {
        return Objects.hash(head, direction);
    }
}
