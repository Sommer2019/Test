package de.axa.robin.vertragsverwaltung.user_interaction;

import de.axa.robin.vertragsverwaltung.modell.Vertrag;
import de.axa.robin.vertragsverwaltung.storage.Vertragsverwaltung;
import de.axa.robin.vertragsverwaltung.storage.editor.Create;
import de.axa.robin.vertragsverwaltung.storage.editor.Delete;
import de.axa.robin.vertragsverwaltung.storage.editor.Edit;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class MenuTest {

    private Menu menu;
    private Output outputMock;
    private Input inputMock;
    private Vertragsverwaltung vertragsverwaltungMock;
    private Create createMock;
    private Edit editMock;
    private Delete deleteMock;

    @BeforeEach
    public void setUp() {
        outputMock = mock(Output.class);
        inputMock = mock(Input.class);
        vertragsverwaltungMock = mock(Vertragsverwaltung.class);
        createMock = mock(Create.class);
        editMock = mock(Edit.class);
        deleteMock = mock(Delete.class);

        menu = new Menu(inputMock);
        // Inject the mocked dependencies
        menu.setOutput(outputMock);
        menu.setVertragsverwaltung(vertragsverwaltungMock);
        menu.setCreate(createMock);
        menu.setEdit(editMock);
        menu.setDelete(deleteMock);
    }

    @Test
    public void testHandleMenuAction_case1() {
        when(inputMock.getNumber(Integer.class, "", 1, 6, -1, false)).thenReturn(1);
        menu.handleMenuAction(1, Collections.emptyList());

        verify(outputMock).druckeVertrage(Collections.emptyList());
    }

    @Test
    public void testHandleMenuAction_case2() {
        when(inputMock.getNumber(Integer.class, "8-stellige VSNR oder 0 zum abbrechen", -1, -1, -1, true)).thenReturn(12345678);
        Vertrag vertragMock = mock(Vertrag.class);
        when(vertragsverwaltungMock.getVertrag(12345678)).thenReturn(vertragMock);

        menu.handleMenuAction(2, Collections.emptyList());

        verify(outputMock).druckeVertrag(vertragMock);
    }

    @Test
    public void testHandleMenuAction_case3() {
        menu.handleMenuAction(3, Collections.emptyList());

        verify(createMock).createVertrag();
    }

    @Test
    public void testHandleMenuAction_case4() {
        menu.handleMenuAction(4, Collections.emptyList());

        verify(editMock).editmenu(Collections.emptyList());
    }

    @Test
    public void testHandleMenuAction_case5() {
        when(inputMock.getNumber(Integer.class, "8-stellige VSNR oder 0 zum abbrechen", -1, -1, -1, true)).thenReturn(12345678);

        menu.handleMenuAction(5, Collections.emptyList());

        verify(deleteMock).delete(12345678);
    }
}