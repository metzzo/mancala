package at.pwd.randomagent;


import at.pwd.boardgame.game.mancala.MancalaGame;
import at.pwd.boardgame.game.mancala.agent.MancalaAgent;
import at.pwd.boardgame.game.mancala.agent.MancalaAgentAction;

import java.util.List;
import java.util.Random;

/**
 * Created by rfischer on 18/04/2017.
 */
public class MancalaRandomAgent implements MancalaAgent {
    private Random r = new Random();

    @Override
    public MancalaAgentAction doTurn(int computationTime, MancalaGame game) {
        List<String> slots = game.getSelectableSlots();
        int pos = r.nextInt(slots.size());
        return new MancalaAgentAction(slots.get(pos));
    }

    @Override
    public String toString() {
        return "Random";
    }
}
