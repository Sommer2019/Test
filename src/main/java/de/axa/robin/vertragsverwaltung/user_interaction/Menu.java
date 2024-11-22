package de.axa.robin.vertragsverwaltung.user_interaction;

import de.axa.robin.vertragsverwaltung.modell.Vertrag;
import de.axa.robin.vertragsverwaltung.storage.Vertragsverwaltung;
import de.axa.robin.vertragsverwaltung.storage.editor.Create;
import de.axa.robin.vertragsverwaltung.storage.editor.Delete;
import de.axa.robin.vertragsverwaltung.storage.editor.Edit;

import java.util.List;

public class Menu {
    ////Klassen einlesen////
    private Output output = new Output();
    private Input input;
    private Delete delete = new Delete(input);
    private Vertragsverwaltung vertragsverwaltung = new Vertragsverwaltung();
    private Create create = new Create(input, vertragsverwaltung, output);
    private Edit edit = new Edit(input, vertragsverwaltung, output);

    public Menu(Input input) {
        this.input = input;
    }

    public Output getOutput() {
        return output;
    }

    public void setOutput(Output output) {
        this.output = output;
    }

    public Input getInput() {
        return input;
    }

    public void setInput(Input input) {
        this.input = input;
    }

    public void setDelete(Delete delete) {
        this.delete = delete;
    }

    public Create getCreate() {
        return create;
    }

    public void setCreate(Create create) {
        this.create = create;
    }

    public void setEdit(Edit edit) {
        this.edit = edit;
    }

    public Vertragsverwaltung getVertragsverwaltung() {
        return vertragsverwaltung;
    }

    public void setVertragsverwaltung(Vertragsverwaltung vertragsverwaltung) {
        this.vertragsverwaltung = vertragsverwaltung;
    }

    public void menu() {
        while (true) {
            output.menu();
            List<Vertrag> vertrage = vertragsverwaltung.getVertrage();
            int number = input.getNumber(Integer.class, "", 1, 6, -1, false);

            // Handle menu actions
            handleMenuAction(number, vertrage);
        }
    }

    public void handleMenuAction(int number, List<Vertrag> vertrage) {
        switch (number) {
            case 1:
                output.druckeVertrage(vertrage);
                break;
            case 2:
                int vsnr = input.getNumber(Integer.class, "8-stellige VSNR oder 0 zum abbrechen", -1, -1, -1, true);
                if (vsnr != 0) {
                    Vertrag vertrag = vertragsverwaltung.getVertrag(vsnr);
                    output.druckeVertrag(vertrag);
                }
                break;
            case 3:
                create.createVertrag();
                break;
            case 4:
                edit.editmenu(vertrage);
                break;
            case 5:
                vsnr = input.getNumber(Integer.class, "8-stellige VSNR oder 0 zum abbrechen", -1, -1, -1, true);
                if (vsnr != 0) {
                    delete.delete(vsnr);
                }
                break;
            case 6:
                System.exit(0);
                break;
            default:
                // Handle invalid input if necessary
                break;
        }
    }
}