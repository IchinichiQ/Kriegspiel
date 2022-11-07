package ru.vsu.cs.p_p_v.unit;

import ru.vsu.cs.p_p_v.cell.BoardCell;
import ru.vsu.cs.p_p_v.game.Board;
import ru.vsu.cs.p_p_v.game.Coordinate;
import ru.vsu.cs.p_p_v.game.Teams;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

abstract public class BoardUnit {
    private Board board;
    private Coordinate position;
    private Teams team;

    private final UnitStats baseStats;

    private boolean hasConnection;
    private boolean isArsenal;
    private boolean isRelay;

    public BoardUnit(Board board, Teams team, Coordinate position, UnitStats baseStats) {
        this.board = board;
        this.team = team;
        this.position = position;

        this.baseStats = baseStats;

        switch (this.team) {
            case North -> board.addNorthUnit(this);
            case South -> board.addSouthUnit(this);
        }

        move(position);
    }

    public abstract String getStringRepresentation();

    public abstract Image getImageRepresentation();

    public Coordinate getPosition() {
        return this.position;
    }

    public UnitStats getBaseStats() {
        return this.baseStats;
    }

    public int getDefenseBuff() {
        return board.getCell(position).getDefenseBuff();
    }

    public Teams getTeam() {
        return this.team;
    }

    public boolean move(Coordinate moveTo) {
        BoardCell cellMoveFrom = board.getCell(position);
        BoardCell cellMoveTo = board.getCell(moveTo);
        if (cellMoveTo == null || cellMoveTo.getUnit() != null || cellMoveTo.isObstacle()) {
            return false;
        }

        this.position = moveTo;
        cellMoveFrom.setUnit(null);
        cellMoveTo.setUnit(this);

        return true;
    }

    public int getDefenseScore() {
        List<BoardUnit> friendlyUnits = null;
        switch (getTeam()) {
            case South -> friendlyUnits = board.getSouthUnits();
            case North -> friendlyUnits = board.getNorthUnits();
        }

        Coordinate unitPos = getPosition();
        int defenseScore = hasConnection() ? baseStats.defense : 0;
        defenseScore += getDefenseBuff();

        for (BoardUnit nextUnit : friendlyUnits) {
            if (nextUnit == this || !nextUnit.hasConnection())
                continue;

            Coordinate nextUnitPos = nextUnit.getPosition();
            int nextUnitRange = baseStats.range;
            if (unitPos.x == nextUnitPos.x || unitPos.y == nextUnitPos.y || Math.abs(unitPos.x - nextUnitPos.x) == Math.abs(unitPos.y - nextUnitPos.y)) {
                if (Math.abs(unitPos.x - nextUnitPos.x) <= nextUnitRange && Math.abs(unitPos.y - nextUnitPos.y) <= nextUnitRange) {
                    defenseScore += baseStats.defense;
                }
            }
        }

        return defenseScore;
    }

    public int getAttackScore() {
        List<BoardUnit> hostileUnits = null;
        switch (getTeam()) {
            case South -> hostileUnits = board.getNorthUnits();
            case North -> hostileUnits = board.getSouthUnits();
        }

        Coordinate unitPos = getPosition();
        int attackScore = 0;
        for (BoardUnit nextUnit : hostileUnits) {
            if (!nextUnit.hasConnection())
                continue;

            Coordinate nextUnitPos = nextUnit.getPosition();
            int nextUnitRange = nextUnit.baseStats.range;
            if (unitPos.x == nextUnitPos.x || unitPos.y == nextUnitPos.y || Math.abs(unitPos.x - nextUnitPos.x) == Math.abs(unitPos.y - nextUnitPos.y)) {
                if (Math.abs(unitPos.x - nextUnitPos.x) <= nextUnitRange && Math.abs(unitPos.y - nextUnitPos.y) <= nextUnitRange) {
                    attackScore += nextUnit.baseStats.attack;
                }
            }
        }

        return attackScore;
    }

    public boolean hasConnection() {
        return hasConnection;
    }

    // TODO: Должно вызываться толкьо из BoardCell, но сейчас может отовсюду
    public void setHasConnection(boolean hasConnection) {
        this.hasConnection = hasConnection;
    }
}
