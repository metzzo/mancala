package at.pwd.boardgame.game;

import at.pwd.boardgame.game.mancala.MancalaGame;

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

    public Game create(int numStones, String name) {
        try {
            Game game = games.get(name).newInstance();
            game.loadBoard(numStones);
            return game;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
