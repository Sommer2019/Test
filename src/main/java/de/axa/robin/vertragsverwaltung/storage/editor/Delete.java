package de.axa.robin.vertragsverwaltung.storage.editor;

import de.axa.robin.vertragsverwaltung.config.Setup;
import de.axa.robin.vertragsverwaltung.storage.Vertragsverwaltung;
import de.axa.robin.vertragsverwaltung.user_interaction.Input;
import de.axa.robin.vertragsverwaltung.user_interaction.Output;


public class Delete {
    ////Klassen einlesen////
    private final Setup setup = new Setup();
    private final Vertragsverwaltung vertragsverwaltung = new Vertragsverwaltung(setup);
    private final Output output = new Output();
    private final Input input;
    public static final String DEL = "gelöscht";

    public Delete(Input input) {
        this.input = input;
    }

    public void delete(int vsnrdelete) {
        char inputresult = input.getChar(vertragsverwaltung.getVertrag(vsnrdelete), DEL);
        switch (Character.toLowerCase(inputresult)) {
            case 'y':
                vertragsverwaltung.vertragLoeschen(vsnrdelete);
                output.done("Vertrag erfolgreich gelöscht.");
                break;
            case 'n':
                output.cancel();
                break;
            default:
                output.error("Ungültige Eingabe!");
                break;
        }
    }
}
