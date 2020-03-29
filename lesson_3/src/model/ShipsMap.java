package model;

import model.enums.Box;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShipsMap {

    private Matrix map;
    private ArrayList<Ship> ships = new ArrayList<>();

    private ShipDestroyedListener listener;

    public ShipsMap() {
        map = new Matrix(Box.SEA);
    }

    public void init() {
        ships.clear();
        clearMap();
        placeShips();
    }

    private void clearMap() {
        for (Map.Entry<Coord, Box> entry : map.getMap().entrySet()) {
            map.setBox(entry.getKey(), Box.SEA);
        }
    }

    public void placeShips() {
        place(4);
        place(4);
        place(4);
        place(4);
        place(4);
        place(4);
        place(4);
//        for (int i = 0; i < 2; i++) {
//            place(3);
//        }
//        for (int i = 0; i < 3; i++) {
//            place(2);
//        }
//        for (int i = 0; i < 4; i++) {
//            place(1);
//        }
        clearMapFromMisses();
    }

    private void place(int size) {
        while (true) {
            boolean flag = false;
            Coord coord = Utils.getRandomCoord();
            int dir = (int) (Math.random() * 2);

            if (tryPlaceDeck(coord)) {
                switch (dir) {
                    case Utils.HORIZONTAL:
                        if (tryPlaceDeck(new Coord(coord.x + (size - 1), coord.y))) flag = true;
                        break;
                    case Utils.VERTICAL:
                        if (tryPlaceDeck(new Coord(coord.x, coord.y + (size - 1)))) flag = true;
                        break;
                }
            }

            if (flag) {
                Ship ship = new Ship(coord, dir, size);
                ships.add(ship);
                setShipOnMap(ship);
                surroundShipWithMisses(ship);
                break;
            }
        }
    }

    private void setShipOnMap(Ship ship) {
        for (Map.Entry<Coord, Box> entry : ship.getDecks().entrySet()) {
            Coord coord = entry.getKey();
            Box box = entry.getValue();
            map.setBox(coord, box);
        }
    }

    public void setHitOnDeck(Coord coord) {
        Ship ship = getShipByCoord(coord);
        if (ship != null) {
            ship.getDecks().put(coord, Box.HIT);

            if (isDestroyed(ship)) setDestroyedOnShip(ship);

            setShipOnMap(ship);
            listener.onShipHited(coord);
        }
    }

    private boolean isDestroyed(Ship ship) {
        return !ship.getDecks().containsValue(Box.DECK);
    }

    private void setDestroyedOnShip(Ship ship) {
        for (Map.Entry<Coord, Box> entry : ship.getDecks().entrySet()) {
            Coord deck = entry.getKey();
            ship.getDecks().put(deck, Box.DESTROYED);
        }
        surroundShipWithMisses(ship);
        ships.remove(ship);
        listener.onShipDestroyed();
    }

    private Ship getShipByCoord(Coord coord) {
        for (Ship ship : ships) {
            if (ship.getDecks().containsKey(coord)) {
                return ship;
            }
        }
        return null;
    }

    private boolean tryPlaceDeck(Coord coord) {
        return Utils.inRange(coord) && map.getBox(coord).equals(Box.SEA);
    }

    public void surroundShipWithMisses(Ship ship) {
        for (Map.Entry<Coord, Box> entry : ship.getDecks().entrySet()) {
            Coord coord = entry.getKey();
            List<Coord> list = Utils.getCoordsAround(coord);
            for (Coord around : list) {
                if (map.getBox(around) == Box.SEA) {
                    map.setBox(around, Box.MISS);
                }
            }
        }
    }

    private void clearMapFromMisses() {
        for (Map.Entry<Coord, Box> entry : map.getMap().entrySet()) {
            Coord coord = entry.getKey();
            Box box = entry.getValue();
            if (box == Box.MISS) {
                map.setBox(coord, Box.SEA);
            }
        }
    }

    public Matrix getMap() {
        return map;
    }

    public void setMissOnSea(Coord coord) {
        map.setBox(coord, Box.MISS);
    }

    public ArrayList<Ship> getShips() {
        return ships;
    }

    interface ShipDestroyedListener {
        void onShipDestroyed();
        void onShipHited(Coord coord);
    }

    public void setListener(ShipDestroyedListener listener) {
        this.listener = listener;
    }

}
