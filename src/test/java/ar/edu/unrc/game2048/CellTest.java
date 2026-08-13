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
}
