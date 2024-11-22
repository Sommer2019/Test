package de.axa.robin.vertragsverwaltung;

import de.axa.robin.vertragsverwaltung.user_interaction.Input;
import de.axa.robin.vertragsverwaltung.user_interaction.Menu;

import java.util.Scanner;

class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Input input = new Input(scanner);
    public static void main(String[] args) {
        Menu menu = new Menu(input);
        menu.menu();
    }
}

