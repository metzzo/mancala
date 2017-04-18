package at.pwd.boardgame.game.mancala;

import at.pwd.boardgame.game.*;
import at.pwd.boardgame.game.interfaces.Board;
import at.pwd.boardgame.game.interfaces.Game;
import at.pwd.boardgame.game.interfaces.State;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * Created by rfischer on 13/04/2017.
 */
public class MancalaGame implements Game<MancalaState, MancalaBoard> {
    public static final String GAME_BOARD = "/normal_mancala_board.xml";
    public static final String GAME_BOARD_TRANFORMER = "/mancala_board_transformer.xsl";

    private MancalaBoard board;
    private MancalaState state;

    public static void init() {
        GameFactory.getInstance().register("normal_mancala", MancalaGame.class);
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

    void selectSlot(String id) {
        int stones = state.getStones(id).getNum();
        state.removeStones(id);
        String currentId = board.next(id);
        while (stones > 0) {
            // if this is the player depot of the enemy, do not put it in
            boolean skip = false;
            for (PlayerDepot depot : board.getDepots()) {
                if (depot.getId().equals(currentId) && depot.getPlayer() != state.getCurrentPlayer()) {
                    skip = true;
                    break;
                }
            }
            if (!skip) {
                state.addStone(currentId);
                stones--;
            }
            currentId = board.next(currentId);
        }
    }

    int checkIfPlayerWins() {
        int win = -1;

        for (PlayerDepot depot : board.getDepots()) {
            int playerId = depot.getPlayer();
            boolean didWin = true;
            for (Slot slot : board.getSlots()) {
                if (slot.belongsToPlayer() == playerId && state.getStones(slot.getId()).getNum() > 0) {
                    didWin = false;
                    break;
                }
            }
            if (didWin) {
                if (win != -1) {
                    throw new RuntimeException("Two player won? How is this possible?");
                }
                win = playerId;
            }
        }

        return win;
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
