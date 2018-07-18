package com.martinheinz;

import org.junit.jupiter.api.Test;
import com.martinheinz.TicTacToe.STATE;
import com.martinheinz.TicTacToe.TURN;
import static org.junit.jupiter.api.Assertions.*;

class TicTacToeTest {

    private TicTacToe t;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        t = new TicTacToe();
    }


    @Test
    void createGameBoard() {
        STATE[][] expected = new TicTacToe.STATE[][]{
                { STATE.BLANK, STATE.BLANK, STATE.BLANK },
                { STATE.BLANK, STATE.BLANK, STATE.BLANK },
                { STATE.BLANK, STATE.BLANK, STATE.BLANK }
        };
        assertArrayEquals(expected, t.getGameBoard());
        assertEquals(TURN.X, t.getPlayerOnTurn());
        String boardState = " | | \n-+-+-\n | | \n-+-+-\n | | \n";
        assertEquals(boardState, t.getBoardState());
    }

    @Test
    void checkXWonWithVerticalLine() {
        assertEquals(false, t.setCell(0, 6));
        assertEquals(true, t.setCell(0, 0));
        assertEquals(false, t.setCell(0, 0));
        assertEquals(true, t.setCell(1, 1));
        assertEquals(true, t.setCell(1, 0));
        assertEquals(true, t.setCell(2, 2));
        assertEquals(true, t.setCell(2, 0));

        assertEquals(STATE.CROSS, t.checkForThreeInCol());
        assertEquals(STATE.CROSS, t.checkWinner());
    }

    @Test
    void checkOWonWithHorizontalLine() {
        assertEquals(true, t.setCell(0, 0));
        assertEquals(true, t.setCell(1, 0));
        assertEquals(true, t.setCell(2, 0));
        assertEquals(true, t.setCell(1, 1));
        assertEquals(true, t.setCell(0, 2));
        assertEquals(true, t.setCell(1, 2));

        assertEquals(STATE.CIRCLE, t.checkForThreeInRow());
        assertEquals(STATE.CIRCLE, t.checkWinner());
    }

    @Test
    void checkXWonWithDiagonalLine() {
        assertEquals(true, t.setCell(0, 0));
        assertEquals(true, t.setCell(1, 0));
        assertEquals(true, t.setCell(1, 1));
        assertEquals(true, t.setCell(2, 0));
        assertEquals(true, t.setCell(2, 2));

        assertEquals(STATE.CROSS, t.checkForThreeInDiag());
        assertEquals(STATE.CROSS, t.checkWinner());
    }

    @Test
    void checkDraw() {
        assertEquals(true, t.setCell(0, 0));
        assertEquals(true, t.setCell(0, 1));
        assertEquals(true, t.setCell(0, 2));

        assertEquals(true, t.setCell(1, 0));
        assertEquals(true, t.setCell(1, 2));
        assertEquals(true, t.setCell(1, 1));

        assertEquals(true, t.setCell(2, 0));
        assertEquals(true, t.setCell(2, 2));
        assertEquals(true, t.setCell(2, 1));

        assertTrue(t.checkForDraw());
    }
}