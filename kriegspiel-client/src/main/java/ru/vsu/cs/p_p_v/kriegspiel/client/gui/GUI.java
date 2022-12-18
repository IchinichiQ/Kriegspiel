package ru.vsu.cs.p_p_v.kriegspiel.client.gui;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.*;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.Game;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.GameEventListener;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.Teams;

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
        game = new LocalGame("field.json", "units.json");

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
