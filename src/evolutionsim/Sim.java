// TODO add more comments everywhere!

package evolutionsim;

import java.awt.*;
import java.text.DecimalFormat;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;
import javax.swing.JPanel;

public class Sim extends JPanel {
	// DYNAMIC \\
	public static double timeElapsed;
	public static int maxGen;

	// CONSTANTS \\
	// Don't really know what this does, probably something to do with JPanel.
	private static final long serialVersionUID = 1L;
	// Lists of different Agent types that we use when iterating for
	// certain types.
	// TODO think about if using LinkedList instead of ArrayList would be more
	// efficient
	public static ArrayList<Puck> puckList = new ArrayList<Puck>();
	public static ArrayList<Adult> adultList = new ArrayList<Adult>();
	public static ArrayList<Egg> eggList = new ArrayList<Egg>();
	public static ArrayList<Melon> melonList = new ArrayList<Melon>();
	public static ArrayList<Agent> agentList = new ArrayList<Agent>();
	public static Agent observed = null;

	public static boolean pause = false;

	// Width and height of the canvas or Sim "field"
	public static int canvasWidth = 575;
	public static int canvasHeight = 575;
	// Number of seconds in a tick
	public static double tickLength = 10.0 / 1000.0;
	// Population of Adults at the start of the Sim
	public static int count = 5;
	// Chance of a Melon spawning at per second
	public static double spawnChance = tickLength * 5;
	// Formatting constants.
	public static int fontSize = 12;
	public static Font plain = new Font("Courier", Font.PLAIN, fontSize);
	public static Font bold = new Font("Courier", Font.BOLD, fontSize);

	/**
	 * Constructor
	 */
	Sim() {
		timeElapsed = 0;
		maxGen = 0;
		populate();
	}

	public void populate() {
		Adult newAdult;
		for (int i = 0; i < count; i++) {
			// Used for tweaking phenotype expression.
			// double[] defaultGenes = {500.0, 500.00, 500.0, 500.0, 500.0, 500.0, 500.0,
			// 500.0, 500.0, 500.0, 999.9};
			// All traits randomized except for mutation chance which is 999.9 to start (for
			// lots of genetic variation at the start).
			double[] randomGenes = { Math.random() * 1000.0, Math.random() * 1000.0, Math.random() * 1000.0,
					Math.random() * 1000.0, Math.random() * 1000.0, Math.random() * 1000.0, Math.random() * 1000.0,
					Math.random() * 1000.0, Math.random() * 1000.0, Math.random() * 1000.0, 999.9 };
			newAdult = new Adult(randomGenes, Math.random() * canvasWidth, Math.random() * canvasHeight, 1500.0, 0);
			newAdult.add();
		}
	}

	public static void spawnMelon() {
		Melon newMelon;
		if (Math.random() <= spawnChance) {
			newMelon = new Melon(Math.random() * canvasWidth, Math.random() * canvasHeight, Math.random() * 200);
			newMelon.add();
		}
	}

	public static void layEggs(Adult a, Adult b) {
		int numChild = (int) Math.round(average(a.childCount.pheno, b.childCount.pheno));
		for (int i = 0; i < numChild; i++) {
			Egg newEgg = layEgg(a, b);
			a.offspring.add(newEgg);
			b.offspring.add(newEgg);
			newEgg.add();
		}
	}

