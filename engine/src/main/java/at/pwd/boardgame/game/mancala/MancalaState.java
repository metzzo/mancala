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
 * State of the current MancalaGame.
 */
public class MancalaState implements State {
    /**
     * Class containing the number of stones for a depot/slot. This is used for binding in JavaFX
     */
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

    /**
     * Class containing whether the current slot/depot is enabled for the current player turn state.
     * This is used for binding in JavaFX
     */
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

    /**
     * Constructor for custom states, useful for testing
     */
    protected MancalaState() { }

    /**
     * Creates a deep copy of the given MancalaState.
     * @param state The MancalaState that should be copied
     */
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

    /**
     * Creates the default MancalaState for the board, where each slot has "stones per slot" many
     * stones.
     *
     * @param board The board for which the state should be created
     */
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

    /**
     * returns how many stones are in the given slot/depot.
     * If the id does not exist a Exception is thrown.
     *
     * @param id The id of the slot/depot
     * @return The number of stones
     */
    public int stonesIn(String id) {
        return stones.get(id).getNum();
    }

    /**
     * Removes the stones from the given slot/depot (= sets the number to 0)
     * @param id The ID of the slot/depot
     */
    void removeStones(String id) {
        StoneNumProperty num = stones.get(id);
        num.setNum(0);
    }

    /**
     * Increments the number of stones for the given slot/depot
     * @param id The id of the depot/slot
     * @param amount How much should be added
     */
    void addStones(String id, int amount) {
        StoneNumProperty num = stones.get(id);
        num.setNum(num.getNum() + amount);
    }

    /**
     * Stone number property. This should be used by the UI
     * @param id For what slot/depot?
     * @return Returns the property displaying how many stones are in slot/depot.
     */
    public IntegerProperty getStoneProperty(String id) {
        return stones.get(id).numProperty();
    }

    /**
     * Slot enabled property. This should be used by the UI
     * @param id For what slot?
     * @return Returns the property describing whether a slot is enabled or not.
     */
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
