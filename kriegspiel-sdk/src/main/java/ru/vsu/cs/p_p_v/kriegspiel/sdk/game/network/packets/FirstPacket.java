package ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.Teams;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.GamePhase;

public class FirstPacket {
    public final Teams team;
    public final GamePhase phase;

    public FirstPacket(Teams team, GamePhase phase) {
        this.team = team;
        this.phase = phase;
    }
}
