package ru.vsu.cs.p_p_v.kriegspiel.client.gui;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class ControlsPanel extends JPanel {
    private Game game;
    JLabel labelMyTeam = new JLabel();
    JLabel labelCurrentTurn = new JLabel();
    JLabel labelLeftMoves = new JLabel();
    JLabel labelIsAttackUsed = new JLabel();
    JButton buttonNextTurn = new JButton();

    public ControlsPanel(Game game) {
        this.game = game;
        this.setLayout(new GridBagLayout());

        updateState();

        // TODO: Kostyl?
        labelLeftMoves.setBorder(new EmptyBorder(0, 0, 0, 1));

        buttonNextTurn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game.endTurn();
            }
        });
        buttonNextTurn.setText("End turn");

        GridBagConstraints gbc = new GridBagConstraints();

        int gridY = 0;

        if (game.isOnlineGame()) {
            gbc.gridy = gridY++;
            gbc.anchor = GridBagConstraints.NORTHWEST;
            this.add(labelMyTeam, gbc);
        }

        gbc.gridy = gridY++;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        this.add(labelCurrentTurn, gbc);

        gbc.gridy = gridY++;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        this.add(labelLeftMoves, gbc);

        gbc.gridy = gridY++;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        this.add(labelIsAttackUsed, gbc);

        gbc.gridy = gridY++;
        gbc.anchor = GridBagConstraints.NORTH;
        this.add(buttonNextTurn, gbc);

        game.addGameEventListener(new GameEventListener() {
            @Override
            public void onNextTurn() {
                updateState();
            }

            @Override
            public void onUnitMove(MoveUnitResult result) {
                updateState();
            }

            @Override
            public void onAttack(AttackUnitResult result) {
                updateState();
            }
        });
    }

    public void updateState() {
        labelMyTeam.setText(String.format("My team: %s", game.getMyTeam()));
        labelCurrentTurn.setText(String.format("Current turn: %s", game.getCurrentTurnTeam()));
        labelLeftMoves.setText(String.format("Unit movements left: %d", game.getLeftMoves()));
        labelIsAttackUsed.setText(String.format("Is attack used: %s", game.isAttackUsed() ? "Yes" : "No"));

        buttonNextTurn.setEnabled(game.getMyTeam() == game.getCurrentTurnTeam());

        labelMyTeam.repaint();
        labelCurrentTurn.repaint();
        labelLeftMoves.repaint();
        labelIsAttackUsed.repaint();
    }
}
