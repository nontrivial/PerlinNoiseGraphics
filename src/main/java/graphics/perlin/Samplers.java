package graphics.perlin;

/**
 * Created by Nick on 6/3/14.
 */
public final class Samplers {
    public static double bilinear(double [][] data, double x, double y) {

        //Using the corners of a 1 unit grid square surrounding the point,
        //linearly interpolate in x and y directions to get interpolation.
        int xi = fastfloor( x );
        int yi = fastfloor( y );

        double tx = x - xi;
        double ty = y - yi;

        double c00 = data[xi][yi];
        double c10 = data[xi+1][yi];
        double c01 = data[xi][yi+1];
        double c11 = data[xi+1][yi+1];



        double sx = smoothstep( 0, 1, tx );
        double sy = smoothstep( 0, 1, ty );

        double nx0 = linear( c00, c10, sx );
        double nx1 = linear( c01, c11, sx );

        double v = linear( nx0, nx1, sy );
        return v;
    }

    private static double smoothstep(double y1, double y2, double x) {
        //Cosine smoothing using Taylor approximation
        return linear(y1, y2, x * x * (3 - 2 * x));
    }

    private static double linear(double y1, double y2, double x) {
        return y1 + x * (y2 - y1);
    }

    public static int fastfloor(double x) {
        int xi = (int)x;
        return x<xi ? xi-1 : xi;
    }
}
