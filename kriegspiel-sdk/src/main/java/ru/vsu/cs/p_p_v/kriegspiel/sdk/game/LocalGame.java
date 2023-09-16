package ru.vsu.cs.p_p_v.kriegspiel.sdk.game;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.cell.BoardCell;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.unit.*;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.unit.stats.UnitBaseStats;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.unit.stats.UnitCombatStats;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class LocalGame implements Game {
    protected final Board board;

    private Team currentTurnTeam = Team.South;
    private int leftMoves = 5;
    private final List<BoardUnit> movedUnits = new ArrayList<>();
    private boolean isAttackUsed = false;

    private final List<GameEventListener> eventListeners = new ArrayList<>();

    public LocalGame(Path fieldJson, Path unitsJson) {
        board = new Board();

        try {
            board.appendFieldFromFile(fieldJson);
            board.appendUnitsFromFile(unitsJson);
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateConnections();
    }

    public Team getCurrentTurnTeam() {
        return currentTurnTeam;
    }

    @Override
    public Team getMyTeam() {
        return currentTurnTeam;
    }

    @Override
    public boolean isOnlineGame() {
        return false;
    }

    public int getLeftMoves() {
        return leftMoves;
    }

    public boolean isAttackUsed() {
        return isAttackUsed;
    }

    public void endTurn() {
        leftMoves = 5;
        movedUnits.clear();
        isAttackUsed = false;
        currentTurnTeam = currentTurnTeam == Team.North ? Team.South : Team.North;

        for (GameEventListener listener : eventListeners)
            listener.onNextTurn();
    }

    public int getBoardSizeX() {
        return board.xSize;
    }

    public int getBoardSizeY() {
        return board.ySize;
    }

    public BoardCell getBoardCell(Coordinate cellCoordinate) {
        return board.getCell(cellCoordinate);
    }

    private Team getWinner() {
        boolean isSouthWon = true;
        for (BoardUnit unit : board.getNorthUnits()) {
            if (unit instanceof Arsenal) {
                isSouthWon = false;
                break;
            }
        }
        if (isSouthWon)
            return Team.South;

        boolean isNorthWon = true;
        for (BoardUnit unit : board.getSouthUnits()) {
            if (unit instanceof Arsenal) {
                isNorthWon = false;
                break;
            }
        }
        if (isNorthWon)
            return Team.North;

        return Team.None;
    }

    public boolean unitCanMove(Coordinate unitCoordinate) {
        BoardCell unitCell = board.getCell(unitCoordinate);
        if (unitCell == null)
            return false;

        BoardUnit unit = unitCell.getUnit();

        return leftMoves != 0 && !movedUnits.contains(unit) && unit.hasConnection();
    }

    public void moveUnit(Coordinate unitCoordinate, Coordinate destCellCoordinate) {
        if (leftMoves == 0) {
            triggerMoveEvent(MoveUnitResult.NoMovementsLeft);
            return;
        }

        BoardCell unitCell = board.getCell(unitCoordinate);
        if (unitCell == null) {
            triggerMoveEvent(MoveUnitResult.IncorrectUnitCoordinates);
            return;
        }

        BoardUnit unit = unitCell.getUnit();
        if (unit == null) {
            triggerMoveEvent(MoveUnitResult.IncorrectUnitCoordinates);
            return;
        }
        if (movedUnits.contains(unit)) {
            triggerMoveEvent(MoveUnitResult.UnitAlreadyMoved);
            return;
        }
        if (unit.getTeam() != currentTurnTeam) {
            triggerMoveEvent(MoveUnitResult.UnitInDifferentTeam);
            return;
        }
        if (!unit.hasConnection()) {
            triggerMoveEvent(MoveUnitResult.NoConnection);
            return;
        }


        int unitSpeed = unit.getBaseStats().speed;
        if (Math.abs(unitCoordinate.x - destCellCoordinate.x) > unitSpeed || Math.abs(unitCoordinate.y - destCellCoordinate.y) > unitSpeed) {
            triggerMoveEvent(MoveUnitResult.UnitLackSpeed);
            return;
        }

        if (!unit.move(destCellCoordinate)) {
            triggerMoveEvent(MoveUnitResult.IncorrectCellCoordinates);
            return;
        }

        leftMoves--;
        movedUnits.add(unit);
        updateConnections();

        triggerMoveEvent(MoveUnitResult.Success);
    }

    private void triggerMoveEvent(MoveUnitResult result) {
        for (GameEventListener listener : eventListeners)
            listener.onUnitMove(result);
    }

    public boolean unitCanBeCaptured(Coordinate unitCoordinate) {
        BoardCell cell = board.getCell(unitCoordinate);
        BoardUnit cellUnit = cell.getUnit();
        if (cellUnit == null) {
            return false;
        }

        return getUnitAttackScore(cellUnit) - getUnitDefenseScore(cellUnit) >= 2 && !isAttackUsed;
    }

    @Override
    public UnitCombatStats getUnitCombatStats(Coordinate unitCoordinate) {
        BoardCell unitCell = board.getCell(unitCoordinate);
        if (unitCell == null) {
            return null;
        }

        BoardUnit unit = unitCell.getUnit();
        if (unit == null) {
            return null;
        }

        return new UnitCombatStats(getUnitDefenseScore(unit), getUnitAttackScore(unit));
    }

    private int getUnitDefenseScore(BoardUnit unit) {
        List<BoardUnit> friendlyUnits = new ArrayList<>();
        switch (unit.getTeam()) {
            case South -> friendlyUnits = board.getSouthUnits();
            case North -> friendlyUnits = board.getNorthUnits();
        }

        Coordinate unitPos = unit.getPosition();
        int defenseScore = unit.hasConnection() ? unit.getBaseStats().defense : 0;
        defenseScore += unit.getDefenseBuff();

        for (BoardUnit nextUnit : friendlyUnits) {
            if (nextUnit == unit || !nextUnit.hasConnection())
                continue;

            Coordinate nextUnitPos = nextUnit.getPosition();
            UnitBaseStats nextUnitBaseStats = nextUnit.getBaseStats();

            int nextUnitRange = nextUnitBaseStats.range;
            if (unitPos.x == nextUnitPos.x || unitPos.y == nextUnitPos.y || Math.abs(unitPos.x - nextUnitPos.x) == Math.abs(unitPos.y - nextUnitPos.y)) {
                if (Math.abs(unitPos.x - nextUnitPos.x) <= nextUnitRange && Math.abs(unitPos.y - nextUnitPos.y) <= nextUnitRange) {
                    defenseScore += nextUnitBaseStats.defense;
                }
            }
        }

        return defenseScore;
    }

    private int getUnitAttackScore(BoardUnit unit) {
        List<BoardUnit> hostileUnits = new ArrayList<>();
        switch (unit.getTeam()) {
            case South -> hostileUnits = board.getNorthUnits();
            case North -> hostileUnits = board.getSouthUnits();
        }

        Coordinate unitPos = unit.getPosition();
        int attackScore = 0;
        for (BoardUnit nextUnit : hostileUnits) {
            if (!nextUnit.hasConnection())
                continue;

            Coordinate nextUnitPos = nextUnit.getPosition();
            UnitBaseStats nextUnitBaseStats = nextUnit.getBaseStats();

            int nextUnitRange = nextUnitBaseStats.range;
            if (unitPos.x == nextUnitPos.x || unitPos.y == nextUnitPos.y || Math.abs(unitPos.x - nextUnitPos.x) == Math.abs(unitPos.y - nextUnitPos.y)) {
                if (Math.abs(unitPos.x - nextUnitPos.x) <= nextUnitRange && Math.abs(unitPos.y - nextUnitPos.y) <= nextUnitRange) {
                    attackScore += nextUnitBaseStats.attack;
                }
            }
        }

        return attackScore;
    }

    public void attackUnit(Coordinate unitCoordinate) {
        if (isAttackUsed) {
            triggerAttackEvent(AttackUnitResult.NoAttackLeft);
            return;
        }

        BoardCell unitCell = board.getCell(unitCoordinate);
        if (unitCell == null) {
            triggerAttackEvent(AttackUnitResult.IncorrectUnitCoordinates);
            return;
        }

        BoardUnit unit = unitCell.getUnit();
        if (unit == null) {
            triggerAttackEvent(AttackUnitResult.IncorrectUnitCoordinates);
            return;
        }
        if (unit.getTeam() == currentTurnTeam) {
            triggerAttackEvent(AttackUnitResult.UnitInSameTeam);
            return;
        }

        UnitCombatStats combatStats = getUnitCombatStats(unit.getPosition());

        AttackUnitResult result;
        if (combatStats.attackScore - combatStats.defenseScore == 1) {
            result = AttackUnitResult.ForcedRetreat;
        } else if (combatStats.attackScore - combatStats.defenseScore >= 2) {
            board.removeUnit(unit);
            updateConnections();
            isAttackUsed = true;
            result = AttackUnitResult.Capture;
        } else {
            result = AttackUnitResult.None;
        }

        triggerAttackEvent(result);

        Team winner = getWinner();
        if (winner != Team.None) {
            for (GameEventListener listener : eventListeners)
                listener.onWin(winner);
        }
    }

    private void triggerAttackEvent(AttackUnitResult result) {
        for (GameEventListener listener : eventListeners)
            listener.onAttack(result);
    }

    private void updateConnections() {
        updateTeamConnections(board.getNorthArsenalUnits(), Team.North);
        updateTeamConnections(board.getSouthArsenalUnits(), Team.South);
    }

    private void updateTeamConnections(List<BoardUnit> arsenalList, Team unitsTeam) {
        board.clearCellConnections(unitsTeam);

        Queue<BoardUnit> unitsToProcess = new LinkedList<>(arsenalList);
        List<RelayUnit> visitedRelays = new ArrayList<>();
        while (!unitsToProcess.isEmpty()) {
            BoardUnit unit = unitsToProcess.poll();
            int x = unit.getPosition().x;
            int y = unit.getPosition().y;

            for (int curX = x; curX < board.xSize; curX++) {
                if (!updateCellConnection(unitsTeam, unitsToProcess, visitedRelays, ConnectionDirection.Horizontal, y, curX))
                    break;
            }
            for (int curX = x; curX >= 0; curX--) {
                if (!updateCellConnection(unitsTeam, unitsToProcess, visitedRelays, ConnectionDirection.Horizontal, y, curX))
                    break;
            }
            for (int curY = y; curY < board.ySize; curY++) {
                if (!updateCellConnection(unitsTeam, unitsToProcess, visitedRelays, ConnectionDirection.Vertical, curY, x))
                    break;
            }
            for (int curY = y; curY >= 0; curY--) {
                if (!updateCellConnection(unitsTeam, unitsToProcess, visitedRelays, ConnectionDirection.Vertical, curY, x))
                    break;
            }

            for (int curX = x, curY = y; curX >= 0 && curY >= 0; curX--, curY--) {
                if (!updateCellConnection(unitsTeam, unitsToProcess, visitedRelays, ConnectionDirection.DiagonalMajor, curY, curX))
                    break;
            }
            for (int curX = x, curY = y; curX >= 0 && curY < board.ySize; curX--, curY++) {
                if (!updateCellConnection(unitsTeam, unitsToProcess, visitedRelays, ConnectionDirection.DiagonalMinor, curY, curX))
                    break;
            }
            for (int curX = x, curY = y; curX < board.xSize && curY < board.ySize; curX++, curY++) {
                if (!updateCellConnection(unitsTeam, unitsToProcess, visitedRelays, ConnectionDirection.DiagonalMajor, curY, curX))
                    break;
            }
            for (int curX = x, curY = y; curX < board.xSize && curY >= 0; curX++, curY--) {
                if (!updateCellConnection(unitsTeam, unitsToProcess, visitedRelays, ConnectionDirection.DiagonalMinor, curY, curX))
                    break;
            }
        }
    }

    private boolean updateCellConnection(Team unitsTeam, Queue<BoardUnit> unitsToProcess, List<RelayUnit> visitedRelays, ConnectionDirection dir, int y, int x) {
        BoardCell cell = board.getCell(new Coordinate(x, y));
        BoardUnit cellUnit = cell.getUnit();
        if (cell.isObstacle() || (cellUnit != null && cellUnit.getTeam() != unitsTeam))
            return false;

        if (cellUnit != null) {
            if (!cellUnit.hasConnection()) {
                List<BoardUnit> friendlyUnits = new ArrayList<>();
                switch (cellUnit.getTeam()) {
                    case South -> friendlyUnits = board.getSouthUnits();
                    case North -> friendlyUnits = board.getNorthUnits();
                }

                Queue<BoardUnit> unitsToCheck = new LinkedList<>();
                unitsToCheck.add(cellUnit);

                while (!unitsToCheck.isEmpty()) {
                    BoardUnit curUnit = unitsToCheck.poll();
                    Coordinate unitPos = curUnit.getPosition();
                    BoardCell curCell = getBoardCell(unitPos);
                    if (unitsTeam == Team.North)
                        curCell.setHasNorthConnection(true);
                    else
                        curCell.setHasSouthConnection(true);

                    for (BoardUnit nextUnit : friendlyUnits) {
                        if (nextUnit == curUnit)
                            continue;

                        Coordinate nextUnitPos = nextUnit.getPosition();
                        int range = 1;
                        if ((unitPos.x == nextUnitPos.x || unitPos.y == nextUnitPos.y || Math.abs(unitPos.x - nextUnitPos.x) == Math.abs(unitPos.y - nextUnitPos.y))
                                && Math.abs(unitPos.x - nextUnitPos.x) <= range && Math.abs(unitPos.y - nextUnitPos.y) <= range
                                && !nextUnit.hasConnection()) {
                            unitsToCheck.add(nextUnit);
                        }
                    }
                }
            }

            if (cellUnit instanceof RelayUnit && !visitedRelays.contains(cellUnit)) {
                visitedRelays.add((RelayUnit) cellUnit);
                unitsToProcess.add(cellUnit);
            }
        }

        cell.addConnection(dir, unitsTeam);
        return true;
    }

    public void addGameEventListener(GameEventListener gameListener) {
        this.eventListeners.add(gameListener);
    }
}
