package ru.vsu.cs.p_p_v;

import ru.vsu.cs.p_p_v.cli.CLI;
import ru.vsu.cs.p_p_v.game.Game;
import ru.vsu.cs.p_p_v.gui.GUI;

import java.util.Arrays;
import java.util.Objects;

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
