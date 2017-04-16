package at.pwd.boardgame.game;

/**
 * Created by rfischer on 14/04/2017.
 */
public interface HumanAgent extends Agent {
    void handleAction(Game game, String id);
}
