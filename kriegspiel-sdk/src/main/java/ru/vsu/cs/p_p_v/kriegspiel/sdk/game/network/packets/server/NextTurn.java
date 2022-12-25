package ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets.server;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.Teams;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets.Packet;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets.PacketType;

public class NextTurn extends Packet {
    public final Teams nextTurnTeam;

    public NextTurn(Teams nextTurnTeam) {
        super(PacketType.NextTurn);

        this.nextTurnTeam = nextTurnTeam;
    }
}
