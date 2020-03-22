package model;

import presenter.ModelListener;

public interface IModel {

    void startGame();

    void setListener(ModelListener listener);

    void onMousePressed(int x, int y);

}