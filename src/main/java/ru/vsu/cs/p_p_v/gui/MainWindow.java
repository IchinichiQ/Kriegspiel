package ru.vsu.cs.p_p_v.gui;

import ru.vsu.cs.p_p_v.Main;
import ru.vsu.cs.p_p_v.game.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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
