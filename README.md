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