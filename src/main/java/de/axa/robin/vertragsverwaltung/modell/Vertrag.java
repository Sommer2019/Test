package de.axa.robin.vertragsverwaltung.modell;

import de.axa.robin.vertragsverwaltung.storage.editor.Create;

import java.time.LocalDate;

public class Vertrag {
    private final Create create = new Create();
    private int vsnr;
    private double preis;
    boolean monatlich;
    private LocalDate versicherungsbeginn;
    private LocalDate versicherungsablauf;
    private LocalDate antragsDatum;
    private Fahrzeug fahrzeug;
    private Partner partner;

    // Konstruktor
    public Vertrag(int vsnr, boolean monatlich, double preis, LocalDate versicherungsbeginn, LocalDate versicherungsablauf, LocalDate antragsDatum, Fahrzeug fahrzeug, Partner partner) {
        this.vsnr = vsnr;
        this.preis = preis;
        this.monatlich = monatlich;
        this.versicherungsbeginn = versicherungsbeginn;
        this.versicherungsablauf = versicherungsablauf;
        this.antragsDatum = antragsDatum;
        this.fahrzeug = fahrzeug;
        this.partner = partner;
    }

    // Getter und Setter
    public int getVsnr() {
        return vsnr;
    }

    public void setVsnr(int vsnr) {
        this.vsnr = vsnr;
    }

    public double getPreis() {
        return preis;
    }

    public void setPreis(boolean monatlich, Partner partner, Fahrzeug fahrzeug) {
        this.preis = create.createPreis(monatlich, partner, fahrzeug);
    }

    public boolean getMonatlich() {
        return monatlich;
    }

    public void setMonatlich(boolean monatlich) {
        this.monatlich = monatlich;
    }

    public LocalDate getVersicherungsbeginn() {
        return versicherungsbeginn;
    }

    public void setVersicherungsbeginn(LocalDate versicherungsbeginn) {
        this.versicherungsbeginn = versicherungsbeginn;
    }

    public LocalDate getVersicherungsablauf() {
        return versicherungsablauf;
    }

    public void setVersicherungsablauf(LocalDate versicherungsablauf) {
        this.versicherungsablauf = versicherungsablauf;
    }

    public LocalDate getAntragsDatum() {
        return antragsDatum;
    }

    public void setAntragsDatum(LocalDate antragsDatum) {
        this.antragsDatum = antragsDatum;
    }

    public Fahrzeug getFahrzeug() {
        return fahrzeug;
    }

    public void setFahrzeug(Fahrzeug fahrzeug) {
        this.fahrzeug = fahrzeug;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    @Override
    public String toString() {
        return "Vertragsdaten: " +
                "\n\tVertragsnummer: " + vsnr +
                "\n\tPreis: " + preis + "€" +
                "\n\tAbrechnungszeitraum monatlich: " + monatlich +
                "\n\tVersicherungsbeginn: " + versicherungsbeginn +
                "\n\tVersicherungsablauf: " + versicherungsablauf +
                "\n\tAntragsdatum: " + antragsDatum +
                fahrzeug +
                partner;
    }
}


