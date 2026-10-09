#!/bin/bash
# Mide la cobertura de JaCoCo SOLO sobre los tests generados por EvoSuite.
#
# Usa instrumentacion offline de JaCoCo: es la unica forma de que el agente
# registre el SUT, porque EvoSuite lo carga en su propio EvoClassLoader
# (separateClassLoader = true) y el agente normal no lo instrumenta.
#
# Reporte: target/site/jacoco/index.html
#          target/site/jacoco/jacoco.csv

set -e

TESTS="${1:-*_ESTest}"

mvn clean test-compile jacoco:instrument -q
mvn test -Dtest="$TESTS" -Dsurefire.failIfNoSpecifiedTests=false
mvn jacoco:restore-instrumented-classes jacoco:report

echo
echo "Cobertura de los tests de EvoSuite ($TESTS):"
cat target/site/jacoco/jacoco.csv
echo
echo "Abrir: target/site/jacoco/index.html"
