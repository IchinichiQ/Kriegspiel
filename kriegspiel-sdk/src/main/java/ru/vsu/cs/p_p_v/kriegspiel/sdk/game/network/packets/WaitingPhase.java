package ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.PacketType;

public class WaitingPhase extends Packet {
    public WaitingPhase() {
        super(PacketType.WaitingPhase.name());
    }
}
