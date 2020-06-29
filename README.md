# FirstPass
FirstPass is a simple password manager

## Documentation
### Execution
#### IDE
To execute the application in the IDE go to the FirstPassApplication class and start it (press the arrow)
#### Console
To execute the application in the console we first need to build the application and then we are able to start it
###### Build
* Open the console and go to the directory where the project is downloaded
* Type in `mvn package`. It will compile the code and package it into a jar-file in the target directory
###### Start
* Open the console and go to the directory where the project is downloaded
* Type in `java -jar <artifactId>-<version>.jar`, e.g. `java -jar FirstPass-0.0.1-SNAPSHOT.jar`. The `artifactId` and `version` can be found in the pom.xml file.
* Login: the default user is "simon" and the password "asdf". They can be changed in the data.sql file

### Tech/frameworks used
* Java
* Spring Boot
* Thymeleaf
* H2 embedded database
* Bootstrap
* jQuery

### Requirements
* Java 11

## Reflexion (in German)
### Aufbau der Applikation
Wir haben uns dazu entschieden, unser Projekt mit Hilfe des MVC Patterns umzusetzen. Dazu brauchen wir Spring Boot mit Thymeleaf.

Die Datenklassen gehören zum **Model**. Aus ihnen werden die Tabellen in der "Embedded Database" (eingebettete Datenbank) generiert.
Hier ist wichtig zu wissen, das aufgrund der Embedded DB die Daten beim beenden der Applikation verloren gehen. Der Benutzer
der zu Beginn bereits existiert, wird mit Hilfe der data.sql Datei erstellt, welche von Spring Boot automatisch erkennt wird.

Zu der **View** gehören alle HTML und CSS Dateien.

Die Controller, Repository, Service und Security Klassen gehören zum Bereich **Controller**. Mit den Controller Klassen
werden die Daten in das Model eingefügt. Die Repository Klassen sind die Schnittstelle zur Datenbank. Mit den Services können
mit Hilfe der Repository Daten erstellt, gelesen, bearbeitet und gelöscht werden (CRUD).

### Sicherheitsaspekte
Die Klasse Cypher kümmert sich um das hashen von Passwörtern sowie das verschlüsseln und entschlüsseln der Benutzerdaten.
Die Applikation hat keinen Zugriff auf die unverschlüsselten Daten der Nutzer. Das Masterpasswort wird mithilfe der PKDF2-Utility mit dem SHA-1 Algorithmus mit 65536 Iterationen gehasht und anschliessend in die DB gespeichert.
Für jeden User wird ein zufälliger Salt generiert, mit dem die Sicherheit des Hashes noch verstärkt wird. Die Benutzerdaten werden mithilfe der Cipherklasse nach dem AES verschlüsselt. Der Key wird dabei aus dem Masterpasswort hergeleitet, welches dafür erneut gehashed und gesalted wird, wodurch wir für jeden Benutzer einen einzigartigen Key generieren können, welcher sich bei jeder Session ändert und nicht gespeichert wird. Selbst wenn jemand Zugriff auf die Session des Users bekäme, würde er durch den Key nicht auf das Masterpasswort kommen.
Die Daten werden beim login entschlüsselt und beim logout verschlüsselt wieder in die DB geschrieben. Wenn ein Benutzer sich einlogged, etwas ändert und die Seite verlässt, ohne sich auszuloggen

#### Passwort ändern
Wenn der User eingelogged ist, kann er neben dem Logout button auf einen change-password button klicken, woraufhin er auf ein Formular weitergeleitet wird, in dem er sein Passwort ändern kann.

#### Passwort vergessen
Dafür hatten wir leider keine Zeit mehr aber wir hätten das folgendermassen gelöst:
1. Jeder user hat nebst dem Username noch eine Email
2. Wenn man das Passwort vergessen hat, kann man beim Loginscreen auf einen Button klicken, wodurch eine Email mit einem einmaligen, für kurze Zeit validen Link verschickt werden würde mit dessen Hilfe man das Passwort zurücksetzen kann.

### Probleme
Das entschlüsseln der Applikationen gibt noch einen Fehler aus. Die Fehlermeldung ist "javax.crypto.IllegalBlockSizeException: Input length must be multiple of 16 when decrypting with padded cipher" wir hatten leider keine Zeit mehr, diese Exception zu beheben.
Auf dem Master-branch funkioniert die Applikation, jedoch werden die Applikationen nicht verschlüsselt und beim erstellen eines neuen
Passwortes wird dieses nicht validiert.
Auf dem Dev-branch finden sie unsere Applikation inklusive Validierung und Verschlüsselung der Daten, jedoch kann man keine Applications bearbeiten oder löschen.
Auch kann sich der Benutzer nich ausloggen und wieder einloggen.