package at.pwd.boardgame.game.mancala;

import at.pwd.boardgame.game.*;
import at.pwd.boardgame.services.BoardTransformer;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rfischer on 13/04/2017.
 */
public class MancalaGame implements Game {
    public static final String GAME_BOARD = "/normal_mancala_board.xml";
    public static final String GAME_BOARD_TRANFORMER = "/mancala_board_transformer.xsl";

    private MancalaBoard board;
    private MancalaState state;

    public static void init() {
        GameFactory.getInstance().register("normal_mancala", MancalaGame.class);
    }

    @Override
    public InputStream getViewXml() {
        return BoardTransformer.getInstance().transform(GAME_BOARD, GAME_BOARD_TRANFORMER);
    }

    public void loadBoard(int startNumStones) {
        Serializer serializer = new Persister();
        try {
            this.board = serializer.read(MancalaBoard.class, getClass().getResourceAsStream(GAME_BOARD));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.state = new MancalaState(board, startNumStones);
    }

    public void selectSlot(String id) {
        int stones = state.removeStones(id);
        String currentId = board.next(id);
        while (stones > 0) {
            state.addStone(currentId);
            stones--;
            currentId = board.next(currentId);
        }
    }

    @Override
    public Board getBoard() {
        return board;
    }


    @Override
    public State getState() {
        return state;
    }
}
