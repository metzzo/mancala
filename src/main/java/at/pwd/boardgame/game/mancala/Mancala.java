package at.pwd.boardgame.game.mancala;

import at.pwd.boardgame.game.Game;
import at.pwd.boardgame.game.GameFactory;

/**
 * Created by rfischer on 13/04/2017.
 */
public class Mancala implements Game {
    static {
        GameFactory.getInstance().register("boardgame", Mancala.class);
    }


}
