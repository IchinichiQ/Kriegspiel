package ru.vsu.cs.p_p_v.cell;

import ru.vsu.cs.p_p_v.cache.ImageFileCached;
import ru.vsu.cs.p_p_v.game.Coordinate;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Paths;

public class Mountain extends BoardCell{
    public Mountain(Coordinate coordinate) {
        super(coordinate, true);
    }

    @Override
    public String getStringRepresentation() {
        return "^^^";
    }

    @Override
    public Image getBackgroundImage() {
        String imgPath = "images/cells/mountain.png";

        Image img = null;
        try {
            img = ImageFileCached.readImage(new File(imgPath));
        } catch (Exception ex) {
        }

        return img;
    }
}
