package at.pwd.boardgame.game.base;

/**
 * Created by rfischer on 14/04/2017.
 */
public interface HumanAgent<GameType extends Game, BoardType extends Board, StateType extends State, ActionType extends AgentAction> extends Agent<StateType, BoardType, ActionType> {
    void handleAction(GameType game, String id);
}
