package at.pwd.boardgame.game.agent;

import at.pwd.boardgame.game.base.Game;

/**
 * Encapsulates a certain action that an Agent plays.
 */
public interface AgentAction<GameType extends Game> {
    /**
     * Described whether the applied action ends the turn (NEXT_PLAYER)
     * or the same player plays again (SAME_PLAYER)
     */
    enum NextAction {
        NEXT_PLAYER, SAME_PLAYER
    }

    /**
     * Applies the game by applying the given action (e.g. selecting a slot).
     * This alters the gamestate
     *
     * @param game on what game should the action be applied
     * @return whether this ends the turn
     */
    NextAction applyAction(GameType game);
}
