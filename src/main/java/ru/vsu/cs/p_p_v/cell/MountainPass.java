package ru.vsu.cs.p_p_v.cell;

import ru.vsu.cs.p_p_v.game.Coordinate;

public class MountainPass extends BoardCell{
    public MountainPass(Coordinate coordinate) {
        super(coordinate, false, null, null, 2);
    }

    @Override
    public String getStringRepresentation() {
        return "   ";
    }
}
