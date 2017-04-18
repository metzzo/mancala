package at.pwd.boardgame.game.base;

/**
 * Created by rfischer on 17/04/2017.
 */
public interface AgentAction<GameType extends Game> {
    void applyAction(GameType game);
}
