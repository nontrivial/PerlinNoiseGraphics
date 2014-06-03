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

    //Noise Parameters
    double scale = 1.0;
    boolean shift = false;

    //Stroke Parameters
    int particleCount = 500;
    int trailLength = 50;
    int thickness = 20;
    double maxAge = 1.0;
    double ageingSpeed = 1.0;
    double alpha = 0.2;
    boolean motion = true;
    //Brush shape?

    //Color Parameters
    boolean darken = true;

    //State Variables
    double time = 0.0;
    int step = 0;
    BufferedImage image = null;
    Graphics2D g = null;
    SimplexNoise sn = new SimplexNoise(1.0);
    Particle [] p = new Particle[particleCount];

    public void loadImage(String img) throws Exception {
        image = ImageIO.read(new File(img));
        g = image.createGraphics();

        width = image.getWidth();
        height = image.getHeight();
    }

    public void paintNextFrame() {
        time += 0.1;
        step++;

        if (shift && (step % 20 == 0)) {
            sn.scale = this.scale;
            sn.generateNoise(256);
        }

        for (int i = 0; i < particleCount; i++) {
            //Add motion
            if (motion) {
                p[i].x += p[i].dx;
                p[i].y += p[i].dy;
            }

            //Calculate new velocity
            int x = fastfloor(p[i].x);
            int y = fastfloor(p[i].y);
            double noiseValue = Samplers.bilinear(sn.noiseMap, x, y);
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

            //If we have moved off the canvas we reset the particle
            if (p[i].x > width || p[i].y > height || p[i].y < 0) {
                p[i].reset(width, height);
            }

            //Now comes the drawing



            //Reset dead particles
            p[i].age += 0.1 * ageingSpeed * p[i].ageingSpeed;
            if (p[i].age >= maxAge) {
                p[i].reset(width, height);
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
//context.strokeStyle = 'hsl('+lerp(alpha, alpha-100, rot)+', '+(1-val)*lerp(0.2, 0.9, rot)*options.saturation*100+'%, '+(val)*lerp(0.45, 1, rot)*brightness*options.lightness*100+'%)';