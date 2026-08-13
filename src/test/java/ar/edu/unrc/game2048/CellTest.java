package ar.edu.unrc.game2048;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CellTest {

    @Test
    public void testConstructor() {
        assertThrows(IllegalArgumentException.class, () -> {
            ar.edu.unrc.game2048.Cell cell1 = new ar.edu.unrc.game2048.Cell(-1);
        });  
    }

    public static void main(String[] args) {
        CellTest cellTest1 = new CellTest();
        cellTest1.testConstructor();
    }
}
