package model;

import java.util.ArrayList;
import java.util.Random;

public class Utils {

    public static final int IMAGE_SIZE = 30;
    public static final int ROWS = 10;
    public static final int COLS = 10;

    public static final int HORISONTAL = 0;
    public static final int VERTICAL = 1;

    private static Random random = new Random();

    public static boolean inRange(Coord coord) {
        return coord.x >= 0 && coord.x < ROWS && coord.y >= 0 && coord.y < COLS;
    }

    public static Coord getRandomCoord() {
        return new Coord(random.nextInt(COLS), random.nextInt(ROWS));
    }

    public static ArrayList<Coord> getCoordsAround(Coord coord) {
        ArrayList<Coord> list = new ArrayList<>();
        for (int x = coord.x - 1; x <= coord.x + 1; x++) {
            for (int y = coord.y - 1; y <= coord.y + 1; y++) {
                Coord around;
                if (inRange(around = new Coord(x, y))) {
                    if (!around.equals(coord)) {
                        list.add(around);
                    }
                }
            }
        }
        return list;
    }

}
