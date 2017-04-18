package at.pwd.boardgame.game.interfaces;

import at.pwd.boardgame.game.mancala.MancalaGame;

/**
 * Created by rfischer on 14/04/2017.
 */
public interface HumanAgent<GameType extends Game, StateType extends State, ActionType extends AgentAction> extends Agent<StateType, ActionType> {
    void handleAction(GameType game, String id);
}
