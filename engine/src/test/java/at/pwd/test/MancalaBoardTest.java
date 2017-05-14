package at.pwd.test;

import at.pwd.boardgame.game.mancala.MancalaGame;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * Created by rfischer on 14/05/2017.
 */
public class MancalaBoardTest {
    private MancalaGame game;

    @Before
    public void setUp() {
        game = new MancalaGame();
        game.loadBoard();
    }

    @Test(expected = RuntimeException.class)
    public void cannotSelectDepot() {
        game.selectSlot("1");
    }

    
}
