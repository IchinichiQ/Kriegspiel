package ru.vsu.cs.p_p_v.gui;

import ru.vsu.cs.p_p_v.game.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GUI {
    Game game;
    MainWindow window;

    public void run() {
        window = new MainWindow();
        window.setLocationRelativeTo(null);

        showMenu();
    }

    public void showMenu() {
        MenuPanel menuPanel = new MenuPanel(this);

        window.setMainPanel(menuPanel);
        window.setVisible(true);
    }

    public void showGameResults() {
        Teams winner = game.getWinner();

        GameResultsPanel resultsPanel = new GameResultsPanel(this, winner);

        window.setMainPanel(resultsPanel);
    }

    public void startNewLocalGame() {
        game = new Game("field.json", "units.json");

        window.setMainPanel(new GamePanel(game));

        game.addGameEventListener(new GameEventListener() {
            @Override
            public void onWin() {
                showGameResults();
            }
        });
    }

    public void exit() {
        window.dispose();
    }
}
