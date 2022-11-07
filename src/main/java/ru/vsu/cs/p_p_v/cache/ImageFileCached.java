package ru.vsu.cs.p_p_v.cache;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ImageFileCached {
    private static final Map<String, Image> cache = new ConcurrentHashMap<>();

    public static Image readImage(File file) throws IOException {
        Image cachedImage = cache.get(file.getPath());

        if (cachedImage == null) {
            cachedImage = ImageIO.read(file);

            GraphicsConfiguration gfx_config = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();;
            BufferedImage new_image = gfx_config.createCompatibleImage(
                    cachedImage.getWidth(null), cachedImage.getHeight(null), Transparency.TRANSLUCENT);

            // get the graphics context of the new image to draw the old image on
            Graphics2D g2d = (Graphics2D) new_image.getGraphics();

            // actually draw the image and dispose of context no longer needed
            g2d.drawImage(cachedImage, 0, 0, null);
            g2d.dispose();

            cache.put(file.getPath(), cachedImage);
        }

        return cachedImage;
    }
}
