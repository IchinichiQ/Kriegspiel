package ru.vsu.cs.p_p_v.kriegspiel.sdk.unit;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.Board;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.Coordinate;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.Teams;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.unit.stats.UnitBaseStats;

public abstract class ArsenalUnit extends BoardUnit {
    public ArsenalUnit(Board board, Teams team, Coordinate position, UnitBaseStats baseStats) {
        super(board, team, position, baseStats);
    }
}
