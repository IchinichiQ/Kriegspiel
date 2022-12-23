package ru.vsu.cs.p_p_v.kriegspiel.client.gui;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.cell.BoardCell;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.*;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.Coordinate;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.Game;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.GameEventListener;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.unit.BoardUnit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.util.List;

public class BoardPanel extends JPanel {
    private final Game game;
    private final int boardHeight;
    private final int boardWidth;
    private final int cellSideLength;

    private int panelWidth;
    private int panelHeight;

    private final UnitStatsOverlay overlay;
    private BoardCell selectedUnitCell = null;

    public BoardPanel(Game game) {
        this.game = game;

        double boardProportion = (double) game.getBoardSizeX() / game.getBoardSizeY();
        this.boardHeight = 500;
        this.boardWidth = (int) (boardHeight * boardProportion);
        this.cellSideLength = boardWidth / game.getBoardSizeX();

        game.addGameEventListener(new GameEventListener() {
            @Override
            public void onNextTurn() {
                // TODO: Remove after debug
                repaint();

                selectedUnitCell = null;
            }

            @Override
            public void onUnitMove(MoveUnitResult result) {
                if (result == MoveUnitResult.Success)
                    repaint();
            }

            @Override
            public void onAttack(AttackUnitResult result) {
                if (result == AttackUnitResult.Capture)
                    repaint();
            }
        });

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                BoardCell cell = getCellByPanelPoint(e.getPoint());
                BoardUnit cellUnit = cell.getUnit();

                if (e.getButton() == MouseEvent.BUTTON1) {
                    // Left button click
                    if (cellUnit != null && cellUnit.getTeam() == game.getCurrentTurnTeam()) {
                        if (cell != selectedUnitCell)
                            selectedUnitCell = cell;
                        else
                            selectedUnitCell = null;
                    } else if (selectedUnitCell != null) {
                        game.moveUnit(selectedUnitCell.getCoordinate(), cell.getCoordinate());
                        selectedUnitCell = null;
                    }

                    repaint();
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    // Right button click
                    if (cellUnit != null && cellUnit.getTeam() != game.getCurrentTurnTeam()) {
                        game.attackUnit(cellUnit.getPosition());

                        repaint();
                    }
                }
            }
        });

        overlay = new UnitStatsOverlay(this);
        this.setBackground(Color.black);
    }

    public BoardCell getCellByPanelPoint(Point cellPoint) {
        Point worldCellPoint = pointScreenToWorld(cellPoint);

        int cellX = worldCellPoint.x / cellSideLength;
        int cellY = worldCellPoint.y / cellSideLength;

        return game.getBoardCell(new Coordinate(cellX, cellY));
    }

    public Point pointScreenToWorld(Point worldPoint) {
        double proportionWidth = (double) boardWidth / panelWidth;
        double proportionHeight = (double) boardHeight / panelHeight;

        return new Point((int) (worldPoint.x * proportionWidth), (int) (worldPoint.y * proportionHeight));
    }

    public void drawBoard(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        int cellImageSideLength = (int) (cellSideLength * 0.9);
        int cellImageOffset = (cellSideLength - cellImageSideLength) / 2;

        int cellHighlightSideLength = (int) (cellSideLength * 0.3);
        int cellHighlightOffset = (cellSideLength - cellHighlightSideLength) / 2;

        int selectedUnitSpeed = 0;
        Coordinate selectedUnitCoordinate = null;
        Color highlightColor = null;
        if (selectedUnitCell != null) {
            BoardUnit selectedUnit = selectedUnitCell.getUnit();

            selectedUnitSpeed = selectedUnit.getBaseStats().speed;
            selectedUnitCoordinate = selectedUnitCell.getCoordinate();
            highlightColor = game.unitCanMove(selectedUnitCoordinate) ? new Color(0, 204, 0, 130) : new Color(204, 0, 0, 130);
        }

        for (int cellRow = 0; cellRow < game.getBoardSizeY(); cellRow++) {
            for (int cellColumn = 0; cellColumn < game.getBoardSizeX(); cellColumn++) {
                BoardCell curCell = game.getBoardCell(new Coordinate(cellColumn, cellRow));
                BoardUnit curUnit = curCell.getUnit();
                int curCellX = cellSideLength * cellColumn;
                int curCellY = cellSideLength * cellRow;

                //TODO Вынести цвета в отдельный класс
                if ((cellRow + cellColumn) % 2 == 0) {
                    g2d.setColor(new Color(232, 232, 232));
                } else {
                    g2d.setColor(new Color(195, 195, 195));
                }
                g2d.fillRect(curCellX, curCellY, cellSideLength, cellSideLength);

                if (selectedUnitCell == curCell) {
                    g2d.setColor(highlightColor);
                    g2d.fillRect(curCellX, curCellY, cellSideLength, cellSideLength);
                }

                if (curCell.getBackgroundImage() != null)
                    g2d.drawImage(curCell.getBackgroundImage(), curCellX, curCellY, cellSideLength, cellSideLength, null);

                if (curUnit != null && curUnit.getTeam() != game.getCurrentTurnTeam()) {
                    if (game.unitCanBeCaptured(curUnit.getPosition())) {
                        g2d.setColor(new Color(0, 0, 204, 130));
                        g2d.fillRect(curCellX, curCellY, cellSideLength, cellSideLength);
                    }
                }

                List<Image> connectionImages = curCell.getConnectionImages();
                if (!connectionImages.isEmpty()) {
                    for (Image connection : connectionImages)
                        g2d.drawImage(connection, curCellX, curCellY, cellSideLength, cellSideLength, null);
                }

                if (curUnit != null) {
                    Image unitImage = curUnit.getImageRepresentation();

                    if (!curUnit.hasConnection()) {
                        ImageFilter filter = new GrayFilter(true, 50);
                        ImageProducer producer = new FilteredImageSource(unitImage.getSource(), filter);
                        unitImage = Toolkit.getDefaultToolkit().createImage(producer);
                    }

                    g2d.drawImage(unitImage, curCellX + cellImageOffset, curCellY + cellImageOffset,
                            cellImageSideLength, cellImageSideLength, null);
                }

                if (selectedUnitCoordinate != null && !curCell.isObstacle() && curCell.getUnit() == null
                        && (Math.abs(selectedUnitCoordinate.x - cellColumn) <= selectedUnitSpeed
                        && Math.abs(selectedUnitCoordinate.y - cellRow) <= selectedUnitSpeed)) {
                    g2d.setColor(highlightColor);
                    g2d.fillOval(curCellX + cellHighlightOffset, curCellY + cellHighlightOffset,
                            cellHighlightSideLength, cellHighlightSideLength);
                }
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        // Relies on being the only component
        // in a layout that will center it without
        // expanding it to fill all the space.
        Dimension d = this.getParent().getSize();

        double preferredProportion = (double) game.getBoardSizeX() / game.getBoardSizeY();
        if ((double) d.width / d.height > preferredProportion) {
            // Width is bigger
            panelHeight = d.height;
            panelWidth = (int) (d.height * preferredProportion);
        } else {
            // Height is bigger
            panelWidth = d.width;
            panelHeight = (int) (d.width * (1 / preferredProportion));
        }

        return new Dimension(panelWidth, panelHeight);
    }

    private int drawNum = 0;
    @Override
    protected void paintComponent(Graphics g) {
        long startTime = System.nanoTime();

        Graphics2D g2d = (Graphics2D) g;
        g2d.scale((double) panelWidth / boardWidth, (double) panelHeight / boardHeight);

        super.paintComponent(g2d);
        drawBoard(g2d);
        overlay.draw(g2d);

        System.out.printf("Draw #%d: %d ms\n", drawNum++, (System.nanoTime() - startTime) / 1000000);
    }

    public Game getGame() {
        return game;
    }
}
