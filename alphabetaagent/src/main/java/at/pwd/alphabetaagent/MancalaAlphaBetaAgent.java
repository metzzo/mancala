package at.pwd.alphabetaagent;


import at.pwd.boardgame.game.base.WinState;
import at.pwd.boardgame.game.mancala.MancalaGame;
import at.pwd.boardgame.game.mancala.agent.MancalaAgent;
import at.pwd.boardgame.game.mancala.agent.MancalaAgentAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by rfischer on 18/04/2017.
 */
public class MancalaAlphaBetaAgent implements MancalaAgent {
    private Random r = new Random();
    private static final int DEPTH = 10;
    private int currentPlayer;
    private String currentBest;

    @Override
    public MancalaAgentAction doTurn(int computationTime, MancalaGame initialGame) {
        currentPlayer = initialGame.getState().getCurrentPlayer();
        currentBest = null;

        alphabeta(initialGame, DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, true);

        return new MancalaAgentAction(currentBest);
    }

    private int heuristic(MancalaGame node) {
        String ownDepot = node.getBoard().getDepotOfPlayer(currentPlayer);
        String enemyDepot = node.getBoard().getDepotOfPlayer(1 - currentPlayer);
        return node.getState().stonesIn(ownDepot) - node.getState().stonesIn(enemyDepot);
    }

    private int alphabeta(MancalaGame node, int depth, int alpha, int beta, boolean maximizingPlayer) {
        if (depth == 0 || node.checkIfPlayerWins().getState() != WinState.States.NOBODY) {
            return heuristic(node);
        }

        List<String> legalMoves = node.getSelectableSlots();
        for (String move : legalMoves) {
            MancalaGame newGame = new MancalaGame(node);
            boolean moveAgain = newGame.selectSlot(move);
            if (!moveAgain) {
                newGame.nextPlayer();
            }

            if (maximizingPlayer) {
                int oldAlpha = alpha;
                alpha = Math.max(alpha, alphabeta(newGame, depth - 1, alpha, beta, moveAgain));
                if (depth == DEPTH && (oldAlpha < alpha || currentBest == null)) {
                    currentBest = move;
                }
            } else {
                beta = Math.min(beta, alphabeta(newGame, depth - 1, alpha, beta, !moveAgain));
            }

            if (beta <= alpha) {
                break;
            }
        }
        return maximizingPlayer ? alpha : beta;
    }


    @Override
    public String toString() {
        return "Alpha Beta Pruning";
    }
}
