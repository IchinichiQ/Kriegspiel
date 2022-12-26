package ru.vsu.cs.p_p_v.kriegspiel.sdk.cell;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.cache.ImageFileCached;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.ConnectionDirection;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.Coordinate;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.Teams;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.unit.BoardUnit;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BoardCell {
    private final Coordinate coordinate;
    private BoardUnit unit = null;
    private final boolean isObstacle;
    private boolean hasNorthConnection = false;
    private final List<ConnectionDirection> northConnectionsDirections = new ArrayList<>();
    private boolean hasSouthConnection = false;
    private final List<ConnectionDirection> southConnectionsDirections = new ArrayList<>();

    private int defenseBuff = 0;

    public BoardCell(Coordinate coordinate, boolean isObstacle, int defenseBuff) {
        this(coordinate, isObstacle);
        this.defenseBuff = defenseBuff;
    }

    public BoardCell(Coordinate coordinate, boolean isObstacle) {
        this.coordinate = coordinate;
        this.isObstacle = isObstacle;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public boolean isObstacle() {
        return isObstacle;
    }

    public Image getBackgroundImage() {return null;}

    public List<Image> getConnectionImages() {
        List<Image> images = new ArrayList<>();

        if (isObstacle || (!hasNorthConnection && !hasSouthConnection))
            return images;

        for (ConnectionDirection connectionDir : northConnectionsDirections) {
            String imgPath = String.format("images/connections/north%s.png", connectionDir.toString());
            images.add(ImageFileCached.readImage(new File(imgPath)));
        }

        for (ConnectionDirection connectionDir : southConnectionsDirections) {
            String imgPath = String.format("images/connections/south%s.png", connectionDir.toString());
            images.add(ImageFileCached.readImage(new File(imgPath)));
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

    public void addConnection(ConnectionDirection connectionDirection, Teams team) {
        if (team == Teams.North) {
            setHasNorthConnection(true);
            northConnectionsDirections.add(connectionDirection);
        } else {
            setHasSouthConnection(true);
            southConnectionsDirections.add(connectionDirection);
        }
    }

    public boolean hasNorthConnection() {
        return hasNorthConnection;
    }

    public void resetNorthConnection() {
        setHasNorthConnection(false);
        northConnectionsDirections.clear();
    }

    public List<ConnectionDirection> getNorthConnectionsDirections() {
        return Collections.unmodifiableList(northConnectionsDirections);
    }

    public boolean hasSouthConnection() {
        return hasSouthConnection;
    }

    public void resetSouthConnection() {
        setHasSouthConnection(false);
        southConnectionsDirections.clear();
    }

    public List<ConnectionDirection> getSouthConnectionsDirections() {
        return Collections.unmodifiableList(southConnectionsDirections);
    }

    public void setHasNorthConnection(boolean hasNorthConnection) {
        this.hasNorthConnection = hasNorthConnection;

        if (unit != null && unit.getTeam() == Teams.North) {
            unit.setHasConnection(hasNorthConnection);
        }
    }

    public void setHasSouthConnection(boolean hasSouthConnection) {
        this.hasSouthConnection = hasSouthConnection;

        if (unit != null && unit.getTeam() == Teams.South) {
            unit.setHasConnection(hasSouthConnection);
        }
    }
}
