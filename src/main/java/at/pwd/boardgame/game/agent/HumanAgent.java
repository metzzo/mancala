package at.pwd.boardgame.game.agent;

import at.pwd.boardgame.game.agent.Agent;
import at.pwd.boardgame.game.agent.AgentAction;
import at.pwd.boardgame.game.base.Board;
import at.pwd.boardgame.game.base.Game;
import at.pwd.boardgame.game.base.State;

/**
 * Created by rfischer on 14/04/2017.
 */
public interface HumanAgent<GameType extends Game, BoardType extends Board, StateType extends State, ActionType extends AgentAction> extends Agent<StateType, BoardType, ActionType> {
    void handleAction(GameType game, String id);
}
