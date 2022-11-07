package ru.vsu.cs.p_p_v.gui;

import ru.vsu.cs.p_p_v.game.*;

public class GUI {
    Game game;
    MainWindow window;
    ControlsPanel controlsPanel;
    BoardPanel boardPanel;

    public GUI(Game game) {
        this.game = game;
    }

    public void run() {
        boardPanel = new BoardPanel(game);
        controlsPanel = new ControlsPanel(game);
        window = new MainWindow(game, controlsPanel, boardPanel);

        window.setVisible(true);
    }
}
