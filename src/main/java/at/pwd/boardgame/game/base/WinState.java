package at.pwd.boardgame.game.base;

/**
 * Created by rfischer on 18/04/2017.
 */



public class WinState {
    public enum States {
        NOBODY,
        SOMEONE,
        MULTIPLE
    }

    private final States state;
    private final int playerId;

    public WinState(States state, int playerId) {
        this.state = state;
        this.playerId = playerId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public States getState() {
        return state;
    }
}
