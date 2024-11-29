package de.axa.robin.vertragsverwaltung.storage.editor;

import de.axa.robin.vertragsverwaltung.modell.Vertrag;
import de.axa.robin.vertragsverwaltung.config.Setup;
import de.axa.robin.vertragsverwaltung.storage.validators.AdressValidator;
import de.axa.robin.vertragsverwaltung.storage.Vertragsverwaltung;
import de.axa.robin.vertragsverwaltung.user_interaction.Input;
import de.axa.robin.vertragsverwaltung.user_interaction.Output;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Edit {
    /// /Klassen einlesen////
    private final Output output;
    private final Input input;
    private final Setup setup = new Setup();
    private final Vertragsverwaltung vertragsverwaltung;
    private final AdressValidator addressAdressValidator = new AdressValidator();
    public Edit(Input input, Vertragsverwaltung vertragsverwaltung, Output output) {
        this.input = input;
        this.vertragsverwaltung = vertragsverwaltung;
        this.output = output;
    }

    public void editVertrag(Vertrag vertrag) {
        while (true) {
            vertrag.setPreis(vertrag.getMonatlich(), vertrag.getPartner(), vertrag.getFahrzeug());
            output.druckeVertrag(vertrag);
            output.editMenu();
            int choice = input.getNumber(Integer.class, "", -1, -1, -1, false);

            switch (choice) {
                case 1:
                    editAllgemeineDaten(vertrag);
                    break;
                case 2:
                    editPersonendaten(vertrag);
                    break;
                case 3:
                    editFahrzeugdaten(vertrag);
                    break;
                case 4:
                    vertragsverwaltung.vertragLoeschen(vertrag.getVsnr());
                    vertragsverwaltung.vertragAnlegen(vertrag);
                    output.done("erfolgreich aktualisiert.");
                    output.preis(vertrag.getMonatlich(), vertrag.getPreis());
                    return; // Zurück zum Hauptmenü
                default:
                    output.error("Ungültige Eingabe!");
                    break;
            }
        }
    }

    void editAllgemeineDaten(Vertrag vertrag) {
        boolean rerun = true;
        while (rerun) {
            rerun = false;
            output.editDates();
            int choice = input.getNumber(Integer.class, "", -1, -1, -1, false);

            switch (choice) {
                case 1:
                    vertrag.setVersicherungsbeginn(input.getDate("den Versicherungsbeginn", vertrag.getAntragsDatum(), vertrag.getVersicherungsablauf()));
                    break;
                case 2:
                    vertrag.setVersicherungsablauf(input.getDate("das neue Versicherungsende", vertrag.getVersicherungsbeginn(), null));
                    break;
                case 3:
                    vertrag.setAntragsDatum(input.getDate("das Antragsdatum", null, vertrag.getVersicherungsbeginn()));
                    break;
                case 4:
                    char booking = input.getChar(vertrag, "Abbuchung monatlich oder jährlich? (y/m): ");
                    if (booking == 'm') {
                        vertrag.setMonatlich(true);
                    } else if (booking == 'y') {
                        vertrag.setMonatlich(false);
                    }
                    break;
                default:
                    output.error("Ungültige Eingabe!");
                    rerun = true;
                    break;
            }
        }
    }

    void editPersonendaten(Vertrag vertrag) {
        boolean rerun = true;
        while (rerun) {
            rerun = false;
            output.editPerson();
            int choice = input.getNumber(Integer.class, "", -1, -1, -1, false);

            switch (choice) {
                case 1:
                    vertrag.getPartner().setVorname(input.getString("den Vornamen des Partners", "^[a-zA-Z0-9\\s-äöüÄÖÜçéèêáàâíìîóòôúùûñÑ'-]+$", false, false, false, false));
                    break;
                case 2:
                    vertrag.getPartner().setNachname(input.getString("den Nachnamen des Partners", "^[a-zA-Z0-9\\s-äöüÄÖÜçéèêáàâíìîóòôúùûñÑ'-]+$", false, false, false, false));
                    break;
                case 3:
                    vertrag.getPartner().setGeschlecht(input.getChar(null, "das Geschlecht des Partners"));
                    break;
                case 4:
                    vertrag.getPartner().setGeburtsdatum(input.getDate("das Geburtsdatum", LocalDate.now().minusYears(18), LocalDate.now().minusYears(110)));
                    break;
                case 5:
                    while (true) {
                        String land = input.getString("das Land", ".*", false, true, true, false);
                        String strasse = input.getString("die Straße", ".*", false, false, false, false);
                        String hausnummer = input.getString("die Hausnummer", "[a-zA-Z0-9]+", false, false, false, false);
                        int plz = input.getNumber(Integer.class, "die PLZ", -1, -1, -1, false);
                        String stadt = input.getString("die Stadt", ".*", false, true, false, false);
                        String bundesland = input.getString("das Bundesland", ".*", false, true, false, false);
                        if (addressAdressValidator.validateAddress(strasse, hausnummer, String.valueOf(plz), stadt, bundesland, land)) {
                            vertrag.getPartner().setLand(land);
                            vertrag.getPartner().setStrasse(strasse);
                            vertrag.getPartner().setHausnummer(hausnummer);
                            vertrag.getPartner().setPlz(plz);
                            vertrag.getPartner().setStadt(stadt);
                            vertrag.getPartner().setBundesland(bundesland);
                            break;
                        }
                    }
                    break;
                default:
                    output.error("Ungültige Eingabe!");
                    rerun = true;
                    break;
            }
        }
    }

    void editFahrzeugdaten(Vertrag vertrag) {
        boolean rerun = true;
        while (rerun) {
            rerun = false;
            output.editFahrzeug();
            int choice = input.getNumber(Integer.class, "", -1, -1, -1, false);

            switch (choice) {
                case 1:
                    vertrag.getFahrzeug().setAmtlichesKennzeichen(input.getString("das amtliche Kennzeichen", "^[\\p{Lu}]{1,3}-[\\p{Lu}]{1,2}\\d{1,4}[EH]?$", true, false, false, false));
                    break;
                case 2:
                    vertrag.getFahrzeug().setHersteller(input.getString("die Marke", ".*", false, false, false, true));
                    break;
                case 3:
                    vertrag.getFahrzeug().setTyp(input.getString("den Typ", "^[a-zA-Z0-9\\s-äöüÄÖÜçéèêáàâíìîóòôúùûñÑ]+$", false, false, false, false));
                    break;
                case 4:
                    vertrag.getFahrzeug().setHoechstgeschwindigkeit(input.getNumber(Integer.class, "die Höchstgeschwindigkeit", 50, 250, -1, false));
                    break;
                case 5:
                    vertrag.getFahrzeug().setWagnisskennziffer(input.getNumber(Integer.class, "die Wagnisskennziffer", -1, -1, 112, false));
                    break;
                default:
                    output.error("Ungültige Eingabe!");
                    rerun = true;
                    break;
            }
        }
    }

    public void editmenu(List<Vertrag> vertrage) {
        while (true) {
            output.editwhat();
            int choice = input.getNumber(Integer.class, "", -1, -1, -1, false);
            switch (choice) {
                case 1:
                    int vsnr = input.getNumber(Integer.class, "8-stellige VSNR oder 0 zum abbrechen", -1, -1, -1, true);
                    if (vsnr != 0) {
                        if (vertragsverwaltung.getVertrag(vsnr) == null) return;
                        editVertrag(vertragsverwaltung.getVertrag(vsnr));
                    }
                    return;
                case 2:
                    recalcprice(vertrage);
                    return;
                case 3:
                    return; // Zurück zum Hauptmenü
                default:
                    output.error("Ungültige Eingabe!");
                    break;
            }
        }
    }

    public void recalcprice(List<Vertrag> vertrage) {
        output.create("den neuen allgemeinen Faktor");
        double factor = input.getNumber(Double.class, "", -1, -1, -1, false);
        output.create("den neuen Altersfaktor");
        double factoralter = input.getNumber(Double.class, "", -1, -1, -1, false);
        output.create("den neuen Geschwindigkeitsfaktor");
        double factorspeed = input.getNumber(Double.class, "", -1, -1, -1, false);
        recalcpricerun(factor,factoralter,factorspeed,vertrage);
    }
    public BigDecimal recalcpricerun(double factor, double factoralter, double factorspeed, List<Vertrag> vertrage) {
        JsonObject jsonObject = Json.createObjectBuilder()
                .add("factor", factor)
                .add("factorage", factoralter)
                .add("factorspeed", factorspeed)
                .build();

        try (JsonWriter writer = Json.createWriter(new FileWriter(setup.getPreisPath()))) {
            writer.writeObject(jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BigDecimal summe = BigDecimal.ZERO;
        for (Vertrag v : vertrage) {
            v.setPreis(v.getMonatlich(), v.getPartner(), v.getFahrzeug());
            if (!v.getMonatlich()) {
                summe = summe.add(BigDecimal.valueOf(v.getPreis()));
            } else {
                summe = summe.add(BigDecimal.valueOf(v.getPreis() * 12));
            }
        }
        output.sum("Neue", summe);
        return summe;
    }
}