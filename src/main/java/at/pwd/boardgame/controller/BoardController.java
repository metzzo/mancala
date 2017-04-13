package at.pwd.boardgame.controller;

import at.pwd.boardgame.services.BoardTransformer;
import at.pwd.boardgame.services.ScreenFactory;
import javafx.fxml.Initializable;

import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by rfischer on 13/04/2017.
 */
public abstract class BoardController implements ControlledScreen, Initializable {
    private NavigationController navigationController;

    @Override
    public void setNavigationController(NavigationController navigationController) {
        this.navigationController = navigationController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
