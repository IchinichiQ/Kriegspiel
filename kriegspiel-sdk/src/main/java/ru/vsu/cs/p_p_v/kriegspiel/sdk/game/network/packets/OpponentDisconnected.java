package ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.PacketType;

public class OpponentDisconnected extends Packet {
    public OpponentDisconnected() {
        super(PacketType.OpponentDisconnected.name());
    }
}
