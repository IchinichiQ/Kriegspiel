package ru.vsu.cs.p_p_v.kriegspiel.client.gui;

import ru.vsu.cs.p_p_v.kriegspiel.client.gui.panels.BoardPanel;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.cell.BoardCell;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.Game;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.unit.BoardUnit;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.unit.stats.UnitBaseStats;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.unit.stats.UnitCombatStats;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UnitStatsOverlay {
    private final int intervalToShowOverlayMs = 1000;

    private final BoardPanel boardPanel;
    private final Game game;

    private Point mousePoint = null;
    private long hoverTimeMs = 0;
    private boolean shouldDraw = false;
    private BoardCell hoveredCell = null;
    private BoardUnit hoveredUnit = null;
    private UnitBaseStats hoveredUnitBaseStats = null;
    private UnitCombatStats hoveredUnitCombatStats = null;

    public UnitStatsOverlay(BoardPanel boardPanel) {
        this.boardPanel = boardPanel;
        this.game = boardPanel.getGame();

        this.boardPanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                updateHoveredCell(null);
                boardPanel.repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                mousePoint = e.getPoint();

                BoardCell currentlyHoveredCell = boardPanel.getCellByPanelPoint(e.getPoint());
                if (currentlyHoveredCell != hoveredCell) {
                    updateHoveredCell(currentlyHoveredCell);
                } else if (shouldDraw) {
                    boardPanel.repaint();
                }
            }
        });

        Timer hoverTimeUpdater = new Timer(50, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (hoveredCell != null) {
                    hoverTimeMs += 50;
                    if (hoverTimeMs >= intervalToShowOverlayMs && !shouldDraw) {
                        shouldDraw = true;

                        hoveredUnit = hoveredCell.getUnit();
                        if (hoveredUnit != null) {
                            hoveredUnitBaseStats = hoveredUnit.getBaseStats();
                            hoveredUnitCombatStats = game.getUnitCombatStats(hoveredUnit.getPosition());
                        }

                        boardPanel.repaint();
                    }
                }
            }
        });
        hoverTimeUpdater.start();
    }

    private void updateHoveredCell(BoardCell newHoveredCell) {
        hoveredCell = newHoveredCell;
        hoveredUnitBaseStats = null;
        hoveredUnitCombatStats = null;
        hoverTimeMs = 0;

        if (shouldDraw) {
            shouldDraw = false;
            boardPanel.repaint();
        }
    }

    public void draw(Graphics2D g) {
        if (!shouldDraw || hoveredUnit == null)
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


        g.drawString("    STATS", x + 1, y + 10);
        g.drawString("Speed: " + hoveredUnitBaseStats.speed, x + 1, y + 20);
        g.drawString("Range: " + hoveredUnitBaseStats.range, x + 1, y + 30);
        g.drawString("Attack: " + hoveredUnitBaseStats.attack, x + 1, y + 40);
        g.drawString("Defense: " + hoveredUnitBaseStats.defense, x + 1, y + 50);


        g.drawString("   COMBAT", x + 1, y + 65);
        g.drawString("Defense: " + hoveredUnitCombatStats.defenseScore, x + 1, y + 75);
        g.drawString("Attack: " + hoveredUnitCombatStats.attackScore, x + 1, y + 85);
    }
}
