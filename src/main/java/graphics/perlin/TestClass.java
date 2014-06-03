package graphics.perlin;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

/**
 * To try out the Perlin Noise generator and some image manipulation with Marvin.
 * Created by Nick on 6/2/14.
 */
public class TestClass {

    public static void main(String [] args) throws Exception {
        //BufferedImage img = ImageIO.read(new File("./images/8i.png"));

        int height = 1000;
        int width = 1000;

        BufferedImage image = new BufferedImage(height, width, BufferedImage.TYPE_INT_ARGB);


        // Generate random offset to get a new part of the simplex noise each time
        Random r = new Random();
        double xOffset = r.nextDouble();
        double yOffset = r.nextDouble();

        SimplexNoise pn = new SimplexNoise(256);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int num = (int)(pn.noise(i + xOffset, j + yOffset) * 128.0 + 128.0);
                num = num + num << 8 + num << 16 + 0xFF000000;
                image.setRGB(i, j, num);
                //System.out.println(num + " " + image.getRGB(i,j));
            }
        }

        File outputFile = new File("./images/output.png");
        ImageIO.write(image, "png", outputFile);
    }
}
