package ru.vsu.cs.p_p_v.kriegspiel.sdk.unit;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.Board;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.Coordinate;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.Teams;

public abstract class ArsenalUnit extends BoardUnit {
    public ArsenalUnit(Board board, Teams team, Coordinate position, UnitStats baseStats) {
        super(board, team, position, baseStats);
    }
}
