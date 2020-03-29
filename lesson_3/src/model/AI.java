package model;

import java.util.ArrayList;

public class AI {

    private ArrayList<Coord> predictions;
    private int dir;
    private Coord lastHit = null;

    public AI() {
        predictions = new ArrayList<>();
        dir = -1;
    }

    public Coord turn() {
        if (!predictions.isEmpty()) {

            if (dir == -1) {
                Coord coord = predictions.get(0);
                predictions.remove(coord);
                return coord;
            }

            if (dir == Utils.HORIZONTAL) {
                for (Coord coord : predictions) {
                    if (coord.y == lastHit.y) {
                        predictions.remove(coord);
                        return coord;
                    }
                }
            }

            if (dir == Utils.VERTICAL) {
                for (Coord coord : predictions) {
                    if (coord.x == lastHit.x) {
                        predictions.remove(coord);
                        return coord;
                    }
                }
            }

        }

        return Utils.getRandomCoord();
    }

    public void addPredictionsAround(ArrayList<Coord> predictions) {
        this.predictions.addAll(predictions);
    }

    public void clearPredictions() {
        predictions.clear();
        lastHit = null;
        dir = -1;
    }

    public void improvePredictions(Coord coord) {
        if (lastHit != null) {
            if (coord.x == lastHit.x) {
                dir = Utils.VERTICAL;
            } else {
                dir = Utils.HORIZONTAL;
            }
        }

        this.lastHit = coord;
    }

}
