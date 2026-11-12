# OASIs-lite - Cell: equals() demasiado fuerte y mergeWith() demasiado debil

## Asercion

- **Metodo:** `Cell mergeWith(Cell other)` | **Direccion:** falso negativo (demasiado debil)
- **Asercion:** `Cell r = c.mergeWith(other); r.getValue() >= 2 && (r.getValue() & (r.getValue() - 1)) == 0`
- **Invariante Daikon:** `return.value is a power of 2` en `ar.edu.unrc.game2048.Cell.mergeWith(ar.edu.unrc.game2048.Cell):::EXIT`
- **Observacion:** La postcondicion solo exige que el resultado sea una potencia de 2. Es demasiado debil: un mutante que devuelva this (o new Cell(this.value)) la satisface, aunque la semantica correcta de mergeWith es duplicar el valor. Falta la relacion return.value == 2 * this.value.

## Analisis del LLM

The assertion only guarantees the result is a power of two (≥2). It does not enforce that the merge actually doubles the value. Consequently, mutants that return `this` or a copy of `this` still satisfy the assertion, hiding the bug.

**Refinamiento sugerido:** A stronger postcondition should require the merged value to be exactly double the original: `r.getValue() == 2 * this.value` and still be a power of two.

## Mutantes generados que la asercion no mata

### `return_self`

Returns this instead of a new doubled cell.

*Sobrevive porque:* The assertion only checks power‑of‑two, not value doubling.

```json
[
  {
    "old": "return new Cell(this.value * 2);",
    "new": "return this;"
  }
]
```

### `return_copy`

Returns a copy of this cell, not the doubled value.

*Sobrevive porque:* The weak assertion still passes because the copy is a power of two.

```json
[
  {
    "old": "return new Cell(this.value * 2);",
    "new": "return new Cell(this.value);"
  }
]
```

### Programa sonda

```java
import ar.edu.unrc.game2048.*;
import java.util.Random;

public class OasisProbe {
    public static void main(String[] args) {
        // Deterministic test: merge two cells with value 8
        Cell c1 = new Cell(8);
        Cell c2 = new Cell(8);
        Cell r = c1.mergeWith(c2);
        boolean holds = r.getValue() >= 2 && (r.getValue() & (r.getValue() - 1)) == 0;
        System.out.println("RESULT=" + holds);
        System.out.println("# merge " + c1.getValue() + "+" + c2.getValue() + " => " + r.getValue());
    }
}
```

## Verificacion local

- `return_self`: **survives**
  > RESULT=true
- `return_copy`: **survives**
  > RESULT=true

## Metadata

- Modelo: `cohere/north-mini-code:free` (gratuito, costo 0 USD)
- Verificacion: `javac` + JUnit 4 / sonda sobre el mutante

> La herramienta provee feedback; la decision de refinar la asercion es del usuario.
