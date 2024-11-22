package de.axa.robin.vertragsverwaltung.modell;

public class Fahrzeug {
    private String amtlichesKennzeichen;
    private String hersteller;
    private String typ;
    private int hoechstgeschwindigkeit;
    private int wagnisskennziffer;

    // Konstruktor
    public Fahrzeug(String amtlichesKennzeichen, String hersteller, String typ, int hoechstgeschwindigkeit, int wagnisskennziffer) {
        this.amtlichesKennzeichen = amtlichesKennzeichen;
        this.hersteller = hersteller;
        this.typ = typ;
        this.hoechstgeschwindigkeit = hoechstgeschwindigkeit;
        this.wagnisskennziffer = wagnisskennziffer;
    }

    // Getter und Setter
    public String getAmtlichesKennzeichen() {
        return amtlichesKennzeichen;
    }

    public void setAmtlichesKennzeichen(String amtlichesKennzeichen) {
        this.amtlichesKennzeichen = amtlichesKennzeichen;
    }

    public String getHersteller() {
        return hersteller;
    }

    public void setHersteller(String hersteller) {
        this.hersteller = hersteller;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public int getHoechstgeschwindigkeit() {
        return hoechstgeschwindigkeit;
    }

    public void setHoechstgeschwindigkeit(int hoechstgeschwindigkeit) {
        this.hoechstgeschwindigkeit = hoechstgeschwindigkeit;
    }

    public int getWagnisskennziffer() {
        return wagnisskennziffer;
    }

    public void setWagnisskennziffer(int wagnisskennziffer) {
        this.wagnisskennziffer = wagnisskennziffer;
    }

    @Override
    public String toString() {
        return "\nFahrzeug: " +
                "\n\tAmtliches Kennzeichen: " + amtlichesKennzeichen +
                "\n\tHersteller: " + hersteller +
                "\n\tTyp: " + typ +
                "\n\tHöchstgeschwindigkeit: " + hoechstgeschwindigkeit +
                "\n\tWagnisskennziffer: " + wagnisskennziffer;
    }
}

