package evolutionsim;

import java.awt.*;

import javax.swing.JFrame;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import javax.swing.JPanel;

public class Sim extends JPanel {
    
    // no idea what this does
    private static final long serialVersionUID = 1L;
    public static ArrayList<Puck> population;
    public static double friction, dt;
    public static int canvasSize, tickrate, count;
    public static String[] traitNames = {"power", "cooldown", "friction"};
    
    Sim() {
        canvasSize = 500;
        //force of friction, this should be a specific trait later on
        //friction = -10000;
        tickrate = 5;
        dt = tickrate/1000.0;
        count = 1;
        population = new ArrayList<Puck>();
        populate();
    }
    
    public void populate() {
        Adult newAdult;
        double[] defaultGenes = {500.0, 500.0, 500.0};
        for (int i = 0; i < count; i++) {
            newAdult = new Adult(defaultGenes, Math.random()*canvasSize, Math.random()*canvasSize, 5000.0);
            population.add(newAdult);
        }
    }

    
    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame("Frame");
        Sim sim = new Sim();
        frame.add(sim);
        frame.setSize(canvasSize, canvasSize);
        frame.setVisible(true);
        //frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //population.get(0).v = 50;
        while (true) {
            for (Puck p : population) {
                ((Adult) p).go();
            }
            frame.repaint();
            TimeUnit.MILLISECONDS.sleep(10);
        }
    }

    

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        for (int i = 0; i < Sim.population.size(); i++) {
            Sim.population.get(i).draw(g2d);
        }
    }
}