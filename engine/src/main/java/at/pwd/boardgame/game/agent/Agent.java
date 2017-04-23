package at.pwd.boardgame.game.agent;

import at.pwd.boardgame.game.base.Board;
import at.pwd.boardgame.game.base.State;

/**
 * Created by rfischer on 14/04/2017.
 */
public interface Agent<StateType extends State, BoardType extends Board, ActionType extends AgentAction> {
    ActionType doTurn(StateType state, BoardType board);
}
