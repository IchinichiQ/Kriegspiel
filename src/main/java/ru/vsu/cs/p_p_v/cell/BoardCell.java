package ru.vsu.cs.p_p_v.cell;

import ru.vsu.cs.p_p_v.cache.ImageFileCached;
import ru.vsu.cs.p_p_v.game.ConnectionDirection;
import ru.vsu.cs.p_p_v.game.Coordinate;
import ru.vsu.cs.p_p_v.game.Teams;
import ru.vsu.cs.p_p_v.unit.BoardUnit;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public abstract class BoardCell {
    private Coordinate coordinate;
    private BoardUnit unit = null;
    private final boolean isObstacle;
    private boolean hasNorthConnection = false;
    private List<ConnectionDirection> northConnectionsDirections = new ArrayList<>();
    private boolean hasSouthConnection = false;
    private List<ConnectionDirection> southConnectionsDirections = new ArrayList<>();
    private final Color evenCellColor;
    private final Color oddCellColor;

    private int defenseBuff = 0;

    public BoardCell(Coordinate coordinate, boolean isObstacle, Color evenCellColor, Color oddCellColor, int defenseBuff) {
        this.coordinate = coordinate;
        this.isObstacle = isObstacle;
        this.evenCellColor = evenCellColor;
        this.oddCellColor = oddCellColor;
        this.defenseBuff = defenseBuff;
    }

    public BoardCell(Coordinate coordinate, boolean isObstacle) {
        this.coordinate = coordinate;
        this.isObstacle = isObstacle;
        this.evenCellColor = null;
        this.oddCellColor = null;
    }

    public abstract String getStringRepresentation();

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public boolean isObstacle() {
        return isObstacle;
    }

    public Color getEvenCellColor() {
        return evenCellColor;
    }

    public Color getOddCellColor() {
        return oddCellColor;
    }

    public Image getBackgroundImage() {return null;}

    public List<Image> getConnectionImages() {
        List<Image> images = new ArrayList<>();

        if (isObstacle || (!hasNorthConnection && !hasSouthConnection))
            return images;

        // TODO: Refactor
        for (ConnectionDirection connectionDir : northConnectionsDirections) {
            String imgPath = String.format("images/connections/north%s.png", connectionDir.toString());

            try {
                images.add(ImageFileCached.readImage(new File(imgPath)));
            } catch (Exception ex) {
            }
        }

        for (ConnectionDirection connectionDir : southConnectionsDirections) {
            String imgPath = String.format("images/connections/south%s.png", connectionDir.toString());

            try {
                images.add(ImageFileCached.readImage(new File(imgPath)));
            } catch (Exception ex) {
            }
        }

        return images;
    }

    public int getDefenseBuff() {
        return defenseBuff;
    }

    public BoardUnit getUnit() {
        return unit;
    }

    public void setUnit(BoardUnit unit) {
        this.unit = unit;
    }

    // TODO: Don't really like connection api
    public boolean hasNorthConnection() {
        return hasNorthConnection;
    }

    public void resetNorthConnection() {
        this.hasNorthConnection = false;
        northConnectionsDirections.clear();
    }

    public List<ConnectionDirection> getNorthConnectionsDirections() {
        return Collections.unmodifiableList(northConnectionsDirections);
    }

    public void addNorthConnection(ConnectionDirection connectionDirection) {
        this.hasNorthConnection = true;
        northConnectionsDirections.add(connectionDirection);
    }

    public boolean hasSouthConnection() {
        return hasSouthConnection;
    }

    public void resetSouthConnection() {
        this.hasSouthConnection = false;
        southConnectionsDirections.clear();
    }

    public List<ConnectionDirection> getSouthConnectionsDirections() {
        return Collections.unmodifiableList(southConnectionsDirections);
    }

    public void addSouthConnection(ConnectionDirection connectionDirection) {
        this.hasSouthConnection = true;
        southConnectionsDirections.add(connectionDirection);
    }
}
