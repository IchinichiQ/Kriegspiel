package ru.vsu.cs.p_p_v.kriegspiel.sdk.cell;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.cache.ImageFileCached;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.Coordinate;

import java.awt.*;
import java.io.File;

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
