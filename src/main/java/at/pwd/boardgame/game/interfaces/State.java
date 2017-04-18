package at.pwd.boardgame.game.interfaces;

/**
 * Created by rfischer on 14/04/2017.
 */
public interface State {
    void setCurrentPlayer(int currentPlayer);
    int getCurrentPlayer();
}
