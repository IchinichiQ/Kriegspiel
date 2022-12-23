package ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.CellConnectionsData;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.PacketType;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.UnitData;

import java.util.Collections;
import java.util.List;

public class BoardUpdate extends Packet {
    private final List<UnitData> unitData;
    private final List<CellConnectionsData> cellConnectionsData;

    public BoardUpdate(List<UnitData> unitData, List<CellConnectionsData> cellConnectionsData) {
        super(PacketType.BoardUpdate);

        this.unitData = unitData;
        this.cellConnectionsData = cellConnectionsData;
    }

    public List<UnitData> getUnitData() {
        return Collections.unmodifiableList(unitData);
    }

    public List<CellConnectionsData> getCellConnectionsData() {
        return Collections.unmodifiableList(cellConnectionsData);
    }
}


