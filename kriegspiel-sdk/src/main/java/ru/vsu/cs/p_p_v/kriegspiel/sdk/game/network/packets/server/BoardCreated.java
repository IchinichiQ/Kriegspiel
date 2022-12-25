package ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets.server;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.CellTypeData;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets.PacketType;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets.Packet;

import java.util.Collections;
import java.util.List;

public class BoardCreated extends Packet {
    private final List<CellTypeData> cellTypeData;

    public BoardCreated(List<CellTypeData> cellTypeData) {
        super(PacketType.BoardCreated);

        this.cellTypeData = cellTypeData;
    }

    public List<CellTypeData> getCellTypeData() {
        return Collections.unmodifiableList(cellTypeData);
    }
}
