package ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets.client;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets.PacketType;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets.Packet;

public class EndTurn extends Packet {
    public EndTurn() {
        super(PacketType.EndTurn);
    }
}
