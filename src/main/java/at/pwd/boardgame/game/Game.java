package at.pwd.boardgame.game;

import java.io.InputStream;
import java.util.List;

/**
 * Created by rfischer on 13/04/2017.
 */
public interface Game {
    InputStream getViewXml();

    void loadBoard(int numStones);

    State getState();
    Board getBoard();
}
