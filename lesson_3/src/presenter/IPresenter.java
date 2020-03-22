package presenter;

import model.IModel;
import view.IView;

public interface IPresenter {

    void setView(IView view);

    void setModel(IModel model);

    void startGame();

    void initListeners();

}