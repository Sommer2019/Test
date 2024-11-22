package de.axa.robin.vertragsverwaltung.storage;

import de.axa.robin.vertragsverwaltung.modell.Fahrzeug;
import de.axa.robin.vertragsverwaltung.modell.Partner;
import de.axa.robin.vertragsverwaltung.modell.Vertrag;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RepositoryTest {

    private Repository repository;
    private final Setup setup = new Setup();
    private List<Vertrag> vertrage;
    private File file;

    @BeforeEach
    void setUp() {
        repository = new Repository();
        vertrage = new ArrayList<>();
        vertrage.add(new Vertrag(12345, true, 299.99, LocalDate.of(2023, 1, 1), LocalDate.of(2024, 1, 1), LocalDate.of(2022, 12, 1),
                new Fahrzeug("ABC123", "BMW", "X5", 240, 1234),
                new Partner("Max", "Mustermann", 'M', LocalDate.of(1980, 1, 1), "Deutschland", "Musterstraße", "11", 51469, "Musterstadt", "NRW")));
        file = new File(setup.getRepositoryPath());
    }

    @AfterEach
    void tearDown() {
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testSpeichereUndLadeVertrage() {
        // Verträge speichern
        repository.speichereVertrage(vertrage);

        // Verträge laden
        List<Vertrag> geladeneVertrage = repository.ladeVertrage();

        // Überprüfen, ob die geladenen Verträge den gespeicherten entsprechen
        assertEquals(vertrage.size(), geladeneVertrage.size());
        Vertrag original = vertrage.getFirst();
        Vertrag geladen = geladeneVertrage.getFirst();

        assertEquals(original.getVsnr(), geladen.getVsnr());
        assertEquals(original.getMonatlich(), geladen.getMonatlich());
        assertEquals(original.getPreis(), geladen.getPreis());
        assertEquals(original.getVersicherungsbeginn(), geladen.getVersicherungsbeginn());
        assertEquals(original.getVersicherungsablauf(), geladen.getVersicherungsablauf());
        assertEquals(original.getAntragsDatum(), geladen.getAntragsDatum());

        Fahrzeug originalFahrzeug = original.getFahrzeug();
        Fahrzeug geladenFahrzeug = geladen.getFahrzeug();
        assertEquals(originalFahrzeug.getAmtlichesKennzeichen(), geladenFahrzeug.getAmtlichesKennzeichen());
        assertEquals(originalFahrzeug.getHersteller(), geladenFahrzeug.getHersteller());
        assertEquals(originalFahrzeug.getTyp(), geladenFahrzeug.getTyp());
        assertEquals(originalFahrzeug.getHoechstgeschwindigkeit(), geladenFahrzeug.getHoechstgeschwindigkeit());
        assertEquals(originalFahrzeug.getWagnisskennziffer(), geladenFahrzeug.getWagnisskennziffer());

        Partner originalPartner = original.getPartner();
        Partner geladenPartner = geladen.getPartner();
        assertEquals(originalPartner.getVorname(), geladenPartner.getVorname());
        assertEquals(originalPartner.getNachname(), geladenPartner.getNachname());
        assertEquals(originalPartner.getGeschlecht(), geladenPartner.getGeschlecht());
        assertEquals(originalPartner.getGeburtsdatum(), geladenPartner.getGeburtsdatum());
        assertEquals(originalPartner.getLand(), geladenPartner.getLand());
        assertEquals(originalPartner.getStrasse(), geladenPartner.getStrasse());
        assertEquals(originalPartner.getHausnummer(), geladenPartner.getHausnummer());
        assertEquals(originalPartner.getPlz(), geladenPartner.getPlz());
        assertEquals(originalPartner.getStadt(), geladenPartner.getStadt());
        assertEquals(originalPartner.getBundesland(), geladenPartner.getBundesland());
    }
}
