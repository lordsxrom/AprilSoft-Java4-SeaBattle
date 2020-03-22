package model;

import model.enums.Box;

import java.util.Map;
import java.util.TreeMap;

public class Ship {

    private int landscape;
    private int size;
    private Map<Coord, Box> decks = new TreeMap<>();

    public Ship(Coord head, int landscape, int size) {
        this.landscape = landscape;
        this.size = size;

        decks.put(head, Box.DECK);

        for (int i = 1; i < size; i++) {
            if (landscape == Utils.HORIZONTAL) {
                Coord coord = new Coord(head.x + i, head.y);
                decks.put(coord, Box.DECK);
            } else if (landscape == Utils.VERTICAL) {
                Coord coord = new Coord(head.x, head.y + i);
                decks.put(coord, Box.DECK);
            }
        }
    }

    public Map<Coord, Box> getDecks() {
        return decks;
    }

}
