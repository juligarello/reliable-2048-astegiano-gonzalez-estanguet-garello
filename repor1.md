# OASIs-lite - Board: getSize() demasiado fuerte y moveLeft() demasiado debil

## Asercion

- **Metodo:** `int getSize()` | **Direccion:** falso positivo (demasiado fuerte)
- **Asercion:** `board.getSize() == Board.DEFAULT_SIZE`
- **Invariante Daikon:** `ar.edu.unrc.game2048.Board.DEFAULT_SIZE == return` en `ar.edu.unrc.game2048.Board.getSize():::EXIT`
- **Observacion:** Daikon infirio esta postcondicion observando solo tableros 4x4. El constructor permite new Board(size) con 1 <= size <= 19, asi que la postcondicion es demasiado fuerte: new Board(3).getSize() devuelve 3 y la viola. Es un falso positivo clasico producido por trazas poco diversas.

## Analisis del LLM

Daikon infirió que `Board.getSize()` siempre devuelve `Board.DEFAULT_SIZE` basándose únicamente en trazas de tableros 4×4. El constructor de `Board` permite cualquier tamaño entre 1 y 19, por lo que la postcondición es demasiado fuerte. Dos tests válidos crean tableros con tamaños diferentes (3 y 19) y fallan al afirmar que el tamaño es igual a `DEFAULT_SIZE`.

**Refinamiento sugerido:** Asegura que `getSize()` devuelve el tamaño del tablero, el cual debe estar entre 1 y 19 inclusive:
`post: 1 <= board.getSize() <= 19`

## Tests generados que violan la asercion

### `BoardGetSizeNotDefaultTest`

Tablero válido de tamaño 3 creado con semilla determinista; la aserción de que el tamaño es igual a DEFAULT_SIZE falla.

```java
package oasis.gen;

import org.junit.Test;
import static org.junit.Assert.*;
import ar.edu.unrc.game2048.*;

public class BoardGetSizeNotDefaultTest {
    @Test
    public void testSizeNotDefault() {
        // Tablero válido de tamaño 3, construido con semilla determinista
        Board b = new Board(123L, 3);
        // La aserción de que el tamaño es igual a DEFAULT_SIZE es demasiado fuerte; falla para tamaño 3
        assertEquals(Board.DEFAULT_SIZE, b.getSize());
    }
}
```

### `BoardGetSizeEdgeTest`

Tablero válido de tamaño máximo 19 creado con semilla determinista; la aserción de que el tamaño es igual a DEFAULT_SIZE falla.

```java
package oasis.gen;

import org.junit.Test;
import static org.junit.Assert.*;
import ar.edu.unrc.game2048.*;

public class BoardGetSizeEdgeTest {
    @Test
    public void testSizeMax() {
        // Tablero válido de tamaño máximo 19, semilla determinista
        Board b = new Board(0L, 19);
        // La aserción de que el tamaño es igual a DEFAULT_SIZE es demasiado fuerte; falla para tamaño 19
        assertEquals(Board.DEFAULT_SIZE, b.getSize());
    }
}
```

## Verificacion local

- `BoardGetSizeNotDefaultTest`: **violates_assertion**
  > JUnit version 4.13.2
- `BoardGetSizeEdgeTest`: **violates_assertion**
  > JUnit version 4.13.2

## Metadata

- Modelo: `cohere/north-mini-code:free` (gratuito, costo 0 USD)
- Verificacion: `javac` + JUnit 4 / sonda sobre el mutante

> La herramienta provee feedback; la decision de refinar la asercion es del usuario.
