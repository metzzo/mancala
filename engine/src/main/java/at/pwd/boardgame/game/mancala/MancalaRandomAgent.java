package at.pwd.boardgame.game.mancala;

import at.pwd.boardgame.game.agent.Agent;
import at.pwd.boardgame.game.agent.AgentService;

import java.util.Random;

/**
 * Created by rfischer on 18/04/2017.
 */
public class MancalaRandomAgent implements Agent<MancalaState, MancalaBoard, MancalaAgentAction> {
    private Random r = new Random();

    public static void init() {
        AgentService.getInstance().register(new MancalaRandomAgent());
    }

    @Override
    public MancalaAgentAction doTurn(MancalaState state, MancalaBoard board) {
        return new MancalaAgentAction(board.getSlots().get(r.nextInt(board.getSlots().size())).getId());
    }

    @Override
    public String toString() {
        return "Random";
    }
}
