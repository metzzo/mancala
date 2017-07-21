package at.pwd.randomagent;


import at.pwd.boardgame.game.agent.Agent;
import at.pwd.boardgame.game.agent.AgentService;
import at.pwd.boardgame.game.mancala.MancalaAgentAction;
import at.pwd.boardgame.game.mancala.MancalaBoard;
import at.pwd.boardgame.game.mancala.MancalaState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by rfischer on 18/04/2017.
 */
public class MancalaRandomAgent implements Agent<MancalaState, MancalaBoard, MancalaAgentAction> {
    private Random r = new Random();

    @Override
    public MancalaAgentAction doTurn(MancalaState state, MancalaBoard board) {
        List<String> slots = new ArrayList<>();
        for (MancalaBoard.Slot slot : board.getSlots()) {
            // slot should belong to the current player and not be empty
            if (slot.belongsToPlayer() == state.getCurrentPlayer() && state.getStones(slot.getId()).getNum() > 0) {
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
