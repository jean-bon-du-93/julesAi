package utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Manages loading and storing of resources like images.
 */
public class ResourceManager {
    public static BufferedImage head;
    public static BufferedImage body;
    public static BufferedImage food;

    static {
        try {
            head = ImageIO.read(new File("res/head.png"));
            body = ImageIO.read(new File("res/body.png"));
            food = ImageIO.read(new File("res/food.png"));
        } catch (IOException e) {
            System.err.println("Could not load images. Make sure they are in the 'res' directory.");
            e.printStackTrace();
        }
    }
}
