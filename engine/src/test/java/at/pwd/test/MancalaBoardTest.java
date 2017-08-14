package at.pwd.test;

import at.pwd.boardgame.game.base.WinState;
import at.pwd.boardgame.game.mancala.MancalaBoard;
import at.pwd.boardgame.game.mancala.MancalaGame;
import at.pwd.boardgame.game.mancala.MancalaState;
import org.junit.Before;
import org.junit.Test;
import org.simpleframework.xml.core.Persister;

import static at.pwd.boardgame.game.mancala.MancalaGame.GAME_BOARD;
import static org.junit.Assert.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Created by rfischer on 14/05/2017.
 */
public class MancalaBoardTest {
    public static class MancalaTestState extends MancalaState {
        public MancalaTestState(MancalaBoard board) {
            super(board);
        }

        MancalaTestState(int stonesPerSlot) {
            this(0, stonesPerSlot, stonesPerSlot, stonesPerSlot, stonesPerSlot, stonesPerSlot, stonesPerSlot,
                 0, stonesPerSlot, stonesPerSlot, stonesPerSlot, stonesPerSlot, stonesPerSlot, stonesPerSlot);
        }

        MancalaTestState(
                int num1, int num2, int num3, int num4, int num5, int num6, int num7,
                int num8, int num9, int num10, int num11, int num12, int num13, int num14
        ) {
            PlayerTurnState s0 = new PlayerTurnState();
            playerStates.put(0, s0);
            PlayerTurnState s1 = new PlayerTurnState();
            playerStates.put(1, s1);

            states.put("2", s1);
            states.put("3", s1);
            states.put("4", s1);
            states.put("5", s1);
            states.put("6", s1);
            states.put("7", s1);

            states.put("9",  s0);
            states.put("10", s0);
            states.put("11", s0);
            states.put("12", s0);
            states.put("13", s0);
            states.put("14", s0);


            stones.put("1", new StoneNum(num1));
            stones.put("2", new StoneNum(num2));
            stones.put("3", new StoneNum(num3));
            stones.put("4", new StoneNum(num4));
            stones.put("5", new StoneNum(num5));
            stones.put("6", new StoneNum(num6));
            stones.put("7", new StoneNum(num7));
            stones.put("8", new StoneNum(num8));
            stones.put("9", new StoneNum(num9));
            stones.put("10", new StoneNum(num10));
            stones.put("11", new StoneNum(num11));
            stones.put("12", new StoneNum(num12));
            stones.put("13", new StoneNum(num13));
            stones.put("14", new StoneNum(num14));
        }
    }

    private MancalaGame game;

    @Before
    public void setUp() {
        MancalaBoard board = null;
        try {
            board  = new Persister().read(MancalaBoard.class, getClass().getResourceAsStream(GAME_BOARD));
        } catch (Exception e) {
            e.printStackTrace();
        }
        game = new MancalaGame(new MancalaTestState(3), board);
        game.nextPlayer();
    }

    @Test(expected = RuntimeException.class)
    public void cannotSelectDepot1() {
        game.selectSlot("1");
    }

    @Test(expected = RuntimeException.class)
    public void cannotSelectDepot2() {
        game.selectSlot("8");
    }

    @Test
    public void cannotSelectEmptySlot() {
        boolean redo = game.selectSlot("14");
        assertThat(redo, is(equalTo(false)));
        game.nextPlayer();

        redo = game.selectSlot("7");
        assertThat(redo, is(equalTo(false)));
        game.nextPlayer();

        try {
            game.selectSlot("14");
            fail();
        } catch (RuntimeException ex) { }
    }

    @Test(expected = RuntimeException.class)
    public void cannotSelectEnemySlot1() {
        game.selectSlot("2");
    }

    @Test(expected = RuntimeException.class)
    public void cannotSelectEnemySlot2() {
        boolean redo = game.selectSlot("14");
        assertThat(redo, is(equalTo(false)));

        game.nextPlayer();
        game.selectSlot("14");
    }

    @Test
    public void simpleSelectSlotWorks() {
        boolean redo = game.selectSlot("14");
        assertThat(redo, is(equalTo(false)));
        checkBoardConfiguration(
                0, 3, 3,3, 3,3,3,
                0,3,3,4,4,4,0
        );
    }

    @Test
    public void selectSlotOverOwnDepot() {
        boolean redo = game.selectSlot("10");
        assertThat(redo, is(equalTo(false)));
        checkBoardConfiguration(
                0, 3, 3,3, 3,3,4,
                1,4,0,3,3,3,3
        );
    }

