package ru.vsu.cs.p_p_v.kriegspiel.sdk.cache;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ImageFileCached {
    private static final Map<String, Image> cache = new ConcurrentHashMap<>();

    public static Image readImage(File file) throws IOException {
        Image cachedImage = cache.get(file.getPath());
        if (cachedImage != null)
            return cachedImage;

        Image rawImage = ImageIO.read(file);

        GraphicsConfiguration gfx_config = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice().getDefaultConfiguration();
        BufferedImage optimisedImage = gfx_config.createCompatibleImage(
                rawImage.getWidth(null), rawImage.getHeight(null), Transparency.TRANSLUCENT);

        // Draw the old image on the new
        Graphics2D g2d = (Graphics2D) optimisedImage.getGraphics();
        g2d.drawImage(rawImage, 0, 0, null);
        g2d.dispose();

        cache.put(file.getPath(), optimisedImage);

        return optimisedImage;
    }
}
