package at.pwd.boardgame.controller;

import at.pwd.boardgame.services.ControllerFactory;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by rfischer on 18/04/2017.
 */
public class GameEndController implements ControlledScreen, Initializable {
    public static final String GAMEEND_SCREEN = "/gameend_controller.fxml";
    private NavigationController navigationController;

    public static void init() {
        ControllerFactory.getInstance().register(
                GAMEEND_SCREEN,
                GameEndController.class.getResource(GAMEEND_SCREEN),
                GameEndController.class.getResourceAsStream(GAMEEND_SCREEN)
        );
    }

    @Override
    public void setNavigationController(NavigationController navigationController) {
        this.navigationController = navigationController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
