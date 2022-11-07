package ru.vsu.cs.p_p_v;

import ru.vsu.cs.p_p_v.cli.CLI;
import ru.vsu.cs.p_p_v.game.Game;
import ru.vsu.cs.p_p_v.gui.GUI;

import java.util.Arrays;
import java.util.Objects;

public class Main {

    public static void main(String[] args) {
        Game game = new Game("field.json", "units.json");

        if (Arrays.asList(args).contains("-CLI")) {
            CLI cli = new CLI(game);
            cli.run();
        } else {
            GUI gui = new GUI(game);
            gui.run();
        }
    }
}
