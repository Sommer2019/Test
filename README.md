# Vertragsverwaltung

## Zusammenfassung
Der Auszubildende entwickelt eine Java-Anwendung die JSON-Dateien einliest, mit denen es möglich ist (einfache)
Versicherungsverträge anzulegen, zu ändern, zu stornieren und eine Preisberechnung für neue Verträge und Änderungen zur Verfügung zu stellen.
Um die Verträge zu speichern wird der Auszubildende sie in einem JSON-File speichern und beim Aufrufen des Vertrages von der Festplatte lesen (keine Datenbank).
Neben Java wird der Auszubildende Maven nutzen. Die Nutzung von Java Bibliotheken außerhalb des JDKs muss mit dem Ausbilder abgesprochen werden und wird so gering gehalten wie möglich (z.B. kein Spring).
Als IDE nutzt der Auszubildende Intellij.

## Ziel
Der Auszubildende soll bei dieser Übung die grundlegende Handhabung von Java erlernen und vertiefen. Im späteren Verlauf seiner Ausbildung soll dieses Projekt mit Java Bibliotheken (Spring, JPA) und Datenbanken, Funktionen und einem Front-End erweitert werden.

# Aufgabenteile

## Funktionen
folgende Funktionen sollen über die Konsole möglich sein: 
- GET Verträge → gibt alle gespeicherten Verträge zurück
- GET Vertrag → gibt einen bestimmten Vertrag (nach Angabe von VSNR) zurück
- CREATE Vertrag → erstellt einen neuen Vertrag
- EDIT Vertrag → bearbeitet einen Vertrag
- DELETE Vertrag → löscht einen Vertrag nach Angabe der VSNR

## Vertragsdaten
die zu speichernden Verträge enthalten folgende Daten:
- VSNR: nummerisch, 8 Stellen
- Preis
- Versicherungsbeginn
- Versicherungsablauf
- Antragsdatum
### Fahrzeugdaten
- amtliches Kennzeichen
- Fahrzeughersteller (z.B. BMW, Mercedes)
- Fahrzeugtyp (z.B. GLA, Focus, X1)
- Fahrzeughöchstgeschwindigkeit: Bis maximal 250kmh
- Wagnisskennziffer: Es wird **nur** WKZ 112 zugelassen. Alle anderen sollen auf Fehler laufen.
### Partnerdaten (Partner ist der Versicherungsnehmer)
#### Der Versicherungsnehmer ist mit dem Fahrzeughalter identisch. Es gibt immer nur einen Partner pro Vertrag!
- Vorname
- Nachname
- Geschlecht
- Adresse (Land, Bundesland, Stadt, Straße, Hausnummer, PLZ): andere Länder außer Deutschland laufen auf Fehlermeldung
- Geburtsdatum

## Preisberechnung
- Der Preis eines Vertrags berechnet sich aus dem Geburtsdatum des Halters und der Höchstgeschwindigkeit des Fahrzeugs
