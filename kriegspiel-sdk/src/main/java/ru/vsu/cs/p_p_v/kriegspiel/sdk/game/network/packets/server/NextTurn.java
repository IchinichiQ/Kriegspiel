package ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets.server;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.Team;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets.Packet;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets.PacketType;

public class NextTurn extends Packet {
    public final Team nextTurnTeam;

    public NextTurn(Team nextTurnTeam) {
        super(PacketType.NextTurn);

        this.nextTurnTeam = nextTurnTeam;
    }
}
