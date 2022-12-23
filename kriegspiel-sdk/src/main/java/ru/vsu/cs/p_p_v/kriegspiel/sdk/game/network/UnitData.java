package ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.Coordinate;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.unit.UnitCombatStats;

public class UnitData {
    public final String type;
    public final Coordinate position;
    public final boolean canMove;
    public final boolean canBeCaptured;
    public final UnitCombatStats combatStats;

    public UnitData(String type, Coordinate position, boolean canMove, boolean canBeCaptured, UnitCombatStats combatStats) {
        this.type = type;
        this.position = position;
        this.canMove = canMove;
        this.canBeCaptured = canBeCaptured;
        this.combatStats = combatStats;
    }
}
