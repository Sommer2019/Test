package de.axa.robin.vertragsverwaltung.storage.editor;

import de.axa.robin.vertragsverwaltung.modell.Vertrag;
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
    ////Klassen einlesen////
    private final Output output = new Output();
    private final Input input = new Input();
    private final AdressValidator addressAdressValidator = new AdressValidator();

    public void editVertrag(Vertrag vertrag, Vertragsverwaltung vertragsverwaltung) {
        while (true) {
            vertrag.setPreis(vertrag.getMonatlich(), vertrag.getPartner(), vertrag.getFahrzeug(), vertragsverwaltung);
            output.druckeVertrag(vertrag);
            output.editMenu();
            int choice = input.getNumber(Integer.class,"",-1,-1,-1, vertragsverwaltung, false);

            switch (choice) {
                case 1:
                    editAllgemeineDaten(vertrag, vertragsverwaltung);
                    break;
                case 2:
                    editPersonendaten(vertrag, vertragsverwaltung);
                    break;
                case 3:
                    editFahrzeugdaten(vertrag, vertragsverwaltung);
                    break;
                case 4:
                    vertragsverwaltung.vertragLoeschen(vertrag.getVsnr());
                    vertragsverwaltung.vertragAnlegen(vertrag);
                    output.done("erfolgreich aktualisiert.");
                    output.preis(vertrag.getMonatlich(),vertrag.getPreis());
                    return; // Zurück zum Hauptmenü
                default:
                    output.invalidinput();
                    break;
            }
        }
    }

    private void editAllgemeineDaten(Vertrag vertrag, Vertragsverwaltung vertragsverwaltung) {
        boolean rerun = true;
        while (rerun) {
            rerun = false;
            output.editDates();
            int choice = input.getNumber(Integer.class,"",-1,-1,-1, vertragsverwaltung, false);

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
                    char booking= input.getChar(vertrag,"Abbuchung monatlich oder jährlich? (y/m): ");
                    if (booking=='m'){
                        vertrag.setMonatlich(true);
                    }
                    else if (booking=='y'){
                        vertrag.setMonatlich(false);
                    }
                    break;
                default:
                    output.invalidinput();
                    rerun = true;
                    break;
            }
        }
    }

    private void editPersonendaten(Vertrag vertrag, Vertragsverwaltung vertragsverwaltung) {
        boolean rerun = true;
        while (rerun) {
            rerun = false;
            output.editPerson();
            int choice = input.getNumber(Integer.class,"",-1,-1,-1, vertragsverwaltung, false);

            switch (choice) {
                case 1:
                    vertrag.getPartner().setVorname(input.getString("den Vornamen des Partners",null,false,vertragsverwaltung,true,false,false));
                    break;
                case 2:
                    vertrag.getPartner().setNachname(input.getString("den Nachnamen des Partners",null,false,vertragsverwaltung,true,false,false));
                    break;
                case 3:
                    vertrag.getPartner().setGeschlecht(input.getChar(null,"das Geschlecht des Partners"));
                    break;
                case 4:
                    vertrag.getPartner().setGeburtsdatum(input.getDate("das Geburtsdatum", LocalDate.now().minusYears(18), LocalDate.now().minusYears(110)));
                    break;
                case 5:
                    while(true){
                        String land = input.getString("das Land",null,false,vertragsverwaltung,true,true,false);
                        String strasse = input.getString("die Straße",null,false,vertragsverwaltung,false,false,false);
                        String hausnummer = input.getString("die Hausnummer", "[a-zA-Z0-9]+", false, vertragsverwaltung, false, false, false);
                        int plz = input.getNumber(Integer.class,"die PLZ",-1,-1,-1,vertragsverwaltung,false);
                        String stadt = input.getString("die Stadt", null,false,vertragsverwaltung,true,false,false);
                        String bundesland = input.getString("das Bundesland", null,false,vertragsverwaltung,true,false,false);
                        if(addressAdressValidator.validateAddress(strasse, hausnummer, String.valueOf(plz), stadt, bundesland, land)){
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
                    output.invalidinput();
                    rerun = true;
                    break;
            }
        }
    }

    private void editFahrzeugdaten(Vertrag vertrag, Vertragsverwaltung vertragsverwaltung) {
        boolean rerun = true;
        while (rerun) {
            rerun = false;
            output.editFahrzeug();
            int choice = input.getNumber(Integer.class,"",-1,-1,-1, vertragsverwaltung, false);

            switch (choice) {
                case 1:
                    vertrag.getFahrzeug().setAmtlichesKennzeichen(input.getString("das amtliche Kennzeichen", "^[\\p{Lu}]{1,3}-[\\p{Lu}]{1,2}\\d{1,4}$", true, vertragsverwaltung, false, false, false));
                    break;
                case 2:
                    vertrag.getFahrzeug().setHersteller(input.getString("die Marke", null,false,vertragsverwaltung,false,false,true));
                    break;
                case 3:
                    vertrag.getFahrzeug().setTyp(input.getString("den Typ", "^[a-zA-Z0-9\\s-äöüÄÖÜçéèêáàâíìîóòôúùûñÑ]+$", false, vertragsverwaltung, false, false, false));
                    break;
                case 4:
                    vertrag.getFahrzeug().setHoechstgeschwindigkeit(input.getNumber(Integer.class,"die Höchstgeschwindigkeit", 50, 250, -1, vertragsverwaltung, false));
                    break;
                case 5:
                    vertrag.getFahrzeug().setWagnisskennziffer(input.getNumber(Integer.class,"die Wagnisskennziffer", -1, -1, 112, vertragsverwaltung, false));
                    break;
                default:
                    output.invalidinput();
                    rerun = true;
                    break;
            }
        }
    }

    public void editmenu(Vertragsverwaltung vertragsverwaltung) {
        while (true) {
            output.editwhat();
            int choice = input.getNumber(Integer.class,"",-1,-1,-1, vertragsverwaltung, false);
            switch (choice) {
                case 1:
                    int vsnr = input.getNumber(Integer.class,"8-stellige VSNR oder 0 zum abbrechen",-1,-1,-1, vertragsverwaltung, true);
                    if (vsnr != 0) {
                        editVertrag(vertragsverwaltung.getVertrag(vsnr), vertragsverwaltung);
                    }
                    return;
                case 2:
                    recalcprice(vertragsverwaltung);
                    return;
                case 3:
                    return; // Zurück zum Hauptmenü
                default:
                    output.invalidinput();
                    break;
            }
        }
    }

    public void recalcprice(Vertragsverwaltung vertragsverwaltung) {
        output.create("den neuen allgemeinen Faktor");
        double factor = input.getNumber(Double.class,"",-1,-1,-1, vertragsverwaltung, false);
        output.create("den neuen Altersfaktor");
        double factoralter = input.getNumber(Double.class,"",-1,-1,-1, vertragsverwaltung, false);
        output.create("den neuen Geschwindigkeitsfaktor");
        double factorspeed = input.getNumber(Double.class,"",-1,-1,-1, vertragsverwaltung, false);

        JsonObject jsonObject = Json.createObjectBuilder()
                .add("factor", factor)
                .add("factorage", factoralter)
                .add("factorspeed", factorspeed)
                .build();

        try (JsonWriter writer = Json.createWriter(new FileWriter("preiscalc.json"))) {
            writer.writeObject(jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Vertrag> vertrage = vertragsverwaltung.getVertrage();
        BigDecimal summe=BigDecimal.ZERO;
        for (Vertrag v : vertrage) {
            v.setPreis(v.getMonatlich(), v.getPartner(), v.getFahrzeug(), vertragsverwaltung);
            if(!v.getMonatlich()){
                summe = summe.add(BigDecimal.valueOf(v.getPreis()));
            }
            else{
                summe = summe.add(BigDecimal.valueOf(v.getPreis()*12));
            }
        }
        output.newsum(summe);
    }
}

