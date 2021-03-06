package at.pwd.boardgame.controller;

import at.pwd.boardgame.game.agent.Agent;
import at.pwd.boardgame.game.agent.AgentAction;
import at.pwd.boardgame.game.agent.HumanAgent;
import at.pwd.boardgame.game.base.*;
import at.pwd.boardgame.game.mancala.MancalaBoard;
import at.pwd.boardgame.game.mancala.MancalaGame;
import at.pwd.boardgame.game.mancala.MancalaState;
import at.pwd.boardgame.services.ScreenFactory;
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
 * Controller for handling the Mancala Boardgame
 */
public class BoardController implements ScreenFactory.BoardGameScreen, Initializable {
    private static final PseudoClass SELECTED_PSEUDO_CLASS =
            PseudoClass.getPseudoClass("selected");

    private NavigationController navigationController;
    private MancalaGame game;
    private int currentTurn = -1;

    @FXML
    private Label depotLabel0;

    @FXML
    private Label depotLabel1;

    private Label[] depotLabels;

    @FXML
    private GridPane grid;

    @FXML
    private Label turnCounter;

    private Map<String, Node> nodes = new HashMap<>();

    private List<Agent> agents;
    private IntegerProperty currentAgent = new SimpleIntegerProperty(-1);
    private int computationTime;
    private boolean calculating = false;
    private Thread timeoutTimer;
    private Thread agentRunner;

    public void start() {
        depotLabels = new Label[] {depotLabel0, depotLabel1};

        depotLabel0.setText("1: "+agents.get(0).toString());
        depotLabel1.setText("2: "+agents.get(1).toString());

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

    /**
     * @return Returns the current MancalaGame instance that is currently displayed
     */
    public MancalaGame getGame() {
        return game;
    }

    /**
     * Sets the current MancalaGame that is currently displayed
     * @param game the instane
     */
    public void setGame(MancalaGame game) {
        this.game = game;
    }

    /**
     * Called when a slot is selected and this action should be handled. This should only
     * execute code if a human is currently playing
     * @param event The event that caused it
     */
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

            button.disableProperty().bind(state.isSlotEnabledProperty(id));
        } else if (board.isDepot(id)) {
            BorderPane pane = (BorderPane) nodes.get(id);
            button = (Button)pane.getCenter();
        }

        if (button != null) {
            StringBinding binding = state.getStoneProperty(id).asString();
            button.textProperty().bind(binding);
        }
    }

    /**
     * sets the agents that are playing
     * @param agents the list of Agents
     */
    public void setAgents(List<Agent> agents) {
        this.agents = agents;
        currentAgent.set(-1);
    }

    /**
     * Called when current turn is finished and next players turn starts.
     */
    public void nextTurn() {
        currentTurn++;
        turnCounter.setText("Turn: " + (1 + currentTurn/2));

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

    /**
     * @return Returns the current players agent
     */
    public Agent getCurrentAgent() {
        return agents.get(currentAgentProperty().get());
    }

    /**
     * @return Returns the player ID of the current agent
     */
    public IntegerProperty currentAgentProperty() {
        return currentAgent;
    }

    private void runTimeout() {
        this.timeoutTimer = new Thread(() -> {
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
        timeoutTimer.start();
    }

    AgentAction.NextAction selectedNextAction;

    private void runAgent() {
        System.out.println("Running agent " + getCurrentAgent());

        runTimeout();

        if (this.agentRunner != null) {
            throw new RuntimeException("Previous agent runner is still running.");
        }

        this.agentRunner = new Thread(() -> {
            try {
                AgentAction<MancalaGame> action;
                try {
                    action = getCurrentAgent().doTurn(getComputationTime(), new MancalaGame(game));
                } finally {
                    timeoutTimer.interrupt();
                    calculating = false;
                }

                Thread.sleep(500);

                Platform.runLater(() -> {
                    selectedNextAction = action.applyAction(game);

                    synchronized (agentRunner) { agentRunner.notify(); }
                });
                synchronized (agentRunner) { agentRunner.wait(); }

                Thread.sleep(500);

                Platform.runLater(() -> {
                    this.agentRunner = null;
                    switch (selectedNextAction) {
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
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });
        calculating = true;
        agentRunner.start();
    }

    /**
     * Sets the computation time in seconds when a normal agent has a timeout
     * @param computationTime the value
     */
    public void setComputationTime(int computationTime) {
        this.computationTime = computationTime;
    }

    /**
     * @return Returns the computation time in seconds a agent can think for its turn
     */
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
