package at.pwd.boardgame.controller;

import at.pwd.boardgame.game.interfaces.Agent;
import at.pwd.boardgame.game.interfaces.Game;
import at.pwd.boardgame.game.interfaces.HumanAgent;
import at.pwd.boardgame.services.ControllerFactory;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Control;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by rfischer on 13/04/2017.
 */
public abstract class BoardController implements ControlledScreen, Initializable {
    public static final String GAME_SCREEN = "/board_controller.fxml";

    protected NavigationController navigationController;
    private Game game;
    private List<Agent> agents;
    private int currentAgentId = 0;

    public static void init(final Game game, final List<Agent> agents) {
        ControllerFactory.getInstance().register(
                GAME_SCREEN,
                BoardController.class.getResource(GAME_SCREEN),
                game.getViewXml(),
                screen -> {
                    BoardController ctrl = (BoardController) screen;
                    ctrl.setGame(game);
                    ctrl.setAgents(agents);
                }
        );
    }

    @Override
    public void setNavigationController(NavigationController navigationController) {
        this.navigationController = navigationController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //this.updateUI();
    }

    public Game getGame() {
        return game;
    }

    private void setGame(Game game) {
        this.game = game;
    }

    private Agent getCurrentAgent() {
        return agents.get(currentAgentId);
    }

    public void handleAction(ActionEvent event) {
        if (getCurrentAgent() instanceof HumanAgent) {
            String id = ((Control)event.getSource()).getId();
            ((HumanAgent) getCurrentAgent()).handleAction(game, id);

            updateUI();
        }
    }

    protected abstract void updateUI();

    public void setAgents(List<Agent> agents) {
        this.agents = agents;
    }
}
