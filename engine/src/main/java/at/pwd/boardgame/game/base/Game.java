package at.pwd.boardgame.game.base;

import java.io.InputStream;

/**
 * Created by rfischer on 13/04/2017.
 */
public interface Game<StateType extends State, BoardType extends Board> {
    InputStream getViewXml();

    void loadBoard();

    StateType getState();
    BoardType getBoard();

    int nextPlayer();
}
