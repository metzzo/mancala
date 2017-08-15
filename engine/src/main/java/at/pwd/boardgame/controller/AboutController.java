package at.pwd.boardgame.controller;

import at.pwd.boardgame.Main;
import at.pwd.boardgame.services.ScreenFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by rfischer on 15/08/2017.
 */
public class AboutController implements Initializable, ControlledScreen {
    public static final String ABOUT_SCREEN = "/about_controller.fxml";
    private static final String REPO_DOMAIN = "https://github.com/metzzo/mancala/";


    private NavigationController navigationController;

    @FXML
    private Hyperlink website;

    @FXML
    private Hyperlink repo;

    @Override
    public void setNavigationController(NavigationController navigationController) {
        this.navigationController = navigationController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        website.setOnAction(event -> Main.getApp().getHostServices().showDocument(SetUpController.PWD_DOMAIN));
        repo.setOnAction(event -> Main.getApp().getHostServices().showDocument(AboutController.REPO_DOMAIN));

    }

    public static void show() {
        Parent screen = ScreenFactory.getInstance().loadScreen(
                AboutController.class.getResource(ABOUT_SCREEN),
                AboutController.class.getResourceAsStream(ABOUT_SCREEN),
                null
        );

        Stage stage = new Stage();
        stage.setTitle("About");
        stage.setScene(new Scene(screen, 300, 250));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
}
