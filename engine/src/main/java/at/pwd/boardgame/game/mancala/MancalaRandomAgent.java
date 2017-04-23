package at.pwd.boardgame.game.mancala;

import at.pwd.boardgame.game.agent.Agent;
import at.pwd.boardgame.game.agent.AgentService;

import java.util.ArrayList;
import java.util.List;
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
        List<String> slots = new ArrayList<>();
        for (Slot slot : board.getSlots()) {
            if (slot.belongsToPlayer() == state.getCurrentPlayer()) {
                slots.add(slot.getId());
            }
        }
        int pos = r.nextInt(slots.size());
        return new MancalaAgentAction(slots.get(pos));
    }

    @Override
    public String toString() {
        return "Random";
    }
}
