package ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.Coordinate;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.PacketType;

public class MoveUnit extends Packet {
    public final Coordinate moveFrom;
    public final Coordinate moveTo;

    public MoveUnit(Coordinate moveFrom, Coordinate moveTo) {
        super(PacketType.MoveUnit.name());

        this.moveFrom = moveFrom;
        this.moveTo = moveTo;
    }
}
