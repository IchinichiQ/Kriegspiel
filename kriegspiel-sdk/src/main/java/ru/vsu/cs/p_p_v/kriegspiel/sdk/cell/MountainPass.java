package ru.vsu.cs.p_p_v.kriegspiel.sdk.cell;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.Coordinate;

public class MountainPass extends BoardCell{
    public MountainPass(Coordinate coordinate) {
        super(coordinate, false, null, null, 2);
    }

    @Override
    public String getStringRepresentation() {
        return "   ";
    }
}
