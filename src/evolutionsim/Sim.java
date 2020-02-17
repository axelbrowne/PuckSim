package evolutionsim;

import java.awt.*;
import java.text.DecimalFormat;

import javax.swing.JFrame;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import javax.swing.JPanel;

/**
 * GENERAL COMMENTS
 *******************
 * Might be faster to use LinkedList
 * instead of ArrayList for iterating over
 * the different object types.
 */


public class Sim extends JPanel {
    
    // no idea what this does
    private static final long serialVersionUID = 1L;
    public static ArrayList<Puck> puckList;
    public static ArrayList<Adult> adultList;
    public static ArrayList<Egg> eggList;
    public static ArrayList<Melon> melonList;
    public static ArrayList<GameObject> objectList;
    public static GameObject observed;
    public static double ticklength, spawnChance;
    public static int canvasWidth, canvasHeight, count;
    public static String[] traitNames = {"power", "cooldown", "friction"};
    public static boolean pause;
    //public static boolean showHitboxes;
    
    Sim() {
        //width and height of the square canvas
        canvasWidth = 1250;
        canvasHeight = 650;
        // number of seconds in a tick
        ticklength = 10.0/1000.0;
        count = 2;
        // melon spawn chance per second
        spawnChance = 0.5 * ticklength * 2;
        objectList = new ArrayList<GameObject>();
        puckList = new ArrayList<Puck>();
        adultList = new ArrayList<Adult>();
        eggList = new ArrayList<Egg>();
        melonList = new ArrayList<Melon>();
        populate();
    }
    
    public void populate() {
        Adult newAdult;
        for (int i = 0; i < count; i++) {
            // power, freq, friction, pointSmell, vagueSmell, melSizePref, untilBored
            double[] defaultGenes = {500.0, 500.0, 500.0, 200.0, 500.0, 0.0, 500.0, 500.0, 500.0, 500.0, 500.0};
            double[] randomGenes = {Math.random() * 1000.0, Math.random() * 1000.0, Math.random() * 1000.0, Math.random() * 1000.0, Math.random() * 1000.0, Math.random() * 1000.0, Math.random() * 1000.0, Math.random() * 1000.0, Math.random() * 1000.0, Math.random() * 1000.0, Math.random() * 1000.0};
            newAdult = new Adult(randomGenes, Math.random()*canvasWidth, Math.random()*canvasHeight, 2000.0);
            objectList.add(newAdult);
            puckList.add(newAdult);
            adultList.add(newAdult);
            System.out.println(Math.log(10) + "," + Math.log(100));
            System.out.println(Math.pow(10,-1) + "," + Math.pow(100,-1));
        }
    }
    
    public static void spawnMelon() {
        Melon newMelon;
        if (Math.random() <= spawnChance) {
            newMelon = new Melon(Math.random()*canvasWidth, Math.random()*canvasHeight, Math.random()*200);
            objectList.add(newMelon);
            melonList.add(newMelon);
        }
    }
    
    public static void layEggs(Adult a, Adult b) {
        int numChild = (int) Math.round(average(a.childCount.pheno, b.childCount.pheno));
        for (int i = 0; i < numChild; i++) {
            Egg newEgg = layEgg(a,b);
            a.children.add(newEgg);
            b.children.add(newEgg);
            objectList.add(newEgg);
            puckList.add(newEgg);
            eggList.add(newEgg);            
        }
    }
    
    private static Egg layEgg(Adult a, Adult b) {
        double newGenes[] = {
                average(a.power.geno, b.power.geno),
                average(a.freq.geno, b.freq.geno),
                average(a.friction.geno, b.friction.geno),
                average(a.vision.geno, b.vision.geno),
                average(a.smell.geno, b.smell.geno),
                average(a.melSizePref.geno, b.melSizePref.geno),
                average(a.sexWill.geno, b.sexWill.geno),
                average(a.childCount.geno, b.childCount.geno),
                average(a.maturation.geno, b.maturation.geno),
                average(a.mutationChance.geno, b.mutationChance.geno)};
        mutateGenes(newGenes);
        Egg newEgg = new Egg(newGenes, a, b, average(a.x, b.x) + 10*Math.random(), average(a.y, b.y) + 10*Math.random(), 0);
        return newEgg;
    }
    
    private static double[] mutateGenes(double[] genes) {
        for (int i = 0; i < genes.length; i++) {
            if (Math.random() < translateMutationChance(genes[genes.length - 1])) {
                genes[i] = Math.random() * 1000;
            }
        }
        return genes;
    }

    public static void main(String[] args) throws InterruptedException {

        
        JFrame simulation = new JFrame("Simulation");
        Sim sim = new Sim();
        simulation.add(sim);
        simulation.setSize(canvasWidth, canvasHeight);
        simulation.setVisible(true);
        simulation.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.pack();
        simulation.setExtendedState(JFrame.MAXIMIZED_BOTH); 

        simulation.addMouseListener(new InputListener());
        simulation.addKeyListener(new InputListener());
        
        //JTextField console = new JTextField();
        //console.setText("test");
        //simulation.add(console);
        
        while (puckList.size() > 0) {
            simulation.repaint();
            if (!pause) {
                for (int i = 0; i < adultList.size(); i++) {
                    Adult a = adultList.get(i);
                    a.go();
                }
                spawnMelon();
                TimeUnit.MILLISECONDS.sleep((long) (ticklength * 1000.0));
            }
        }
    }
    
    static double average(double a, double b) {
        return (a+b)/2;
    }
    
    public static String formatTime(int hr, int min, int sec) {
        DecimalFormat df = new DecimalFormat("00");
        String hours = df.format(hr);
        String minutes = df.format(min);
        String seconds = df.format(sec);
        String time = (hr > 0) ? hours + ":" + minutes + ":" + seconds : minutes + ":" + seconds;
        return time;
    }
    
    public static double translateMutationChance(double mutationChanceGeno) {
        return mutationChanceGeno / 40000;
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, canvasWidth, canvasHeight);
        for (int i = 0; i < objectList.size(); i++) {
            GameObject o = objectList.get(i);
            o.draw(g2d);
            if (observed != null && observed.equals(o)) {
                o.displayInfo(g2d);
            }
        }
    }
}