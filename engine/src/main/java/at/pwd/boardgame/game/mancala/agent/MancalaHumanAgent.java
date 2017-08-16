package at.pwd.boardgame.game.mancala.agent;

import at.pwd.boardgame.game.agent.HumanAgent;
import at.pwd.boardgame.game.mancala.MancalaBoard;
import at.pwd.boardgame.game.mancala.MancalaGame;
import at.pwd.boardgame.game.mancala.MancalaState;
import at.pwd.boardgame.services.AgentService;

/**
 * HumanAgent for the game Mancala
 */
public class MancalaHumanAgent implements HumanAgent<MancalaGame, MancalaAgentAction>  {
    private String selectedSlot = null;

    /**
     * Adds MancalaHumanAgent to the AgentService
     */
    public static void init() {
        AgentService.getInstance().register(new MancalaHumanAgent());
    }

    @Override
    public void handleAction(MancalaGame game, String id) {
        System.out.println("Agent selected id '" + id + "'");

        selectedSlot = id;
    }

    @Override
    public MancalaAgentAction doTurn(int computationTime, MancalaGame game) {
        return new MancalaAgentAction(selectedSlot);
    }

    @Override
    public String toString() {
        return "Human";
    }
}
