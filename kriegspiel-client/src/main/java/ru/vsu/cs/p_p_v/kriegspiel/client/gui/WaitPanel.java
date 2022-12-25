package ru.vsu.cs.p_p_v.kriegspiel.client.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WaitPanel extends JPanel {
    private JLabel waitLabel;
    private final String labelText = "Waiting for another player";
    private int dotCount;

    public WaitPanel() {
        this.waitLabel = new JLabel(labelText);
        this.dotCount = 0;

        waitLabel.setFont(waitLabel.getFont().deriveFont(40F));

        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        this.add(waitLabel, gbc);

        new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (dotCount == 3){
                    dotCount = 0;
                } else {
                    dotCount += 1;
                }

                waitLabel.setText(labelText + ".".repeat(dotCount));
            }
        }).start();
    }
}
