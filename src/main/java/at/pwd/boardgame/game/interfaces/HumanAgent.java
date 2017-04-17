package at.pwd.boardgame.game.interfaces;

import at.pwd.boardgame.game.mancala.MancalaGame;

/**
 * Created by rfischer on 14/04/2017.
 */
public interface HumanAgent extends Agent {
    void handleAction(Game game, String id);
}
