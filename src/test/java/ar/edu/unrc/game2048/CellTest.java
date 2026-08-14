package ar.edu.unrc.game2048;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CellTest {

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

        Cell cell2= new Cell(2);
        assertFalse(cell2.isEmpty());
    }

    @Test
    public void testGetValue(){
        Cell cell1 = new Cell(2);

        assertEquals(2, cell1.getValue());
        assertNotEquals(4, cell1.getValue());
    }

    @Test
    public void testCanMergeWith(){
        Cell cell1 = new Cell(2);
        Cell cell2 = new Cell(2);
        Cell cell3 = new Cell(4);
        Cell cell4 = new Cell(0);

        assertTrue(cell1.canMergeWith(cell2));
        assertFalse(cell1.canMergeWith(cell3));
        assertFalse(cell1.canMergeWith(cell4));
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
}
