@echo off

REM (Opzionale) Se vuoi impostare qui dentro il tuo JAVA_HOME specifico:
REM set JAVA_HOME=C:\Program Files\Java\jdk-17
REM set PATH=%JAVA_HOME%\bin;%PATH%

REM Avvia l'esecuzione del JAR
java -jar ./target/demo-1.0.0.jar --spring.config.location=file:.\exec\config\bootstrap.yml

REM (Opzionale) Se vuoi mantenere aperta la finestra del prompt al termine:
pause