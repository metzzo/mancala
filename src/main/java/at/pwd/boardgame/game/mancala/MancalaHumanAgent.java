package at.pwd.boardgame.game.mancala;

import at.pwd.boardgame.game.base.HumanAgent;
import at.pwd.boardgame.services.AgentService;

/**
 * Created by rfischer on 14/04/2017.
 */
public class MancalaHumanAgent implements HumanAgent<MancalaGame, MancalaBoard, MancalaState, MancalaAgentAction>  {
    private String selectedSlot = null;

    public static void init() {
        AgentService.getInstance().register(new MancalaHumanAgent());
    }

    @Override
    public void handleAction(MancalaGame game, String id) {
        System.out.println("Agent selected id '" + id + "'");

        selectedSlot = id;
    }

    @Override
    public MancalaAgentAction doTurn(MancalaState state, MancalaBoard board) {
        return new MancalaAgentAction(selectedSlot);
    }

    @Override
    public String toString() {
        return "Human";
    }
}
