package ru.vsu.cs.p_p_v.kriegspiel.client.gui;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    public MainWindow() throws HeadlessException {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle("Kriegspiel");

        setSize(750, 500);
    }

    public void setMainPanel(JPanel panel) {
        JPanel content = (JPanel) this.getContentPane();
        content.removeAll();
        content.add(panel);

        this.revalidate();
        this.repaint();
    }
}
