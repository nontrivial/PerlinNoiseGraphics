package graphics.perlin;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by Nick on 6/3/14. Following Matt DesLauriers.
 */
public class PerlinCanvas {

    //Image Information
    int width = 0;
    int height = 0;
    int [] imgColorMap = null;

    //Noise Parameters
    double scale = 0.1;
    boolean shift = true;
    int noiseSize = 256;

    //Stroke Parameters
    int particleCount = 1000;
    int trailLength = 15;
    int thickness = 20;
    double maxAge = 1.0;
    double ageingSpeed = 1.0;
    float alpha = 0.15f;
    boolean motion = true;

    //Color Parameters
    boolean darken = false;

    //State Variables
    double time = 0.0;
    int step = 0;
    BufferedImage image = null;
    Graphics2D g = null;
    SimplexNoise sn = new SimplexNoise(scale);
    Particle [] p = new Particle[particleCount];

    public void loadImage(String img) throws Exception {
        image = ImageIO.read(new File(img));
        g = (Graphics2D) image.getGraphics();

        width = image.getWidth();
        height = image.getHeight();

        imgColorMap = image.getRGB(0,0,width, height,null,0,width);

        initializeParticles();
        initializeNoise();
    }

    private void initializeParticles() {
        for (int i = 0; i < particleCount; i++) {
            p[i] = new Particle();
            p[i].reset(width, height);
            p[i].random();
        }
    }

    private void initializeNoise() {
        sn.scale = this.scale;
        sn.generateNoise(noiseSize);
    }

    public void paintNextFrame() {
        time += 0.1;
        step++;

        if (shift && (step % 20 == 0)) {
            sn.scale = this.scale;
            sn.generateNoise(noiseSize);
        }

        for (int i = 0; i < particleCount; i++) {
            //Add motion
            if (motion) {
                p[i].x += p[i].dx;
                p[i].y += p[i].dy;
            }

            //If we have moved off the canvas we reset the particle
            if (p[i].x >= width || p[i].y >= height || p[i].y < 0 || p[i].x < 0) {
                p[i].reset(width, height);
            }

            //Calculate new velocity
            int x = fastfloor(p[i].x);
            int y = fastfloor(p[i].y);
            double noiseValue = Samplers.bilinear(sn.noiseMap,
                    (double)(x) * ((double)noiseSize - 1) / width,
                    (double)(y) * ((double)noiseSize - 1) / height);
            //Converting noise to an angle
            double angle = noiseValue * Math.PI * 2;
            double vx = Math.cos(angle);
            double vy = Math.sin(angle);
            //Adding a unit vector in that angle (you're kind of averaging with the previous vector after normalizing)
            p[i].dx += vx;
            p[i].dy += vy;
            //Normalize the vector
            p[i].dx /= p[i].dx * p[i].dx + p[i].dy * p[i].dy;
            p[i].dy /= p[i].dx * p[i].dx + p[i].dy * p[i].dy;

            //Setup drawing
            BasicStroke bs = new BasicStroke((int)(thickness * (noiseValue/2.0 + 0.5)),
                    BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND);
            int imageColor = imgColorMap[p[i].x + p[i].y * width];
            Color imgColor = new Color(imageColor);
            int red = imgColor.getRed();
            int green = imgColor.getGreen();
            int blue = imgColor.getBlue();

            double darker = 0.2126 * ((double)red/255.0) +
                    0.7152 * ((double)green/255.0) +
                    0.0722 * ((double)blue/255.0);

            double brightnessAdjustment = this.darken ? darker : 1.0;

            Color c = new Color((float)(red * brightnessAdjustment)/255.0f,
                    (float)(green * brightnessAdjustment)/255.0f,
                    (float)(blue * brightnessAdjustment)/255.0f,
                    alpha);
            g.setColor(c);
            g.setStroke(bs);

            //Draw
            double lineSize = trailLength * (noiseValue/2.0 + 0.5) * p[i].age;
            double scaleFactor = lineSize / Math.sqrt(p[i].dx * p[i].dx + p[i].dy * p[i].dy);
            g.drawLine(p[i].x, p[i].y, (int)(p[i].x + p[i].dx * scaleFactor), (int)(p[i].y + p[i].dy * scaleFactor));

            //Reset dead particles
            p[i].age += 0.1 * ageingSpeed * p[i].ageingSpeed;
            if (p[i].age >= maxAge) {
                p[i].reset(width, height);
                p[i].random();
            }
        }
    }

    public void writeImage(String path) throws Exception {
        File outputFile = new File(path);
        ImageIO.write(image, "png", outputFile);
    }
    //In MDL's implementation he uses a custom sized noise mesh instead of just generating a noise
    //map for each pixel of the original image... that's why he needs the bilinear sampler.

    public static int fastfloor(double x) {
        int xi = (int)x;
        return x<xi ? xi-1 : xi;
    }
}