package ar.edu.unrc.game2048;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BoardTest {

    @Test
    public void testConstructor() {
        assertThrows(IllegalArgumentException.class, () -> {
            ar.edu.unrc.game2048.Board board1 = new ar.edu.unrc.game2048.Board(0);
        });
    }

    @Test
    public void testGetSize() {
        Board board1 = new Board(4);
        assertEquals(board1.getSize(), 4);
    }

    @Test
    public void testGetScore() {
        Board board1 = new Board(4);
        assertEquals(board1.getScore(), 0);
    }

    @Test
    public void testGetCell() {
        Board board1 = new Board(4);
        assertThrows(IndexOutOfBoundsException.class, () -> {
            board1.getCell(-1, 0);
        });
    }

    @Test
    public void testSetCell() {
        Board board1 = new Board(4);
        Cell cel1 = null;
        assertThrows(IllegalArgumentException.class, () -> {
            board1.setCell(0, 0, cel1);
        });
    }

    @Test
    public void testGetEmptyPosition() {
        Board board1 = new Board(2);
        assertEquals(board1.getEmptyPositions().size(), 2);
    }

    @Test
    public void testHasEmptyCell() {
        Board board1 = new Board(2);
        assertTrue(board1.hasEmptyCells());
    }

    @Test
    public void testIsWinningBoard() {
        // caso true
        Board board1 = new Board(4);
        board1.setCell(0, 0, new Cell(2048));
        assertTrue(board1.isWinningBoard());

        board1.setCell(0, 0, new Cell(0));
        assertFalse(board1.isWinningBoard());
    }

    @Test
    public void testisLosingBoard1() {
        // caso true
        Board board1 = new Board(2);
        board1.setCell(0, 0, new Cell(2));
        board1.setCell(1, 0, new Cell(4));
        board1.setCell(0, 1, new Cell(6));
        board1.setCell(1, 1, new Cell(8));
        assertTrue(board1.isLosingBoard());
    }

    @Test
    public void testisFull() {
        Board board1 = new Board(2);
        board1.setCell(0, 0, new Cell(2));
        board1.setCell(1, 0, new Cell(4));
        board1.setCell(0, 1, new Cell(2));
        board1.setCell(1, 1, new Cell(8));
        assertTrue(board1.isFull());
    }

    @Test
    public void testMoveUp() {
        Board board1 = new Board(2);

        board1.setCell(0, 0, Cell.EMPTY);
        board1.setCell(0, 1, Cell.EMPTY);
        board1.setCell(1, 0, new Cell(2));
        board1.setCell(1, 1, new Cell(2));

        board1.moveUp();

        int count = 0;
        for (int i = 0; i < 2; i++) {
            int cellValue = board1.getCell(0, i).getValue();
            if (cellValue == 2) {
                count++;
            }
        }

        assertEquals(count, 2);

        count = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                if (board1.getCell(i, j).getValue() == 2 || board1.getCell(i, j).getValue() == 4) {
                    count++;
                }
            }
        }

        assertEquals(count, 3);
    }

    @Test
    public void testMoveDown() {
        Board board1 = new Board(2);

        board1.setCell(0, 0, new Cell(4));
        board1.setCell(1, 0, Cell.EMPTY);
        board1.setCell(0, 1, Cell.EMPTY);
        board1.setCell(1, 1, Cell.EMPTY);

        assertEquals(count, 2);

        count = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                if (board1.getCell(i, j).getValue() == 2 || board1.getCell(i, j).getValue() == 4) {
                    count++;
                }
            }
        }
        board1.moveDown();

        // La celda 4 que estaba en (0,0) deberia haber bajado a (1,0)
        assertEquals(4, board1.getCell(1, 0).getValue());
    }

    @Test
    public void testMoveLeft() {
        Board board1 = new Board(2);

        board1.setCell(0, 0, Cell.EMPTY);
        board1.setCell(0, 1, new Cell(2));
        board1.setCell(1, 0, Cell.EMPTY);
        board1.setCell(1, 1, new Cell(2));

        board1.moveLeft();

        int count = 0;

        if (board1.getCell(0, 0).getValue() == 2) {
            count++;
        }

        if (board1.getCell(1, 0).getValue() == 2) {
            count++;
        }

        assertEquals(count, 2);

        count = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                if (board1.getCell(i, j).getValue() == 2 || board1.getCell(i, j).getValue() == 4) {
                    count++;
                }
            }
        }

        assertEquals(count, 3);
    }

    @Test
    public void testMoveRight() {
        Board board1 = new Board(2);

        board1.setCell(0, 0, new Cell(2));
        board1.setCell(0, 1, Cell.EMPTY);
        board1.setCell(1, 0, new Cell(2));
        board1.setCell(1, 1, Cell.EMPTY);

        board1.moveRight();

        int count = 0;

        if (board1.getCell(0, 1).getValue() == 2) {
            count++;
        }

        if (board1.getCell(1, 1).getValue() == 2) {
            count++;
        }

        assertEquals(count, 2);

        count = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                if (board1.getCell(i, j).getValue() == 2 || board1.getCell(i, j).getValue() == 4) {
                    count++;
                }
            }
        }

        assertEquals(count, 3);
    }

    /*
     * ESTE TEST NO PASA POR QUE NO FUNCIONA BIEN EL PROGRAMA?, CHEQUEA LOS MAS A LA
     * DERECHA?
     * O pasa que cuando corres a la derecha, se juntan los dos y forman un 4 y al
     * quedar un nuevo dos en el
     * tablero ya no hay posibilidad y pierde? que seria lo correcto!
     */
    @Test
    public void testisLosingBoard2() {
        // caso true
        Board board1 = new Board(2);
        board1.setCell(0, 0, new Cell(2));
        board1.setCell(1, 0, new Cell(4));
        board1.setCell(0, 1, new Cell(2));
        board1.setCell(1, 1, new Cell(8));
        board1.moveRight();
        assertFalse(board1.isLosingBoard());
    }

    @Test
    public void testIsWinningBoardWithHigherValue() {
        Board board = new Board(4);
        board.setCell(0, 0, new Cell(4096));
        assertTrue(board.isWinningBoard());
    }

    // === Copy constructor ===

    @Test
    public void testCopyConstructor() {
        Board original = new Board(4);
        original.setCell(0, 0, new Cell(8));
        original.setCell(2, 3, new Cell(16));

        Board copy = new Board(original);

        assertEquals(original.getSize(), copy.getSize());
        assertEquals(original.getScore(), copy.getScore());
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                assertEquals(original.getCell(r, c), copy.getCell(r, c));
            }
        }
    }

    @Test
    public void testCopyConstructorIsDeepCopy() {
        Board original = new Board(4);
        original.setCell(0, 0, new Cell(32));

        Board copy = new Board(original);
        copy.setCell(0, 0, new Cell(64));

        assertEquals(32, original.getCell(0, 0).getValue());
        assertEquals(64, copy.getCell(0, 0).getValue());
    }

    // === equals ===

    @Test
    public void testEqualsSameBoard() {
        Board board = new Board(4);
        assertTrue(board.equals(board));
    }

    @Test
    public void testEqualsEqualBoards() {
        Board a = new Board(4);
        Board b = new Board(4);

        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                a.setCell(r, c, new Cell(2));
                b.setCell(r, c, new Cell(2));
            }
        }

        assertEquals(a, b);
    }

    @Test
    public void testEqualsDifferentBoards() {
        Board a = new Board(4);
        Board b = new Board(4);

        a.setCell(0, 0, new Cell(4));
        b.setCell(0, 0, new Cell(8));

        assertFalse(a.equals(b));
    }

    @Test
    public void testEqualsWithNull() {
        Board board = new Board(4);
        assertFalse(board.equals(null));
    }

    @Test
    public void testEqualsDifferentSize() {
        Board a = new Board(4);
        Board b = new Board(3);
        assertFalse(a.equals(b));
    }

    // === hashCode ===

    @Test
    public void testHashCodeEqualBoards() {
        Board a = new Board(4);
        Board b = new Board(4);

        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                a.setCell(r, c, new Cell(2));
                b.setCell(r, c, new Cell(2));
            }
        }

        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void testHashCodeDifferentBoards() {
        Board a = new Board(4);
        Board b = new Board(4);

        a.setCell(0, 0, new Cell(4));
        b.setCell(0, 0, new Cell(8));

        assertNotEquals(a.hashCode(), b.hashCode());
    }

    // === toString ===

    @Test
    public void testToString() {
        Board board = new Board(2);
        board.setCell(0, 0, Cell.EMPTY);
        board.setCell(0, 1, Cell.EMPTY);
        board.setCell(1, 0, Cell.EMPTY);
        board.setCell(1, 1, Cell.EMPTY);

        String str = board.toString();
        assertTrue(str.contains("Score: 0"));
        assertTrue(str.contains("+"));
        assertTrue(str.contains("|"));
    }

    @Test
    public void testToStringWithValues() {
        Board board = new Board(2);
        board.setCell(0, 0, new Cell(2));
        board.setCell(0, 1, new Cell(4));
        board.setCell(1, 0, new Cell(8));
        board.setCell(1, 1, new Cell(16));

        String str = board.toString();
        assertTrue(str.contains("2"));
        assertTrue(str.contains("4"));
        assertTrue(str.contains("8"));
        assertTrue(str.contains("16"));
    }

}
