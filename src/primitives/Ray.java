package primitives;

import java.util.Objects;

public class Ray {
    final Point head;
    final Vector direction;

    public Ray(Point head, Vector direction) {
        this.head = head;
        this.direction=direction.normalize();
    }

    @Override
    public String toString() {
        return "Ray: head="+ head.toString()+"direction="+direction.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return  (o instanceof Ray other)
                && this.head.equals(other.head)
                && this.direction.equals(other.direction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(head, direction);
    }
}
