@echo off

REM Legge il PID dal file shutdown.pid e lo assegna alla variabile "PID"
set /p PID=<shutdown.pid

REM Se la variabile è vuota oppure il file non esiste, gestisci l’errore a tuo piacere:
if "%PID%"=="" (
    echo ERRORE: impossibile leggere un PID valido dal file shutdown.pid.
    exit /b 1
)

REM Uccide il processo con l'ID specificato
echo Arresto del processo con PID=%PID%...
taskkill /F /PID %PID%

REM (Opzionale) Metti in pausa per vedere eventuali messaggi di errore
pause
