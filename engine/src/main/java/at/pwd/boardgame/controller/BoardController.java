package at.pwd.boardgame.controller;

import at.pwd.boardgame.game.agent.Agent;
import at.pwd.boardgame.game.agent.AgentAction;
import at.pwd.boardgame.game.agent.HumanAgent;
import at.pwd.boardgame.game.base.*;
import at.pwd.boardgame.game.mancala.MancalaBoard;
import at.pwd.boardgame.game.mancala.MancalaGame;
import at.pwd.boardgame.game.mancala.MancalaState;
import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by rfischer on 13/04/2017.
 */
public class BoardController implements ControlledScreen, Initializable {
    private static final PseudoClass SELECTED_PSEUDO_CLASS =
            PseudoClass.getPseudoClass("selected");

    private NavigationController navigationController;
    private MancalaGame game;

    @FXML
    private Label depotLabel0;

    @FXML
    private Label depotLabel1;

    private Label[] depotLabels;

    @FXML private GridPane grid;
    private Map<String, Node> nodes = new HashMap<>();

    private List<Agent> agents;
    private IntegerProperty currentAgent = new SimpleIntegerProperty(-1);
    private int computationTime;
    private boolean calculating = false;

    public void start() {
        depotLabels = new Label[] {depotLabel0, depotLabel1};

        currentAgentProperty().addListener((observable, oldValue, newValue) -> {
            int oldVal = oldValue.intValue();
            int newVal = newValue.intValue();

            if (oldVal >= 0) {
                depotLabels[oldVal].pseudoClassStateChanged(SELECTED_PSEUDO_CLASS, false);
            }
            depotLabels[newVal].pseudoClassStateChanged(SELECTED_PSEUDO_CLASS, true);

        });

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

    public MancalaGame getGame() {
        return game;
    }

    public void setGame(MancalaGame game) {
        this.game = game;
    }

    public void handleAction(ActionEvent event) {
        if (getCurrentAgent() instanceof HumanAgent) {
            String id = ((Control)event.getSource()).getId();
            ((HumanAgent) getCurrentAgent()).handleAction(game, id);

            runAgent();
        }
    }

    private void bindNode(String id, Node node) {
        MancalaState state = getGame().getState();
        MancalaBoard board = getGame().getBoard();

        Button button = null;

        if (board.isSlot(id)) {
            button = (Button)node;

            button.disableProperty().bind(state.getState(id));
        } else if (board.isDepot(id)) {
            BorderPane pane = (BorderPane) nodes.get(id);
            button = (Button)pane.getCenter();
        }

        if (button != null) {
            StringBinding binding = state.getStones(id).numProperty().asString();
            button.textProperty().bind(binding);
        }
    }

    public void setAgents(List<Agent> agents) {
        this.agents = agents;
        currentAgent.set(-1);
    }

    public void nextTurn() {
        WinState winState = getGame().checkIfPlayerWins();
        if (winState.getState() != WinState.States.NOBODY) {
            gameEnded(winState);
        } else {
            currentAgent.set(getGame().nextPlayer());

            if (!(getCurrentAgent() instanceof HumanAgent)) {
                runAgent();
            }
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
                action = getCurrentAgent().doTurn(getComputationTime(), getGame().getState().copy(), getGame().getBoard());
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

    private void gameEnded(WinState winState) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game has ended");
        alert.setHeaderText(null);
        switch (winState.getState()) {
            case MULTIPLE:
                alert.setContentText("The game ended in a draw!");
                break;
            case TIMEOUT:
                alert.setContentText("The game ended in a timeout by the current agent " + getCurrentAgent());
                break;
            default:
                alert.setContentText("Player " + (winState.getPlayerId() + 1) + " has won the game!");
                break;
        }

        alert.showAndWait();
        navigationController.setScreen(SetUpController.createSetUpScreen());
    }
}
