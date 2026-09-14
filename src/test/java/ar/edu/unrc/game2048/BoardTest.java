package ar.edu.unrc.game2048;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class BoardTest {

    @Test
    public void testBoardSinArgumentos() {
        assertEquals(new Board().getSize(), 4);
    }

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

    // SetCell
    @Test
    public void testSetCell() {
        Board board1 = new Board(4);
        Cell cel1 = null;
        assertThrows(IllegalArgumentException.class, () -> {
            board1.setCell(0, 0, cel1);
        });
    }

    @Test
    public void testSetCell1() {
        Board board1 = new Board(4);
        Cell cel1 = null;
        assertThrows(IndexOutOfBoundsException.class, () -> {
            board1.setCell(-1, 0, cel1);
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
        board1.setCell(1, 1, new Cell(8));
        board1.setCell(0, 1, new Cell(16));
        assertTrue(board1.isLosingBoard());
    }

    // FULL

    @Test
    public void testisFull() {
        Board board1 = new Board(2);
        board1.setCell(0, 0, new Cell(2));
        board1.setCell(1, 0, new Cell(4));
        board1.setCell(0, 1, new Cell(2));
        board1.setCell(1, 1, new Cell(8));
        assertTrue(board1.isFull());
    }

    public void testisFull2() {
        Board board1 = new Board(2);

        assertFalse(board1.isFull());
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
        //Probar columnas que puedan mergear, que no tengan
        //que merrgear y que no puedan mergear

        Board board2 = new Board(4);

        //Columna 1
        board2.setCell(0, 0, Cell.EMPTY);
        board2.setCell(1, 0, Cell.EMPTY);
        board2.setCell(2, 0, new Cell(2));
        board2.setCell(3, 0, new Cell(2));

        //Columna 2
        board2.setCell(0, 1, new Cell (2));
        board2.setCell(1, 1, Cell.EMPTY);
        board2.setCell(2, 1, Cell.EMPTY);
        board2.setCell(3, 1, new Cell(2));

        //Columna 3
        board2.setCell(0, 2, Cell.EMPTY);
        board2.setCell(1, 2, Cell.EMPTY);
        board2.setCell(2, 2, Cell.EMPTY);
        board2.setCell(3, 2, new Cell(2));

        //Columna 4
        board2.setCell(0, 3, new Cell(2));
        board2.setCell(1, 3, new Cell(4));
        board2.setCell(2, 3, new Cell(8));
        board2.setCell(3, 3, new Cell(16));

        board2.moveUp();


        count = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (!board2.getCell(i, j).isEmpty()) {
                    count++;
                }
            }
        }

        assertEquals(count, 8);

        // PROBAR CUANDO UN MOVEUP NO DEBERIA CAMBIAR EL TABLERO
        Board board3 = new Board(2);
        board3.setCell(0, 0, new Cell(2));
        board3.setCell(1, 0, Cell.EMPTY);
        board3.setCell(0, 1, new Cell(2));
        board3.setCell(1, 1, Cell.EMPTY);

        Board board4 = new Board(2);
        board4.setCell(0, 0, new Cell(2));
        board4.setCell(1, 0, Cell.EMPTY);
        board4.setCell(0, 1, new Cell(2));
        board4.setCell(1, 1, Cell.EMPTY);

        board3.moveUp();
        assertEquals(board3, board4);
    }

    @Test
    public void testMoveDown() {
        Board board1 = new Board(2);

        board1.setCell(0, 0, new Cell(4));
        board1.setCell(1, 0, Cell.EMPTY);
        board1.setCell(0, 1, Cell.EMPTY);
        board1.setCell(1, 1, Cell.EMPTY);

        board1.moveDown();

        assertEquals(4, board1.getCell(1, 0).getValue());

        //Probar columnas que puedan mergear, que no tengan
        //que merrgear y que no puedan mergear

        Board board2 = new Board(4);

        //Columna 1
        board2.setCell(0, 0, Cell.EMPTY);
        board2.setCell(1, 0, Cell.EMPTY);
        board2.setCell(2, 0, new Cell(2));
        board2.setCell(3, 0, new Cell(2));

        //Columna 2
        board2.setCell(0, 1, new Cell (2));
        board2.setCell(1, 1, Cell.EMPTY);
        board2.setCell(2, 1, Cell.EMPTY);
        board2.setCell(3, 1, new Cell(2));

        //Columna 3
        board2.setCell(0, 2, Cell.EMPTY);
        board2.setCell(1, 2, Cell.EMPTY);
        board2.setCell(2, 2, Cell.EMPTY);
        board2.setCell(3, 2, new Cell(2));

        //Columna 4
        board2.setCell(0, 3, new Cell(2));
        board2.setCell(1, 3, new Cell(4));
        board2.setCell(2, 3, new Cell(8));
        board2.setCell(3, 3, new Cell(16));

        board2.moveDown();

        int count = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (!board2.getCell(i, j).isEmpty()) {
                    count++;
                }
            }
        }

        assertEquals(count, 8);

        // PROBAR CUANDO UN MOVEUP NO DEBERIA CAMBIAR EL TABLERO
        Board board3 = new Board(2);
        board3.setCell(0, 0, Cell.EMPTY);
        board3.setCell(1, 0, new Cell(2));
        board3.setCell(0, 1, Cell.EMPTY);
        board3.setCell(1, 1, new Cell(2));

        Board board4 = new Board(2);
        board4.setCell(0, 0, Cell.EMPTY);
        board4.setCell(1, 0, new Cell(2));
        board4.setCell(0, 1, Cell.EMPTY);
        board4.setCell(1, 1, new Cell(2));

        board3.moveDown();
        assertEquals(board3, board4);
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

        if(board1.getCell(0, 0).getValue() == 2) {
            count++;
        }
        if(board1.getCell(1, 0).getValue() == 2) {
            count++;
        }

        assertEquals(count, 2);

        count = 0;
        for(int i = 0; i < 2; i++) {
            for(int j = 0; j < 2; j++) {
                if(board1.getCell(i, j).getValue() == 2 || board1.getCell(i, j).getValue() == 4) {
                    count++;
                }
            }
        }

        assertEquals(count, 3);

        // PROBAR COLUMNAS QUE PUEDAN MERGEAR, QUE NO TENGAN QUE MERGEAR Y QUE NO PUEDEN MERGEAR

        Board board2 = new Board(4);

        // Fila 1
        board2.setCell(0, 0, new Cell(2));
        board2.setCell(0, 1, new Cell(2));
        board2.setCell(0, 2, Cell.EMPTY);
        board2.setCell(0, 3, Cell.EMPTY);

        // Fika 2
        board2.setCell(1, 0, new Cell(2));
        board2.setCell(1, 1, Cell.EMPTY);
        board2.setCell(1, 2, Cell.EMPTY);
        board2.setCell(1, 3, new Cell(2));

        // Fila 3
        board2.setCell(2, 0, new Cell(2));
        board2.setCell(2, 1, new Cell(4));
        board2.setCell(2, 2, Cell.EMPTY);
        board2.setCell(2, 3, Cell.EMPTY);

        // Fila 4
        board2.setCell(3, 0, Cell.EMPTY);
        board2.setCell(3, 1, Cell.EMPTY);
        board2.setCell(3, 2, new Cell(8));
        board2.setCell(3, 3, Cell.EMPTY);

        board2.moveLeft();

        count = 0;
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                if(!board2.getCell(i, j).isEmpty()) {
                    count++;
                }
            }
        }

        assertEquals(8, board2.getScore());
        assertEquals(6, count);

        // PROBAR CUANDO MOVELEFT NO DEBERIA CAMBIAR EL TABLERO

        Board board3 = new Board(2);
        board3.setCell(0, 0, new Cell(2));
        board3.setCell(0, 1, Cell.EMPTY);
        board3.setCell(1, 0, new Cell(2));
        board3.setCell(1, 1, Cell.EMPTY);

        Board board4 = new Board(2);
        board4.setCell(0, 0, new Cell(2));
        board4.setCell(0, 1, Cell.EMPTY);
        board4.setCell(1, 0, new Cell(2));
        board4.setCell(1, 1, Cell.EMPTY);

        board3.moveLeft();
        assertEquals(board3, board4);
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

        if(board1.getCell(0, 1).getValue() == 2) {
            count++;
        }
        if(board1.getCell(1, 1).getValue() == 2) {
            count++;
        }

        assertEquals(count, 2);

        count = 0;
        for(int i = 0; i < 2; i++) {
            for(int j = 0; j < 2; j++) {
                if(board1.getCell(i, j).getValue() == 2 || board1.getCell(i, j).getValue() == 4) {
                    count++;
                }
            }
        }

        assertEquals(count, 3);

        // PROBAR COLUMNAS QUE PUEDAN MERGEAR, QUE NO TENGAN QUE MERGEAR Y QUE NO PUEDEN MERGEAR

        Board board2 = new Board(4);

        // Fila 1
        board2.setCell(0, 0, new Cell(2));
        board2.setCell(0, 1, new Cell(2));
        board2.setCell(0, 2, Cell.EMPTY);
        board2.setCell(0, 3, Cell.EMPTY);

        // Fila 2
        board2.setCell(1, 0, new Cell(2));
        board2.setCell(1, 1, Cell.EMPTY);
        board2.setCell(1, 2, Cell.EMPTY);
        board2.setCell(1, 3, new Cell(2));

        // Fila 3
        board2.setCell(2, 0, new Cell(2));
        board2.setCell(2, 1, new Cell(4));
        board2.setCell(2, 2, Cell.EMPTY);
        board2.setCell(2, 3, Cell.EMPTY);

        // Fila 4
        board2.setCell(3, 0, Cell.EMPTY);
        board2.setCell(3, 1, Cell.EMPTY);
        board2.setCell(3, 2, new Cell(8));
        board2.setCell(3, 3, Cell.EMPTY);

        board2.moveRight();

        count = 0;
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                if(!board2.getCell(i, j).isEmpty()) {
                    count++;
                }
            }
        }

        assertEquals(8, board2.getScore());
        assertEquals(6, count);

        // PROBAR CUANDO MOVER A LA DERECHA NO DEBERIA CAMBIAR EL TABLERO

        Board board3 = new Board(2);
        board3.setCell(0, 0, Cell.EMPTY);
        board3.setCell(0, 1, new Cell(2));
        board3.setCell(1, 0, Cell.EMPTY);
        board3.setCell(1, 1, new Cell(2));

        Board board4 = new Board(2);
        board4.setCell(0, 0, Cell.EMPTY);
        board4.setCell(0, 1, new Cell(2));
        board4.setCell(1, 0, Cell.EMPTY);
        board4.setCell(1, 1, new Cell(2));

        board3.moveRight();
        assertEquals(board3, board4);
    }

    // IslogisngBoar()

    @Test
    public void testisLosingBoard2() {
        // caso true
        Board board1 = new Board(2);
        board1.setCell(0, 0, new Cell(2));
        board1.setCell(1, 0, new Cell(4));
        board1.setCell(0, 1, new Cell(2));
        board1.setCell(1, 1, new Cell(8));
        board1.moveRight();
        int valorAgregado = board1.getCell(0, 0).getValue();

        if (valorAgregado == 4) {
            assertFalse(board1.isLosingBoard());
        } else {

            assertTrue(board1.isLosingBoard());
        }
    }

    @Test
    public void testisLosingBoard3() {
        Board board1 = new Board(2);
        assertFalse(board1.isLosingBoard());
        board1.setCell(0, 0, new Cell(2));
        board1.setCell(1, 0, new Cell(2));
        board1.setCell(0, 1, new Cell(2));
        board1.setCell(1, 1, new Cell(2));
        assertFalse(board1.isLosingBoard());

    }

    @Test
    public void testisLosingBoard4() {
        Board board1 = new Board(2);
        assertFalse(board1.isLosingBoard());
        board1.setCell(0, 0, new Cell(2));
        board1.setCell(1, 0, new Cell(2));
        board1.setCell(0, 1, new Cell(4));
        board1.setCell(1, 1, new Cell(8));
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

    @Test
    public void testEqualsDifferentClass() {
        Board board = new Board(4);
        assertFalse(board.equals("no soy un board"));
    }

    // === Board.Position tests ===

    @Test
    public void testPositionEqualsSameReference() {
        Board.Position posicion = new Board.Position(1, 2);
        assertTrue(posicion.equals(posicion));
    }

    @Test
    public void testPositionEqualsSameValues() {
        Board.Position a = new Board.Position(1, 2);
        Board.Position b = new Board.Position(1, 2);
        assertTrue(a.equals(b));
    }

    @Test
    public void testPositionEqualsNull() {
        Board.Position posicion = new Board.Position(1, 2);
        assertFalse(posicion.equals(null));
    }

    @Test
    public void testPositionEqualsDifferentClass() {
        Board.Position posicion = new Board.Position(1, 2);
        assertFalse(posicion.equals("no soy position"));
    }

    @Test
    public void testPositionEqualsDifferentRow() {
        Board.Position a = new Board.Position(1, 2);
        Board.Position b = new Board.Position(2, 2);
        assertFalse(a.equals(b));
    }

    @Test
    public void testPositionEqualsDifferentCol() {
        Board.Position a = new Board.Position(1, 2);
        Board.Position b = new Board.Position(1, 3);
        assertFalse(a.equals(b));
    }

    @Test
    public void testPositionToString() {
        Board.Position posicion = new Board.Position(2, 3);
        assertEquals("(2, 3)", posicion.toString());
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