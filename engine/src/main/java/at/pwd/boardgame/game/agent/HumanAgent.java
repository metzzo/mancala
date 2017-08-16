package at.pwd.boardgame.game.agent;

import at.pwd.boardgame.game.base.Game;

/**
 * An agent that is played by a human.
 */
public interface HumanAgent<GameType extends Game, ActionType extends AgentAction> extends Agent<GameType, ActionType> {
    /**
     * HumanAgent has the additional "handleAction" method
     * which is called when the human interacted with the board.
     *
     * @param game The game where the action is executed
     * @param id What slot id has been selected by this agent
     */
    void handleAction(GameType game, String id);
}
