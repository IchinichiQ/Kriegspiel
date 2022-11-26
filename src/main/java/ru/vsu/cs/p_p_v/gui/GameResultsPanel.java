package ru.vsu.cs.p_p_v.gui;

import ru.vsu.cs.p_p_v.game.Teams;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GameResultsPanel extends JPanel {
    private GUI gui;

    public GameResultsPanel(GUI gui, Teams winner) {
        this.gui = gui;
        this.setLayout(new GridBagLayout());

        JLabel labelInfo = new JLabel(String.format("The %s won!", winner));
        labelInfo.setFont(labelInfo.getFont().deriveFont(50F));

        JPanel buttonsPanel = createButtonPanel();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NORTH;

        gbc.gridy = 0;
        gbc.weighty = 0.333;
        this.add(labelInfo, gbc);

        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weighty = 0.666;
        gbc.weightx = 1;
        this.add(buttonsPanel, gbc);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());

        JButton buttonRestart = new JButton("Restart");
        JButton buttonReturnToMainMenu = new JButton("Return to main menu");

        buttonRestart.setPreferredSize(new Dimension(180, 25));
        buttonReturnToMainMenu.setPreferredSize(new Dimension(180, 25));

        // TODO: Add support for online games
        buttonRestart.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.startNewLocalGame();
            }
        });
        buttonReturnToMainMenu.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.showMenu();
            }
        });

        GridBagConstraints gbcButtons = new GridBagConstraints();
        gbcButtons.insets = new Insets(5, 0, 0, 0);

        gbcButtons.gridy = 0;
        buttonPanel.add(buttonRestart, gbcButtons);
        gbcButtons.gridy = 1;
        buttonPanel.add(buttonReturnToMainMenu, gbcButtons);

        return buttonPanel;
    }
}
