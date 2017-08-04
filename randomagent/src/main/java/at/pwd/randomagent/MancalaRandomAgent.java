package at.pwd.randomagent;


import at.pwd.boardgame.game.agent.Agent;
import at.pwd.boardgame.game.mancala.MancalaAgentAction;
import at.pwd.boardgame.game.mancala.MancalaBoard;
import at.pwd.boardgame.game.mancala.MancalaGame;
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
    public MancalaAgentAction doTurn(int computationTime, MancalaState state, MancalaBoard board) {
        List<String> slots = new MancalaGame(state, board).getSelectableSlots();
        int pos = r.nextInt(slots.size());
        return new MancalaAgentAction(slots.get(pos));
    }

    @Override
    public String toString() {
        return "Random";
    }
}
