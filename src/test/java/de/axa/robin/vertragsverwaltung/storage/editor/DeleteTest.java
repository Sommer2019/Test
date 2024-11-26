package de.axa.robin.vertragsverwaltung.storage.editor;

import de.axa.robin.vertragsverwaltung.modell.Fahrzeug;
import de.axa.robin.vertragsverwaltung.modell.Partner;
import de.axa.robin.vertragsverwaltung.modell.Vertrag;
import de.axa.robin.vertragsverwaltung.storage.Vertragsverwaltung;
import de.axa.robin.vertragsverwaltung.user_interaction.Input;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

class DeleteTest {

    private Delete delete;
    private Input input;
    private Vertragsverwaltung vertragsverwaltung;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private final Fahrzeug fahrzeug = new Fahrzeug("ABC123", "BMW", "X5", 240, 1234);
    private final Partner partner = new Partner("Max", "Mustermann", 'M', LocalDate.of(1980, 1, 1), "Deutschland", "Musterstraße", "1", 12345, "Musterstadt", "NRW");
    private final Vertrag vertrag = new Vertrag(12345, true, 299.99, LocalDate.of(2023, 1, 1), LocalDate.of(2024, 1, 1), LocalDate.of(2022, 12, 1), fahrzeug, partner);

    @BeforeEach
    void setUp() {
        input = Mockito.mock(Input.class);
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
        delete = new Delete(input);
        vertragsverwaltung = new Vertragsverwaltung();
    }

    @Test
    void testDeleteVertrag() {
        // Create a test contract
        vertrag.setVsnr(12345678);
        vertragsverwaltung.vertragAnlegen(vertrag);

        // Simulate user input for confirmation
        given(input.getChar(vertragsverwaltung.getVertrag(12345678), Delete.DEL)).willReturn('y');
        // Perform delete operation
        delete.delete(12345678);

        // Verify the contract is deleted
        assertFalse(vertragsverwaltung.vertragExistiert(12345678));
        assertTrue(outContent.toString().contains("Vertrag erfolgreich gelöscht."));
    }

    @Test
    void testDeleteVertragCancelled() {
        // Create a test contract
        vertrag.setVsnr(12345678);
        vertragsverwaltung.vertragAnlegen(vertrag);
        // Simulate user input for cancellation
        given(input.getChar(vertragsverwaltung.getVertrag(12345678), Delete.DEL)).willReturn('n');
        // Perform delete operation
        delete.delete(12345678);

        // Verify the contract is not deleted
        assertTrue(vertragsverwaltung.vertragExistiert(12345678));
        assertTrue(outContent.toString().contains("Abgebrochen"));
    }

    @Test
    void testDeleteVertragInvalidInput() {
        // Create a test contract
        vertrag.setVsnr(12345678);
        vertragsverwaltung.vertragAnlegen(vertrag);
        // Simulate user input for invalid input
        given(input.getChar(vertragsverwaltung.getVertrag(12345678), Delete.DEL)).willReturn('x');
        // Perform delete operation
        delete.delete(12345678);

        // Verify the contract is not deleted and error message is shown
        assertTrue(vertragsverwaltung.vertragExistiert(12345678));
        assertTrue(errContent.toString().contains("Error: Ungültige Eingabe!"));
    }

    @BeforeEach
    void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }
}
