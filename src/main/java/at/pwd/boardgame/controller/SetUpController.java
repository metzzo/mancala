package at.pwd.boardgame.controller;

import at.pwd.boardgame.services.ControllerFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by rfischer on 13/04/2017.
 */
public class SetUpController implements ControlledScreen, Initializable {
    public static final String SETUP_SCREEN = "/setup_controller.fxml";

    private NavigationController navigationController;

    public static void init() {
        ControllerFactory.getInstance().register(
                SETUP_SCREEN,
                SetUpController.class.getResource(SETUP_SCREEN),
                SetUpController.class.getResourceAsStream(SETUP_SCREEN)
        );
    }

    @Override
    public void setNavigationController(NavigationController navigationController) {
        this.navigationController = navigationController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void startGamePressed(ActionEvent actionEvent) {
        navigationController.setScreen(BoardController.GAME_SCREEN);
    }
}
