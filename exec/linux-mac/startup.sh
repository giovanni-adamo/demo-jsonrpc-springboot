#!/usr/bin/env bash

# (Opzionale) Se vuoi impostare qui il JAVA_HOME specifico:
# export JAVA_HOME="/usr/lib/jvm/jdk-17"
# export PATH="$JAVA_HOME/bin:$PATH"

# Esegui il JAR
java -jar ./target/demo-1.0.0.jar --spring.config.location=file:./exec/config/bootstrap.yml
