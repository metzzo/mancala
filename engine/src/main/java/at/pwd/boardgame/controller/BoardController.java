package at.pwd.boardgame.controller;

import at.pwd.boardgame.game.agent.Agent;
import at.pwd.boardgame.game.agent.AgentAction;
import at.pwd.boardgame.game.agent.HumanAgent;
import at.pwd.boardgame.game.base.*;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
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

    @FXML protected GridPane grid;
    protected Map<String, Node> nodes = new HashMap<>();

    protected List<Agent> agents;
    private IntegerProperty currentAgent = new SimpleIntegerProperty(-1);
    private int computationTime;
    private boolean calculating = false;

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
        }
    }

    protected abstract void bindNode(String id, Node node);

    public void setAgents(List<Agent> agents) {
        this.agents = agents;
        currentAgent.set(-1);
    }

    public void nextTurn() {
        currentAgent.set(getGame().nextPlayer());

        if (!(getCurrentAgent() instanceof HumanAgent)) {
            runAgent();
        }
    }

    public Agent getCurrentAgent() {
        return agents.get(currentAgentProperty().get());
    }

    public IntegerProperty currentAgentProperty() {
        return currentAgent;
    }

    private void runAgent() {
        System.out.println("Running agent " + getCurrentAgent());

        final Thread timer = new Thread(() -> {
            try {
                Thread.sleep(1000*getComputationTime());
                Platform.runLater(() -> {
                    if (calculating) {
                        WinState state = new WinState(WinState.States.TIMEOUT, -1);
                        gameEnded(state);
                    }
                });
            } catch (InterruptedException ignored) { }
        });
        timer.start();

        final Thread calculator = new Thread(() -> {
            final AgentAction action;
            try {
                action = getCurrentAgent().doTurn(getGame().getState().copy(), getGame().getBoard());
            } finally {
                timer.interrupt();
                calculating = false;
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }

            Platform.runLater(() -> {
                AgentAction.NextAction next = action.applyAction(game);

                new Thread(() -> {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
                    Platform.runLater(() -> {
                        switch (next) {
                            case NEXT_PLAYER:
                                nextTurn();
                                break;
                            case SAME_PLAYER:
                                if (!(getCurrentAgent() instanceof HumanAgent)) {
                                    runAgent();
                                }
                                break;
                        }
                    });
                }).start();
            });
        });
        calculating = true;
        calculator.start();
    }

    public void setComputationTime(int computationTime) {
        this.computationTime = computationTime;
    }

    public int getComputationTime() {
        return computationTime;
    }

    protected abstract void gameEnded(WinState state);
}
