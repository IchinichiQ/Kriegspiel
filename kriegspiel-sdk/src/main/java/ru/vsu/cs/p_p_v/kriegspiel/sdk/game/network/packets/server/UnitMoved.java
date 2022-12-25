package ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets.server;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.MoveUnitResult;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets.PacketType;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets.Packet;

public class UnitMoved extends Packet {
    public final MoveUnitResult result;

    public UnitMoved(MoveUnitResult result) {
        super(PacketType.UnitMoved);

        this.result = result;
    }
}
