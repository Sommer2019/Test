package de.axa.robin.vertragsverwaltung.user_interaction;

import de.axa.robin.vertragsverwaltung.modell.Fahrzeug;
import de.axa.robin.vertragsverwaltung.modell.Partner;
import de.axa.robin.vertragsverwaltung.modell.Vertrag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OutputTest {

    private Output output;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private final Fahrzeug fahrzeug = new Fahrzeug("ABC123", "BMW", "X5", 240, 1234);
    private final Partner partner = new Partner("Max", "Mustermann", 'M', LocalDate.of(1980, 1, 1), "Deutschland", "Musterstraße", "1", 12345, "Musterstadt", "NRW");
    private final Vertrag vertrag1 = new Vertrag(12345, true, 500, LocalDate.of(2023, 1, 1), LocalDate.of(2024, 1, 1), LocalDate.of(2022, 12, 1), fahrzeug, partner);
    private final Vertrag vertrag2 = new Vertrag(67890, false, 1000, LocalDate.of(2024, 1, 1), LocalDate.of(2025, 1, 1), LocalDate.of(2023, 12, 1), fahrzeug, partner);

    @BeforeEach
    void setUp() {
        output = new Output();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @Test
    void testMenu() {
        output.menu();
        assertTrue(outContent.toString().contains("Was soll als nächstes getan werden?"));
    }

    @Test
    void testSum() {
        BigDecimal summe = new BigDecimal("123.45");
        output.sum("Test", summe);
        assertTrue(outContent.toString().contains("Test Summe aller Beiträge im Jahr: 123.45€"));
    }

    @Test
    void testCreate() {
        output.create("TestInput");
        assertTrue(outContent.toString().contains("Geben Sie TestInput ein:"));
    }

    @Test
    void testConfirm() {
        vertrag1.setVersicherungsablauf(LocalDate.now().plusDays(1));
        output.confirm(vertrag1, "bearbeitet");
        assertTrue(outContent.toString().contains("Soll dieser Vertrag wirklich bearbeitet werden?"));
    }

    @Test
    void testSkip() {
        output.skip();
        assertTrue(errContent.toString().contains("Überprüfung überspringen?"));
    }

    @Test
    void testDone() {
        output.done("verarbeitet");
        assertTrue(outContent.toString().contains("Vertrag verarbeitet"));
    }

    @Test
    void testCancel() {
        output.cancel();
        assertTrue(outContent.toString().contains("Abgebrochen"));
    }
    @Test
    void testDruckeVertrag() {
        output.druckeVertrag(vertrag2);
        assertFalse(outContent.toString().contains("Vertrag abgelaufen!"));
    }
    @Test
    void testDruckeVertragAbgelaufen() {
        assertFalse(output.druckeVertrag(vertrag1));
    }

    @Test
    void testDruckeVertrage() {
        List<Vertrag> vertrage = new ArrayList<>();
        vertrage.add(vertrag1);
        vertrage.add(vertrag2);
        output.druckeVertrage(vertrage);
        assertTrue(outContent.toString().contains("Summe aller Beiträge im Jahr: 7000.00€"));
    }


    @BeforeEach
    void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }
}
