package ru.vsu.cs.p_p_v.gui;

import ru.vsu.cs.p_p_v.game.Game;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    public GamePanel(Game game) {
        BoardPanel boardPanel = new BoardPanel(game);
        JPanel controlsPanel = new ControlsPanel(game);

        JPanel MainPanel = new JPanel(new BorderLayout());

        JPanel boardContainer = new JPanel(new GridBagLayout());
        boardContainer.setBackground(Color.black);
        boardContainer.add(boardPanel);

        JPanel controlsContainer = new JPanel();
        controlsContainer.add(controlsPanel);

        MainPanel.add(boardContainer, BorderLayout.CENTER);
        MainPanel.add(controlsContainer, BorderLayout.EAST);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        this.setLayout(new GridBagLayout());

        this.add(MainPanel, gbc);
    }
}