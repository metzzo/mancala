package at.pwd.boardgame.game.mancala;

import at.pwd.boardgame.controller.BoardController;
import at.pwd.boardgame.services.BoardTransformer;
import at.pwd.boardgame.services.ScreenFactory;

import java.io.InputStream;

/**
 * Created by rfischer on 13/04/2017.
 */
public class MancalaBoardController extends BoardController {
    public static final String GAME_SCREEN = "/board_controller.fxml";

    public static final String GAME_BOARD = "/normal_mancala_board.xml";
    public static final String GAME_BOARD_TRANFORMER = "/mancala_board_transformer.xsl";


    public static void init() {
        InputStream stream = BoardTransformer.getInstance().transform(GAME_BOARD, GAME_BOARD_TRANFORMER);
        ScreenFactory.getInstance().register(GAME_SCREEN, stream);
    }

}
