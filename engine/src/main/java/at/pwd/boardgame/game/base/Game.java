package at.pwd.boardgame.game.base;

import java.io.InputStream;

/**
 * An interface describing an abstract Game. Currently there only exists MancalaGame
 */
public interface Game<StateType extends State, BoardType extends Board> {
    /**
     * Returns the view as an FXML inputstream. This generates the board for the BoardController
     * @return The inputstream of the fxml file
     */
    InputStream getViewXml();

    /**
     * This is called at the beginning loading the board. This should initialize the Game.
     * After this getState() and getBoard() should return non null values.
     *
     * When an error happens during loading a RuntimeException may be thrown.
     *
     * @param board InputStream of the board XML
     */
    void loadBoard(InputStream board);

    /**
     * Returns the state of the current game.
     * @return Some object implementing State interface
     */
    StateType getState();

    /**
     * Returns the board configuration of the current game.
     * @return Some object implementing Board interface
     */
    BoardType getBoard();

    /**
     * Finishes the current players turn and starting the turn of the next player.
     * Keep in mind, that the Game class does not check for valid moves. Always call methods
     * according to the rules.
     *
     * @return The next players ID
     */
    int nextPlayer();
}
