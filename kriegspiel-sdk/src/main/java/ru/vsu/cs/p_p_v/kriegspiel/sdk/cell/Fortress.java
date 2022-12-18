package ru.vsu.cs.p_p_v.kriegspiel.sdk.cell;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.cache.ImageFileCached;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.Coordinate;

import java.awt.*;
import java.io.File;

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
