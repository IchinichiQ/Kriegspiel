package ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets.client;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.Coordinate;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets.PacketType;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets.Packet;

public class AttackUnit extends Packet {
    public final Coordinate unitCoordinate;

    public AttackUnit(Coordinate unitCoordinate) {
        super(PacketType.AttackUnit);

        this.unitCoordinate = unitCoordinate;
    }
}
