package ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.PacketType;

public class EndTurn extends Packet {
    public EndTurn() {
        super(PacketType.EndTurn.name());
    }
}
