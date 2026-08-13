package ar.edu.unrc.game2048;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class BoardTest {

    @Test
    public void testConstructor() {
        assertThrows(IllegalArgumentException.class, () -> {
            ar.edu.unrc.game2048.Board board1 = new ar.edu.unrc.game2048.Board(0);
        });  
    }

    public static void main(String[] args) {
        BoardTest boardTest1 = new BoardTest();
        boardTest1.testConstructor();
    }

}
