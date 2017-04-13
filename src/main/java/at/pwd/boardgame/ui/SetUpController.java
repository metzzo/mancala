package at.pwd.boardgame.ui;

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
}
