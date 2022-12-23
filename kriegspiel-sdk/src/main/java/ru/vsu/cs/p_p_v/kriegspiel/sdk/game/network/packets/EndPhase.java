package ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.Teams;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.PacketType;

public class EndPhase extends Packet {
    public final Teams winner;

    public EndPhase(Teams winner) {
        super(PacketType.EndPhase.name());

        this.winner = winner;
    }
}
