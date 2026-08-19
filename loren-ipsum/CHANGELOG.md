# Changelog - Loren Ipsum

Todos los cambios documentados de nuestro grupo para la materia Seminario de Ciencias de la Computacion.

## [Fase 1.2 - Tests Unitarios] - 2026-08-19

### Agregado
- `CellTest.java`: 9 tests cubriendo todos los metodos publicos de Cell
- `BoardTest.java`: 27 tests cubriendo todos los metodos publicos de Board
  - Tests de copy constructor (`Board(Board other)`)
  - Tests de `equals()`, `hashCode()`, `toString()`
  - Tests de movimientos (`moveUp`, `moveDown`, `moveLeft`, `moveRight`)
  - Tests de estado del tablero (`isWinningBoard`, `isLosingBoard`, `isFull`)

### Descubierto (tests que fallan)
- `testCanMergeWith` - NullPointerException en `Cell.canMergeWith(null)`
- `testConstructorWithOne` - `Cell(1)` no lanza excepcion
- `testMoveDown` - `moveDown()` pierde celdas de la fila 0 por loop `row > 0`
- `testisLosingBoard2` - `isLosingBoard()` falla por off-by-one en `c + 1 < size - 1`
- `testIsWinningBoardWithHigherValue` - `isWinningBoard()` usa `== 2048` en vez de `>= 2048`
