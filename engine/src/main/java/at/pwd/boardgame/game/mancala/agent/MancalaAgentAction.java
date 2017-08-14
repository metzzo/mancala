package at.pwd.boardgame.game.mancala.agent;

import at.pwd.boardgame.game.agent.AgentAction;
import at.pwd.boardgame.game.mancala.MancalaGame;

/**
 * Created by rfischer on 17/04/2017.
 */
public class MancalaAgentAction implements AgentAction<MancalaGame> {
    private final String id;

    public MancalaAgentAction(String id) {
        this.id = id;
    }

    @Override
    public NextAction applyAction(MancalaGame game) {
        if (game.selectSlot(id)) {
            return NextAction.SAME_PLAYER;
        } else {
            return NextAction.NEXT_PLAYER;
        }
    }
}
