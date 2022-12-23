package ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets;

public abstract class Packet {
    public final String type;

    public Packet(String type) {
        this.type = type;
    }
}
