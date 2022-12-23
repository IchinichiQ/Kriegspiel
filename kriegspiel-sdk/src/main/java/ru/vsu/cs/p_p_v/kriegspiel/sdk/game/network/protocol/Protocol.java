package ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.protocol;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets.Packet;

public interface Protocol {
    Packet parsePacket(byte[] data);
    byte[] encodePacket(Packet packet);
}
