# OASIs-lite - Board: getSize() demasiado fuerte y moveLeft() demasiado debil

## Asercion

- **Metodo:** `boolean moveLeft()` | **Direccion:** falso negativo (demasiado debil)
- **Asercion:** `int before = board.getScore(); board.moveLeft(); board.getScore() >= before`
- **Invariante Daikon:** `this.score >= orig(this.score)` en `ar.edu.unrc.game2048.Board.moveLeft():::EXIT`
- **Observacion:** La postcondicion solo exige que el score no disminuya. Es demasiado debil: un mutante que olvide sumar el puntaje de las fusiones (o que sume de menos) la sigue satisfaciendo, aunque el juego puntue mal. Deberia relacionar el incremento del score con las celdas efectivamente fusionadas.

## Analisis del LLM

The assertion only checks that the score does not decrease after moveLeft, which is too weak. A mutant that omits or undercounts merge scores, or that moves tiles in the wrong direction, still satisfies the check because the score never drops. The postcondition should tie the score increase to actual merges.

**Refinamiento sugerido:** A stronger postcondition for moveLeft could be:  
`board.getScore() == before + Σ (mergedCell.getValue())` for each merge performed during the move, ensuring the score increase exactly matches the value of merged tiles.

## Mutantes generados que la asercion no mata

### `ReducedScoreAddition`

The line that adds the merged cell value to the score is changed to add a reduced amount (value minus 2), causing the board to underreport points.

*Sobrevive porque:* The assertion only checks that the score does not decrease; undercounting still yields a score that is ≥ the previous score, so the assertion passes.

```json
[
  {
    "old": "score += mergedCell.getValue();",
    "new": "score += mergedCell.getValue() - 2;"
  }
]
```

### `AlwaysMerge`

The condition that checks whether two cells can merge is replaced with a constant true, causing any adjacent cells to merge regardless of their values.

*Sobrevive porque:* This can increase the score (or keep it the same) but never decreases it, so the assertion that the score is non‑decreasing does not detect the fault.

```json
[
  {
    "old": "nonEmpty.get(i).canMergeWith(nonEmpty.get(i + 1))",
    "new": "true"
  }
]
```

### Programa sonda

```java
import ar.edu.unrc.game2048.*;
import java.util.*;

public class OasisProbe {
    public static void main(String[] args) {
        Board board = new Board(123456L, 4);
        for (int r = 0; r < board.getSize(); r++) {
            for (int c = 0; c < board.getSize(); c++) {
                board.setCell(r, c, Cell.EMPTY);
            }
        }
        board.setCell(0, 0, new Cell(2));
        board.setCell(0, 1, new Cell(2));
        int before = board.getScore();
        board.moveLeft();
        int after = board.getScore();
        boolean result = after >= before;
        System.out.println("RESULT=" + result);
        System.out.println("# before=" + before + " after=" + after);
    }
}
```
