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
    private MancalaState originalState;
    private static final int MAX_MOVES = 100;
    private static final double C = 1.0f/Math.sqrt(2.0f);

    private class MCTSTree {
        private int visitCount;
        private int winCount;

        private MancalaGame game;
        private WinState winState;
        private MCTSTree parent;
        private List<MCTSTree> children;
        String action;

        MCTSTree(MancalaGame game) {
            this.game = game;
            this.children = new ArrayList<>();
            this.winState = game.checkIfPlayerWins();
        }

        boolean isNonTerminal() {
            return winState.getState() == WinState.States.NOBODY;
        }

        MCTSTree getBestNode() {
            MCTSTree best = null;
            double value = 0;
            for (MCTSTree m : children) {
                double wC = (double)m.winCount;
                double vC = (double)m.visitCount;
                double currentValue =  wC/vC + C*Math.sqrt(2*Math.log(visitCount) / vC);


                if (best == null || currentValue > value) {
                    value = currentValue;
                    best = m;
                }
            }

            return best;
        }

        boolean isFullyExpanded() {
            return children.size() == game.getSelectableSlots().size();
        }

        MCTSTree move(String action) {
            MancalaGame newGame = new MancalaGame(this.game);
            if (!newGame.selectSlot(action)) {
                newGame.nextPlayer();
            }

            MCTSTree tree = new MCTSTree(newGame);
            tree.action = action;
            tree.parent = this;

            this.children.add(tree);

            return tree;
        }
    }

    @Override
    public MancalaAgentAction doTurn(MancalaState state, MancalaBoard board) {
        long start = System.currentTimeMillis();
        this.originalState = state;

        MCTSTree root = new MCTSTree(new MancalaGame(state, board));

        // TODO: get computation time from configuration settings
        while ((System.currentTimeMillis() - start) < 5*1000) {
            MCTSTree best = treePolicy(root);
            WinState winning = runSimulation(best.game);
            statistics(winning, best);
        }

        MCTSTree selected = root.getBestNode();
        System.out.println("Selected action " + selected.winCount + " / " + selected.visitCount);
        return new MancalaAgentAction(selected.action);
    }

    private void statistics(WinState winState, MCTSTree current) {
        while (current != null) {
            // always increase visit count
            current.visitCount++;

            // if it ended in a win => increase the win count
            if (winState.getState() == WinState.States.SOMEONE && winState.getPlayerId() == originalState.getCurrentPlayer()) {
                current.winCount++;
            }

            current = current.parent;
        }
    }

    private MCTSTree treePolicy(MCTSTree current) {
        while (current.isNonTerminal()) {
            if (!current.isFullyExpanded()) {
                return expand(current);
            } else {
                current = current.getBestNode();
            }
        }
        return current;
    }

    private MCTSTree expand(MCTSTree best) {
        List<String> legalMoves = best.game.getSelectableSlots();
        return best.move(legalMoves.get(r.nextInt(legalMoves.size())));
    }

    private WinState runSimulation(MancalaGame game) {
        WinState state = new WinState(WinState.States.NOBODY, -1);
        game = new MancalaGame(game); // copy original game

        for (int i = 0; i < MAX_MOVES && state.getState() == WinState.States.NOBODY; i++) {
            state = game.checkIfPlayerWins();

            if (state.getState() == WinState.States.NOBODY) {
                String play;
                do {
                    List<String> legalMoves = game.getSelectableSlots();
                    play = legalMoves.get(r.nextInt(legalMoves.size()));
                } while(game.selectSlot(play));
                game.nextPlayer();
            }
        }

        return state;
    }

    @Override
    public String toString() {
        return "Monte Carlo Tree Search";
    }
}
