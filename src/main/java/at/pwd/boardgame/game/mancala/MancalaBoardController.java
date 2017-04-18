package at.pwd.boardgame.game.mancala;

import at.pwd.boardgame.controller.BoardController;
import at.pwd.boardgame.controller.SetUpController;
import at.pwd.boardgame.game.base.WinState;
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

        if (board.isDepot(id)) {
            button = (Button)node;
        } else if (board.isSlot(id)) {
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
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game has ended");
            alert.setHeaderText(null);
            if (winState.getState() == WinState.States.MULTIPLE) {
                alert.setContentText("The game ended in a draw!");

            } else {
                alert.setContentText("Player " + (winState.getPlayerId() + 1) + " has won the game!");
            }
            alert.showAndWait();
            navigationController.setScreen(SetUpController.SETUP_SCREEN);
        } else {
            super.nextTurn();
        }
    }
}
