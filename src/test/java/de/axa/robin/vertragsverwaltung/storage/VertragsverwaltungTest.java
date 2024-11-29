package de.axa.robin.vertragsverwaltung.storage;

import de.axa.robin.vertragsverwaltung.config.Setup;
import de.axa.robin.vertragsverwaltung.modell.Fahrzeug;
import de.axa.robin.vertragsverwaltung.modell.Partner;
import de.axa.robin.vertragsverwaltung.modell.Vertrag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

class VertragsverwaltungTest {

    private Vertragsverwaltung vertragsverwaltung;
    private Setup setup;
    private final Fahrzeug fahrzeug = new Fahrzeug("ABC123", "BMW", "X5", 240, 1234);
    private final Partner partner = new Partner("Max", "Mustermann", 'M', LocalDate.of(1980, 1, 1), "Deutschland", "Musterstraße", "1", 12345, "Musterstadt", "NRW");
    private final Vertrag vertrag = new Vertrag(12345, true, 299.99, LocalDate.of(2023, 1, 1), LocalDate.of(2024, 1, 1), LocalDate.of(2022, 12, 1), fahrzeug, partner);

    @BeforeEach
    void setUp() {
        setup = Mockito.mock(Setup.class);
        vertragsverwaltung = new Vertragsverwaltung(setup);
        Repository repository = new Repository(setup) {
            private List<Vertrag> vertrage = new ArrayList<>();

            @Override
            public List<Vertrag> ladeVertrage() {
                return new ArrayList<>(vertrage);
            }

            @Override
            public void speichereVertrage(List<Vertrag> vertrage) {
                this.vertrage = new ArrayList<>(vertrage);
            }
        };
        given(setup.getRepositoryPath()).willReturn("src/main/resources/vertragetest.json");
    }

    @Test
    void testGetVertrage() {
        List<Vertrag> vertrage = vertragsverwaltung.getVertrage();
        assertNotNull(vertrage);
    }

    @Test
    void testGetVertrag() {
        vertrag.setVsnr(12345678);
        vertragsverwaltung.vertragAnlegen(vertrag);

        Vertrag result = vertragsverwaltung.getVertrag(12345678);
        assertNotNull(result);
        assertEquals(12345678, result.getVsnr());
    }

    @Test
    void testVertragAnlegen() {
        vertrag.setVsnr(12345679);
        vertragsverwaltung.vertragAnlegen(vertrag);

        assertTrue(vertragsverwaltung.vertragExistiert(12345679));
    }

    @Test
    void testVertragLoeschen() {
        vertrag.setVsnr(12345670);
        vertragsverwaltung.vertragAnlegen(vertrag);

        vertragsverwaltung.vertragLoeschen(12345670);
        List<Vertrag> vertrage = vertragsverwaltung.getVertrage();
        assertFalse(vertrage.contains(vertrag));
    }

    @Test
    void testVertragExistiert() {
        vertrag.setVsnr(12345689);
        vertragsverwaltung.vertragAnlegen(vertrag);

        assertTrue(vertragsverwaltung.vertragExistiert(12345689));
        assertFalse(vertragsverwaltung.vertragExistiert(87654321));
    }

    @Test
    void testKennzeichenExistiert() {
        fahrzeug.setAmtlichesKennzeichen("ABC123");
        vertrag.setFahrzeug(fahrzeug);
        vertragsverwaltung.vertragAnlegen(vertrag);

        assertTrue(vertragsverwaltung.kennzeichenExistiert("ABC123"));
        assertFalse(vertragsverwaltung.kennzeichenExistiert("XYZ789"));
    }
}
