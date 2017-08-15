package at.pwd.boardgame.controller;

import at.pwd.boardgame.game.agent.Agent;
import at.pwd.boardgame.services.AgentService;
import at.pwd.boardgame.services.ConfigService;
import at.pwd.boardgame.services.ScreenFactory;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ResourceBundle;

/**
 * Created by rfischer on 21/07/2017.
 */
public class AddAgentController implements Initializable, ControlledScreen {
    public static final String ADDAGENT_SCREEN = "/addagent_controller.fxml";

    private NavigationController navigationController;

    private File jarFile;

    @FXML
    private TextField path;

    @FXML
    private TextField className;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public static void show() {
        Parent screen = ScreenFactory.getInstance().loadScreen(
                AddAgentController.class.getResource(ADDAGENT_SCREEN),
                AddAgentController.class.getResourceAsStream(ADDAGENT_SCREEN),
                null
        );

        Stage stage = new Stage();
        stage.setTitle("Add Agent");
        stage.setScene(new Scene(screen, 300, 150));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    @Override
    public void setNavigationController(NavigationController navigationController) {
        this.navigationController = navigationController;
    }

    public void fileChooser(Event evt) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose jar file");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Jar files (*.jar)", "*.jar");
        fileChooser.setSelectedExtensionFilter(extFilter);
        this.jarFile = fileChooser.showOpenDialog(((Button)evt.getSource()).getScene().getWindow());
        path.setText(jarFile != null ? jarFile.toString() : "");
    }

    public void load(Event evt) {
        String error = "";
        if (this.jarFile != null && this.jarFile.exists()) {
            System.out.println("Load jar " + jarFile);

            String name = className.getText();
            try {
                AgentService.getInstance().load(jarFile, className.getText());
                ConfigService.getInstance().addAgent(jarFile, className.getText());
                close(evt);
            } catch (Exception e) {
                e.printStackTrace();
                error = "Could not load agent (maybe wrong class name?)";
            }
        } else {
            error = "No jar found";
        }
        System.out.println(error);
    }

    public void close(Event evt) {
        Stage s = (Stage) ((Button)evt.getSource()).getScene().getWindow();
        s.close();
    }
}
