package de.axa.robin.vertragsverwaltung.user_interaction;

import de.axa.robin.vertragsverwaltung.modell.Vertrag;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

public class Output {
    public void menu() {
        System.out.println("Was soll als nächstes getan werden? ");
        System.out.println("1 -- alle Verträge anzeigen");
        System.out.println("2 -- bestimmten Vertrag anzeigen");
        System.out.println("3 -- Vertrag erstellen");
        System.out.println("4 -- Bearbeiten");
        System.out.println("5 -- Vertrag löschen");
        System.out.println("6 -- beenden");
    }

    public void sum(String string, BigDecimal summe) {
        System.out.println(string + " Summe aller Beiträge im Jahr: " + summe.setScale(2, RoundingMode.HALF_DOWN) + "€");
        System.out.println("-------------------------------");
    }

    public void create(String input) {
        System.out.println("Geben Sie " + input + " ein:");
    }

    public void confirm(Vertrag v, String handle) {
        druckeVertrag(v);
        System.out.println("Soll dieser Vertrag wirklich " + handle + " werden? (y/n): ");
    }

    public void skip() {
        System.err.println("Überprüfung überspringen? (y/n): ");
    }

    public void done(String verarbeitet) {
        System.out.println("Vertrag " + verarbeitet);
    }

    public void cancel() {
        System.out.println("Abgebrochen");
    }

    public boolean druckeVertrag(Vertrag v) {
        System.out.println(v.toString());
        if (v.getVersicherungsablauf().isBefore(LocalDate.now())) {
            System.err.println("Vertrag abgelaufen!");
            return false;
        }
        System.out.println("-------------------------------");
        return true;
    }

    public void druckeVertrage(List<Vertrag> vertrage) {
        BigDecimal summe = BigDecimal.ZERO;
        for (Vertrag v : vertrage) {
            druckeVertrag(v);
            if (!v.getMonatlich()) {
                summe = summe.add(BigDecimal.valueOf(v.getPreis()));
            } else {
                summe = summe.add(BigDecimal.valueOf(v.getPreis() * 12));
            }
        }
        sum("", summe);
    }

    public void preisMJ() {
        System.out.println("Abbuchung monatlich oder jährlich? (y/m): ");
    }

    public void preis(boolean monatlich, double preis) {
        System.out.print("Berechneter Preis: " + preis + "€");
        if (monatlich) {
            System.out.print(" im Monat.");
        } else {
            System.out.print(" im Jahr.");
        }
        System.out.println();
    }

    public void editwhat() {
        System.out.println("Was möchten Sie bearbeiten?");
        System.out.println("1. Bestimmten Vertrag");
        System.out.println("2. Preisneuberechnung (alle Verträge)");
        System.out.println("3. Abbrechen");
    }

    public void editMenu() {
        System.out.println("Was möchten Sie bearbeiten?");
        System.out.println("1. Allgemeine Daten");
        System.out.println("2. Personendaten");
        System.out.println("3. Fahrzeugdaten");
        System.out.println("4. Bearbeitung abschließen");
    }

    public void editDates() {
        System.out.println("Welche allgemeinen Daten möchten Sie bearbeiten?");
        System.out.println("1. Versicherungsbeginn");
        System.out.println("2. Versicherungsablauf");
        System.out.println("3. Antragsdatum");
        System.out.println("4. Abrechnungszeitraum");
    }

    public void inputDate(String datename) {
        System.out.println("Geben Sie " + datename + " ein (YYYY-MM-DD):");
    }

    public void editPerson() {
        System.out.println("Welche Personendaten möchten Sie bearbeiten?");
        System.out.println("1. Vorname");
        System.out.println("2. Nachname");
        System.out.println("3. Geschlecht");
        System.out.println("4. Geburtsdatum");
        System.out.println("5. Adresse");
    }

    public void editFahrzeug() {
        System.out.println("Welche Fahrzeugdaten möchten Sie bearbeiten?");
        System.out.println("1. Kennzeichen");
        System.out.println("2. Marke");
        System.out.println("3. Typ");
        System.out.println("4. Höchstgeschwindigkeit");
        System.out.println("5. Wagnisskennziffer");
    }

    public void error(String message) {
        System.err.println("Error: " + message);
    }
}
