

# Assignment 3
## Comparación: EvoSuite vs Randoop

| Métrica                  | EvoSuite                      | Randoop (pre-repOK)             | Randoop (post-repOK)            |
| ------------------------ | ----------------------------- | ------------------------------- | ------------------------------- |
| Cantidad de tests        | 77                            | mucha cantidad todos aleatorios | mucha cantidad todos aleatorios |
| Cobertura global (instr) | **89.9%**                     | 74.1%                           | 76.1%                           |
| Cobertura global (ramas) | **89.4%**                     | 66.2%                           | 69.1%                           |
| Cell                     | **97.7%** (169/173)           | 56.4%                           | 82.1%                           |
| Board                    | **98.9%** (1236/1250)         | 83.5%                           | 83.2%                           |
| Board.Position           | **100%**                      | 87.7%                           | 74.0%                           |
| Board.Direction          | 100%                          | 100%                            | 100%                            |
| MainCLI                  | 0% (sin tests generados)      | 0%                              | 0%                              |
| Mutación (PIT)           | no medido                     | 80.4% (152/248)                 | **91.0%** (182/277)             |
| Stabilidad               | alta (mockea no-determinismo) | 5 errores                       | idem                            |

##  Ventajas de EvoSuite

- Tiene mayor cobertura:
- Tests mínimos: elimina tests redundantes
- **Determinismo**: mockea el no-determinismo haciendo que no sean al azar
- **Tests de excepción**: genera casos que cubren caminos de error , útiles para validar pre/postcondiciones.
- genera regression y assert ajustados 

##  Ventajas de Randoop
- Es simple 
- Es mucho mas rapido en el tiempo y la cantidad de test que genera

## Desventajas de EvoSuite

- Complejidad en la lectura de los test 
- Es mas costoso y lleva mas tiempo


## Desventajas de Randoop

- **Cobertura baja
- Tiene test redundantes
- No tiene determinismo
- Los oraculos tienden a ser mas genericos

##  Similitudes

- Ambos producen regression assertions.
- Ambos generan tests de excepción y los anotan como tales.
- Ambos requieren un paso de **filtrado/limpieza** del output antes de usar como suite de regresión.

## Diferencias
Evosouite: genera suites atraves de la busqueda evolutiva minetras que randoop es alaeatoria, el criterio que usa evosuite es la cobertura , randoop usa como guia los contrato del objeto.La cantidad de test es mas baja en evosouite y randoop muchisimos. Ademas evosuite cuenta con un mockeo propio cosa que randoop no
## Conclusión
Evosuite logro mejores resultados en el proyecto abaracando mas cobertura con mucha menos cantidad de test incluso cuando el randoop tenia el repOk, lo que no se pudo comparar es la mutación.


## Descripción del fuzzer
Para ello se implementaron dos componentes: un Fuzzer y un Runner. Método fuzz(). La clase RandomFuzzer recibe una longitud mínima y una máxima. Al invocar fuzz(), se elige al azar la cantidad de movimientos de la secuencia dentro de ese rango y luego se genera cada movimiento seleccionando aleatoriamente una tecla del dominio válido del juego (w, a, s, d). Las teclas se separan con saltos de línea, de modo que cada una sea interpretada como una entrada independiente por la entrada estándar, y la secuencia siempre finaliza con la tecla q, que provoca la salida controlada del programa. Clase CLIRunner. Esta clase ejecuta MainCLI como un subproceso de Java con las aserciones habilitadas (-ea), de modo que se detecten violaciones de aserciones del código. La secuencia generada se envía por entrada estándar y se capturan la salida estándar, la salida de error y el código de salida. Además, se define un timeout de 10 segundos para evitar que una ejecución quede bloqueada indefinidamente.
Cada ejecución se clasifica en: 
PASS: el programa terminó con código de salida 0 y sin escribir en stderr. 
FAIL: el código de salida es distinto de cero o hubo salida por stderr (por ejemplo, una excepción o un AssertionError). 
UNRESOLVED: se superó el tiempo máximo, por lo que no pudo determinarse el resultado. 
## Ejecución
El main() crea un CLIRunner y un RandomFuzzer, y repite 50 veces el ciclo de generar una secuencia con fuzz(), ejecutarla con el runner y registrar su clasificación. Al finalizar, imprime el total de ejecuciones por cada categoría. 

