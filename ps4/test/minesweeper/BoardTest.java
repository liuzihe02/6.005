package minesweeper;

import static org.junit.Assert.*;
import org.junit.Test;

public class BoardTest {

    // Testing strategy from original code:
    // dig(x, y)
    //   (x, y) is in the board, or out of the board
    //   (x, y) is _flagged_, _dug_, or _untouched_
    //   (x, y) contains a bomb or doesn't
    //   (x, y) has neighbor squares with bombs or doesn't
    // flag(x, y)
    //   (x, y) is in the board, or out of the board
    //   (x, y) is _untouched_ or isn't
    // deflag(x, y)
    //   (x, y) is in the board, or out of the board
    //   (x, y) is _flagged_ or isn't
    // toString()
    //   squares are _flagged_, _dug_, or _untouched_

    // Shared board configuration for Junqi Xie test cases
    private static final int JUN_BOARD_SIZE = 7;
    private static final int[][] JUN_MINES = { { 4, 1 }, { 0, 6 } };

    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false;
    }

    @Test
    public void testDigAlreadyDug() {
        Board board = new Board(3, 3, new int[][] { { 1, 1 } });
        board.dig(0, 0);
        assertFalse(board.dig(0, 0));
    }

    @Test
    public void testDeflagUntouched() {
        Board board = new Board(3, 3, new int[][] {});
        assertFalse(board.deflag(0, 0));
    }

    // Constructor tests
    @Test
    public void testConstructorBasic() {
        int[][] mines = { { 1, 1 }, { 2, 3 } };
        Board board = new Board(5, 5, mines);
        String expected = "- - - - -\n" +
                "- - - - -\n" +
                "- - - - -\n" +
                "- - - - -\n" +
                "- - - - -";
        assertEquals(expected, board.toString());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testConstructorInvalidMines() {
        int[][] mines = { { 10, 10 } };
        new Board(5, 5, mines);
    }

    // Dig tests
    @Test
    public void testDigOutOfBounds() {
        Board board = new Board(5, 5, new int[][] { { 1, 1 } });
        boolean result = board.dig(-1, -1);
        assertFalse(result);
    }

    @Test
    public void testDigUntouched() {
        Board board = new Board(JUN_BOARD_SIZE, JUN_BOARD_SIZE, JUN_MINES);
        boolean result = board.dig(3, 1);
        String expected = "- - - - - - -\n" +
                "- - - 1 - - -\n" +
                "- - - - - - -\n" +
                "- - - - - - -\n" +
                "- - - - - - -\n" +
                "- - - - - - -\n" +
                "- - - - - - -";
        assertFalse("expected no explosion", result);
        assertEquals("expected initial board", expected, board.toString());
    }

    @Test
    public void testDigEmpty() {
        Board board = new Board(JUN_BOARD_SIZE, JUN_BOARD_SIZE, JUN_MINES);
        boolean result = board.dig(0, 0);
        String expected = "      1 - 1  \n" +
                "      1 - 1  \n" +
                "      1 1 1  \n" +
                "             \n" +
                "             \n" +
                "1 1          \n" +
                "- 1          ";
        assertFalse("expected no explosion", result);
        assertEquals("expected board after dig", expected, board.toString());
    }

    @Test
    public void testDigFlagged() {
        Board board = new Board(3, 3, new int[][] { { 1, 1 } });
        board.flag(1, 1);
        assertFalse(board.dig(1, 1));
        assertEquals(
                "- - -\n" +
                        "- F -\n" +
                        "- - -",
                board.toString());
    }

    @Test
    public void testDigDug() {
        Board board = new Board(JUN_BOARD_SIZE, JUN_BOARD_SIZE, JUN_MINES);
        board.dig(0, 0);
        boolean result = board.dig(1, 3);
        String expected = "      1 - 1  \n" +
                "      1 - 1  \n" +
                "      1 1 1  \n" +
                "             \n" +
                "             \n" +
                "1 1          \n" +
                "- 1          ";
        assertFalse("expected no explosion", result);
        assertEquals("expected board after dig", expected, board.toString());
    }

    // Flag tests
    @Test
    public void testFlagOutOfBounds() {
        Board board = new Board(3, 3, new int[][] {});
        assertFalse(board.flag(-1, -1));
    }

    @Test
    public void testFlagUntouched() {
        Board board = new Board(3, 3, new int[][] { { 1, 1 } });
        assertTrue(board.flag(0, 0));
        assertEquals(
                "F - -\n" +
                        "- - -\n" +
                        "- - -",
                board.toString());
    }

    @Test
    public void testFlagDug() {
        Board board = new Board(JUN_BOARD_SIZE, JUN_BOARD_SIZE, JUN_MINES);
        board.dig(3, 1);
        assertFalse("should not be able to flag dug square", board.flag(3, 1));
        String expected = "- - - - - - -\n" +
                "- - - 1 - - -\n" +
                "- - - - - - -\n" +
                "- - - - - - -\n" +
                "- - - - - - -\n" +
                "- - - - - - -\n" +
                "- - - - - - -";
        assertEquals(expected, board.toString());
    }

    // Deflag tests
    @Test
    public void testDeflagOutOfBounds() {
        Board board = new Board(3, 3, new int[][] {});
        assertFalse(board.deflag(-1, -1));
    }

    @Test
    public void testDeflagFlagged() {
        Board board = new Board(3, 3, new int[][] { { 1, 1 } });
        board.flag(0, 0);
        assertTrue(board.deflag(0, 0));
        assertEquals(
                "- - -\n" +
                        "- - -\n" +
                        "- - -",
                board.toString());
    }

    @Test
    public void testDeflagDug() {
        Board board = new Board(JUN_BOARD_SIZE, JUN_BOARD_SIZE, JUN_MINES);
        board.dig(3, 1);
        assertFalse("should not be able to deflag dug square", board.deflag(3, 1));
        String expected = "- - - - - - -\n" +
                "- - - 1 - - -\n" +
                "- - - - - - -\n" +
                "- - - - - - -\n" +
                "- - - - - - -\n" +
                "- - - - - - -\n" +
                "- - - - - - -";
        assertEquals(expected, board.toString());
    }

    // Special cases
    @Test
    public void testEmptyBoard() {
        Board board = new Board(2, 2, new int[][] {});
        board.dig(0, 0);
        assertEquals(
                "   \n" +
                        "   ",
                board.toString());
    }

    @Test
    public void testCornerMines() {
        int[][] mines = { { 0, 0 }, { 2, 0 }, { 0, 2 }, { 2, 2 } };
        Board board = new Board(3, 3, mines);
        board.dig(1, 1);
        assertEquals(
                "- - -\n" +
                        "- 4 -\n" +
                        "- - -",
                board.toString());
    }
}
