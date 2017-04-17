package at.pwd.boardgame.game.mancala;

import at.pwd.boardgame.game.interfaces.Game;
import at.pwd.boardgame.game.interfaces.HumanAgent;

/**
 * Created by rfischer on 14/04/2017.
 */
public class MancalaHumanAgent implements HumanAgent {
    @Override
    public void doTurn(MancalaGame game) {

    }

    @Override
    public void handleAction(Game game, String id) {
        System.out.println("Agent selected id '" + id + "'");

        MancalaGame g = (MancalaGame) game;
        g.selectSlot(id);
    }
}
