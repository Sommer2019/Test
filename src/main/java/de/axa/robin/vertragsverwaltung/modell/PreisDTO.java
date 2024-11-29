package de.axa.robin.vertragsverwaltung.modell;

import de.axa.robin.vertragsverwaltung.config.Setup;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import java.io.FileReader;

public class PreisDTO {
    private double speed;
    private double age;
    private double faktor;
    private final Setup setup = new Setup();

    public PreisDTO() {
        try (JsonReader reader = Json.createReader(new FileReader(setup.getPreisPath()))) {
            JsonObject jsonObject = reader.readObject();
            this.faktor = jsonObject.getJsonNumber("factor").doubleValue();
            this.age = jsonObject.getJsonNumber("factorage").doubleValue();
            this.speed = jsonObject.getJsonNumber("factorspeed").doubleValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getAge() {
        return age;
    }

    public void setAge(double age) {
        this.age = age;
    }

    public double getFaktor() {
        return faktor;
    }

    public void setFaktor(double faktor) {
        this.faktor = faktor;
    }
}
