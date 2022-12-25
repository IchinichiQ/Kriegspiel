package ru.vsu.cs.p_p_v.kriegspiel.client.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ConnectionPanel extends JPanel {
    private GUI gui;

    public ConnectionPanel(GUI gui) {
        this.gui = gui;
        this.setLayout(new GridBagLayout());

        JPanel connectionPanel = createConnectionPanel();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NORTH;

        gbc.gridy = 0;
        this.add(connectionPanel, gbc);
    }

    private JPanel createConnectionPanel() {
        JPanel connectionPanel = new JPanel();
        connectionPanel.setLayout(new GridBagLayout());

        JLabel labelInfo = new JLabel("Please enter server ip and port:");
        labelInfo.setFont(labelInfo.getFont().deriveFont(16.0F));

        JTextField textFieldServerIp = new JTextField();
        textFieldServerIp.setPreferredSize(new Dimension(180, 25));

        JTextField textFieldServerPort = new JTextField();
        textFieldServerPort.setPreferredSize(new Dimension(60, 25));

        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(textFieldServerIp);
        inputPanel.add(textFieldServerPort);

        JButton buttonConnect = new JButton("Connect");
        buttonConnect.setPreferredSize(new Dimension(180, 25));

        buttonConnect.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String serverIp = textFieldServerIp.getText();
                int port;
                try {
                    port = Integer.parseInt(textFieldServerPort.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(new JFrame(), "Please enter valid port number",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                gui.startNewOnlineGame(serverIp, port);
            }
        });

        GridBagConstraints gbcConnection = new GridBagConstraints();
        gbcConnection.insets = new Insets(5, 0, 0, 0);

        gbcConnection.gridy = 0;
        connectionPanel.add(labelInfo, gbcConnection);
        gbcConnection.gridy = 1;
        connectionPanel.add(inputPanel, gbcConnection);
        gbcConnection.gridy = 2;
        connectionPanel.add(buttonConnect, gbcConnection);

        return connectionPanel;
    }
}
