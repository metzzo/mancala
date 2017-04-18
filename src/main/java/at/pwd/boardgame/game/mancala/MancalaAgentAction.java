package at.pwd.boardgame.game.mancala;

import at.pwd.boardgame.game.base.AgentAction;

/**
 * Created by rfischer on 17/04/2017.
 */
public class MancalaAgentAction implements AgentAction<MancalaGame> {
    private final String id;

    public MancalaAgentAction(String id) {
        this.id = id;
    }

    @Override
    public void applyAction(MancalaGame game) {
        game.selectSlot(id);
    }
}
