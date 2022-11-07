package ru.vsu.cs.p_p_v.gui;

import ru.vsu.cs.p_p_v.Main;
import ru.vsu.cs.p_p_v.game.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainWindow extends JFrame {
    private Game game;
    ControlsPanel controlsPanel;
    BoardPanel boardPanel;

    public MainWindow(Game game, ControlsPanel controlsPanel, BoardPanel boardPanel) throws HeadlessException {
        this.game = game;
        this.controlsPanel = controlsPanel;
        this.boardPanel = boardPanel;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle("Kriegspiel");

        setSize(1280, 720);

        JPanel MainPanel = new JPanel(new BorderLayout());

        JPanel boardContainer = new JPanel(new GridBagLayout());
        boardContainer.setBackground(Color.black);
        boardContainer.add(boardPanel);

        JPanel controlsContainer = new JPanel();
        controlsContainer.add(controlsPanel);

        MainPanel.add(boardContainer, BorderLayout.CENTER);
        MainPanel.add(controlsContainer, BorderLayout.EAST);

        this.add(MainPanel);
    }


}
