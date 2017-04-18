package at.pwd.boardgame.game.interfaces;

import at.pwd.boardgame.game.mancala.MancalaGame;

/**
 * Created by rfischer on 14/04/2017.
 */
public interface Agent<StateType extends State, ActionType extends AgentAction> {
    ActionType doTurn(StateType state);
}
