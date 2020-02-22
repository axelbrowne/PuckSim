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
    public static double ticklength, spawnChance, timeElapsed;
    public static int canvasWidth, canvasHeight, count, fontSize, maxGen;
    public static String[] traitNames = {"power", "cooldown", "friction"};
    public static boolean pause;
    public static Font plain, bold;
    //public static boolean showHitboxes;
    
    Sim() {
        //width and height of the square canvas
        canvasWidth = 1250;
        canvasHeight = 650;
        // number of seconds in a tick
        ticklength = 10.0/1000.0;
        count = 50;
        timeElapsed = 0;
        // melon spawn chance per second
        spawnChance = 0.5 * ticklength * 100;
        // maximum generation
        maxGen = 0;
        // formatting stuff
        fontSize = 12;
        plain = new Font("Courier", Font.PLAIN, fontSize);
        bold = new Font("Courier", Font.BOLD, fontSize);
        
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
            double[] defaultGenes = {500.0, 500.00, 500.0, 500.0, 500.0, 500.0, 500.0, 500.0, 500.0, 500.0, 999.9};
            // all Math.random() except for mutation chance which is 999.9 to start
            double[] randomGenes = {Math.random() * 1000.0, Math.random() * 1000.0, Math.random() * 1000.0, Math.random() * 1000.0, Math.random() * 1000.0, Math.random() * 1000.0, Math.random() * 1000.0, Math.random() * 1000.0, Math.random() * 1000.0, Math.random() * 1000.0, 999.9};
            newAdult = new Adult(randomGenes, Math.random()*canvasWidth, Math.random()*canvasHeight, 1500.0, 0);
            newAdult.add();
        }
    }

    public static void spawnMelon() {
        Melon newMelon;
        if (Math.random() <= spawnChance) {
            newMelon = new Melon(Math.random()*canvasWidth, Math.random()*canvasHeight, Math.random()*200);
            newMelon.add();
            //objectList.add(newMelon);
            //melonList.add(newMelon);
        }
    }
    
    public static void layEggs(Adult a, Adult b) {
        int numChild = (int) Math.round(average(a.childCount.pheno, b.childCount.pheno));
        for (int i = 0; i < numChild; i++) {
            Egg newEgg = layEgg(a,b);
            a.offspring.add(newEgg);
            b.offspring.add(newEgg);
            newEgg.add();
            //objectList.add(newEgg);
            //puckList.add(newEgg);
            //eggList.add(newEgg);            
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
                average(a.standards.geno, b.standards.geno),
                average(a.childCount.geno, b.childCount.geno),
                average(a.maturation.geno, b.maturation.geno),
                average(a.mutationChance.geno, b.mutationChance.geno)};
        mutateGenes(newGenes);
        Egg newEgg = new Egg(newGenes, a, b, average(a.x, b.x) + 10*Math.random(), average(a.y, b.y) + 10*Math.random(), 0, Math.max(a.gen, b.gen) + 1);
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
    
    public static void hatchEgg (Egg e) {
        Adult newAdult;
        newAdult = new Adult(e.genes, e.x, e.y, e.mass, e.gen);
        newAdult.add();
        observed = (observed != null && observed.equals(e)) ? newAdult : observed;
        maxGen = (e.gen > maxGen) ? e.gen : maxGen; 
        e.die();
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
                for (int i = 0; i < puckList.size(); i++) {
                    Puck p = puckList.get(i);
                    if (p instanceof Adult) { 
                        Adult a = (Adult) p;
                        a.go();
                    } else if (p instanceof Egg) {
                        Egg e = (Egg) p;
                        e.go();
                    }
                }
                spawnMelon();
                TimeUnit.MILLISECONDS.sleep((long) (ticklength * 1000.0));
                timeElapsed += Sim.ticklength / 60;
            }
        }
    }
    
    static double average(double a, double b) {
        return (a+b)/2;
    }
    
    protected static int[] formatTimeHelper(double time) {
        int[] output = new int[] {
                (int) time / 60,
                (int) time % 60,
                (int) (60 * (time - Math.floor(time)))
        };
        return output;
    }
    
    public static String formatTime(double raw) {
        int[] time = formatTimeHelper(raw);
        DecimalFormat df = new DecimalFormat("00");
        String hours = df.format(time[0]);
        String minutes = df.format(time[1]);
        String seconds = df.format(time[2]);
        return (time[0] > 0) ? hours + ":" + minutes + ":" + seconds : minutes + ":" + seconds;
    }
    
    public static double translateMutationChance(double mutationChanceGeno) {
        return mutationChanceGeno / 4000;
    }
    
    

    public void paint(Graphics g) {        
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);        
        
        for (int i = 0; i < objectList.size(); i++) {
            GameObject o = objectList.get(i);
            o.draw(g2d);
            if (observed != null && observed.equals(o)) {
                o.displayInfo(g2d);
            }
        }
        
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, canvasWidth, canvasHeight);
        
        g.setColor(Color.BLACK);
        g.setFont(plain);
        g.drawString("Max Generation: " + maxGen, 0, canvasHeight - fontSize * 3);
        g.drawString("Population:     " + adultList.size(), 0, canvasHeight - fontSize * 2);
        g.drawString("Elapsed Time:   " + formatTime(timeElapsed), 0, canvasHeight - fontSize * 1);
    }
}