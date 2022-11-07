package ru.vsu.cs.p_p_v.cell;

import ru.vsu.cs.p_p_v.game.Coordinate;

public class Empty extends BoardCell {
    public Empty(Coordinate coordinate) {
        super(coordinate, false);
    }

    @Override
    public String getStringRepresentation() {
        return "   ";
    }
}
