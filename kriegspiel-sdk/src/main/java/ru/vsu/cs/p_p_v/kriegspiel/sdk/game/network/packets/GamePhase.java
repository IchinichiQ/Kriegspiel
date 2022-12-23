package ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.PacketType;

public class GamePhase extends Packet {
    public GamePhase() {
        super(PacketType.GamePhase.name());
    }
}
