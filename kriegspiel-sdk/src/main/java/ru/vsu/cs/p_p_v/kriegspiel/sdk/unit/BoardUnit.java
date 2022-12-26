package ru.vsu.cs.p_p_v.kriegspiel.sdk.unit;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.cell.BoardCell;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.Board;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.Coordinate;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.Teams;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.unit.stats.UnitBaseStats;

import java.awt.*;

abstract public class BoardUnit {
    private transient Board board;
    private Coordinate position;
    private Teams team;
    private final UnitBaseStats baseStats;
    private boolean hasConnection;

    public BoardUnit(Board board, Teams team, Coordinate position, UnitBaseStats baseStats) {
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

    public abstract Image getImageRepresentation();

    public Coordinate getPosition() {
        return this.position;
    }

    public UnitBaseStats getBaseStats() {
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

    public boolean hasConnection() {
        return hasConnection;
    }

    public void setHasConnection(boolean hasConnection) {
        this.hasConnection = hasConnection;
    }
}
