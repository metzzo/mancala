package at.pwd.boardgame.services;

import at.pwd.boardgame.game.base.Game;
import at.pwd.boardgame.game.mancala.MancalaGame;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rfischer on 13/04/2017.
 */
public class GameFactory {
    private static GameFactory instance;

    private Map<String, Class<? extends Game>> games;

    private GameFactory() {
        this.games = new HashMap<>();
    }

    public static GameFactory getInstance() {
        if (instance == null) {
            instance = new GameFactory();

            MancalaGame.init();
        }
        return instance;
    }

    public void register(String name, Class<? extends Game> cls) {
        games.put(name, cls);
    }

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
