package de.axa.robin.vertragsverwaltung.storage;

import de.axa.robin.vertragsverwaltung.config.Setup;
import de.axa.robin.vertragsverwaltung.modell.Fahrzeug;
import de.axa.robin.vertragsverwaltung.modell.Partner;
import de.axa.robin.vertragsverwaltung.modell.Vertrag;
import jakarta.json.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Repository {
    private final Logger logger = Logger.getLogger(Repository.class.getName());
    private final Setup setup;

    public Repository(Setup setup) {
        this.setup = new Setup();
    }

    public void speichereVertrage(List<Vertrag> vertrage) {
        try (FileWriter file = new FileWriter(setup.getRepositoryPath(), false)) {
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            for (Vertrag v : vertrage) {
                arrayBuilder.add(Json.createObjectBuilder()
                        .add("vsnr", v.getVsnr())
                        .add("abrechnungszeitraum monatlich", v.getMonatlich())
                        .add("preis", v.getPreis())
                        .add("versicherungsbeginn", v.getVersicherungsbeginn().toString())
                        .add("versicherungsablauf", v.getVersicherungsablauf().toString())
                        .add("antragsDatum", v.getAntragsDatum().toString())
                        .add("fahrzeug", Json.createObjectBuilder()
                                .add("amtlichesKennzeichen", v.getFahrzeug().getAmtlichesKennzeichen())
                                .add("hersteller", v.getFahrzeug().getHersteller())
                                .add("typ", v.getFahrzeug().getTyp())
                                .add("hoechstgeschwindigkeit", v.getFahrzeug().getHoechstgeschwindigkeit())
                                .add("wagnisskennziffer", v.getFahrzeug().getWagnisskennziffer()))
                        .add("partner", Json.createObjectBuilder()
                                .add("vorname", v.getPartner().getVorname())
                                .add("nachname", v.getPartner().getNachname())
                                .add("geschlecht", Character.toString(v.getPartner().getGeschlecht()))
                                .add("geburtsdatum", v.getPartner().getGeburtsdatum().toString())
                                .add("land", v.getPartner().getLand())
                                .add("strasse", v.getPartner().getStrasse())
                                .add("hausnummer", v.getPartner().getHausnummer())
                                .add("plz", v.getPartner().getPlz())
                                .add("stadt", v.getPartner().getStadt())
                                .add("bundesland", v.getPartner().getBundesland())
                        ));
            }
            JsonArray jsonArray = arrayBuilder.build();
            JsonWriter writer = Json.createWriter(file);
            writer.writeArray(jsonArray);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Fehler beim speichern", e);
        }
    }

    public List<Vertrag> ladeVertrage() {
        List<Vertrag> vertrage = new ArrayList<>();
        try (FileReader file = new FileReader(setup.getRepositoryPath())) {
            JsonReader reader = Json.createReader(file);
            JsonArray jsonArray = reader.readArray();
            for (JsonValue jsonValue : jsonArray) {
                JsonObject jsonObject = jsonValue.asJsonObject();
                Vertrag vertrag = new Vertrag(
                        jsonObject.getInt("vsnr"),
                        jsonObject.getBoolean("abrechnungszeitraum monatlich"),
                        jsonObject.getJsonNumber("preis").doubleValue(),
                        LocalDate.parse(jsonObject.getString("versicherungsbeginn")),
                        LocalDate.parse(jsonObject.getString("versicherungsablauf")),
                        LocalDate.parse(jsonObject.getString("antragsDatum")),
                        new Fahrzeug(
                                jsonObject.getJsonObject("fahrzeug").getString("amtlichesKennzeichen"),
                                jsonObject.getJsonObject("fahrzeug").getString("hersteller"),
                                jsonObject.getJsonObject("fahrzeug").getString("typ"),
                                jsonObject.getJsonObject("fahrzeug").getInt("hoechstgeschwindigkeit"),
                                jsonObject.getJsonObject("fahrzeug").getInt("wagnisskennziffer")
                        ),
                        new Partner(
                                jsonObject.getJsonObject("partner").getString("vorname"),
                                jsonObject.getJsonObject("partner").getString("nachname"),
                                jsonObject.getJsonObject("partner").getString("geschlecht").charAt(0),
                                LocalDate.parse(jsonObject.getJsonObject("partner").getString("geburtsdatum")),
                                jsonObject.getJsonObject("partner").getString("land"),
                                jsonObject.getJsonObject("partner").getString("strasse"),
                                jsonObject.getJsonObject("partner").getString("hausnummer"),
                                jsonObject.getJsonObject("partner").getInt("plz"),
                                jsonObject.getJsonObject("partner").getString("stadt"),
                                jsonObject.getJsonObject("partner").getString("bundesland")
                        )
                );
                vertrage.add(vertrag);
            }
        } catch (FileNotFoundException e) {
            logger.log(Level.WARNING, "Datei 'vertrage.json' nicht gefunden", e);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Fehler beim laden", e);
        }
        return vertrage;
    }
}
