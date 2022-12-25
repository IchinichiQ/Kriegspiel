package ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets;

public abstract class Packet {
    public final PacketType type;

    public Packet(PacketType type) {
        this.type = type;
    }
}
