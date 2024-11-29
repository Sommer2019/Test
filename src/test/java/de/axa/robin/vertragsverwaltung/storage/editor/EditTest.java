package de.axa.robin.vertragsverwaltung.storage.editor;

import de.axa.robin.vertragsverwaltung.modell.Fahrzeug;
import de.axa.robin.vertragsverwaltung.modell.Partner;
import de.axa.robin.vertragsverwaltung.modell.Vertrag;
import de.axa.robin.vertragsverwaltung.config.Setup;
import de.axa.robin.vertragsverwaltung.storage.Vertragsverwaltung;
import de.axa.robin.vertragsverwaltung.user_interaction.Input;
import de.axa.robin.vertragsverwaltung.user_interaction.Output;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class EditTest {
    private Edit edit;
    private Input mockInput;
    private Output mockOutput;
    private Setup setup;
    private Vertragsverwaltung mockVertragsverwaltung;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final Fahrzeug fahrzeug = new Fahrzeug("ABC123", "BMW", "X5", 240, 1234);
    private final Partner partner = new Partner("Max", "Mustermann", 'M', LocalDate.of(1980, 1, 1), "Deutschland", "Musterstraße", "1", 12345, "Musterstadt", "NRW");
    private final Vertrag mockVertrag = new Vertrag(12345, true, 299.99, LocalDate.of(2023, 1, 1), LocalDate.of(2024, 1, 1), LocalDate.of(2022, 12, 1), fahrzeug, partner);


    @BeforeEach
    public void setUp() {
        setup = mock(Setup.class);
        System.setOut(new PrintStream(outContent));
        mockInput = mock(Input.class);
        mockOutput = mock(Output.class);
        mockVertragsverwaltung = mock(Vertragsverwaltung.class);
        edit = new Edit(mockInput, mockVertragsverwaltung, mockOutput);
    }

    @Test
    void editVertrag() {
        // Arrange
        Vertrag vertrag = new Vertrag(12345678, true, 100.0, LocalDate.now(), LocalDate.now().plusYears(1), LocalDate.now(),
                new Fahrzeug("GL-GL123", "Toyota", "Corolla", 150, 112),
                new Partner("John", "Doe", 'M', LocalDate.of(1980, 1, 1), "Deutschland", "Hauptstraße", "11", 51465, "Bergisch Gladbach", "Nordrhein-Westfalen"));

        when(mockInput.getNumber(Integer.class, "", -1, -1, -1, false)).thenReturn(4);
        when(mockInput.getChar(any(), eq("erstellt"))).thenReturn('y');

        // Act
        edit.editVertrag(vertrag);

        // Assert
        ArgumentCaptor<Vertrag> vertragCaptor = ArgumentCaptor.forClass(Vertrag.class);
        verify(mockVertragsverwaltung).vertragAnlegen(vertragCaptor.capture());
        Vertrag capturedVertrag = vertragCaptor.getValue();

        assertNotNull(capturedVertrag);
        assertEquals(12345678, capturedVertrag.getVsnr());
        assertEquals("GL-GL123", capturedVertrag.getFahrzeug().getAmtlichesKennzeichen());
        assertEquals("John", capturedVertrag.getPartner().getVorname());
        assertTrue(capturedVertrag.getMonatlich());
        assertEquals(LocalDate.now(), capturedVertrag.getVersicherungsbeginn());
        assertEquals(LocalDate.now().plusYears(1), capturedVertrag.getVersicherungsablauf());
        assertTrue(capturedVertrag.getPreis() >= 0); // Assuming a valid calculation should return a positive price
    }

    @Test
    void editAllgemeineDaten_setVersicherungsbeginn() {
        // Arrange
        Vertrag mockVertrag = mock(Vertrag.class);
        when(mockVertrag.getAntragsDatum()).thenReturn(LocalDate.now());
        when(mockVertrag.getVersicherungsablauf()).thenReturn(LocalDate.now().plusYears(1));
        when(mockInput.getNumber(Integer.class, "", -1, -1, -1, false)).thenReturn(1); // Simulate user choice for Versicherungsbeginn

        // Act
        edit.editAllgemeineDaten(mockVertrag);

        // Assert
        verify(mockOutput, times(1)).editDates();
        verify(mockVertrag, times(1)).setVersicherungsbeginn(any());
    }

    @Test
    void editAllgemeineDaten_setVersicherungsablauf() {
        // Arrange
        Vertrag mockVertrag = mock(Vertrag.class);
        when(mockVertrag.getVersicherungsbeginn()).thenReturn(LocalDate.now());
        when(mockInput.getNumber(Integer.class, "", -1, -1, -1, false)).thenReturn(2); // Simulate user choice for Versicherungsablauf

        // Act
        edit.editAllgemeineDaten(mockVertrag);

        // Assert
        verify(mockOutput, times(1)).editDates();
        verify(mockVertrag, times(1)).setVersicherungsablauf(any());
    }

    @Test
    void editAllgemeineDaten_setAntragsDatum() {
        // Arrange
        Vertrag mockVertrag = mock(Vertrag.class);
        when(mockInput.getNumber(Integer.class, "", -1, -1, -1, false)).thenReturn(3); // Simulate user choice for AntragsDatum

        // Act
        edit.editAllgemeineDaten(mockVertrag);

        // Assert
        verify(mockOutput, times(1)).editDates();
        verify(mockVertrag, times(1)).setAntragsDatum(any());
    }

    @Test
    void editAllgemeineDaten_setMonatlich() {
        // Arrange
        Vertrag mockVertrag = mock(Vertrag.class);
        when(mockInput.getNumber(Integer.class, "", -1, -1, -1, false)).thenReturn(4); // Simulate user choice for Monatlich
        when(mockInput.getChar(any(), anyString())).thenReturn('m'); // Simulate user input for monthly booking

        // Act
        edit.editAllgemeineDaten(mockVertrag);

        // Assert
        verify(mockOutput, times(1)).editDates();
        verify(mockVertrag, times(1)).setMonatlich(true);
    }

    @Test
    void editPersonendaten_setVorname() {
        // Arrange
        Vertrag mockVertrag = mock(Vertrag.class);
        Partner mockPartner = mock(Partner.class);
        when(mockVertrag.getPartner()).thenReturn(mockPartner);
        when(mockInput.getNumber(Integer.class, "", -1, -1, -1, false)).thenReturn(1); // Simulate user choice for Vorname
        when(mockInput.getString(anyString(), anyString(), anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean()))
                .thenReturn("John");

        // Act
        edit.editPersonendaten(mockVertrag);

        // Assert
        verify(mockOutput, times(1)).editPerson();
        verify(mockPartner, times(1)).setVorname("John");
    }

    @Test
    void editPersonendaten_setNachname() {
        // Arrange
        Vertrag mockVertrag = mock(Vertrag.class);
        Partner mockPartner = mock(Partner.class);
        when(mockVertrag.getPartner()).thenReturn(mockPartner);
        when(mockInput.getNumber(Integer.class, "", -1, -1, -1, false)).thenReturn(2); // Simulate user choice for Nachname
        when(mockInput.getString(anyString(), anyString(), anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean()))
                .thenReturn("Doe");

        // Act
        edit.editPersonendaten(mockVertrag);

        // Assert
        verify(mockOutput, times(1)).editPerson();
        verify(mockPartner, times(1)).setNachname("Doe");
    }

    @Test
    void editPersonendaten_setGeschlecht() {
        // Arrange
        Vertrag mockVertrag = mock(Vertrag.class);
        Partner mockPartner = mock(Partner.class);
        when(mockVertrag.getPartner()).thenReturn(mockPartner);
        when(mockInput.getNumber(Integer.class, "", -1, -1, -1, false)).thenReturn(3); // Simulate user choice for Geschlecht
        when(mockInput.getChar(any(), anyString())).thenReturn('M');

        // Act
        edit.editPersonendaten(mockVertrag);

        // Assert
        verify(mockOutput, times(1)).editPerson();
        verify(mockPartner, times(1)).setGeschlecht('M');
    }

    @Test
    void editPersonendaten_setGeburtsdatum() {
        // Arrange
        Vertrag mockVertrag = mock(Vertrag.class);
        Partner mockPartner = mock(Partner.class);
        when(mockVertrag.getPartner()).thenReturn(mockPartner);
        when(mockInput.getNumber(Integer.class, "", -1, -1, -1, false)).thenReturn(4); // Simulate user choice for Geburtsdatum
        when(mockInput.getDate(anyString(), any(), any())).thenReturn(LocalDate.of(1980, 1, 1));

        // Act
        edit.editPersonendaten(mockVertrag);

        // Assert
        verify(mockOutput, times(1)).editPerson();
        verify(mockPartner, times(1)).setGeburtsdatum(LocalDate.of(1980, 1, 1));
    }

    @Test
    void editFahrzeugdaten_setAmtlichesKennzeichen() {
        // Arrange
        Vertrag mockVertrag = mock(Vertrag.class);
        Fahrzeug mockFahrzeug = mock(Fahrzeug.class);
        when(mockVertrag.getFahrzeug()).thenReturn(mockFahrzeug);
        when(mockInput.getNumber(Integer.class, "", -1, -1, -1, false)).thenReturn(1); // Simulate user choice for AmtlichesKennzeichen
        when(mockInput.getString(anyString(), anyString(), anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean())).thenReturn("GL-GL1234");

        // Act
        edit.editFahrzeugdaten(mockVertrag);

        // Assert
        verify(mockOutput, times(1)).editFahrzeug();
        verify(mockFahrzeug, times(1)).setAmtlichesKennzeichen("GL-GL1234");
    }

    @Test
    void editFahrzeugdaten_setHersteller() {
        // Arrange
        Vertrag mockVertrag = mock(Vertrag.class);
        Fahrzeug mockFahrzeug = mock(Fahrzeug.class);
        when(mockVertrag.getFahrzeug()).thenReturn(mockFahrzeug);
        when(mockInput.getNumber(Integer.class, "", -1, -1, -1, false)).thenReturn(2); // Simulate user choice for Hersteller
        when(mockInput.getString(anyString(), anyString(), anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean())).thenReturn("Toyota");

        // Act
        edit.editFahrzeugdaten(mockVertrag);

        // Assert
        verify(mockOutput, times(1)).editFahrzeug();
        verify(mockFahrzeug, times(1)).setHersteller("Toyota");
    }

    @Test
    void editFahrzeugdaten_setTyp() {
        // Arrange
        Vertrag mockVertrag = mock(Vertrag.class);
        Fahrzeug mockFahrzeug = mock(Fahrzeug.class);
        when(mockVertrag.getFahrzeug()).thenReturn(mockFahrzeug);
        when(mockInput.getNumber(Integer.class, "", -1, -1, -1, false)).thenReturn(3); // Simulate user choice for Typ
        when(mockInput.getString(anyString(), anyString(), anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean())).thenReturn("Corolla");

        // Act
        edit.editFahrzeugdaten(mockVertrag);

        // Assert
        verify(mockOutput, times(1)).editFahrzeug();
        verify(mockFahrzeug, times(1)).setTyp("Corolla");
    }

    @Test
    void editFahrzeugdaten_setHoechstgeschwindigkeit() {
        // Arrange
        Vertrag mockVertrag = mock(Vertrag.class);
        Fahrzeug mockFahrzeug = mock(Fahrzeug.class);
        when(mockVertrag.getFahrzeug()).thenReturn(mockFahrzeug);
        when(mockInput.getNumber(Integer.class, "", -1, -1, -1, false)).thenReturn(4); // Simulate user choice for Hoechstgeschwindigkeit
        when(mockInput.getNumber(Integer.class, "die Höchstgeschwindigkeit", 50, 250, -1, false)).thenReturn(200); // Return a valid speed

        // Act
        edit.editFahrzeugdaten(mockVertrag);

        // Assert
        verify(mockOutput, times(1)).editFahrzeug();
        verify(mockFahrzeug, times(1)).setHoechstgeschwindigkeit(200);
    }

    @Test
    void editFahrzeugdaten_setWagnisskennziffer() {
        // Arrange
        Vertrag mockVertrag = mock(Vertrag.class);
        Fahrzeug mockFahrzeug = mock(Fahrzeug.class);
        when(mockVertrag.getFahrzeug()).thenReturn(mockFahrzeug);
        when(mockInput.getNumber(Integer.class, "", -1, -1, -1, false)).thenReturn(5);
        when(mockInput.getNumber(Integer.class, "die Wagnisskennziffer", -1, -1, 112, false)).thenReturn(112);

        // Act
        edit.editFahrzeugdaten(mockVertrag);

        // Assert
        verify(mockOutput, times(1)).editFahrzeug();
        verify(mockFahrzeug, times(1)).setWagnisskennziffer(112);
    }

    @Test
    void testEditmenu() {
        // Arrange
        List<Vertrag> vertrage = new ArrayList<>();
        vertrage.add(mockVertrag);
        when(mockInput.getNumber(any(), anyString(), anyInt(), anyInt(), anyInt(), anyBoolean())).thenReturn(1, 12345);

        // Act
        edit.editmenu(vertrage);

        // Assert
        verify(mockOutput, times(1)).editwhat();
    }

    @Test
    void testRecalcprice() {
        // Arrange
        List<Vertrag> vertrage = new ArrayList<>();
        vertrage.add(mockVertrag);
        when(mockInput.getNumber(Double.class, "", -1, -1, -1, false)).thenReturn(1.0, 2.0, 3.0);
        given(setup.getPreisPath()).willReturn("src/main/resources/preiscalctest.json");

        // Act
        edit.recalcprice(vertrage);

        // Assert
        verify(mockOutput, times(1)).create("den neuen allgemeinen Faktor");
        verify(mockOutput, times(1)).create("den neuen Altersfaktor");
        verify(mockOutput, times(1)).create("den neuen Geschwindigkeitsfaktor");
        verify(mockInput, times(3)).getNumber(Double.class, "", -1, -1, -1, false);
    }
}