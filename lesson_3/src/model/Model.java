package model;

import model.enums.Box;
import model.enums.State;
import presenter.ModelListener;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

public class Model implements IModel {

    private ShipsMap playerMap;
    private ModelListener listener;

    private ShipsMap aiMap;


    public Model() {
        initImages();

        playerMap = new ShipsMap();
        aiMap = new ShipsMap();
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
            Coord coord = Utils.getRandomCoord();
            Box box = playerMap.getMap().getBox(coord);
            if (box == Box.DECK) {
                playerMap.setHitOnDeck(coord);
                listener.updatePlayerMap(drawPlayerMap(playerMap));
                if (playerMap.getShips().size() == 0) {
                    listener.updateState(State.LOSE);
                    break;
                }
            } else if (box == Box.SEA) {
                listener.updatePlayerMap(drawPlayerMap(playerMap));
                playerMap.setMissOnSea(coord);
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


}
