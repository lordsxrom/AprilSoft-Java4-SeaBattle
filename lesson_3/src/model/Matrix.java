package model;

import model.enums.Box;

import java.util.HashMap;

public class Matrix {

    private HashMap<Coord, Box> map;

    public Matrix(Box defaultBox) {
        map = new HashMap<>();
        for (int x = 0; x < Utils.COLS; x++)
            for (int y = 0; y < Utils.ROWS; y++)
                map.put(new Coord(x, y), defaultBox);
    }

    public Box getBox(Coord coord) {
        if (Utils.inRange(coord)) {
            return map.get(coord);
        }
        return null;
    }

    public void setBox(Coord coord, Box box) {
        if (Utils.inRange(coord)) {
            map.put(coord, box);
        }
    }

    public HashMap<Coord, Box> getMap() {
        return map;
    }

}