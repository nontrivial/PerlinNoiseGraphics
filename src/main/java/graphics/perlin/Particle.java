package graphics.perlin;

import java.util.Random;

/**
 * Created by Nick on 6/3/14.
 */
public class Particle {
    int x = 0;
    int y = 0;
    double dx = 0.0;
    double dy = 0.0;
    double age = 0.0;               //For some reason there is a connection between age and size
    double ageingSpeed = Math.random();

    public void reset(int width, int height) {
        age = 0.0;
        dx = 0.0;
        dy = 0.0;
        x = fastfloor((Math.random()) * width);
        y = fastfloor((Math.random()) * height);
    }

    public void random() {
        age = Math.random();
    }

    public static int fastfloor(double x) {
        int xi = (int)x;
        return x<xi ? xi-1 : xi;
    }

    public void particleInfo() {
        System.out.println("x: " + x + ", y: " + y + ", age: " + age);
    }
}
