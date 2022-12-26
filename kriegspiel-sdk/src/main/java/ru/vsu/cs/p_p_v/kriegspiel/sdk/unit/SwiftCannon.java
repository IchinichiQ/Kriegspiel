package ru.vsu.cs.p_p_v.kriegspiel.sdk.unit;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.cache.ImageFileCached;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.Board;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.Coordinate;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.Teams;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.unit.stats.UnitBaseStats;

import java.awt.*;
import java.io.File;

public class SwiftCannon extends BoardUnit {
    public SwiftCannon(Board board, Teams team, Coordinate position) {
        super(board, team, position, new UnitBaseStats(2, 3, 5, 8));
    }

    @Override
    public Image getImageRepresentation() {
        String imgPath = String.format("images/units/%s/swiftCannon.png", getTeam() == Teams.North ? "north" : "south");

        return ImageFileCached.readImage(new File(imgPath));
    }
}
