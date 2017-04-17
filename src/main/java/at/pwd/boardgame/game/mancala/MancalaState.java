package at.pwd.boardgame.game.mancala;

import at.pwd.boardgame.game.interfaces.State;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rfischer on 14/04/2017.
 */
public class MancalaState implements State {
    private Map<String, Integer> stones = new HashMap<>();

    public MancalaState(MancalaBoard board) {
        for (Slot slot : board.getSlots()) {
            stones.put(slot.getId(), board.getNumStones());
        }
        for (PlayerDepot depot : board.getDepots()) {
            stones.put(depot.getId(), 0);
        }
    }

    public int removeStones(String id) {
        int num = stones.get(id);
        stones.replace(id, 0);
        return num;
    }

    public int addStone(String id) {
        int num = stones.get(id);
        stones.replace(id, num + 1);
        return num + 1;
    }

    public int getStones(String id) {
        return stones.get(id);
    }
}
