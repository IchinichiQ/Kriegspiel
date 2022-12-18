package ru.vsu.cs.p_p_v.kriegspiel.client;

import ru.vsu.cs.p_p_v.kriegspiel.client.cli.CLI;
import ru.vsu.cs.p_p_v.kriegspiel.client.gui.GUI;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        if (Arrays.asList(args).contains("-CLI")) {
            CLI cli = new CLI();
            cli.run();
        } else {
            GUI gui = new GUI();
            gui.run();
        }
    }
}
