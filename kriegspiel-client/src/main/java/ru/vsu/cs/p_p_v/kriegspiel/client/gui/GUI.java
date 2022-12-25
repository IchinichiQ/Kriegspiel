package ru.vsu.cs.p_p_v.kriegspiel.client.gui;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.*;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.Game;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.GameEventListener;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.Teams;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.NetworkGame;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.NetworkGameEventListener;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.protocol.JsonProtocol;

import javax.swing.*;
import java.nio.file.Path;

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

    public void showGameResults(Teams winner) {
        GameResultsPanel resultsPanel = new GameResultsPanel(this, winner);

        window.setMainPanel(resultsPanel);
    }

    public void startNewLocalGame() {
        game = new LocalGame(Path.of("field.json"), Path.of("units.json"));

        window.setMainPanel(new GamePanel(game));

        game.addGameEventListener(new GameEventListener() {
            @Override
            public void onWin(Teams winner) {
                showGameResults(winner);
            }
        });
    }

    public void showConnectionPanel() {
        window.setMainPanel(new ConnectionPanel(this));
    }

    public void startNewOnlineGame(String server, int port) {
        try {
            game = new NetworkGame(server, port, new JsonProtocol());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(new JFrame(), String.format("Error while connecting to the server:\n%s", ex.getMessage()),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        game.addGameEventListener(new GameEventListener() {
            @Override
            public void onWin(Teams winner) {
                showGameResults(winner);
            }
        });

        ((NetworkGame) game).addNetworkGameEventListener(new NetworkGameEventListener() {
            @Override
            public void onWaitingPhase() {
                window.setMainPanel(new WaitPanel());
            }

            @Override
            public void onGamePhase() {
                window.setMainPanel(new GamePanel(game));
            }
        });
    }

    public void exit() {
        window.dispose();
    }
}
