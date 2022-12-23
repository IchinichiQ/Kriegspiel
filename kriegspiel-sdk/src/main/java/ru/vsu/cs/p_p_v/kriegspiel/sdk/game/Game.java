package ru.vsu.cs.p_p_v.kriegspiel.sdk.game;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.cell.BoardCell;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.unit.Arsenal;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.unit.BoardUnit;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.unit.RelayUnit;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.unit.UnitCombatStats;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public interface Game {
    public Teams getCurrentTurnTeam();

    public int getLeftMoves();

    public boolean isAttackUsed();

    public void endTurn();

    public int getBoardSizeX();

    public int getBoardSizeY();

    public BoardCell getBoardCell(Coordinate cellCoordinate);

    public boolean unitCanMove(Coordinate unitCoordinate);

    public void moveUnit(Coordinate unitCoordinate, Coordinate destCellCoordinate);

    public boolean unitCanBeCaptured(Coordinate unitCoordinate);

    public UnitCombatStats getUnitCombatStats(Coordinate unitCoordinate);

    public void attackUnit(Coordinate unitCoordinate);

    public void addGameEventListener(GameEventListener gameListener);
}
