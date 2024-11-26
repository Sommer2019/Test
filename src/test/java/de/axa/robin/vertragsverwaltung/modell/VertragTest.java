package de.axa.robin.vertragsverwaltung.modell;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class VertragTest {

    private Vertrag vertrag;
    private Fahrzeug fahrzeug;
    private Partner partner;

    @BeforeEach
    void setUp() {
        fahrzeug = new Fahrzeug("ABC-123", "TestBrand", "TestType", 100, 112);
        partner = new Partner("John", "Doe", 'm', LocalDate.of(1980, 1, 1), "Deutschland", "TestStreet", "11", 51469, "TestCity", "TestState");
        vertrag = new Vertrag(12345678, true, 100.0, LocalDate.now(), LocalDate.now().plusYears(1), LocalDate.now(), fahrzeug, partner);
    }

    @Test
    void testGetVsnr() {
        assertEquals(12345678, vertrag.getVsnr());
    }

    @Test
    void testSetVsnr() {
        vertrag.setVsnr(87654321);
        assertEquals(87654321, vertrag.getVsnr());
    }

    @Test
    void testGetPreis() {
        assertEquals(100.0, vertrag.getPreis());
    }

    @Test
    void testSetPreis() {
        vertrag.setPreis(true, partner, fahrzeug);
        assertTrue(vertrag.getPreis() > 0);
    }

    @Test
    void testGetMonatlich() {
        assertTrue(vertrag.getMonatlich());
    }

    @Test
    void testSetMonatlich() {
        vertrag.setMonatlich(false);
        assertFalse(vertrag.getMonatlich());
    }

    @Test
    void testGetVersicherungsbeginn() {
        assertEquals(LocalDate.now(), vertrag.getVersicherungsbeginn());
    }

    @Test
    void testSetVersicherungsbeginn() {
        LocalDate newDate = LocalDate.now().plusDays(1);
        vertrag.setVersicherungsbeginn(newDate);
        assertEquals(newDate, vertrag.getVersicherungsbeginn());
    }

    @Test
    void testGetVersicherungsablauf() {
        assertEquals(LocalDate.now().plusYears(1), vertrag.getVersicherungsablauf());
    }

    @Test
    void testSetVersicherungsablauf() {
        LocalDate newDate = LocalDate.now().plusYears(2);
        vertrag.setVersicherungsablauf(newDate);
        assertEquals(newDate, vertrag.getVersicherungsablauf());
    }

    @Test
    void testGetAntragsDatum() {
        assertEquals(LocalDate.now(), vertrag.getAntragsDatum());
    }

    @Test
    void testSetAntragsDatum() {
        LocalDate newDate = LocalDate.now().minusDays(1);
        vertrag.setAntragsDatum(newDate);
        assertEquals(newDate, vertrag.getAntragsDatum());
    }

    @Test
    void testGetFahrzeug() {
        assertEquals(fahrzeug, vertrag.getFahrzeug());
    }

    @Test
    void testSetFahrzeug() {
        Fahrzeug newFahrzeug = new Fahrzeug("XYZ-789", "NewBrand", "NewType", 150, 113);
        vertrag.setFahrzeug(newFahrzeug);
        assertEquals(newFahrzeug, vertrag.getFahrzeug());
    }

    @Test
    void testGetPartner() {
        assertEquals(partner, vertrag.getPartner());
    }

    @Test
    void testSetPartner() {
        Partner newPartner = new Partner("Jane", "Doe", 'f', LocalDate.of(1990, 2, 2), "Germany", "NewStreet", "2", 54321, "NewCity", "NewState");
        vertrag.setPartner(newPartner);
        assertEquals(newPartner, vertrag.getPartner());
    }

    @Test
    void testToString() {
        String expected = "Vertragsdaten: " +
                "\n\tVertragsnummer: 12345678" +
                "\n\tPreis: 100.0€" +
                "\n\tAbrechnungszeitraum monatlich: true" +
                "\n\tVersicherungsbeginn: " + LocalDate.now() +
                "\n\tVersicherungsablauf: " + LocalDate.now().plusYears(1) +
                "\n\tAntragsdatum: " + LocalDate.now() +
                fahrzeug +
                partner;
        assertEquals(expected, vertrag.toString());
    }
}
