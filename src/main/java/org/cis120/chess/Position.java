package org.cis120.chess;

import java.util.ArrayList;
import java.util.Objects;

public class Position implements Comparable<Position>{

    private final int x;
    private final int y;

    public Position (int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position (String string) {
        String[] parts = string.split("(?<=\\D)(?=\\d)");
        int horizontal = 0;
        for(char c: parts[0].toLowerCase().toCharArray()){
            horizontal += c - 97;
        }
        this.x = horizontal;
        this.y = Integer.parseInt(parts[1]) - 1;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Position plus(Position other) {
        return new Position (x + other.x, y + other.y);
    }

    public Position times(int scale) {
        return new Position (x * scale, y * scale);
    }

    public Position[] allDirections() {
        if (x == 0 && y == 0) {
            return new Position[] {this};
        }
        if (x == 0) {
            return new Position[] {
                    this,
                    new Position(y, 0),
                    new Position(0, -y),
                    new Position (-y, 0)
            };
        }
        if (y == 0) {
            return new Position[] {
                    this,
                    new Position(0, x),
                    new Position(-x, 0),
                    new Position (0, -x)
            };
        }
        if (x == y) {
            return new Position[] {
                    this,
                    new Position(x, y),
                    new Position(-x, y),
                    new Position(x, -y),
                    new Position(-x, -y)
            };
        }
        return new Position[] {
                this,
                new Position(y, x),
                new Position(-x, y),
                new Position(y, -x),
                new Position(x, -y),
                new Position(-y, x),
                new Position(-x, -y),
                new Position(-y, -x)
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * <p>The implementor must ensure
     * {@code sgn(x.compareTo(y)) == -sgn(y.compareTo(x))}
     * for all {@code x} and {@code y}.  (This
     * implies that {@code x.compareTo(y)} must throw an exception iff
     * {@code y.compareTo(x)} throws an exception.)
     *
     * <p>The implementor must also ensure that the relation is transitive:
     * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies
     * {@code x.compareTo(z) > 0}.
     *
     * <p>Finally, the implementor must ensure that {@code x.compareTo(y)==0}
     * implies that {@code sgn(x.compareTo(z)) == sgn(y.compareTo(z))}, for
     * all {@code z}.
     *
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * {@code (x.compareTo(y)==0) == (x.equals(y))}.  Generally speaking, any
     * class that implements the {@code Comparable} interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     *
     * <p>In the foregoing description, the notation
     * {@code sgn(}<i>expression</i>{@code )} designates the mathematical
     * <i>signum</i> function, which is defined to return one of {@code -1},
     * {@code 0}, or {@code 1} according to whether the value of
     * <i>expression</i> is negative, zero, or positive, respectively.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(Position o) {
        return hashCode() - o.hashCode();
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public static void main(String[] args) {
        Position testing = new Position("A1");
    }
}
