# Comparación: EvoSuite vs Randoop
## 1. Resultados obtenidos

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

## 2. Ventajas de EvoSuite

- Tiene mayor cobertura:
- Tests mínimos: elimina tests redundantes
- **Determinismo**: mockea el no-determinismo haciendo que no sean al azar
- **Tests de excepción**: genera casos que cubren caminos de error , útiles para validar pre/postcondiciones.
- genera regression y assert ajustados 

## 3. Ventajas de Randoop
- Es simple 
- Es mucho mas rapido en el tiempo y la cantidad de test que genera

## 4. Desventajas de EvoSuite

- Complejidad en la lectura de los test 
- Es mas costoso y lleva mas tiempo


## 5. Desventajas de Randoop

- **Cobertura baja
- Tiene test redundantes
- No tiene determinismo
- Los oraculos tienden a ser mas genericos

## 6. Similitudes

- Ambos producen regression assertions.
- Ambos generan tests de excepción y los anotan como tales.
- Ambos requieren un paso de **filtrado/limpieza** del output antes de usar como suite de regresión.

## 7. Diferencias
Evosouite: genera suites atraves de la busqueda evolutiva minetras que randoop es alaeatoria, el criterio que usa evosuite es la cobertura , randoop usa como guia los contrato del objeto.La cantidad de test es mas baja en evosouite y randoop muchisimos. Ademas evosuite cuenta con un mockeo propio cosa que randoop no
## 8. Conclusión
Evosuite logro mejores resultados en el proyecto abaracando mas cobertura con mucha menos cantidad de test incluso cuando el randoop tenia el repOk, lo que no se pudo comparar es la mutación.


## Descripción del fuzzer
Para ello se implementaron dos componentes: un Fuzzer y un Runner. Método fuzz(). La clase RandomFuzzer recibe una longitud mínima y una máxima. Al invocar fuzz(), se elige al azar la cantidad de movimientos de la secuencia dentro de ese rango y luego se genera cada movimiento seleccionando aleatoriamente una tecla del dominio válido del juego (w, a, s, d). Las teclas se separan con saltos de línea, de modo que cada una sea interpretada como una entrada independiente por la entrada estándar, y la secuencia siempre finaliza con la tecla q, que provoca la salida controlada del programa. Clase CLIRunner. Esta clase ejecuta MainCLI como un subproceso de Java con las aserciones habilitadas (-ea), de modo que se detecten violaciones de aserciones del código. La secuencia generada se envía por entrada estándar y se capturan la salida estándar, la salida de error y el código de salida. Además, se define un timeout de 10 segundos para evitar que una ejecución quede bloqueada indefinidamente.
Cada ejecución se clasifica en: 
PASS: el programa terminó con código de salida 0 y sin escribir en stderr. 
FAIL: el código de salida es distinto de cero o hubo salida por stderr (por ejemplo, una excepción o un AssertionError). 
UNRESOLVED: se superó el tiempo máximo, por lo que no pudo determinarse el resultado. 
### Ejecución
El main() crea un CLIRunner y un RandomFuzzer, y repite 50 veces el ciclo de generar una secuencia con fuzz(), ejecutarla con el runner y registrar su clasificación. Al finalizar, imprime el total de ejecuciones por cada categoría. 

## Resultados
En nuestro caso se utilizaron secuencias de entre 100 y 250 movimientos y 50 ejecuciones. En ninguna de ellas se detectaron errores. Cabe aclarar que este resultado no demuestra la ausencia de fallos, sino que las secuencias generadas no lograron exponer ninguno. Una posible limitación es que el fuzzer solo utiliza entradas válidas, por lo que no ejercita el manejo de entradas inesperadas.

## Conclusión General

Para ver qué técnica sirve mejor para controlar errores, probamos las tres sobre el proyecto: el fuzzer, Randoop y EvoSuite. Lo primero que notamos es que cada una ataca el problema de manera distinta. El fuzzer  tira secuencias de teclas a la CLI, espera que no explote y que termine con un código de salida normal. Randoop genera en cantidad de secuencias al azar y se fija si se rompen contratos como equals/hashCode o si salta alguna excepción. EvoSuite busca cubrir la mayor cantidad de líneas y ramas posibles con la menor cantidad de tests, y de paso mockea el azar.En cuanto a resultados, nos pasaron cosas de todo tipo. El fuzzer no encontró crashes en la ejecución normal: la CLI aguanta bien las secuencias random. Con Randoopvarios tests generados no servían (nos tiraban "Loop has been executed more times than the allowed 10000") y hubo que filtrarlos. Igual, en la parte de mutaciones rindió bien: mató el 91% de los mutantes en la fase post-repOK. EvoSuite fue el que mejor cobertura sacó  con menos tests y mucho más prolijitos, pero no mostró bugs nuevos.Qué nos sirvió más en este programa en particular. El 2048 no es un programa que crashee fácil, los errores reales son de lógica, de estados de tablero que quedan inconsistentes. Para resumir: el fuzzer es el mejor detectorr de errores en este proyecto, Randoop sirve para chequear contratos y mutación, y EvoSuite tiene más cobertura.