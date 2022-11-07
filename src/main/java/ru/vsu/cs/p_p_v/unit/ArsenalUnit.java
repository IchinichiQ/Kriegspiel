package ru.vsu.cs.p_p_v.unit;

import ru.vsu.cs.p_p_v.game.Board;
import ru.vsu.cs.p_p_v.game.Coordinate;
import ru.vsu.cs.p_p_v.game.Teams;

public abstract class ArsenalUnit extends BoardUnit {
    public ArsenalUnit(Board board, Teams team, Coordinate position, UnitStats baseStats) {
        super(board, team, position, baseStats);
    }
}
