package at.pwd.boardgame.controller;

import at.pwd.boardgame.game.GameFactory;
import at.pwd.boardgame.game.agent.Agent;
import at.pwd.boardgame.game.mancala.MancalaGame;
import at.pwd.boardgame.game.agent.AgentService;
import at.pwd.boardgame.services.ScreenFactory;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by rfischer on 13/04/2017.
 */
public class SetUpController implements ControlledScreen, Initializable {
    public static final String SETUP_SCREEN = "/setup_controller.fxml";

    private NavigationController navigationController;
    private ListProperty<Agent> agents = new SimpleListProperty<>();

    @FXML
    ComboBox<Agent> player1Agent;
    @FXML
    ComboBox<Agent> player2Agent;
    @FXML
    Spinner computationTime;

    public static Parent createSetUpScreen() {
        return ScreenFactory.getInstance().loadScreen(
                SetUpController.class.getResource(SETUP_SCREEN),
                SetUpController.class.getResourceAsStream(SETUP_SCREEN),
                null
        );
    }

    @Override
    public void setNavigationController(NavigationController navigationController) {
        this.navigationController = navigationController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        reloadAgents();

        player1Agent.setItems(agents);
        player1Agent.setValue(agents.get(0));

        player2Agent.setItems(agents);
        player2Agent.setValue(agents.get(0));
    }

    @FXML
    public void startGamePressed(ActionEvent actionEvent) {
        final MancalaGame game = (MancalaGame) GameFactory.getInstance().create(MancalaGame.GAME_NAME);

        Parent screen = ScreenFactory.getInstance().loadScreen(
                BoardController.class.getResource(BoardController.GAME_SCREEN),
                game.getViewXml(),
                s -> {
                    List<Agent> selectedAgents = new ArrayList<>();
                    selectedAgents.add(player1Agent.getValue());
                    selectedAgents.add(player2Agent.getValue());

                    BoardController ctrl = (BoardController) s;
                    ctrl.setGame(game);
                    ctrl.setAgents(selectedAgents);
                    ctrl.setComputationTime((Integer)computationTime.getValue());
                    ctrl.start();
                });

        navigationController.setScreen(screen);
    }

    public void player1loadAi(ActionEvent actionEvent) {
        loadAi();
        player1Agent.setValue(agents.get(agents.size() - 1));
    }

    public void player2loadAi(ActionEvent actionEvent) {
        loadAi();
        player2Agent.setValue(agents.get(agents.size() - 1));
    }

    private void loadAi() {
        AddAgentController.show();

        reloadAgents();
    }

    private void reloadAgents() {
        List<Agent> list = AgentService.getInstance().getAgents();
        if (list.size() == 0) {
            throw new RuntimeException("There is no agent!");
        }

        agents.set(FXCollections.observableArrayList(list));
    }
}
