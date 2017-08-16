package at.pwd.boardgame.game.agent;

import at.pwd.boardgame.game.base.Game;

/**
 * The interface describing an Agent. An agent is able to "do a turn" so it should return (given a certain
 * computation time and a Game) an action.
 */
public interface Agent<GameType extends Game, ActionType extends AgentAction> {
    /**
     * Should calculate the turn. It may take computationTime many seconds and has a Game as its input.
     *
     * Calculation is not done on the main ui thread, instead on a background thread.
     * You are allowed to alter the Game objects state.
     *
     * @param computationTime given calculation time in seconds
     * @param game given game (with mutable state)
     * @return action that should be done for the current turn.
     */
    ActionType doTurn(int computationTime, GameType game);
}
