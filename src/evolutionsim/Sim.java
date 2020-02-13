package evolutionsim;

import java.awt.*;
import java.text.DecimalFormat;

import javax.swing.JFrame;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import javax.swing.JPanel;

public class Sim extends JPanel {
    
    // no idea what this does
    private static final long serialVersionUID = 1L;
    public static ArrayList<Adult> adultList;
    public static ArrayList<Melon> melonList;
    public static ArrayList<GameObject> objectList;
    public static GameObject observed;
    public static double ticklength, spawnChance;
    public static int canvasSize, count;
    public static String[] traitNames = {"power", "cooldown", "friction"};
    public static boolean pause;
    //public static boolean showHitboxes;
    
    Sim() {
        //width and height of the square canvas
        canvasSize = 500;
        // number of seconds in a tick
        ticklength = 10.0/1000.0;
        count = 5;
        // melon spawn chance per second
        spawnChance = 0.3 * ticklength * 10;
        adultList = new ArrayList<Adult>();
        melonList = new ArrayList<Melon>();
        objectList = new ArrayList<GameObject>();
        populate();
    }
    
    public void populate() {
        Adult newAdult;
        for (int i = 0; i < count; i++) {
            // power, freq, friction, pointSmell, vagueSmell, foodSizePreference, untilBored
            double[] defaultGenes = {500.0, 500.0, 500.0, 400.0, 1000.0, 500.0, 500.0};
            double[] randomGenes = {Math.random() * 1000.0, Math.random() * 1000.0, Math.random() * 1000.0, Math.random() * 1000.0, Math.random() * 1000.0, Math.random() * 1000.0, Math.random() * 1000.0};
            newAdult = new Adult(randomGenes, Math.random()*500, Math.random()*500, 2000.0);
            adultList.add(newAdult);
            objectList.add(newAdult);
        }
    }
    
    public static void spawnMelon() {
        Melon newMelon;
        if (Math.random() <= spawnChance) {
            newMelon = new Melon(Math.random()*canvasSize, Math.random()*canvasSize, Math.random()*200);
            melonList.add(newMelon);
            objectList.add(newMelon);
        }
    }

    public static void main(String[] args) throws InterruptedException {

        
        JFrame simulation = new JFrame("Simulation");
        Sim sim = new Sim();
        simulation.add(sim);
        simulation.setSize(canvasSize, canvasSize);
        simulation.setVisible(true);
        simulation.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.pack();
        simulation.setExtendedState(JFrame.MAXIMIZED_BOTH); 

        simulation.addMouseListener(new InputListener());
        simulation.addKeyListener(new InputListener());
        
        //JTextField console = new JTextField();
        //console.setText("test");
        //simulation.add(console);
        
        while (adultList.size() > 0) {
            simulation.repaint();
            if (!pause) {
                for (Adult a : adultList) {
                    a.go();
                }
                spawnMelon();
                TimeUnit.MILLISECONDS.sleep((long) (ticklength * 1000.0));
            }
        }
    }
    
    public static String formatTime(int hr, int min, int sec) {
        DecimalFormat df = new DecimalFormat("00");
        String hours = df.format(hr);
        String minutes = df.format(min);
        String seconds = df.format(sec);
        String time = (hr > 0) ? hours + ":" + minutes + ":" + seconds : minutes + ":" + seconds;
        return time;
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, canvasSize, canvasSize);
        for (GameObject o : objectList) {
            o.draw(g2d);
            if (observed != null && observed.equals(o)) {
                o.displayInfo(g2d);
            }
        }

    }
}