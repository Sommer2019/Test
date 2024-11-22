package de.axa.robin.vertragsverwaltung.user_interaction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

class InputTest {

    private Input input;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    private final Scanner scanner = Mockito.mock(Scanner.class);

    @BeforeEach
    void setUp() {
        input = new Input(scanner);
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @Test
    void testGetString() {
        given(scanner.nextLine()).willReturn("TestInput");
        String result = input.getString("Geben Sie einen Test-String ein:", ".*", false, false, false, false);
        assertEquals("TestInput", result);
        assertTrue(outContent.toString().contains("Geben Sie einen Test-String ein:"));
    }

    @Test
    void testGetChar() {
        given(scanner.next(".")).willReturn("y");
        char result = input.getChar(null, "");
        assertEquals('y', result);
        assertTrue(errContent.toString().contains("Überprüfung überspringen?"));
    }

    @Test
    void testGetNumber() {
        given(scanner.nextInt()).willReturn(5);
        int result = input.getNumber(Integer.class, "Geben Sie eine Zahl ein:", 1, 10, -1, false);
        assertEquals(5, result);
        assertTrue(outContent.toString().contains("Geben Sie eine Zahl ein:"));
    }

    @Test
    void testGetDate() {
        given(scanner.next()).willReturn(LocalDate.now().toString());
        LocalDate result = input.getDate("Geben Sie ein Datum ein:", LocalDate.of(2020, 1, 1), LocalDate.of(2025, 12, 31));
        assertEquals(LocalDate.now(), result);
        assertTrue(outContent.toString().contains("Geben Sie ein Datum ein:"));
    }

    @Test
    void testInvalidStringInput() {
        given(scanner.nextLine()).willReturn("a");
        String result = input.getString("Geben Sie einen Test-String ein:", ".*", false, false, false, false);
        assertNotEquals("", result);
    }

    @Test
    void testInvalidCharInput() {
        given(scanner.next(".")).willReturn("m");
        char result = input.getChar(null, "das Geschlecht des Partners");
        assertNotEquals('w', result);
    }

    @Test
    void testInvalidNumberInput() {
        given(scanner.nextInt()).willReturn(15);
        int result = input.getNumber(Integer.class, "Geben Sie ein Zahl ein:", 1, 20, -1, false);
        assertNotEquals(20, result);
    }

    @Test
    void testInvalidDateInput() {
        given(scanner.next()).willReturn("2026-01-01");
        LocalDate result =input.getDate("",LocalDate.now().minusYears(100), LocalDate.now().plusYears(200));
        assertNotEquals(LocalDate.of(1900,1, 1), result);
    }

    @BeforeEach
    void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }
}