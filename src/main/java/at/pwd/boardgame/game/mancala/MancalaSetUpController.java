package at.pwd.boardgame.game.mancala;

import at.pwd.boardgame.controller.BoardController;
import at.pwd.boardgame.controller.ControlledScreen;
import at.pwd.boardgame.controller.NavigationController;
import at.pwd.boardgame.game.base.Agent;
import at.pwd.boardgame.services.AgentService;
import at.pwd.boardgame.services.ControllerFactory;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by rfischer on 13/04/2017.
 */
public class MancalaSetUpController implements ControlledScreen, Initializable {
    public static final String SETUP_SCREEN = "/setup_controller.fxml";

    private NavigationController navigationController;
    private ListProperty<Agent> agents = new SimpleListProperty<>();

    @FXML
    ComboBox<Agent> player1Agent;
    @FXML
    ComboBox<Agent> player2Agent;

    public static void init() {
        ControllerFactory.getInstance().register(
                SETUP_SCREEN,
                MancalaSetUpController.class.getResource(SETUP_SCREEN),
                MancalaSetUpController.class.getResourceAsStream(SETUP_SCREEN)
        );
    }

    @Override
    public void setNavigationController(NavigationController navigationController) {
        this.navigationController = navigationController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Agent> list = AgentService.getInstance().getAgents();
        if (list.size() == 0) {
            throw new RuntimeException("There is no agent!");
        }

        agents.set(FXCollections.observableArrayList(list));
        player1Agent.setItems(agents);
        player1Agent.setValue(agents.get(0));

        player2Agent.setItems(agents);
        player2Agent.setValue(agents.get(0));
    }

    @FXML
    public void startGamePressed(ActionEvent actionEvent) {
        navigationController.setScreen(BoardController.GAME_SCREEN);
    }
}
