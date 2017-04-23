package at.pwd.boardgame.controller.mancala;

import at.pwd.boardgame.controller.BoardController;
import at.pwd.boardgame.game.base.WinState;
import at.pwd.boardgame.game.mancala.MancalaBoard;
import at.pwd.boardgame.game.mancala.MancalaGame;
import at.pwd.boardgame.game.mancala.MancalaState;
import javafx.beans.binding.StringBinding;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

/**
 * Created by rfischer on 15/04/2017.
 */
public class MancalaBoardController extends BoardController<MancalaGame> {
    @Override
    protected void bindNode(String id, Node node) {
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

    @Override
    public void nextTurn() {
        WinState winState = getGame().checkIfPlayerWins();
        if (winState.getState() != WinState.States.NOBODY) {
            gameEnded(winState);
        } else {
            super.nextTurn();
        }
    }

    @Override
    protected void gameEnded(WinState winState) {
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
        navigationController.setScreen(MancalaSetUpController.createSetUpScreen());
    }
}
