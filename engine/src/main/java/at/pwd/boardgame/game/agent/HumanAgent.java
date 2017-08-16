package at.pwd.boardgame.game.agent;

import at.pwd.boardgame.game.base.Game;

/**
 * Created by rfischer on 14/04/2017.
 */
public interface HumanAgent<GameType extends Game, ActionType extends AgentAction> extends Agent<GameType, ActionType> {
    void handleAction(GameType game, String id);
}
