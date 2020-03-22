package presenter;

import model.IModel;
import model.enums.State;
import view.IView;

import java.awt.image.BufferedImage;

public class Presenter implements IPresenter, ModelListener, ViewListener {

    private IView view;
    private IModel model;

    public void initListeners() {
        view.setListener(this);
        model.setListener(this);
    }

    public void setView(IView view) {
        this.view = view;
    }

    public void setModel(IModel model) {
        this.model = model;
    }

    @Override
    public void startGame() {
        model.startGame();
    }

    @Override
    public void updatePlayerMap(BufferedImage map) {
        view.updatePlayerPanel(map);
    }

    @Override
    public void updateAiMap(BufferedImage map) {
        view.updateAiPanel(map);
    }

    @Override
    public void updateState(State state) {
        view.updateState(state.ordinal());
    }

    @Override
    public void onMousePressed(int x, int y) {
        model.onMousePressed(x, y);
    }

    @Override
    public void onStartButtonPressed() {
        model.startGame();
    }
}