## Resultados
En nuestro caso se utilizaron secuencias de entre 100 y 250 movimientos y 50 ejecuciones. En ninguna de ellas se detectaron errores. Cabe aclarar que este resultado no demuestra la ausencia de fallos, sino que las secuencias generadas no lograron exponer ninguno. Una posible limitación es que el fuzzer solo utiliza entradas válidas, por lo que no ejercita el manejo de entradas inesperadas.

## Conclusión General

Para ver qué técnica sirve mejor para controlar errores, probamos las tres sobre el proyecto: el fuzzer, Randoop y EvoSuite. Lo primero que notamos es que cada una ataca el problema de manera distinta. El fuzzer  tira secuencias de teclas a la CLI, espera que no explote y que termine con un código de salida normal. Randoop genera en cantidad de secuencias al azar y se fija si se rompen contratos como equals/hashCode o si salta alguna excepción. EvoSuite busca cubrir la mayor cantidad de líneas y ramas posibles con la menor cantidad de tests, y de paso mockea el azar.En cuanto a resultados, nos pasaron cosas de todo tipo. El fuzzer no encontró crashes en la ejecución normal: la CLI aguanta bien las secuencias random. Con Randoopvarios tests generados no servían (nos tiraban "Loop has been executed more times than the allowed 10000") y hubo que filtrarlos. Igual, en la parte de mutaciones rindió bien: mató el 91% de los mutantes en la fase post-repOK. EvoSuite fue el que mejor cobertura sacó  con menos tests y mucho más prolijitos, pero no mostró bugs nuevos.Qué nos sirvió más en este programa en particular. El 2048 no es un programa que crashee fácil, los errores reales son de lógica, de estados de tablero que quedan inconsistentes. Para resumir: el fuzzer es el mejor detectorr de errores en este proyecto, Randoop sirve para chequear contratos y mutación, y EvoSuite tiene más cobertura.



# Assignment 4
## Los comandos que usamos para correr  e instrumentar las trazas fueron, para 

-Trazas sobre los test manuales
-Trazas sobre los test de randoop:
-Trazas sobre  el fuzzer
```
python3 -c "from fuzzer import RandomFuzzer; print(''.join(RandomFuzzer(50, 100).fuzz() for _ in range(10)))" | \
java -cp "lib/daikon.jar:target/classes" \
daikon.Chicory \
--ppt-select-pattern="ar\.edu\.unrc\.game2048\.(Board|Cell)" \
--output-dir=daikon-traces \
--dtrace-file=fuzzer.dtrace.gz \
ar.edu.unrc.game2048.MainCLI
```

```
java -cp lib/daikon.jar daikon.Daikon \
  daikon-traces/fuzzer.dtrace.gz \
  > daikon-outputFuzzerOnly.txt
```

## Invariantes Test Manuales
### Invariantes Board
1-```this.score != size(this.grid[])-1```
 Es una invariante espuria (too strong): se cumple en las trazas observadas, pero no en todos los tableros válidos. Contraejemplo: un tablero de size == 1 con score == 0, o uno de size == 5 con score == 4, donde score == size(grid)-1.
2-``this.size == size(this.grid[])`` esta es una invariante correcta que incluso coincide con nuestra misma invariante del repOK
3-``this.size one of { 2, 3, 4 } ``
 esta es una invariante espuria (too strong) generada a causa de que en los test manuales definimos pocos casos de prueba, con tableros que no tenían mayor tamaño que 4. Daikon infirió que, como no se construían tableros más grandes en la suite de test, era una invariante.
 4-``this.score % this.size == 0`` es una invariante espuria (too strong), no trivial: score y size son campos distintos. Contraejemplo: un tablero de size == 3 donde se fusionan dos fichas 2 queda con score == 4, y 4 % 3 != 0.
 5-``this.grid != null`` esta es una invariante correcta y útil; ya está en repOK.

