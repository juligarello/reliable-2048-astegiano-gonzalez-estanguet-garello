#!/bin/bash
# generate_evosuite_tests.sh

EVOSUITE_JAR="evosuite-1.0.6.jar"
EVOSUITE_URL="https://github.com/EvoSuite/evosuite/releases/download/v1.0.6/evosuite-1.0.6.jar"
TARGET_CLASSES="ar.edu.unrc.game2048.Cell ar.edu.unrc.game2048.Board"   # <-- CAMBIO
SEARCH_BUDGET=60

# Download EvoSuite if not exists
if [ ! -f "$EVOSUITE_JAR" ]; then
    echo "Downloading EvoSuite..."
    wget "$EVOSUITE_URL" || curl -L -o "$EVOSUITE_JAR" "$EVOSUITE_URL"
fi

# Build project first
mvn clean compile

CLASS_PATH=$(pwd)/target/classes

# Generate tests
for TARGET_CLASS in $TARGET_CLASSES; do                                  # <-- CAMBIO
    echo "Generating EvoSuite tests for $TARGET_CLASS..."
    java -jar "$EVOSUITE_JAR" -projectCP "$CLASS_PATH" -class $TARGET_CLASS \
        -Dsearch_budget=$SEARCH_BUDGET -Dtest_dir=src/test/java
done                                                                     # <-- CAMBIO

# Let JaCoCo measure EvoSuite tests                                      # <-- CAMBIO
sed -i 's/separateClassLoader = true/separateClassLoader = true/' src/test/java/ar/edu/unrc/game2048/*_ESTest.java

# Run tests
mvn test