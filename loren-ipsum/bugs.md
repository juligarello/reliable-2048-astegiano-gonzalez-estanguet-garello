# Bugs Encontrados - Loren Ipsum

## Bug 1: NullPointerException en Cell.canMergeWith(null)

- **Clase:** `Cell.java:69`
- **Test que lo demuestra:** `CellTest.testCanMergeWith` (linea `assertFalse(cell1.canMergeWith(null))`)
- **Descripcion:** Al pasar `null` como parametro a `canMergeWith()`, se produce un `NullPointerException` porque se intenta acceder al campo `value` de un objeto nulo.
- **Comportamiento esperado:** Deberia retornar `false` ya que una celda no puede fusionarse con `null`.
- **Severidad:** Media

## Bug 2: Cell(1) no valida correctamente

- **Clase:** `Cell.java:33-38`
- **Test que lo demuestra:** `CellTest.testConstructorWithOne`
- **Descripcion:** El constructor de `Cell` permite crear celdas con valor `1`. Si bien 1 es matematicamente una potencia de dos ($2^0$), en el juego 2048 las fichas validas comienzan desde 2 ($2^1$). La documentacion dice que el valor debe ser 0 o una potencia de dos, pero no valida que sea >= 2.
- **Comportamiento esperado:** Deberia lanzar `IllegalArgumentException` para valores como 1 que no son validos en el juego.
- **Severidad:** Baja

## Bug 3: isLosingBoard() no detecta fusiones horizontales en la ultima columna

- **Clase:** `Board.java:220`
- **Test que lo demuestra:** `BoardTest.testisLosingBoard2`
- **Descripcion:** La condicion `c + 1 < size - 1` deberia ser `c + 1 < size`. Esto causa que no se comprueben fusiones entre la penultima y la ultima columna, haciendo que el juego declare "game over" cuando aun hay movimientos posibles.
- **Severidad:** Alta - afecta la logica principal del juego

## Bug 4: isWinningBoard() no detecta valores mayores a 2048

- **Clase:** `Board.java:195`
- **Test que lo demuestra:** `BoardTest.testIsWinningBoardWithHigherValue`
- **Descripcion:** El metodo compara `== WINNING_VALUE` en vez de `>= WINNING_VALUE`. Si un jugador sigue jugando despues de 2048 y genera un 4096, el juego no reconoce la victoria.
- **Severidad:** Baja

## Bug 5: moveDown() pierde celdas de la fila 0

- **Clase:** `Board.java:314`
- **Test que lo demuestra:** `BoardTest.testMoveDown`
- **Descripcion:** El loop que construye la columna usa `row > 0` en vez de `row >= 0`. Esto causa que la fila 0 nunca se lea, pero luego el write-back la sobreescribe con EMPTY. Resultado: las celdas en la fila 0 desaparecen al hacer moveDown.
- **Comportamiento esperado:** Las celdas de la fila 0 deberian deslizarse hacia abajo junto con las demas.
- **Severidad:** Alta - pierde celdas del tablero
