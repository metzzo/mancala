package at.pwd.mctsagent;


import at.pwd.boardgame.game.agent.Agent;
import at.pwd.boardgame.game.base.WinState;
import at.pwd.boardgame.game.mancala.MancalaAgentAction;
import at.pwd.boardgame.game.mancala.MancalaBoard;
import at.pwd.boardgame.game.mancala.MancalaGame;
import at.pwd.boardgame.game.mancala.MancalaState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by rfischer on 18/04/2017.
 */
public class MancalaMCTSAgent implements Agent<MancalaState, MancalaBoard, MancalaAgentAction> {
    private Random r = new Random();

    private static class MCTSTree {
        private MCTSTree nodes[];
        private MCTSTree parent;

        private int noOfVisits;
        private int reward;
        private MancalaGame gameState;

        MCTSTree(MancalaGame gameState, int numNodes) {
            this.nodes = new MCTSTree[numNodes];
            this.gameState = gameState;
        }
    }

    @Override
    public MancalaAgentAction doTurn(MancalaState state, MancalaBoard board) {
        List<String> slots = state.getSelectableSlotsOf(state.getCurrentPlayer(), board);

        MCTSTree currentTree = new MCTSTree(new MancalaGame(board, state), slots.size());
        MCTSTree nextTree;
        while (isWithinTimeLimit()) {
            nextTree = treePolicy(currentTree);
            int delta = defaultPolicy(currentTree);
            backup(nextTree, delta);
        }

        return new MancalaAgentAction(bestChild(currentTree));
    }

    private String bestChild(MCTSTree currentTree) {
        return null;
    }

    private void backup(MCTSTree tree, int delta) {
        while (tree != null) {
            tree.noOfVisits++;
            tree.reward += score(tree, delta);
            tree = tree.parent;
        }
    }

    private int score(MCTSTree tree, int delta) {
        return 0;
    }

    private int defaultPolicy(MCTSTree currentTree) {
        WinState winState;
        while ( (winState = currentTree.gameState.checkIfPlayerWins()).getState() == WinState.States.NOBODY) {
            MancalaState state = currentTree.gameState.getState();
            List<String> slots = state.getSelectableSlotsOf(state.getCurrentPlayer(), currentTree.gameState.getBoard());
            String slot = slots.get(r.nextInt(slots.size()));
            currentTree.gameState.selectSlot(slot);
        }

    }

    private boolean isWithinTimeLimit() {
        return true;
    }

    private MCTSTree treePolicy(MCTSTree tree) {

    }

    @Override
    public String toString() {
        return "Monte Carlo Tree Search";
    }
}
