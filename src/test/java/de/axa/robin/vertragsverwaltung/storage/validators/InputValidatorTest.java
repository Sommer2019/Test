package de.axa.robin.vertragsverwaltung.storage.validators;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class InputValidatorTest {

    private InputValidator inputValidator;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    void setUp() {
        inputValidator = new InputValidator();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @Test
    void testString() {
        assertTrue(inputValidator.string("123"));
        assertFalse(inputValidator.string("abc"));
        assertTrue(inputValidator.string(""));
    }

    @Test
    void testIsStringInJsonFile() {
        String jsonContent = "{\"brands\": [\"BrandA\", \"BrandB\", \"BrandC\"]}";
        InputStream inputStream = new ByteArrayInputStream(jsonContent.getBytes());
        JsonReader jsonReader = Json.createReader(inputStream);
        JsonObject jsonObject = jsonReader.readObject();

        assertTrue(jsonObject.toString().contains("BrandA"));
        assertFalse(jsonObject.toString().contains("BrandX"));
    }

    @BeforeEach
    void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }
}
