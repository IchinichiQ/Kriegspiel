package ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.MoveUnitResult;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.PacketType;

public class UnitMoved extends Packet {
    public final MoveUnitResult result;

    public UnitMoved(MoveUnitResult result) {
        super(PacketType.UnitMoved.name());

        this.result = result;
    }
}
