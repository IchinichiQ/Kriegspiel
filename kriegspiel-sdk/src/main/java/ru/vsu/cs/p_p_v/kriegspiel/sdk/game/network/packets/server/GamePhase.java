package ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets.server;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.Teams;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets.PacketType;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets.Packet;

public class GamePhase extends Packet {
    public final Teams clientTeam;
    public final Teams currentTurnTeam;

    public GamePhase(Teams clientTeam, Teams currentTurnTeam) {
        super(PacketType.GamePhase);

        this.clientTeam = clientTeam;
        this.currentTurnTeam = currentTurnTeam;
    }
}
