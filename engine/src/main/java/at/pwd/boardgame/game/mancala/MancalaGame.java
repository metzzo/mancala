package at.pwd.boardgame.game.mancala;

import at.pwd.boardgame.game.*;
import at.pwd.boardgame.game.base.Game;
import at.pwd.boardgame.game.base.WinState;
import at.pwd.boardgame.game.mancala.agent.MancalaHumanAgent;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by rfischer on 13/04/2017.
 */
public class MancalaGame implements Game<MancalaState, MancalaBoard> {
    public static final String GAME_BOARD = "/normal_mancala_board.xml";
    public static final String GAME_BOARD_TRANFORMER = "/mancala_board_transformer.xsl";
    public static final String GAME_NAME = "normal_mancala";

    protected MancalaBoard board;
    protected MancalaState state;

    public static void init() {
        GameFactory.getInstance().register(GAME_NAME, MancalaGame.class);
        MancalaHumanAgent.init();
    }

    @Override
    public InputStream getViewXml() {
        final PipedOutputStream transformOutput = new PipedOutputStream();
        final PipedInputStream fxmlInputStream;
        try {
            fxmlInputStream = new PipedInputStream(transformOutput);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Thread transformThread = new Thread( () -> {
            try {
                StreamSource xsltSource = new StreamSource(getClass().getResourceAsStream(GAME_BOARD_TRANFORMER));
                Transformer transformer = TransformerFactory.newInstance().newTransformer(xsltSource);
                transformer.setParameter("num_stones",""+ board.getNumStones());
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                StreamSource xmlSource = new StreamSource(getClass().getResourceAsStream(GAME_BOARD));
                StreamResult transformerResult = new StreamResult(transformOutput);
                transformer.transform(xmlSource, transformerResult);
                transformOutput.close();

            } catch (IOException | TransformerConfigurationException e) {
                throw new RuntimeException(e);
            } catch (TransformerException e) {
                // An error occurred while applying the XSL file
                // Get location of error in input file
                SourceLocator locator = e.getLocator();
                int col = locator.getColumnNumber();
                int line = locator.getLineNumber();
                String publicId = locator.getPublicId();
                String systemId = locator.getSystemId();

                System.out.println(
                        "Error applying XSL in line " + line +
                                " at " + col +
                                " publicId: " + publicId +
                                " systemId: " + systemId
                );
            }
        });
        transformThread.start();
        return fxmlInputStream;
    }

    @Override
    public void loadBoard() {
        Serializer serializer = new Persister();
        try {
            this.board = serializer.read(MancalaBoard.class, getClass().getResourceAsStream(GAME_BOARD));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.state = new MancalaState(board);
    }

    public MancalaGame() {

    }

    public MancalaGame(MancalaBoard board, MancalaState state) {
        this.board = board;
        this.state = state != null ? state : new MancalaState(board);
    }

    /**
     * Selects the slot with the given ID and calculates the turn.
     *
     * @param id The ID of the slot that has been selected
     * @return true ... the current player can play another turn, false ... the current player has to
     */
    public boolean selectSlot(String id) {
        int stones = state.getStones(id).getNum();
        int owner = board.getElement(id).getOwner();

        if (owner != state.getCurrentPlayer() || board.isDepot(id) || stones == 0) {
            throw new RuntimeException("You cannot select this slot: " + id);
        }

        state.removeStones(id);
        String currentId = board.next(id);
        boolean playAnotherTurn = false;
        while (stones > 0) {
            // if this is the player depot of the enemy, do not put it in
            boolean skip = false;
            boolean ownDepot = false;
            if (board.isDepot(currentId)) {
                MancalaBoard.PlayerDepot depot = (MancalaBoard.PlayerDepot)board.getElement(currentId);
                if (depot.getPlayer() != state.getCurrentPlayer()) {
                    skip = true;
                } else {
                    ownDepot = true;
                }
            }

            if (!skip) {
                state.addStones(currentId, 1);
                stones--;

                boolean isLast = stones == 0;
                boolean landedOnEmpty = state.getStones(currentId).getNum() == 1;
                boolean landedOnOwn = board.getElement(currentId).getOwner() == state.getCurrentPlayer();
                if (landedOnEmpty && landedOnOwn && isLast && !ownDepot) {
                    // get the stones from enemys slot
                    String enemy = board.getEnemySlotOf(currentId);
                    int enemyStones = state.getStones(enemy).getNum();
                    state.removeStones(currentId);
                    state.removeStones(enemy);
                    String depot = board.getDepotOf(currentId);
                    state.addStones(depot, enemyStones + 1); // stones from enemy + own stone in slot
                }

                playAnotherTurn = isLast && ownDepot;
            }
            currentId = board.next(currentId);
        }
        if (playAnotherTurn) {
            // finally check if already won => if no, then another round can be played
            playAnotherTurn = checkIfPlayerWins().getState() == WinState.States.NOBODY;
        }
        return playAnotherTurn;
    }

    class Entry implements Comparable<Entry> {
        int num;
        int playerId;

        Entry(int num, int playerId) {
            this.num = num;
            this.playerId = playerId;
        }

        @Override
        public int compareTo(Entry o) {
            return o.num - this.num;
        }
    }

    public WinState checkIfPlayerWins() {
        boolean didEnd = false;
        for (MancalaBoard.PlayerDepot depot : board.getDepots()) {
            didEnd = true;
            int playerId = depot.getPlayer();
            for (MancalaBoard.Slot slot : board.getSlots()) {
                if (slot.belongsToPlayer() == playerId && state.getStones(slot.getId()).getNum() > 0) {
                    didEnd = false;
                    break;
                }
            }
            if (didEnd) {
                break;
            }
        }

        WinState winState;
        if (didEnd) {
            // give all missing stones to enemy
            for (MancalaBoard.Slot slot : board.getSlots()) {
                String depot = board.getDepotOf(slot.getId());
                int num = state.getStones(slot.getId()).getNum();
                state.removeStones(slot.getId());
                state.addStones(depot, num);
            }

            // find out who has more
            List<Entry> nums = new ArrayList<>();
            for (MancalaBoard.PlayerDepot depot : board.getDepots()) {
                int currentNum = state.getStones(depot.getId()).getNum();
                nums.add(new Entry(currentNum, depot.getPlayer()));
            }
            Collections.sort(nums);
            if (nums.size() == 1) {
                winState = new WinState(WinState.States.SOMEONE, nums.get(0).playerId);
            } else if (nums.get(0).num == nums.get(1).num) { // draw
                winState = new WinState(WinState.States.MULTIPLE, -1);
            } else { // one player has the most
                winState = new WinState(WinState.States.SOMEONE, nums.get(0).playerId);
            }
        } else {
            winState = new WinState(WinState.States.NOBODY, -1);
        }

        return winState;
    }

    @Override
    public MancalaBoard getBoard() {
        return board;
    }

    @Override
    public int nextPlayer() {
        int player = (state.getCurrentPlayer() + 1) % getBoard().getPlayers().size();
        state.setCurrentPlayer(player);
        return player;
    }

    @Override
    public MancalaState getState() {
        return state;
    }
}