### Invariantes Cell
1-``this.value >= 0`` Esta es una invariante correcta que ya está reflejada en el repOK
2-``this.value >= ar.edu.unrc.game2048.Cell.EMPTY.value`` esta es una invariante correcta pero no aporta nada que la invariante anterior no refleje


## Invariantes test Randoop
### Invariantes Board
1-``ar.edu.unrc.game2048.Board.WINNING_VALUE > this.score`` es una invariante espuria (too strong): el score es acumulativo, así que se puede superar 2048 y seguir jugando. Daikon la infirió porque las trazas de Randoop no llegan a una partida larga o no siguen jugando después de ganar.
2-``ar.edu.unrc.game2048.Board.WINNING_VALUE > this.size`` es correcta pero trivial: el constructor limita el tamaño a 1..19, así que 2048 > size se cumple siempre, pero no aporta nada. Si sacáramos el límite, sería espuria.
3-``this.score % ar.edu.unrc.game2048.Board.DEFAULT_SIZE == 0`` es correcta y no trivial: cada fusión suma una potencia de dos mayor o igual a 4, por lo que el score siempre es múltiplo de 4. No está en repOK, pero es una consecuencia del sistema de puntaje.
4-``this.random != null`` es correcta pero trivial: el constructor siempre asigna un Random; ya está en repOK.
5-``this.score >= 0`` es correcta y útil; ya está en repOK.

## Conclusión parcial

Lo que más cambió entre las tres corridas no fue la herramienta, sino la variedad de las trazas. Con los tests manuales, Daikon vio pocos estados y sacó invariantes que servían solo para esos casos. Cuando sumamos Randoop, que arma tableros de distintos tamaños y estados, varias de esas restricciones se corrigieron: `this.size one of { 2, 3, 4 }` pasó a `this.size >= 1`, `this.score one of { 0, 4, 8 }` a `this.score >= 0`, `this.score % this.size == 0` a `this.score % DEFAULT_SIZE == 0`, y desapareció `DEFAULT_SIZE >= this.size`. Pero sumar trazas tampoco es garantía de mejora  ya que  también aparecieron invariantes raras como `this.row == 35` o `this.size one of { 1, 4 }`, que hay que filtrar a mano.
La comparación con el fuzzer nos muestra que   en el score mejoró (`this.score >= 0` y `this.score % DEFAULT_SIZE == 0`), pero en el tamaño no pudo corregir nada porque `MainCLI` siempre crea tableros 4x4, y en vez de `this.size one of { 2, 3, 4 }` infirió `DEFAULT_SIZE == this.size`, que sigue siendo espuria. Tampoco aportó invariantes de objeto nuevas y útiles: aparecieron espurias como `this.random has only one value`, `this.value > Cell.EMPTY.value` en `Cell(int).

Si comparamos con `repOK()`, Daikon redescubrió las invariantes estructurales básicas (`size == size(grid)`, `grid != null`, `grid[] elements != null`, `random != null`, `score >= 0` y, con Randoop, `size >= 1`). Lo que no pudo capturar son las propiedades más importantes del dominio: que cada `Cell` sea 0 o potencia de dos como invariante de clase, que la grilla sea cuadrada, que cada celda cumpla `cell.repOk()`, o el límite `size < 20` del constructor. Esto muestra el límite de Daikon este sí infiere `this.value is a power of 2` cuando `value != 0`, pero no lo combina en algo como `value == 0 || value es potencia de 2` para todo el objeto. Pero  encontró cosas que no teníamos en `repOK()`, como `score % DEFAULT_SIZE == 0` y postcondiciones que sirven para agregar como aserciones en los tests .


