package ru.vsu.cs.p_p_v.kriegspiel.client.gui;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.cell.BoardCell;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.unit.BoardUnit;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.unit.UnitStats;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UnitStatsOverlay {
    private final BoardPanel boardPanel;

    private BoardCell hoveredCell = null;
    private Point mousePoint = null;
    private long hoverTimeMs = 0;

    public UnitStatsOverlay(BoardPanel boardPanel) {
        this.boardPanel = boardPanel;

        this.boardPanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                hoveredCell = null;
                hoverTimeMs = 0;
                // TODO: Bug when leaving board with drawn overlay
                boardPanel.repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                mousePoint = e.getPoint();

                BoardCell currentlyHoveredCell = boardPanel.getCellByPanelPoint(e.getPoint());
                if (currentlyHoveredCell != hoveredCell) {
                    hoveredCell = currentlyHoveredCell;

                    // TODO: Bad expressions
                    if (hoverTimeMs >= 1000) {
                        hoverTimeMs = 0;
                        boardPanel.repaint();
                    } else {
                        hoverTimeMs = 0;
                    }
                } else if (hoverTimeMs >= 1000) {
                    boardPanel.repaint();
                }
            }
        });

        Timer hoverTimeUpdater = new Timer(50, new AbstractAction() {
            private boolean isDrawn = false;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (hoveredCell != null) {
                    hoverTimeMs += 50;
                    if (hoverTimeMs >= 1000) {
                        if (!isDrawn) {
                            boardPanel.repaint();
                            isDrawn = true;
                        }
                    } else {
                        isDrawn = false;
                    }
                }
            }
        });
        hoverTimeUpdater.start();
    }

    public void draw(Graphics2D g) {
        if (hoverTimeMs < 1000)
            return;

        BoardUnit hoveredUnit = hoveredCell.getUnit();
        if (hoveredUnit == null)
            return;

        Point worldMousePoint = boardPanel.pointScreenToWorld(mousePoint);

        int width = 70;
        int height = 90;
        int xOffset = 3;
        int yOffset = height * -1 - 3;

        int x = worldMousePoint.x + xOffset;
        int y = worldMousePoint.y + yOffset;

        g.setColor(Color.white);
        g.fillRect(x, y, width, height);

        g.setColor(Color.red);
        g.setFont(g.getFont());

        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        UnitStats hoveredUnitStats = hoveredUnit.getBaseStats();
        g.drawString("    STATS", x + 1, y + 10);
        g.drawString("Speed: " + String.valueOf(hoveredUnitStats.speed), x + 1, y + 20);
        g.drawString("Range: " + String.valueOf(hoveredUnitStats.range), x + 1, y + 30);
        g.drawString("Attack: " + String.valueOf(hoveredUnitStats.attack), x + 1, y + 40);
        g.drawString("Defense: " + String.valueOf(hoveredUnitStats.defense), x + 1, y + 50);


        g.drawString("   COMBAT", x + 1, y + 65);
        g.drawString("Defense: " + String.valueOf(hoveredUnit.getDefenseScore()), x + 1, y + 75);
        g.drawString("Attack: " + String.valueOf(hoveredUnit.getAttackScore()), x + 1, y + 85);
    }
}
