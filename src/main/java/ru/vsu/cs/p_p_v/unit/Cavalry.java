package ru.vsu.cs.p_p_v.unit;

import ru.vsu.cs.p_p_v.cache.ImageFileCached;
import ru.vsu.cs.p_p_v.game.Board;
import ru.vsu.cs.p_p_v.game.Coordinate;
import ru.vsu.cs.p_p_v.game.Teams;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Paths;

public class Cavalry extends BoardUnit {
    public Cavalry(Board board, Teams team, Coordinate position) {
        super(board, team, position, new UnitStats(2, 2, 4, 5));
    }

    @Override
    public String getStringRepresentation() {
        return "Cav";
    }

    @Override
    public Image getImageRepresentation() {
        String imgPath = String.format("images/units/%s/cavalry.png", getTeam() == Teams.North ? "north" : "south");

        Image img = null;
        try {
            img = ImageFileCached.readImage(new File(imgPath));
        } catch (Exception ex) {
        }

        return img;
    }
}
