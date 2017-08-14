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

        public StoneNum copy() {
            return new StoneNum(getNum());
        }
    }

    public class PlayerTurnState {
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

        public PlayerTurnState() {
            this(true);
        }
        public PlayerTurnState(boolean state) {
            setState(state);
        }

        public PlayerTurnState copy() {
            return new PlayerTurnState(getState());
        }
    }

    protected Map<String, StoneNum> stones = new HashMap<>();
    private int currentPlayer = -1;
    protected Map<String, PlayerTurnState> states = new HashMap<>();
    protected Map<Integer, PlayerTurnState> playerStates = new HashMap<>();

    protected MancalaState() {
        // for custom states, useful for testing
    }

    MancalaState(MancalaState state) {
        for (Integer playerId : state.playerStates.keySet()) {
            PlayerTurnState oldState = state.playerStates.get(playerId);
            PlayerTurnState newState = oldState.copy();
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
            PlayerTurnState s = new PlayerTurnState();
            playerStates.put(playerId, s);
            for (MancalaBoard.Slot slot : board.getSlots()) {
                if (slot.belongsToPlayer() == playerId) {
                    states.put(slot.getId(), s);
                }
            }
        }

        for (MancalaBoard.Slot slot : board.getSlots()) {
            stones.put(slot.getId(), new StoneNum(board.getStonesPerSlot()));
        }
        for (MancalaBoard.PlayerDepot depot : board.getDepots()) {
            stones.put(depot.getId(), new StoneNum(0));
        }
    }

    StoneNum removeStones(String id) {
        StoneNum num = stones.get(id);
        num.setNum(0);
        return num;
    }

    StoneNum addStones(String id, int amount) {
        StoneNum num = stones.get(id);
        num.setNum(num.getNum() + amount);
        return num;
    }

    public StoneNum getStones(String id) {
        return stones.get(id);
    }

    public BooleanExpression getState(String id) {
        PlayerTurnState turnState = states.get(id);
        StoneNum num = getStones(id);

        return num.numProperty().isEqualTo(0).or(turnState.stateProperty());
    }

    @Override
    public void setCurrentPlayer(int currentPlayer) {
        if (this.currentPlayer != -1) {
            playerStates.get(this.currentPlayer).setState(true);
        }

        this.currentPlayer = currentPlayer;

        PlayerTurnState turnState = playerStates.get(this.currentPlayer);
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
