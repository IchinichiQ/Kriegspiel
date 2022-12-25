package ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.Coordinate;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.Teams;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.unit.stats.UnitCombatStats;

public class UnitData {
    public final String type;
    public final Coordinate position;
    public final Teams team;
    public final boolean canMove;
    public final boolean canBeCaptured;
    public final UnitCombatStats combatStats;

    public UnitData(String type, Coordinate position, Teams team, boolean canMove, boolean canBeCaptured, UnitCombatStats combatStats) {
        this.type = type;
        this.position = position;
        this.team = team;
        this.canMove = canMove;
        this.canBeCaptured = canBeCaptured;
        this.combatStats = combatStats;
    }
}
