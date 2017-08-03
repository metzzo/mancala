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

    private class Move {
        String action;
        MCTSTree result;

        Move() {
        }

        void move(MCTSTree parent, String action) {
            MancalaGame newGame = new MancalaGame(parent.game);
            newGame.selectSlot(action);

            this.action = action;
            this.result = new MCTSTree(newGame);
            this.result.move = this;
            this.result.parent = parent;

            parent.children.add(this);
        }
    }

    private class MCTSTree {
        private int visitCount;
        private int winCount;

        private MancalaGame game;
        private Move move;
        private MCTSTree parent;
        private List<Move> children;

        MCTSTree(MancalaGame game) {
            this.game = game;
            this.children = new ArrayList<>();
        }

        boolean isNonTerminal() {
            return children.size() != 0;
        }

        MCTSTree getBestNode() {
            Move best = null;
            double value = 0;
            for (Move m : children) {
                double wC = (double)m.result.winCount;
                double vC = (double)m.result.visitCount;
                double currentValue =  wC/vC + C*Math.sqrt(2*Math.log(visitCount) / vC);


                if (best == null || currentValue > value) {
                    value = currentValue;
                    best = m;
                }
            }

            return best.result;
        }

        boolean isFullyExpanded() {
            return children.size() == game.getSelectableSlots().size();
        }
    }

    @Override
    public MancalaAgentAction doTurn(MancalaState state, MancalaBoard board) {
        long start = System.currentTimeMillis();
        this.originalState = state;

        MCTSTree root = new MCTSTree(new MancalaGame(state, board));

        // TODO: get computation time from configuration settings
        while ((System.currentTimeMillis() - start) < 5*1000) {
            MCTSTree best = select(root);
            Move move = expand(best);
            if (move != null) {
                WinState winning = runSimulation(move.result.game);
                statistics(winning, best);

            }
        }



        return new MancalaAgentAction(select(root).move.action);
    }

    private void statistics(WinState winState, MCTSTree current) {
        while (current != null) {
            current.visitCount++;

            if (winState.getState() == WinState.States.SOMEONE && winState.getPlayerId() == originalState.getCurrentPlayer()) {
                current.winCount++;
            }

            current = current.parent;
        }
    }

    private Move expand(MCTSTree best) {
        if (!best.isFullyExpanded() && best.game.checkIfPlayerWins().getState() == WinState.States.NOBODY) {
            List<String> legalMoves = best.game.getSelectableSlots();
            Move move = new Move();
            move.move(best, legalMoves.get(r.nextInt(legalMoves.size())));
            return move;
        } else {
            return null;
        }
    }

    private MCTSTree select(MCTSTree node) {
        while (node.isNonTerminal()) {
            node = node.getBestNode();
        }
        return node;
    }

    private WinState runSimulation(MancalaGame game) {
        WinState state = new WinState(WinState.States.NOBODY, -1);
        for (int i = 0; i < MAX_MOVES && state.getState() == WinState.States.NOBODY; i++) {
            List<String> legalMoves = game.getSelectableSlots();

            String play = legalMoves.get(r.nextInt(legalMoves.size()));
            game.selectSlot(play);

            state = game.checkIfPlayerWins();
        }

        return state;
    }

    @Override
    public String toString() {
        return "Monte Carlo Tree Search";
    }
}
