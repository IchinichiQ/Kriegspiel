package ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets.server;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.Teams;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets.PacketType;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets.Packet;

public class EndPhase extends Packet {
    public final Teams winner;

    public EndPhase(Teams winner) {
        super(PacketType.EndPhase);

        this.winner = winner;
    }
}
