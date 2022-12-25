package ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets.client;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.Coordinate;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets.PacketType;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets.Packet;

public class MoveUnit extends Packet {
    public final Coordinate moveFrom;
    public final Coordinate moveTo;

    public MoveUnit(Coordinate moveFrom, Coordinate moveTo) {
        super(PacketType.MoveUnit);

        this.moveFrom = moveFrom;
        this.moveTo = moveTo;
    }
}
