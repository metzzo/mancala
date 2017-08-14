package at.pwd.boardgame.game.mancala;

import at.pwd.boardgame.game.base.State;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rfischer on 14/04/2017.
 */
public class MancalaState implements State {
    public class StoneNumProperty {
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

        public StoneNumProperty(int initial) {
            setNum(initial);
        }

        public StoneNumProperty copy() {
            return new StoneNumProperty(getNum());
        }
    }

    public class PlayerTurnStateProperty {
        private BooleanProperty state = new SimpleBooleanProperty();

        public final boolean getState() {
            return state.get();
        }

        public final void setState(boolean state) {
            this.state.set(state);
        }

        public final BooleanProperty stateProperty() {
            return state;
        }

        public PlayerTurnStateProperty() {
            this(true);
        }
        public PlayerTurnStateProperty(boolean state) {
            setState(state);
        }

        public PlayerTurnStateProperty copy() {
            return new PlayerTurnStateProperty(getState());
        }
    }

    protected Map<String, StoneNumProperty> stones = new HashMap<>();
    private int currentPlayer = -1;
    protected Map<String, PlayerTurnStateProperty> states = new HashMap<>();
    protected Map<Integer, PlayerTurnStateProperty> playerStates = new HashMap<>();

    protected MancalaState() {
        // for custom states, useful for testing
    }

    MancalaState(MancalaState state) {
        for (Integer playerId : state.playerStates.keySet()) {
            PlayerTurnStateProperty oldState = state.playerStates.get(playerId);
            PlayerTurnStateProperty newState = oldState.copy();
            playerStates.put(playerId, newState);
            for (String slot : state.states.keySet()) {
                if (state.states.get(slot) == oldState) {
                    states.put(slot, newState);
                }
            }
        }

        for (String key : state.stones.keySet()) {
            stones.put(key, state.stones.get(key).copy());
        }

        setCurrentPlayer(state.getCurrentPlayer());
    }

    protected MancalaState(MancalaBoard board) {
        for (Integer playerId : board.getPlayers()) {
            PlayerTurnStateProperty s = new PlayerTurnStateProperty();
            playerStates.put(playerId, s);
            for (MancalaBoard.Slot slot : board.getSlots()) {
                if (slot.belongsToPlayer() == playerId) {
                    states.put(slot.getId(), s);
                }
            }
        }

        for (MancalaBoard.Slot slot : board.getSlots()) {
            stones.put(slot.getId(), new StoneNumProperty(board.getStonesPerSlot()));
        }
        for (MancalaBoard.PlayerDepot depot : board.getDepots()) {
            stones.put(depot.getId(), new StoneNumProperty(0));
        }
    }

    public int stonesIn(String id) {
        return stones.get(id).getNum();
    }

    void removeStones(String id) {
        StoneNumProperty num = stones.get(id);
        num.setNum(0);
    }

    void addStones(String id, int amount) {
        StoneNumProperty num = stones.get(id);
        num.setNum(num.getNum() + amount);
    }

    public IntegerProperty getStoneProperty(String id) {
        return stones.get(id).numProperty();
    }


    public BooleanExpression isSlotEnabledProperty(String id) {
        PlayerTurnStateProperty turnState = states.get(id);
        StoneNumProperty num = stones.get(id);

        return num.numProperty().isEqualTo(0).or(turnState.stateProperty());
    }

    @Override
    public void setCurrentPlayer(int currentPlayer) {
        if (this.currentPlayer != -1) {
            playerStates.get(this.currentPlayer).setState(true);
        }

        this.currentPlayer = currentPlayer;

        PlayerTurnStateProperty turnState = playerStates.get(this.currentPlayer);
        if (turnState != null) {
            turnState.setState(false);
        }
    }

    @Override
    public int getCurrentPlayer() {
        return this.currentPlayer;
    }

    @Override
    public State copy() {
        return new MancalaState(this);
    }
}
