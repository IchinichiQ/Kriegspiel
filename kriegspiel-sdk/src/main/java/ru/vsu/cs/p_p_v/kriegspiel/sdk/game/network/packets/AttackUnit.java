package ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.Coordinate;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.PacketType;

public class AttackUnit extends Packet {
    public final Coordinate unitCoordinate;

    public AttackUnit(Coordinate unitCoordinate) {
        super(PacketType.AttackUnit);

        this.unitCoordinate = unitCoordinate;
    }
}
