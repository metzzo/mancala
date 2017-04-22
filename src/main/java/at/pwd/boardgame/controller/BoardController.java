package at.pwd.boardgame.controller;

import at.pwd.boardgame.game.base.*;
import at.pwd.boardgame.services.ScreenFactory;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by rfischer on 13/04/2017.
 */
public abstract class BoardController<GameType extends Game> implements ControlledScreen, Initializable {
    public static final String GAME_SCREEN = "/board_controller.fxml";

    protected NavigationController navigationController;
    private GameType game;

    @FXML protected AnchorPane root;
    @FXML protected GridPane grid;
    protected Map<String, Node> nodes = new HashMap<>();

    protected List<Agent> agents;
    private int currentAgentId = 0;

    public void start() {
        for (String id : nodes.keySet()) {
            bindNode(id, nodes.get(id));
        }

        nextTurn();
    }

    @Override
    public void setNavigationController(NavigationController navigationController) {
        this.navigationController = navigationController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (Node node : grid.getChildren()) {
            String id = node.getId();
            nodes.put(id, node);
        }
    }

    public GameType getGame() {
        return game;
    }

    public void setGame(GameType game) {
        this.game = game;
    }

    public void handleAction(ActionEvent event) {
        if (getCurrentAgent() instanceof HumanAgent) {
            String id = ((Control)event.getSource()).getId();
            ((HumanAgent) getCurrentAgent()).handleAction(game, id);

            runAgent();

            nextTurn();
        }
    }

    protected abstract void bindNode(String id, Node node);

    public void setAgents(List<Agent> agents) {
        this.agents = agents;
        currentAgentId = -1;
    }

    public void nextTurn() {
        currentAgentId = (currentAgentId + 1) % agents.size();
        getGame().getState().setCurrentPlayer(currentAgentId);

        if (!(getCurrentAgent() instanceof HumanAgent)) {
            runAgent();
            Task<Void> sleeper = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ignored) {
                    }
                    return null;
                }
            };
            sleeper.setOnSucceeded(event -> nextTurn());
            new Thread(sleeper).start();
        }
    }

    public Agent getCurrentAgent() {
        return agents.get(currentAgentId);
    }

    private void runAgent() {
        System.out.println("Running agent " + getCurrentAgent());

        AgentAction action = getCurrentAgent().doTurn(getGame().getState().copy(), getGame().getBoard());
        action.applyAction(game);
    }
}
