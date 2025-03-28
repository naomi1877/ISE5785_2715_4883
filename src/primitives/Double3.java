/**
 * Represents a 3D point with three double values.
 */
package primitives;

import static primitives.Util.isZero;

/**
 * This class represents a 3D coordinate with three double values.
 * It provides arithmetic operations and comparisons.
 */
public record Double3(double d1, double d2, double d3) {

    /** Zero vector (0,0,0) */
    public static final Double3 ZERO = new Double3(0, 0, 0);

    /** Unit vector (1,1,1) */
    public static final Double3 ONE = new Double3(1, 1, 1);

    /**
     * Constructor to initialize a Double3 object with the same value for all three components.
     * @param value the value for all three components
     */
    public Double3(double value) {
        this(value, value, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Double3 other)
                && isZero(d1 - other.d1)
                && isZero(d2 - other.d2)
                && isZero(d3 - other.d3);
    }

    @Override
    public int hashCode() {
        return (int) Math.round(d1 + d2 + d3);
    }

    @Override
    public String toString() {
        return "(" + d1 + "," + d2 + "," + d3 + ")";
    }

    /**
     * Adds two Double3 objects component-wise.
     * @param rhs the right-hand operand
     * @return the resulting Double3 object
     */
    public Double3 add(Double3 rhs) {
        return new Double3(d1 + rhs.d1, d2 + rhs.d2, d3 + rhs.d3);
    }

    /**
     * Subtracts two Double3 objects component-wise.
     * @param rhs the right-hand operand
     * @return the resulting Double3 object
     */
    public Double3 subtract(Double3 rhs) {
        return new Double3(d1 - rhs.d1, d2 - rhs.d2, d3 - rhs.d3);
    }

    /**
     * Scales a Double3 object by a scalar value.
     * @param rhs the scalar value
     * @return the scaled Double3 object
     */
    public Double3 scale(double rhs) {
        return new Double3(d1 * rhs, d2 * rhs, d3 * rhs);
    }

    /**
     * Multiplies two Double3 objects component-wise.
     * @param rhs the right-hand operand
     * @return the resulting Double3 object
     */
    public Double3 product(Double3 rhs) {
        return new Double3(d1 * rhs.d1, d2 * rhs.d2, d3 * rhs.d3);
    }
}
