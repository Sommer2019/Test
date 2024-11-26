package de.axa.robin.vertragsverwaltung.modell;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PartnerTest {

    private Partner partner;

    @BeforeEach
    void setUp() {
        partner = new Partner("John", "Doe", 'm', LocalDate.of(1980, 1, 1), "Germany", "TestStreet", "1", 51469, "TestCity", "TestState");
    }

    @Test
    void testGetVorname() {
        assertEquals("John", partner.getVorname());
    }

    @Test
    void testSetVorname() {
        partner.setVorname("Jane");
        assertEquals("Jane", partner.getVorname());
    }

    @Test
    void testGetNachname() {
        assertEquals("Doe", partner.getNachname());
    }

    @Test
    void testSetNachname() {
        partner.setNachname("Smith");
        assertEquals("Smith", partner.getNachname());
    }

    @Test
    void testGetGeschlecht() {
        assertEquals('m', partner.getGeschlecht());
    }

    @Test
    void testSetGeschlecht() {
        partner.setGeschlecht('f');
        assertEquals('f', partner.getGeschlecht());
    }

    @Test
    void testGetGeburtsdatum() {
        assertEquals(LocalDate.of(1980, 1, 1), partner.getGeburtsdatum());
    }

    @Test
    void testSetGeburtsdatum() {
        LocalDate newDate = LocalDate.of(1990, 2, 2);
        partner.setGeburtsdatum(newDate);
        assertEquals(newDate, partner.getGeburtsdatum());
    }

    @Test
    void testGetLand() {
        assertEquals("Germany", partner.getLand());
    }

    @Test
    void testSetLand() {
        partner.setLand("France");
        assertEquals("France", partner.getLand());
    }

    @Test
    void testGetStrasse() {
        assertEquals("TestStreet", partner.getStrasse());
    }

    @Test
    void testSetStrasse() {
        partner.setStrasse("NewStreet");
        assertEquals("NewStreet", partner.getStrasse());
    }

    @Test
    void testGetHausnummer() {
        assertEquals("1", partner.getHausnummer());
    }

    @Test
    void testSetHausnummer() {
        partner.setHausnummer("2");
        assertEquals("2", partner.getHausnummer());
    }

    @Test
    void testGetPlz() {
        assertEquals(51469, partner.getPlz());
    }

    @Test
    void testSetPlz() {
        partner.setPlz(51469);
        assertEquals(51469, partner.getPlz());
    }

    @Test
    void testGetStadt() {
        assertEquals("TestCity", partner.getStadt());
    }

    @Test
    void testSetStadt() {
        partner.setStadt("NewCity");
        assertEquals("NewCity", partner.getStadt());
    }

    @Test
    void testGetBundesland() {
        assertEquals("TestState", partner.getBundesland());
    }

    @Test
    void testSetBundesland() {
        partner.setBundesland("NewState");
        assertEquals("NewState", partner.getBundesland());
    }

    @Test
    void testToString() {
        String expected = """
                
                Partner: \
                
                \tVorname: John\
                
                \tNachname: Doe\
                
                \tGeschlecht: m\
                
                \tGeburtsdatum: 1980-01-01\
                
                \tStrasse: TestStreet\
                
                \tHausnummer: 1\
                
                \tPLZ: 51469\
                
                \tStadt: TestCity\
                
                \tBundesland: TestState\
                
                \tLand: Germany""";
        assertEquals(expected, partner.toString());
    }
}
