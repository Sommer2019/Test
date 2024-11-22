package de.axa.robin.vertragsverwaltung.storage.editor;

import de.axa.robin.vertragsverwaltung.modell.Fahrzeug;
import de.axa.robin.vertragsverwaltung.modell.Partner;
import de.axa.robin.vertragsverwaltung.modell.Vertrag;
import de.axa.robin.vertragsverwaltung.storage.Setup;
import de.axa.robin.vertragsverwaltung.storage.validators.AdressValidator;
import de.axa.robin.vertragsverwaltung.storage.Vertragsverwaltung;
import de.axa.robin.vertragsverwaltung.user_interaction.Input;
import de.axa.robin.vertragsverwaltung.user_interaction.Output;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import java.io.FileReader;
import java.time.LocalDate;

public class Create {
    ////Klassen einlesen////
    private final Output output;
    private final Input input;
    private final Setup setup = new Setup();
    private final AdressValidator addressAdressValidator = new AdressValidator();
    private final Vertragsverwaltung vertragsverwaltung;

    public Create(Input input, Vertragsverwaltung vertragsverwaltung, Output output) {
        this.input = input;
        this.vertragsverwaltung = vertragsverwaltung;
        this.output = output;
    }

    public void createVertrag() {
        Fahrzeug fahrzeug = createFahrzeug();
        Partner partner = createPartner();
        boolean monatlich = false;
        char booking = input.getChar(null, "Abbuchung monatlich oder jährlich? (y/m): ");
        if (booking == 'm') {
            monatlich = true;
        }
        double preis = createPreis(monatlich, partner, fahrzeug);
        output.preis(monatlich, preis);
        LocalDate beginn = input.getDate("den Versicherungsbeginn", LocalDate.now(), null);

        Vertrag vertrag = new Vertrag(
                createvsnr(),
                monatlich,
                preis,
                beginn,
                beginn.plusYears(1),
                LocalDate.now(),
                fahrzeug,
                partner
        );

        char confirm = input.getChar(vertrag, "erstellt");
        if (confirm == 'y' || confirm == 'Y') {
            vertragsverwaltung.vertragAnlegen(vertrag);
            output.done("erfolgreich erstellt.");
        } else {
            output.done("wurde nicht erstellt.");
        }
    }

    public Fahrzeug createFahrzeug() {
        return new Fahrzeug(
                input.getString("das amtliche Kennzeichen", "^[\\p{Lu}]{1,3}-[\\p{Lu}]{1,2}\\d{1,4}[EH]?$", true, false, false, false),
                input.getString("die Marke", ".*", false, true, false, true),
                input.getString("den Typ", "^[a-zA-Z0-9\\s-äöüÄÖÜçéèêáàâíìîóòôúùûñÑ]+$", false, false, false, false),
                input.getNumber(Integer.class, "die Höchstgeschwindigkeit", 50, 250, -1, false),
                input.getNumber(Integer.class, "die Wagnisskennziffer", -1, -1, 112, false)
        );
    }

    public Partner createPartner() {
        String vorname = input.getString("den Vornamen des Partners", "^[a-zA-Z0-9\\s-äöüÄÖÜçéèêáàâíìîóòôúùûñÑ'-]+$", false, false, false, false);
        String nachname = input.getString("den Nachnamen des Partners", "^[a-zA-Z0-9\\s-äöüÄÖÜçéèêáàâíìîóòôúùûñÑ'-]+$", false, false, false, false);
        char geschlecht = input.getChar(null, "das Geschlecht des Partners");
        LocalDate geburtsdatum = input.getDate("das Geburtsdatum", LocalDate.now().minusYears(110), LocalDate.now().minusYears(18));
        String land;
        String strasse;
        String hausnummer;
        int plz;
        String stadt;
        String bundesland;

        do {
            land = input.getString("das Land", ".*", false, true, true, false);
            strasse = input.getString("die Straße", ".*", false, false, false, false);
            hausnummer = input.getString("die Hausnummer", "[a-zA-Z0-9]+", false, false, false, false);
            plz = input.getNumber(Integer.class, "die PLZ", -1, -1, -1, false);
            stadt = input.getString("die Stadt", ".*", false, true, false, false);
            bundesland = input.getString("das Bundesland", ".*", false, true, false, false);
        } while (!addressAdressValidator.validateAddress(strasse, hausnummer, String.valueOf(plz), stadt, bundesland, land));
        return new Partner(vorname, nachname, geschlecht, geburtsdatum,
                land, strasse, hausnummer, plz, stadt, bundesland);
    }

    public int createvsnr() {
        int vsnr = 10000000;
        while (vertragsverwaltung.getVertrag(vsnr) != null) {
            vsnr++;
        }
        if (vsnr > 99999999) {
            output.error("Keine freien Versicherungsnummern mehr!");
            System.exit(1);
        }
        return vsnr;
    }

    public double createPreis(boolean monatlich, Partner partner, Fahrzeug fahrzeug) {
        double preis = 0;
        double factor = 1.5;
        double factoralter = 0.1;
        double factorspeed = 0.4;
        int alter = LocalDate.now().getYear() - partner.getGeburtsdatum().getYear();
        try (JsonReader reader = Json.createReader(new FileReader(setup.getPreisPath()))) {
            JsonObject jsonObject = reader.readObject();
            factor = jsonObject.getJsonNumber("factor").doubleValue();
            factoralter = jsonObject.getJsonNumber("factorage").doubleValue();
            factorspeed = jsonObject.getJsonNumber("factorspeed").doubleValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            preis = (alter * factoralter + fahrzeug.getHoechstgeschwindigkeit() * factorspeed) * factor;
            if (!monatlich) {
                preis = preis * 11;
            }
        } catch (Exception e) {
            output.error("Ungültige Eingabe!");
        }
        return Math.round(preis * 100.0) / 100.0;
    }
}