    @Test
    public void skipsEnemyDepot() {
        game.getBoard().setStonesPerSlot(12);
        game = new MancalaGame(null, game.getBoard());
        game.nextPlayer();
        boolean redo = game.selectSlot("14");
        assertThat(redo, is(equalTo(false)));

        checkBoardConfiguration(
                0, 13, 13,13, 13,13,13,
                1,13,13,13,13,13,0
        );
    }

    @Test
    public void multipleMovesPerTurnIfOwnDepot() {
        MancalaState s = new MancalaTestState(
                0, 3, 3, 3, 3, 3, 3,
                0, 0, 0, 3, 4, 3, 3
        );
        game = new MancalaGame(s,game.getBoard());
        game.nextPlayer();
        boolean redo = game.selectSlot("11");
        assertThat(redo, is(equalTo(true)));
        redo = game.selectSlot("12");
        assertThat(redo, is(equalTo(true)));
        redo = game.selectSlot("13");
        assertThat(redo, is(equalTo(false)));

        checkBoardConfiguration(
                0, 3, 3, 3, 3, 3, 3,
                2, 2, 3, 2, 1, 0, 3
        );
    }

    @Test
    public void stonesInOwnSlotGrabsEnemiesStones() {
        MancalaState s = new MancalaTestState(
                0, 3, 3, 3, 10, 3, 3,
                1, 3, 3, 0, 3, 3, 3
        );
        game = new MancalaGame(s,game.getBoard());
        game.nextPlayer();
        boolean redo = game.selectSlot("14");
        assertThat(redo, is(equalTo(false)));

        checkBoardConfiguration(
                0, 3, 3, 3, 0, 3, 3,
                12, 3, 3, 0, 4, 4, 0
        );
    }

    @Test
    public void checkNobodyWinning() {
        WinState s = game.checkIfPlayerWins();
        assertThat(s.getState(), is(equalTo(WinState.States.NOBODY)));
    }

    @Test
    public void checkSomeoneIsWinning() {
        MancalaState state = new MancalaTestState(
                0, 3, 3, 3, 3, 3, 3,
                1, 0, 0, 0, 0, 0, 0
        );
        game = new MancalaGame(state, game.getBoard());
        game.nextPlayer();
        WinState s = game.checkIfPlayerWins();
        assertThat(s.getState(), is(equalTo(WinState.States.SOMEONE)));

        checkBoardConfiguration(
                18, 0, 0, 0, 0, 0, 0,
                1, 0, 0, 0, 0, 0, 0
        );
    }

    @Test
    public void checkDraw() {
        MancalaState state = new MancalaTestState(
                0, 3, 3, 3, 3, 3, 3,
                18, 0, 0, 0, 0, 0, 0
        );
        game = new MancalaGame(state,game.getBoard());
        game.nextPlayer();
        WinState s = game.checkIfPlayerWins();
        assertThat(s.getState(), is(equalTo(WinState.States.MULTIPLE)));

        checkBoardConfiguration(
                18, 0, 0, 0, 0, 0, 0,
                18, 0, 0, 0, 0, 0, 0
        );
    }

    private void checkBoardConfiguration(
            int num1, int num2, int num3, int num4,
            int num5, int num6, int num7, int num8,
            int num9, int num10,int num11,int num12,
            int num13,int num14) {
        assertThat(game.getState().getStones("1").getNum(), is(equalTo(num1)));
        assertThat(game.getState().getStones("2").getNum(), is(equalTo(num2)));
        assertThat(game.getState().getStones("3").getNum(), is(equalTo(num3)));
        assertThat(game.getState().getStones("4").getNum(), is(equalTo(num4)));
        assertThat(game.getState().getStones("5").getNum(), is(equalTo(num5)));
        assertThat(game.getState().getStones("6").getNum(), is(equalTo(num6)));
        assertThat(game.getState().getStones("7").getNum(), is(equalTo(num7)));
        assertThat(game.getState().getStones("8").getNum(), is(equalTo(num8)));
        assertThat(game.getState().getStones("9").getNum(), is(equalTo(num9)));
        assertThat(game.getState().getStones("10").getNum(), is(equalTo(num10)));
        assertThat(game.getState().getStones("11").getNum(), is(equalTo(num11)));
        assertThat(game.getState().getStones("12").getNum(), is(equalTo(num12)));
        assertThat(game.getState().getStones("13").getNum(), is(equalTo(num13)));
        assertThat(game.getState().getStones("14").getNum(), is(equalTo(num14)));
    }
}
