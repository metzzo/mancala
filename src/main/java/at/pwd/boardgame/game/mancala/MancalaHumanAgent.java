package at.pwd.boardgame.game.mancala;

import at.pwd.boardgame.game.interfaces.AgentAction;
import at.pwd.boardgame.game.interfaces.Game;
import at.pwd.boardgame.game.interfaces.HumanAgent;
import at.pwd.boardgame.game.interfaces.State;

/**
 * Created by rfischer on 14/04/2017.
 */
public class MancalaHumanAgent implements HumanAgent<MancalaGame, MancalaState, MancalaAgentAction>  {
    private String selectedSlot = null;

    @Override
    public void handleAction(MancalaGame game, String id) {
        System.out.println("Agent selected id '" + id + "'");

        selectedSlot = id;
    }

    @Override
    public MancalaAgentAction doTurn(MancalaState state) {
        return new MancalaAgentAction(selectedSlot);
    }

    @Override
    public String toString() {
        return "Human";
    }
}
