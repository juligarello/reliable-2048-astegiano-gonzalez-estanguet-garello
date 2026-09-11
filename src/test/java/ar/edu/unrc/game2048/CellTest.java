package ar.edu.unrc.game2048;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Cell Tests")
public class CellTest {

    @Test
    public void testPotenciaDe2() {
        assertThrows(IllegalArgumentException.class, () -> {
            Cell cell = new Cell(5);
        });
    }

    @Test
    public void testCanMergeWith2() {
        Cell cell2 = new Cell(0);
        Cell cell3 = new Cell(0);
        assertFalse(cell2.canMergeWith(cell3));
    }

    @Test
    public void testEquals2() {
        Cell cell = new Cell(2);
        assertFalse(cell.equals(null));
    }

    @Test
    public void testEquals3() {
        Cell cell1 = new Cell(2);
        assertFalse(cell1.equals("Hola"));
    }

    @Test
    @DisplayName("should create empty cell with value 0")
    void testEmptyCell() {
        Cell cell = new Cell(0);
        assertTrue(cell.isEmpty());
        assertEquals(0, cell.getValue());
        assertEquals(Cell.EMPTY, cell);
    }

    @Test
    @DisplayName("should create valid power-of-two cells")
    void testValidCell() {
        Cell cell2 = new Cell(2);
        assertFalse(cell2.isEmpty());
        assertEquals(2, cell2.getValue());
    }

    @Test
    public void testConstructor() {
        assertThrows(IllegalArgumentException.class, () -> {
            Cell cell1 = new Cell(-1);
        });
    }

    @Test
    public void testEmpty() {
        Cell cell1 = new Cell(0);
        assertTrue(cell1.isEmpty());

        Cell cell2 = new Cell(2);
        assertFalse(cell2.isEmpty());
    }

    @Test
    public void testGetValue() {
        Cell cell1 = new Cell(2);

        assertEquals(2, cell1.getValue());
        assertNotEquals(4, cell1.getValue());
    }

    @Test
    public void testCanMergeWith() {
        assertThrows(NullPointerException.class, () -> {
            Cell cell1 = new Cell(0);

            cell1.mergeWith(null);
        });

        Cell cell2 = new Cell(2);
        Cell cell3 = new Cell(2);
        Cell cell4 = new Cell(4);
        Cell cell5 = new Cell(0);

        assertTrue(cell2.canMergeWith(cell3));
        assertFalse(cell2.canMergeWith(cell5));
        assertFalse(cell2.canMergeWith(cell4));
    }

    @Test
    public void testMergeWith() {
        assertThrows(IllegalArgumentException.class, () -> {
            Cell cell1 = new Cell(0);
            Cell cell2 = new Cell(2);

            cell1.mergeWith(cell2);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Cell cell3 = new Cell(4);
            Cell cell4 = new Cell(32);

            cell3.mergeWith(cell4);
        });

        Cell cell5 = new Cell(4);
        Cell cell6 = new Cell(4);
        Cell result = cell5.mergeWith(cell6);
        assertEquals(new Cell(8), result);
    }

    @Test
    public void testEquals() {
        Cell cell1 = new Cell(0);
        Cell cell2 = new Cell(2);
        Cell cell3 = new Cell(2);
        Cell cell4 = new Cell(4);

        assertTrue(cell2.equals(cell2));
        assertTrue(cell2.equals(cell3));
        assertFalse(cell1.equals(cell2));
        assertFalse(cell4.equals(cell1));
    }

    @Test
    public void testHashCode() {
        Cell cell1 = new Cell(2);
        Cell cell2 = new Cell(2);
        Cell cell3 = new Cell(4);

        assertEquals(cell1.hashCode(), cell2.hashCode());
        assertNotEquals(cell1.hashCode(), cell3.hashCode());
    }

    @Test
    public void testToString() {
        Cell cell1 = new Cell(0);
        Cell cell2 = new Cell(4);
        Cell cell3 = new Cell(8);

        assertEquals(".", cell1.toString());
        assertEquals("4", cell2.toString());
        assertEquals("8", cell3.toString());
    }

    @Test
    public void testConstructorWithOne() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Cell(1);
        });
    }
}
