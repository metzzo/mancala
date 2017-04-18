package at.pwd.boardgame.game.mancala;

import at.pwd.boardgame.controller.BoardController;
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

        StringBinding binding = state.getStones(id).numProperty().asString();

        if (board.isDepot(id)) {
            ((Button)node).textProperty().bind(binding);
        } else if (board.isSlot(id)) {
            BorderPane pane = (BorderPane) nodes.get(id);
            ((Button)pane.getCenter()).textProperty().bind(binding);
        }
    }
}
