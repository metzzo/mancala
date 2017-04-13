package at.pwd.boardgame.ui;

import at.pwd.boardgame.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by rfischer on 13/04/2017.
 */
public class SetUpController implements ControlledScreen, Initializable {
    private NavigationController navigationController;

    @Override
    public void setNavigationController(NavigationController navigationController) {
        this.navigationController = navigationController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void startGamePressed(ActionEvent actionEvent) {
        navigationController.setScreen(Main.GAME_SCREEN);
    }
}
