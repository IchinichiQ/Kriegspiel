package ru.vsu.cs.p_p_v.cell;

import ru.vsu.cs.p_p_v.cache.ImageFileCached;
import ru.vsu.cs.p_p_v.game.Coordinate;
import ru.vsu.cs.p_p_v.game.Teams;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Paths;

public class Fortress extends BoardCell{
    public Fortress(Coordinate coordinate) {
        super(coordinate, false, new Color(255, 255, 255), new Color(145, 145, 145), 4);
    }

    @Override
    public String getStringRepresentation() {
        return "   ";
    }

    @Override
    public Image getBackgroundImage() {
        String imgPath = "images/cells/fortress.png";

        Image img = null;
        try {
            img = ImageFileCached.readImage(new File(imgPath));
        } catch (Exception ex) {
        }

        return img;
    }
}
