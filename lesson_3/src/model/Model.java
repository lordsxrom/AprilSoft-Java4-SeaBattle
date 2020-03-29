package model;

import model.enums.Box;
import model.enums.State;
import presenter.ModelListener;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;

public class Model implements IModel {

    private ModelListener listener;

    private ShipsMap playerMap;
    private ShipsMap aiMap;

    private AI ai;

    public Model() {
        initImages();

        playerMap = new ShipsMap();
        playerMap.setListener(new ShipsMap.ShipDestroyedListener() {
            @Override
            public void onShipDestroyed() {
                ai.clearPredictions();
            }

            @Override
            public void onShipHited(Coord coord) {
                ai.addPredictionsAround(getPredictionCoords(coord));
                ai.improvePredictions(coord);
            }
        });

        aiMap = new ShipsMap();
        aiMap.setListener(new ShipsMap.ShipDestroyedListener() {
            @Override
            public void onShipDestroyed() {

            }

            @Override
            public void onShipHited(Coord coord) {

            }
        });

        ai = new AI();
    }

    private void initImages() {
        for (Box box : Box.values()) {
            String filename = "img/" + box.name().toLowerCase() + ".png";
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            box.image = new ImageIcon(classloader.getResource(filename)).getImage();
        }
    }

    @Override
    public void startGame() {
        playerMap.init();
        aiMap.init();

        listener.updatePlayerMap(drawPlayerMap(playerMap));
        listener.updateAiMap(drawAIMap(aiMap));
    }

    @Override
    public void setListener(ModelListener listener) {
        this.listener = listener;
    }

    @Override
    public void onMousePressed(int x, int y) {
        Coord coord = new Coord(x, y);
        Box box = aiMap.getMap().getBox(coord);
        if (box == Box.DECK) {
            aiMap.setHitOnDeck(coord);
            listener.updateAiMap(drawAIMap(aiMap));
            if (aiMap.getShips().size() == 0) {
                listener.updateState(State.WIN);
            }
        } else if (box == Box.SEA) {
            aiMap.setMissOnSea(coord);
            listener.updateAiMap(drawAIMap(aiMap));
            aiTurn();
        }
    }

    private void aiTurn() {
        while (true) {
            Coord coord = ai.turn();
            Box box = playerMap.getMap().getBox(coord);
            if (box == Box.DECK) {
                playerMap.setHitOnDeck(coord);
                listener.updatePlayerMap(drawPlayerMap(playerMap));
                if (playerMap.getShips().size() == 0) {
                    listener.updateState(State.LOSE);
                    break;
                }
            } else if (box == Box.SEA) {
                playerMap.setMissOnSea(coord);
                listener.updatePlayerMap(drawPlayerMap(playerMap));
                break;
            }
        }
    }

    private BufferedImage drawPlayerMap(ShipsMap map) {
        BufferedImage img = new BufferedImage(Utils.COLS * Utils.IMAGE_SIZE,
                Utils.ROWS * Utils.IMAGE_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();

        for (Map.Entry<Coord, Box> entry : map.getMap().getMap().entrySet()) {
            Coord coord = entry.getKey();
            Box box = entry.getValue();

            g.drawImage((Image) box.image,
                    coord.x * Utils.IMAGE_SIZE, coord.y * Utils.IMAGE_SIZE,
                    Utils.IMAGE_SIZE, Utils.IMAGE_SIZE, null);
        }

        return img;
    }

    private BufferedImage drawAIMap(ShipsMap map) {
        BufferedImage img = new BufferedImage(Utils.COLS * Utils.IMAGE_SIZE,
                Utils.ROWS * Utils.IMAGE_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();

        for (Map.Entry<Coord, Box> entry : map.getMap().getMap().entrySet()) {
            Coord coord = entry.getKey();
            Box box = entry.getValue();
            if (box == Box.DECK) box = Box.SEA;
            g.drawImage((Image) box.image,
                    coord.x * Utils.IMAGE_SIZE, coord.y * Utils.IMAGE_SIZE,
                    Utils.IMAGE_SIZE, Utils.IMAGE_SIZE, null);
        }

        return img;
    }

    public ArrayList<Coord> getPredictionCoords(Coord coord) {
        ArrayList<Coord> list = new ArrayList<>();

        Coord left = new Coord(coord.x - 1, coord.y);
        if (Utils.inRange(left) &&
                (playerMap.getMap().getBox(left) == Box.SEA ||
                        playerMap.getMap().getBox(left) == Box.DECK)) {
            list.add(left);
        }

        Coord right = new Coord(coord.x + 1, coord.y);
        if (Utils.inRange(right) &&
                (playerMap.getMap().getBox(right) == Box.SEA ||
                        playerMap.getMap().getBox(right) == Box.DECK)) {
            list.add(right);
        }

        Coord up = new Coord(coord.x, coord.y - 1);
        if (Utils.inRange(up) &&
                (playerMap.getMap().getBox(up) == Box.SEA ||
                        playerMap.getMap().getBox(up) == Box.DECK)) {
            list.add(up);
        }

        Coord down = new Coord(coord.x, coord.y + 1);
        if (Utils.inRange(down) &&
                (playerMap.getMap().getBox(down) == Box.SEA ||
                        playerMap.getMap().getBox(down) == Box.DECK)) {
            list.add(down);
        }

        return list;
    }
}
