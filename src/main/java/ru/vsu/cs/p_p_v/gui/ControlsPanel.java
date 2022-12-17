package ru.vsu.cs.p_p_v.gui;

import ru.vsu.cs.p_p_v.game.Game;
import ru.vsu.cs.p_p_v.game.GameEventListener;
import ru.vsu.cs.p_p_v.game.Teams;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class ControlsPanel extends JPanel {
    private Game game;
    JLabel labelCurrentTurn = new JLabel();
    JLabel labelLeftMoves = new JLabel();
    JLabel labelIsAttackUsed = new JLabel();
    JButton buttonNextTurn = new JButton();

    public ControlsPanel(Game game) {
        this.game = game;
        this.setLayout(new GridBagLayout());

        updateLabelsText();

        // TODO: Kostyl?
        labelLeftMoves.setBorder(new EmptyBorder(0, 0, 0, 1));

        buttonNextTurn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game.nextTurn();
            }
        });
        buttonNextTurn.setText("End turn");

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        this.add(labelCurrentTurn, gbc);

        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        this.add(labelLeftMoves, gbc);

        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        this.add(labelIsAttackUsed, gbc);

        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.NORTH;
        this.add(buttonNextTurn, gbc);

        game.addGameEventListener(new GameEventListener() {
            @Override
            public void onNextTurn() {
                updateLabelsText();
            }

            @Override
            public void onUnitMove() {
                updateLabelsText();
            }

            @Override
            public void onAttack() {
                updateLabelsText();
            }
        });
    }

    public void updateLabelsText() {
        labelCurrentTurn.setText(String.format("Current turn: %s", game.getCurrentTurnTeam() == Teams.North ? "North" : "South"));
        labelLeftMoves.setText(String.format("Unit movements left: %d", game.getLeftMoves()));
        labelIsAttackUsed.setText(String.format("Is attack used: %s", game.isAttackUsed() ? "Yes" : "No"));

        labelCurrentTurn.repaint();
        labelLeftMoves.repaint();
        labelIsAttackUsed.repaint();
    }
}
