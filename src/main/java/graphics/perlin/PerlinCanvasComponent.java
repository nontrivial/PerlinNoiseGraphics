package graphics.perlin;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by Nick on 6/4/14.
 */
public class PerlinCanvasComponent extends Component implements MouseListener {

    PerlinCanvas canvas = new PerlinCanvas();

    public static void main(String [] args) throws Exception {
        JFrame f = new JFrame("Perlin Noise Impressionism");

        PerlinCanvasComponent pc = new PerlinCanvasComponent();
        pc.canvas.loadImage("./images/oldman.png");
        pc.addMouseListener(pc);
        pc.startPainting();
        //pc.canvas.writeImage("./images/testoutput.png");
        f.add(pc);
        f.pack();
        f.setVisible(true);
    }

    public void startPainting() {
        (new Thread(new CanvasRunnable())).start();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        try {
            canvas.writeImage("./images/output.png");
        } catch (Exception ex) {

        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public class CanvasRunnable implements Runnable {
        public void run() {
            long startTime = System.currentTimeMillis();
            while (true) {
                if (System.currentTimeMillis() - startTime > 50) {
                    startTime = System.currentTimeMillis();
                    canvas.paintNextFrame();
                    PerlinCanvasComponent.this.repaint();
                }
            }
        }
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.drawImage(canvas.image, 0, 0, null);
    }

    public Dimension getPreferredSize() {
        return new Dimension(500, 1000);
    }
}
