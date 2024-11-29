package de.axa.robin.vertragsverwaltung.storage.validators;

import de.axa.robin.vertragsverwaltung.config.Setup;
import de.axa.robin.vertragsverwaltung.user_interaction.Output;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class InputValidator {
    private final Output output = new Output();
    private final Setup setup = new Setup();

    public boolean string(String input) {
        for (char c : input.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return input.isEmpty();
    }

    public boolean isStringInJsonFile(String searchString) {
        String filePath = setup.getBrandsPath();
        try (InputStream fis = new FileInputStream(filePath);
             JsonReader jsonReader = Json.createReader(fis)) {
            JsonObject jsonObject = jsonReader.readObject();
            return jsonObject.toString().contains(searchString);
        } catch (FileNotFoundException e) {
            output.error("File not found: " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
