package at.pwd.boardgame.game.mancala;

import at.pwd.boardgame.game.interfaces.State;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rfischer on 14/04/2017.
 */
public class MancalaState implements State {
    public class StoneNum {
        private IntegerProperty num = new SimpleIntegerProperty();

        public final int getNum() {
            return num.get();
        }

        public final void setNum(int n) {
            this.num.set(n);
        }

        public final IntegerProperty numProperty() {
            return num;
        }

        public StoneNum(int initial) {
            setNum(initial);
        }

        public StoneNum() {
            setNum(0);
        }
    }

    private Map<String, StoneNum> stones = new HashMap<>();
    private int currentPlayer;

    MancalaState(MancalaState state) {
        for (String key : state.stones.keySet()) {
            stones.put(key, state.stones.get(key));
        }
        setCurrentPlayer(state.getCurrentPlayer());
    }

    MancalaState(MancalaBoard board) {
        for (Slot slot : board.getSlots()) {
            stones.put(slot.getId(), new StoneNum(board.getNumStones()));
        }
        for (PlayerDepot depot : board.getDepots()) {
            stones.put(depot.getId(), new StoneNum(0));
        }
    }

    StoneNum removeStones(String id) {
        StoneNum num = stones.get(id);
        num.setNum(0);
        return num;
    }

    StoneNum addStone(String id) {
        StoneNum num = stones.get(id);
        num.setNum(num.getNum() + 1);
        return num;
    }

    StoneNum getStones(String id) {
        return stones.get(id);
    }

    @Override
    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    @Override
    public int getCurrentPlayer() {
        return this.currentPlayer;
    }
}
