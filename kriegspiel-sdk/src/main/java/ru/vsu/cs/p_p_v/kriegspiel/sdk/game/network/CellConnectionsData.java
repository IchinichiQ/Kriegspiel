package ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.ConnectionDirection;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.Coordinate;

import java.util.Collections;
import java.util.List;

public class CellConnectionsData {
    public final Coordinate coordinate;
    public final boolean hasNorthConnection;
    public final boolean hasSouthConnection;
    private final List<ConnectionDirection> northConnections;
    private final List<ConnectionDirection> southConnections;

    public CellConnectionsData(Coordinate coordinate, boolean hasNorthConnection, boolean hasSouthConnection, List<ConnectionDirection> northConnections, List<ConnectionDirection> southConnections) {
        this.coordinate = coordinate;
        this.hasNorthConnection = hasNorthConnection;
        this.hasSouthConnection = hasSouthConnection;
        this.northConnections = northConnections;
        this.southConnections = southConnections;
    }

    public List<ConnectionDirection> getNorthConnections() {
        return Collections.unmodifiableList(northConnections);
    }

    public List<ConnectionDirection> getSouthConnections() {
        return Collections.unmodifiableList(southConnections);
    }
}
