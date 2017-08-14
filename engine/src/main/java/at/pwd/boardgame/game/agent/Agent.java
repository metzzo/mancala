package at.pwd.boardgame.game.agent;

import at.pwd.boardgame.game.base.Board;
import at.pwd.boardgame.game.base.Game;
import at.pwd.boardgame.game.base.State;

/**
 * Created by rfischer on 14/04/2017.
 */
public interface Agent<GameType extends Game, ActionType extends AgentAction> {
    ActionType doTurn(int computationTime, GameType game);
}
