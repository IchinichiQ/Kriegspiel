package ru.vsu.cs.p_p_v.gui;

import ru.vsu.cs.p_p_v.cell.BoardCell;
import ru.vsu.cs.p_p_v.game.Game;
import ru.vsu.cs.p_p_v.unit.BoardUnit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UnitStatsOverlay {
    private BoardPanel boardPanel;
    private Game game;

    private BoardCell hoveredCell = null;
    private Point mousePoint = null;
    private long hoverTimeMs = 0;

    public UnitStatsOverlay(BoardPanel boardPanel) {
        this.boardPanel = boardPanel;
        this.game = this.boardPanel.getGame();

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

        g.setColor(Color.red);
        g.drawString(String.valueOf(hoveredUnit.getDefenseScore()), worldMousePoint.x, worldMousePoint.y);
        g.drawString(String.valueOf(hoveredUnit.getAttackScore()), worldMousePoint.x, worldMousePoint.y - 15);
    }
}
