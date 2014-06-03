package graphics.perlin;

import java.util.Random;

/**
 * Created by Nick on 6/3/14.
 */
public class Particle {
    Random rand = new Random(System.currentTimeMillis());
    int x = 0;
    int y = 0;
    double dx = 0.0;
    double dy = 0.0;
    double age = 0.0;               //For some reason there is a connection between age and size
    double ageingSpeed = rand.nextDouble() * 0.5 + 0.5;
    double brightness = rand.nextDouble() * 0.5 + 0.5;

    public void reset(int width, int height) {
        age = 0.0;
        brightness = rand.nextDouble();
        dx = 0.0;
        dy = 0.0;
        x = fastfloor((rand.nextDouble() * 0.5 + 0.5) * width);
        y = fastfloor((rand.nextDouble() * 0.5 + 0.5) * height);
    }

    public static int fastfloor(double x) {
        int xi = (int)x;
        return x<xi ? xi-1 : xi;
    }
}
