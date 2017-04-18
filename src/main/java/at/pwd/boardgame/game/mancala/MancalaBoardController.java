package at.pwd.boardgame.game.mancala;

import at.pwd.boardgame.controller.BoardController;
import at.pwd.boardgame.controller.GameEndController;
import javafx.beans.binding.StringBinding;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.converter.NumberStringConverter;

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
        int winState = getGame().checkIfPlayerWins();
        if (winState != -1) {
            navigationController.setScreen(GameEndController.GAMEEND_SCREEN);
        } else {
            super.nextTurn();
        }
    }
}