	private static Egg layEgg(Adult a, Adult b) {
		double newGenes[] = { average(a.power.geno, b.power.geno), average(a.freq.geno, b.freq.geno),
				average(a.friction.geno, b.friction.geno), average(a.vision.geno, b.vision.geno),
				average(a.smell.geno, b.smell.geno), average(a.melSizePref.geno, b.melSizePref.geno),
				average(a.standards.geno, b.standards.geno), average(a.childCount.geno, b.childCount.geno),
				average(a.maturation.geno, b.maturation.geno), average(a.mutationChance.geno, b.mutationChance.geno) };
		mutateGenes(newGenes);
		Egg newEgg = new Egg(newGenes, a, b, average(a.x, b.x) + 10 * Math.random(),
				average(a.y, b.y) + 10 * Math.random(), 0, Math.max(a.gen, b.gen) + 1);
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

	public static void hatchEgg(Egg e) {
		Adult newAdult;
		newAdult = new Adult(e.genes, e.x, e.y, e.mass, e.gen);
		newAdult.add();
		observed = (observed != null && observed.equals(e)) ? newAdult : observed;
		maxGen = (e.gen > maxGen) ? e.gen : maxGen;
		e.die();
	}

	private static JFrame setupSim() {
		JFrame simFrame = new JFrame("Simulation");
		JPanel mousePanel = new JPanel();
		Sim sim = new Sim();
		mousePanel.setSize(canvasWidth, canvasHeight);
		mousePanel.setOpaque(false);
		// didn't know what this did before, still don't know what it does.
		// simulation.pack();
		mousePanel.addMouseListener(new InputListener());
		simFrame.add(mousePanel);
		simFrame.add(sim);
	    simFrame.setPreferredSize(new Dimension(canvasWidth + 14, canvasHeight + 14 + 23));
	    simFrame.pack();
		simFrame.addKeyListener(new InputListener());
		simFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		simFrame.setVisible(true);
		return simFrame;
	}

	public static JFrame setupAgentInfo() {
        JFrame agentInfo = new JFrame("Agent Information");
        agentInfo.setVisible(false);
        //agentInfo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        agentInfo.setSize(200,400);
        agentInfo.addKeyListener(new InputListener());
        return agentInfo;
    }
	
	/**
	 * This sucks more for some reason
	 * @param ai
	 */
	public static void updateAgentInfoMoreLag(JFrame ai) {
		JPanel panel = new JPanel(new GridLayout(0,1));
		ai.setVisible(true);
		
		Puck p = (Puck) observed;
		
		String info = "<html>Generation: " + p.gen + "<br/>"
				+ "Mass:       " + (int) p.mass + "<br/>";
		if (p instanceof Adult) {
			Adult a = (Adult) p;
			info = info +  "Age:       " + formatTime(a.age) + "<br/>"
					+ "Offspring: " + a.offspring.size() + "<br/>";
		} else if (p instanceof Egg) {
			Egg e = (Egg) p;
			info = info + "Maturity:   " + formatTime(e.maturity) + "<br/>";
		}
		info = info + "<b>TRAITS</b><br/>"
	    		+ "Power:      " + (int) p.power.geno + "<br/>"
	    		+ "Frequency:  " +  (int) p.freq.geno + "<br/>"
	    		+ "Friction:   " + (int) p.friction.geno + "<br/>"
	    		+ "Vision:     " + (int) p.vision.geno + "<br/>"
	    		+ "Smell:      " + (int) p.smell.geno + "<br/>"
	    		+ "MelSizPref: " + (int) p.melSizePref.geno + "<br/>"
	    		+ "Standards:  " + (int) p.standards.geno + "<br/>"
	    		+ "ChildCount: " + (int) p.childCount.geno + "<br/>"
	    		+ "Maturation: " + (int) p.maturation.geno + "<br/>"
	    		+ "MutChance:  " + (int) p.mutationChance.geno + "<br/>"
	    		+ "</html>";
		JLabel infoLabel = new JLabel(info);
		infoLabel.setFont(plain);
		ai.add(infoLabel);
	}
	
	/**
	 * This sucks! Adds a lot of lag.
	 * @param ai
	 */
	public static void updateAgentInfo(JFrame ai) {
		JPanel panel = new JPanel(new GridLayout(0,1));
		ai.setVisible(true);
		
		Puck p = (Puck) observed;
		// I don't really know why I need this here, since observed was checked to be != null
		// But there is a bug if I remove this
		if (p == null) { return; }
	    JLabel gen = new JLabel			("Generation: " + p.gen);
	    JLabel mass = new JLabel		("Mass:       " + (int) p.mass);
	    JLabel traits = new JLabel		("TRAITS");
	    JLabel power = new JLabel		("Power:      " + (int) p.power.geno);
	    JLabel frequency = new JLabel	("Frequency:  " +  (int) p.freq.geno);
	    JLabel friction = new JLabel	("Friction:   " + (int) p.friction.geno);
	    JLabel vision = new JLabel		("Vision:     " + (int) p.vision.geno);
	    JLabel smell = new JLabel		("Smell:      " + (int) p.smell.geno);
	    JLabel melSizePref = new JLabel	("MelSizPref: " + (int) p.melSizePref.geno);
	    JLabel standards = new JLabel	("Standards:  " + (int) p.standards.geno);
	    JLabel childCount = new JLabel	("ChildCount: " + (int) p.childCount.geno);
	    JLabel maturation = new JLabel	("Maturation: " + (int) p.maturation.geno);
	    JLabel mutChance = new JLabel	("MutChance:  " + (int) p.mutationChance.geno);

		
	    JLabel[] labelList;
	    
		if (observed instanceof Adult) {
			Adult a = (Adult) observed;
		    JLabel age = new JLabel		("Age:       " + formatTime(a.age));
		    JLabel offspring = new JLabel("Offspring: " + a.offspring.size());
		    labelList = new JLabel[] {gen, age, mass, offspring, traits, power, frequency, friction, vision, smell, melSizePref, standards, childCount, maturation, mutChance};

		} else {
			Egg e = (Egg) observed;
			JLabel maturity = new JLabel ("Maturity:   " + formatTime(e.maturity));
		    labelList = new JLabel[] {gen, maturity, mass, traits, power, frequency, friction, vision, smell, melSizePref, standards, childCount, maturation, mutChance};
		}
		
	    for (JLabel l : labelList) {
	    	l.setFont(plain);
	    	if (l == traits) { l.setFont(bold); }
	       	panel.add(l);
	    }
	    ai.add(panel);
	}

	public static void main(String[] args) throws InterruptedException {
		JFrame simulation = setupSim();
		JFrame agentInfo = setupAgentInfo();
		int startTime = (int) (System.nanoTime() * Math.pow(10, -9));
		int elapsedTime;
		while (puckList.size() > 0) {
			elapsedTime = (int) (System.nanoTime() * Math.pow(10, -9)) - startTime;
			System.out.println((int) (timeElapsed * 60) + "/" + elapsedTime);
			if (observed != null ) { updateAgentInfo(agentInfo); }
			else { agentInfo.setVisible(false); }
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
				TimeUnit.MILLISECONDS.sleep((long) (tickLength * 1000.0));
				timeElapsed += tickLength / 60;
			}

		}
	}

	static double average(double a, double b) {
		return (a + b) / 2;
	}

	protected static int[] formatTimeHelper(double time) {
		int[] output = new int[] { (int) time / 60, (int) time % 60, (int) (60 * (time - Math.floor(time))) };
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
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		for (int i = 0; i < agentList.size(); i++) {
			Agent o = agentList.get(i);
			o.draw(g2d);
			 // if (observed != null && observed.equals(o)) { o.agentInfo(); }
		}

		g.setColor(Color.BLACK);
		g.drawRect(0, 0, canvasWidth-2, canvasHeight-2);

		g.setColor(Color.BLACK);
		g.setFont(plain);
		g.drawString("Max Generation: " + maxGen, 0, canvasHeight - fontSize * 3);
		g.drawString("Population:     " + adultList.size(), 0, canvasHeight - fontSize * 2);
		g.drawString("Elapsed Time:   " + formatTime(timeElapsed), 0, canvasHeight - fontSize * 1);
	}
}