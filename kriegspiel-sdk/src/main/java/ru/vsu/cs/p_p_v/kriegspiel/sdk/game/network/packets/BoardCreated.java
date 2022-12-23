package ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.CellTypeData;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.PacketType;

import java.util.Collections;
import java.util.List;

public class BoardCreated extends Packet {
    private final List<CellTypeData> cellTypeData;

    public BoardCreated(String type, List<CellTypeData> cellTypeData) {
        super(PacketType.BoardCreated.name());

        this.cellTypeData = cellTypeData;
    }

    public List<CellTypeData> getCellTypeData() {
        return Collections.unmodifiableList(cellTypeData);
    }
}
