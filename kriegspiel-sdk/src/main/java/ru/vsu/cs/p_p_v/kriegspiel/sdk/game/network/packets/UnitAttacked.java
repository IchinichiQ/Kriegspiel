package ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.AttackUnitResult;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.PacketType;

public class UnitAttacked extends Packet{
    public final AttackUnitResult result;

    public UnitAttacked(AttackUnitResult result) {
        super(PacketType.UnitAttacked.name());

        this.result = result;
    }
}
