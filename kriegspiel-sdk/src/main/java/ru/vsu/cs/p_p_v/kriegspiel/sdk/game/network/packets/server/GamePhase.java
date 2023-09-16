package ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets.server;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.Team;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets.PacketType;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets.Packet;

public class GamePhase extends Packet {
    public final Team clientTeam;
    public final Team currentTurnTeam;

    public GamePhase(Team clientTeam, Team currentTurnTeam) {
        super(PacketType.GamePhase);

        this.clientTeam = clientTeam;
        this.currentTurnTeam = currentTurnTeam;
    }
}
