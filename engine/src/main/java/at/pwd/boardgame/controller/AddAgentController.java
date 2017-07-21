package at.pwd.boardgame.controller;

import at.pwd.boardgame.controller.mancala.MancalaSetUpController;
import at.pwd.boardgame.game.agent.Agent;
import at.pwd.boardgame.game.agent.AgentService;
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
                MancalaSetUpController.class.getResource(ADDAGENT_SCREEN),
                MancalaSetUpController.class.getResourceAsStream(ADDAGENT_SCREEN),
                null
        );

        Stage stage = new Stage();
        stage.setTitle("Add Agent");
        stage.setScene(new Scene(screen, 300, 300));
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

            Agent agent = null;
            try {
                URLClassLoader child = new URLClassLoader(new URL[]{jarFile.toURI().toURL()}, this.getClass().getClassLoader());
                Class classToLoad = Class.forName (className.getText(), true, child);
                agent = (Agent) classToLoad.newInstance ();
            } catch (MalformedURLException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
                error = "Could not load agent (maybe wrong class name?)";
            }

            if (agent != null) {
                AgentService.getInstance().register(agent);
                close(evt);
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