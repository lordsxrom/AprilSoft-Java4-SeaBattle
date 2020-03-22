package model;

import java.util.Objects;

public class Coord implements Comparable {

    public int x, y;

    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coord coord = (Coord) o;
        return x == coord.x && y == coord.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public int compareTo(Object o) {
        Coord coord = (Coord) o;
        if (this.x == coord.x && this.y == coord.y) return 0;
        else if (this.x + this.y > coord.x + coord.y) return 1;
        else return -1;
    }

}