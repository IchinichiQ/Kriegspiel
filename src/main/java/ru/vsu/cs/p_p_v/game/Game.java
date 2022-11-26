package ru.vsu.cs.p_p_v.game;

import ru.vsu.cs.p_p_v.cell.BoardCell;
import ru.vsu.cs.p_p_v.unit.Arsenal;
import ru.vsu.cs.p_p_v.unit.BoardUnit;
import ru.vsu.cs.p_p_v.unit.RelayUnit;

import java.util.*;

public class Game {
    private final Board board;

    private Teams currentTurnTeam = Teams.South;
    private int leftMoves = 5;
    private final List<BoardUnit> movedUnits = new ArrayList<>();
    private boolean isAttackUsed = false;

    private final List<GameEventListener> eventListeners = new ArrayList<>();

    public Game(String fieldJsonPath, String unitsJsonPath) {
        board = new Board();

        try {
            board.AppendFieldFromFile(fieldJsonPath);
            board.AppendUnitsFromFile(unitsJsonPath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateConnections();
    }

    public Teams getCurrentTurnTeam() {
        return currentTurnTeam;
    }

    public int getLeftMoves() {
        return leftMoves;
    }

    public boolean isAttackUsed() {
        return isAttackUsed;
    }

    public void nextTurn() {
        leftMoves = 5;
        movedUnits.clear();
        isAttackUsed = false;
        currentTurnTeam = currentTurnTeam == Teams.North ? Teams.South : Teams.North;

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

    public Teams getWinner() {
        boolean isSouthWon = true;
        for (BoardUnit unit : board.getNorthUnits()) {
            if (unit instanceof Arsenal) {
                isSouthWon = false;
                break;
            }
        }
        if (isSouthWon)
            return Teams.South;

        boolean isNorthWon = true;
        for (BoardUnit unit : board.getSouthUnits()) {
            if (unit instanceof Arsenal) {
                isNorthWon = false;
                break;
            }
        }
        if (isNorthWon)
            return Teams.North;

        return Teams.None;
    }

    public boolean unitCanMove(Coordinate unitCoordinate) {
        BoardCell unitCell = board.getCell(unitCoordinate);
        if (unitCell == null)
            return false;

        BoardUnit unit = unitCell.getUnit();

        return leftMoves != 0 && !movedUnits.contains(unit) && unit.hasConnection();
    }

    public MoveUnitResult moveUnit(Coordinate unitCoordinate, Coordinate destCellCoordinate) {
        if (leftMoves == 0) {
            return MoveUnitResult.NoMovementsLeft;
        }

        BoardCell unitCell = board.getCell(unitCoordinate);
        if (unitCell == null) {
            return MoveUnitResult.IncorrectUnitCoordinates;
        }

        BoardUnit unit = unitCell.getUnit();
        if (unit == null) {
            return MoveUnitResult.IncorrectUnitCoordinates;
        }
        if (movedUnits.contains(unit)) {
            return MoveUnitResult.UnitAlreadyMoved;
        }
        if (unit.getTeam() != currentTurnTeam) {
            return MoveUnitResult.UnitInDifferentTeam;
        }
        // TODO: Handle in CLI
        if (!unit.hasConnection())
            return MoveUnitResult.NoConnection;

        int unitSpeed = unit.getBaseStats().speed;
        if (Math.abs(unitCoordinate.x - destCellCoordinate.x) > unitSpeed || Math.abs(unitCoordinate.y - destCellCoordinate.y) > unitSpeed) {
            return MoveUnitResult.UnitLackSpeed;
        }

        if (!unit.move(destCellCoordinate)) {
            return MoveUnitResult.IncorrectCellCoordinates;
        }

        leftMoves--;
        movedUnits.add(unit);
        updateConnections();

        for (GameEventListener listener : eventListeners)
            listener.onUnitMove();

        return MoveUnitResult.Success;
    }

    public boolean unitCanBeCaptured(Coordinate unitCoordinate) {
        BoardCell cell = board.getCell(unitCoordinate);
        BoardUnit cellUnit = cell.getUnit();
        if (cellUnit == null) {
            return false;
        }

        return cellUnit.getAttackScore() - cellUnit.getDefenseScore() >= 2 && !isAttackUsed;
    }

    public AttackUnitResult attackUnit(Coordinate unitCoordinate) {
        if (isAttackUsed)
            return AttackUnitResult.NoAttackLeft;

        BoardCell unitCell = board.getCell(unitCoordinate);
        if (unitCell == null) {
            return AttackUnitResult.IncorrectUnitCoordinates;
        }

        BoardUnit unit = unitCell.getUnit();
        if (unit == null) {
            return AttackUnitResult.IncorrectUnitCoordinates;
        }
        if (unit.getTeam() == currentTurnTeam) {
            return AttackUnitResult.UnitInSameTeam;
        }

        int defenseScore = unit.getDefenseScore();
        int attackScore = unit.getAttackScore();

        AttackUnitResult result = null;
        if (attackScore - defenseScore == 1) {
            result = AttackUnitResult.ForcedRetreat;
        } else if (attackScore - defenseScore >= 2) {
            board.removeUnit(unit);
            updateConnections();
            isAttackUsed = true;
            result = AttackUnitResult.Capture;
        } else {
            result = AttackUnitResult.None;
        }

        for (GameEventListener listener : eventListeners)
            listener.onAttack();

        if (true) {
            for (GameEventListener listener : eventListeners)
                listener.onWin();
        }

        return result;
    }

    public void updateConnections() {
        updateTeamConnections(board.getNorthArsenalUnits(), Teams.North);
        updateTeamConnections(board.getSouthArsenalUnits(), Teams.South);
    }

    private void updateTeamConnections(List<BoardUnit> arsenalList, Teams unitsTeam) {
        for (int y = 0; y < board.ySize; y++) {
            for (int x = 0; x < board.xSize; x++) {
                BoardCell curCell = getBoardCell(new Coordinate(x, y));
                BoardUnit curCellUnit = curCell.getUnit();

                if (curCellUnit != null && curCellUnit.getTeam() == unitsTeam)
                    curCellUnit.setHasConnection(false);

                if (unitsTeam == Teams.North)
                    curCell.resetNorthConnection();
                else
                    curCell.resetSouthConnection();
            }
        }

        Queue<BoardUnit> unitsToProcess = new LinkedList<>(arsenalList);
        // TODO: Не самое красивое решение с листом
        List<RelayUnit> visitedRelays = new ArrayList<>();
        while (!unitsToProcess.isEmpty()) {
            BoardUnit unit = unitsToProcess.poll();
            int x = unit.getPosition().x;
            int y = unit.getPosition().y;


            for (int curX = x; curX < board.xSize; curX++) {
                if (!updateCellConnection(unitsTeam, unitsToProcess, visitedRelays, ConnectionDirection.Horizontal, y, curX)) break;
            }
            for (int curX = x; curX >= 0; curX--) {
                if (!updateCellConnection(unitsTeam, unitsToProcess, visitedRelays, ConnectionDirection.Horizontal, y, curX)) break;
            }
            for (int curY = y; curY < board.ySize; curY++) {
                if (!updateCellConnection(unitsTeam, unitsToProcess, visitedRelays, ConnectionDirection.Vertical, curY, x)) break;
            }
            for (int curY = y; curY >= 0; curY--) {
                if (!updateCellConnection(unitsTeam, unitsToProcess, visitedRelays, ConnectionDirection.Vertical, curY, x)) break;
            }

            for (int curX = x, curY = y; curX >= 0 && curY >= 0; curX--, curY--) {
                if (!updateCellConnection(unitsTeam, unitsToProcess, visitedRelays, ConnectionDirection.DiagonalMajor, curY, curX)) break;
            }
            for (int curX = x, curY = y; curX >= 0 && curY < board.ySize; curX--, curY++) {
                if (!updateCellConnection(unitsTeam, unitsToProcess, visitedRelays, ConnectionDirection.DiagonalMinor, curY, curX)) break;
            }
            for (int curX = x, curY = y; curX < board.xSize && curY < board.ySize; curX++, curY++) {
                if (!updateCellConnection(unitsTeam, unitsToProcess, visitedRelays, ConnectionDirection.DiagonalMajor, curY, curX)) break;
            }
            for (int curX = x, curY = y; curX < board.xSize && curY >= 0; curX++, curY--) {
                if (!updateCellConnection(unitsTeam, unitsToProcess, visitedRelays, ConnectionDirection.DiagonalMinor, curY, curX)) break;
            }
        }
    }

    // TODO: Почему просить инвертировать?
    private boolean updateCellConnection(Teams unitsTeam, Queue<BoardUnit> unitsToProcess, List<RelayUnit> visitedRelays, ConnectionDirection dir, int y, int x) {
        BoardCell cell = board.getCell(new Coordinate(x, y));
        BoardUnit cellUnit = cell.getUnit();
        if (cell.isObstacle() || (cellUnit != null && cellUnit.getTeam() != unitsTeam))
            return false;

        if (unitsTeam == Teams.North)
            cell.addNorthConnection(dir);
        else
            cell.addSouthConnection(dir);

        if (cellUnit != null) {
            if (!cellUnit.hasConnection()) {
                // TODO: Refactor!!
                List<BoardUnit> friendlyUnits = null;
                switch (cellUnit.getTeam()) {
                    case South -> friendlyUnits = board.getSouthUnits();
                    case North -> friendlyUnits = board.getNorthUnits();
                }

                Queue<BoardUnit> unitsToCheck = new LinkedList<>();
                unitsToCheck.add(cellUnit);

                while (!unitsToCheck.isEmpty()) {
                    BoardUnit curUnit = unitsToCheck.poll();
                    curUnit.setHasConnection(true);

                    Coordinate unitPos = curUnit.getPosition();
                    for (BoardUnit nextUnit : friendlyUnits) {
                        if (nextUnit == curUnit)
                            continue;

                        Coordinate nextUnitPos = nextUnit.getPosition();
                        int range = 1;
                        if (unitPos.x == nextUnitPos.x || unitPos.y == nextUnitPos.y || Math.abs(unitPos.x - nextUnitPos.x) == Math.abs(unitPos.y - nextUnitPos.y)) {
                            if (Math.abs(unitPos.x - nextUnitPos.x) <= range && Math.abs(unitPos.y - nextUnitPos.y) <= range) {
                                if (!nextUnit.hasConnection()) {
                                    unitsToCheck.add(nextUnit);
                                }
                            }
                        }
                    }
                }
            }

            if (cellUnit instanceof RelayUnit && !visitedRelays.contains(cellUnit)) {
                visitedRelays.add((RelayUnit) cellUnit);
                unitsToProcess.add(cellUnit);
            }
        }

        return true;
    }

    public void addGameEventListener(GameEventListener gameListener)
    {
        this.eventListeners.add(gameListener);
    }
}
