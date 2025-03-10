# Springboot API with JSON-RPC method
Progetto DEMO che espone un API REST che chiama un metodo json-rpc

Prerequisito: JDK 17

---

## Compilazione dell'applicazione:

#### Setup ENV variables (Windows - powershell):
```
# Imposta JAVA_HOME per la sessione corrente:
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17.0.2"

# Aggiunge la cartella /bin della JDK in cima al PATH corrente:
$env:Path="$env:JAVA_HOME\bin;$env:Path"
```

#### Setup ENV variables (Linux/Mac):
```
# Imposta JAVA_HOME per la sessione corrente:
export JAVA_HOME=/usr/lib/jvm/jdk-version

# Aggiunge la cartella /bin della JDK in cima al PATH corrente:
export PATH=$JAVA_HOME/bin:$PATH
```

#### Compilazione applicazione (con test):
```
.\mvnw.cmd clean install
```

#### Compilazione applicazione (senza test):
```
.\mvnw.cmd clean install -DskipTests
```

---

## Startup/Shudown dell'applicazione:

Assicurarsi di aver configurato il path della propria JDK.  
Decommentare e modificare opportunamente le righe relative al path della propria JDK, all'interno dei file .bat/.sh

### Script per Windows:
#### Startup applicazione:
```
./exec/windows/startup.bat
```
#### Shutdown applicazione:
```
./exec/windows/shutdown.bat
```

### Script per Linux/Mac:
#### Startup applicazione:
```
chmod +x ./exec/linux-mac/startup.sh
./exec/linux-mac/startup.sh
```
#### Shutdown applicazione:
```
./exec/linux-mac/shutdown.sh
```