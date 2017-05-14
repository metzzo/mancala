package at.pwd.boardgame.game.mancala;

import at.pwd.boardgame.game.*;
import at.pwd.boardgame.game.base.Game;
import at.pwd.boardgame.game.base.WinState;
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
    private MancalaBoard board;
    private MancalaState state;

    public static void init() {
        GameFactory.getInstance().register(GAME_NAME, MancalaGame.class);
        MancalaHumanAgent.init();
        MancalaRandomAgent.init();
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
            for (PlayerDepot depot : board.getDepots()) {
                if (depot.getId().equals(currentId)) {
                    if (depot.getPlayer() != state.getCurrentPlayer()) {
                        skip = true;
                    } else {
                        ownDepot = true;
                    }


                    break;
                }
            }
            Slot ownSlotAndEmpty = null;
            if (state.getStones(currentId).getNum() == 0) {
                for (Slot slot : board.getSlots()) {
                    if (slot.getId().equals(currentId)) {
                        ownSlotAndEmpty = slot;
                        break;
                    }
                }
            }

            if (!skip) {
                state.addStone(currentId);
                stones--;

                if (ownSlotAndEmpty != null) {
                    // get the stones from enemys slot
                    
                }

                playAnotherTurn = stones == 0 && ownDepot;
            }
            currentId = board.next(currentId);
        }
        return playAnotherTurn;
    }

    public WinState checkIfPlayerWins() {
        boolean didEnd = false;
        for (PlayerDepot depot : board.getDepots()) {
            didEnd = true;
            int playerId = depot.getPlayer();
            for (Slot slot : board.getSlots()) {
                if (slot.belongsToPlayer() == playerId && state.getStones(slot.getId()).getNum() > 0) {
                    didEnd = false;
                    break;
                }
            }
            if (didEnd) {
                break;
            }
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

        WinState winState;
        if (didEnd) {
            // find out who has more
            List<Entry> nums = new ArrayList<>();
            for (PlayerDepot depot : board.getDepots()) {
                int currentNum = state.getStones(depot.getId()).getNum();
                nums.add(new Entry(currentNum, depot.getPlayer()));
            }
            Collections.sort(nums);
            if (nums.size() == 1) {
                winState = new WinState(WinState.States.SOMEONE, nums.get(0).playerId);
            } else {
                if (nums.get(0).num == nums.get(1).num) {
                    winState = new WinState(WinState.States.MULTIPLE, -1);
                } else {
                    winState = new WinState(WinState.States.SOMEONE, nums.get(0).playerId);
                }
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
    public MancalaState getState() {
        return state;
    }
}
