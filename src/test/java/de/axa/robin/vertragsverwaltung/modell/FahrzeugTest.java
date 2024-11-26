package de.axa.robin.vertragsverwaltung.modell;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FahrzeugTest {

    private Fahrzeug fahrzeug;

    @BeforeEach
    void setUp() {
        fahrzeug = new Fahrzeug("ABC-123", "TestBrand", "TestType", 100, 112);
    }

    @Test
    void testGetAmtlichesKennzeichen() {
        assertEquals("ABC-123", fahrzeug.getAmtlichesKennzeichen());
    }

    @Test
    void testSetAmtlichesKennzeichen() {
        fahrzeug.setAmtlichesKennzeichen("XYZ-789");
        assertEquals("XYZ-789", fahrzeug.getAmtlichesKennzeichen());
    }

    @Test
    void testGetHersteller() {
        assertEquals("TestBrand", fahrzeug.getHersteller());
    }

    @Test
    void testSetHersteller() {
        fahrzeug.setHersteller("NewBrand");
        assertEquals("NewBrand", fahrzeug.getHersteller());
    }

    @Test
    void testGetTyp() {
        assertEquals("TestType", fahrzeug.getTyp());
    }

    @Test
    void testSetTyp() {
        fahrzeug.setTyp("NewType");
        assertEquals("NewType", fahrzeug.getTyp());
    }

    @Test
    void testGetHoechstgeschwindigkeit() {
        assertEquals(100, fahrzeug.getHoechstgeschwindigkeit());
    }

    @Test
    void testSetHoechstgeschwindigkeit() {
        fahrzeug.setHoechstgeschwindigkeit(150);
        assertEquals(150, fahrzeug.getHoechstgeschwindigkeit());
    }

    @Test
    void testGetWagnisskennziffer() {
        assertEquals(112, fahrzeug.getWagnisskennziffer());
    }

    @Test
    void testSetWagnisskennziffer() {
        fahrzeug.setWagnisskennziffer(113);
        assertEquals(113, fahrzeug.getWagnisskennziffer());
    }

    @Test
    void testToString() {
        String expected = """
                
                Fahrzeug: \
                
                \tAmtliches Kennzeichen: ABC-123\
                
                \tHersteller: TestBrand\
                
                \tTyp: TestType\
                
                \tHöchstgeschwindigkeit: 100\
                
                \tWagnisskennziffer: 112""";
        assertEquals(expected, fahrzeug.toString());
    }
}
