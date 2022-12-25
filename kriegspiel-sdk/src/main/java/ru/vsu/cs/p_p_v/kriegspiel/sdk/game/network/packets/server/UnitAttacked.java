package ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets.server;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.AttackUnitResult;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets.PacketType;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets.Packet;

public class UnitAttacked extends Packet {
    public final AttackUnitResult result;

    public UnitAttacked(AttackUnitResult result) {
        super(PacketType.UnitAttacked);

        this.result = result;
    }
}
