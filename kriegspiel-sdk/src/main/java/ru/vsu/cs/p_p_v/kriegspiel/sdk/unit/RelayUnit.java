package ru.vsu.cs.p_p_v.kriegspiel.sdk.unit;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.Board;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.Coordinate;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.Teams;

public abstract class RelayUnit extends BoardUnit {
    public RelayUnit(Board board, Teams team, Coordinate position, UnitStats baseStats) {
        super(board, team, position, baseStats);
    }

    @Override
    public boolean hasConnection() {
        return true;
    }
}
