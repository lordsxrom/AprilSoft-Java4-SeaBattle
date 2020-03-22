package presenter;

import model.enums.State;

import java.awt.image.BufferedImage;

public interface ModelListener {

    void updatePlayerMap(BufferedImage map);

    void updateAiMap(BufferedImage map);

    void updateState(State state);

}