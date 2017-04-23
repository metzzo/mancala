package at.pwd.boardgame.game.base;

/**
 * Created by rfischer on 14/04/2017.
 */
public interface State {
    void setCurrentPlayer(int currentPlayer);
    int getCurrentPlayer();

    State copy();
}
