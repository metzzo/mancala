package at.pwd.boardgame.game.mancala.agent;

import at.pwd.boardgame.game.agent.AgentAction;
import at.pwd.boardgame.game.mancala.MancalaGame;

/**
 * Agent action for the game Mancala
 */
public class MancalaAgentAction implements AgentAction<MancalaGame> {
    private final String id;

    /**
     * Construcor for the Agent Action
     * @param id The slot that is selected. This may only be a slot and not a depot (since it is not allowed
     *           to select a depot)
     */
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
