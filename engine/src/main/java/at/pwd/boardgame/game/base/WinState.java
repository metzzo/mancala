package at.pwd.boardgame.game.base;

/**
 * Describes if there is a winner and if there is a winner, who is this winner.
 */
public class WinState {
    /**
     * Enum describing the different states of winning
     */
    public enum States {
        NOBODY,
        SOMEONE,
        MULTIPLE,
        TIMEOUT
    }

    private final States state;
    private final int playerId;

    /**
     * Constructor
     *
     * @param state What win state is the game currently in?
     * @param playerId Who wins? If nobody is currently winning (or if there are multiple winning => Draw) playerId=-1
     */
    public WinState(States state, int playerId) {
        this.state = state;
        this.playerId = playerId;
    }

    /**
     * getter for the player ID
     * @return Returns the winning player (if there is one) or -1
     */
    public int getPlayerId() {
        return playerId;
    }

    /**
     * getter for the state of winning
     * @return States
     */
    public States getState() {
        return state;
    }
}
