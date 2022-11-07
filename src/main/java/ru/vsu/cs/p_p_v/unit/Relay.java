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

public class Relay extends RelayUnit {
    public Relay(Board board, Teams team, Coordinate position) {
        super(board, team, position, new UnitStats(1, 0, 0, 1));
    }

    @Override
    public String getStringRepresentation() {
        return "Rel";
    }

    @Override
    public Image getImageRepresentation() {
        String imgPath = String.format("images/units/%s/relay.png", getTeam() == Teams.North ? "north" : "south");

        Image img = null;
        try {
            img = ImageFileCached.readImage(new File(imgPath));
        } catch (Exception ex) {
        }

        return img;
    }
}
