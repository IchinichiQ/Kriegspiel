package ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.Coordinate;

public class CellTypeData {
    public final Coordinate coordinate;
    public final String type;

    public CellTypeData(Coordinate coordinate, String type) {
        this.coordinate = coordinate;
        this.type = type;
    }
}
