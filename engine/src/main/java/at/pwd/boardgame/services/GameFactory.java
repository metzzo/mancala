package at.pwd.boardgame.services;

import at.pwd.boardgame.game.base.Game;
import at.pwd.boardgame.game.mancala.MancalaGame;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory service for creating games
 */
public class GameFactory {
    private static GameFactory instance;

    private Map<String, Class<? extends Game>> games;

    private GameFactory() {
        this.games = new HashMap<>();
    }

    /**
     * @return Returns the singleton instance of GameFactory
     */
    public static GameFactory getInstance() {
        if (instance == null) {
            instance = new GameFactory();

            MancalaGame.init();
        }
        return instance;
    }

    /**
     * Registers a new game
     * @param name The name of the game
     * @param cls the class of the game
     */
    public void register(String name, Class<? extends Game> cls) {
        games.put(name, cls);
    }

    /**
     * Creates a new game
     * @param name given the name of the game
     * @param board the board configuration of the game
     * @return the game instance
     */
    public Game create(String name, InputStream board) {
        try {
            Game game = games.get(name).newInstance();
            game.loadBoard(board);
            return game;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
