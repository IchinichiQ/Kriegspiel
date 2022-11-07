package ru.vsu.cs.p_p_v.game;

public enum MoveUnitResult {
    Success,
    NoMovementsLeft,
    UnitAlreadyMoved,
    UnitLackSpeed,
    UnitInDifferentTeam,
    IncorrectUnitCoordinates,
    IncorrectCellCoordinates,
    NoConnection
}
