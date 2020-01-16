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
    public static ArrayList<Melon> melonList;
    public static double dt, spawnChance;
    public static int canvasSize, tickrate, count;
    public static String[] traitNames = {"power", "cooldown", "friction"};
    
    Sim() {
        canvasSize = 500;
        tickrate = 10;
        dt = tickrate/1000.0;
        count = 1;
        // melon spawn chance per second
        spawnChance = 4.0 * dt;
        population = new ArrayList<Puck>();
        melonList = new ArrayList<Melon>();
        populate();
    }
    
    public void populate() {
        Adult newAdult;
        double[] defaultGenes = {500.0, 500.0, 500.0};
        for (int i = 0; i < count; i++) {
            newAdult = new Adult(defaultGenes, 250.0, 250.0, 2000.0);
            population.add(newAdult);
        }
    }
    
    public static void spawnMelon() {
        Melon newMelon;
        if (Math.random() <= spawnChance) {
            newMelon = new Melon(Math.random()*canvasSize, Math.random()*canvasSize, Math.random()*200);
            melonList.add(newMelon);
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
        while (true) {
            for (Puck p : population) {
                ((Adult) p).go();
            }
            spawnMelon();
            frame.repaint();
            TimeUnit.MILLISECONDS.sleep(tickrate);
        }
    }    

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        for (Puck p : population) {
            ((Adult) p).draw(g2d);
        }
        for (Melon m : melonList) {
            m.draw(g2d);
        }
    }
}