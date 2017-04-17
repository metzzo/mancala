package at.pwd.boardgame.game.mancala;

import at.pwd.boardgame.controller.BoardController;
import javafx.scene.control.Button;

/**
 * Created by rfischer on 15/04/2017.
 */
public class MancalaBoardController extends BoardController<MancalaGame> {
    @Override
    protected void updateUI() {
        MancalaState state = getGame().getState();
        MancalaBoard board = getGame().getBoard();

        for (Element elem : board.getElements()) {
            Button b = (Button) navigationController.getScene().lookup("#"+elem.getId());
            b.setText(String.valueOf(state.getStones(elem.getId())));
        }
    }
}
