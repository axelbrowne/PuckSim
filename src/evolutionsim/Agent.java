package evolutionsim;

import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Agent extends JPanel {

	double x, y, v, xv, yv, heading, mass, radius, age;
	double drawX, drawY;

	Agent(double xcor, double ycor, double m) {
		x = xcor;
		y = ycor;
		v = 0;
		heading = 0;
		xv = Math.cos(heading) * v;
		yv = Math.sin(heading) * v;
		mass = m;
		radius = Math.cbrt(3 * (mass) / (4 * Math.PI));
	}

	public void die() {
		Sim.agentList.remove(this);

		if (this instanceof Melon) {
			Sim.melonList.remove(this);

		} else if (this instanceof Puck) {
			Sim.puckList.remove(this);

			if (this instanceof Adult) {
				Sim.adultList.remove(this);
				Adult a = (Adult) this;
				for (int i = 0; i < a.offspring.size(); i++) {
					a.offspring.get(i).parents.remove(a);
				}

			} else if (this instanceof Egg) {
				Egg e = (Egg) this;
				for (int i = 0; i < e.parents.size(); i++) {
					e.parents.get(i).offspring.remove(e);
				}
				Sim.eggList.remove(this);
			}
		}
	}

	public void add() {
		Sim.agentList.add(this);

		if (this instanceof Melon) {
			Sim.melonList.add((Melon) this);

		} else if (this instanceof Puck) {
			Sim.puckList.add((Puck) this);

			if (this instanceof Adult) {
				Sim.adultList.add((Adult) this);

			} else if (this instanceof Egg) {
				Sim.eggList.add((Egg) this);
			}
		}
	}

	protected double getDistanceTo(Agent target) {
		return Math.hypot((this.x - target.x), (this.y - target.y));
	}

	protected double getHeadingTowards(Agent target) {
		return Math.atan2((target.y - y), (target.x - x));
	}

	protected void updateRadius() {
		radius = Math.cbrt(3 * (mass) / (4 * Math.PI));
	}

	public void draw(Graphics2D g) {
		this.draw(g);
	}
	
    public static void agentInfo(JFrame agentInfo) {
    	if (Sim.observed == null) { return; }
    	agentInfo.setVisible(true);
    	agentInfo.repaint();
    	
    }
    
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        displayInfo(g2d);
        
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, 100, 100);

        g.setColor(Color.BLACK);
        g.setFont(Sim.plain);
        g.drawString("Max Generation: " + Sim.maxGen, 0, Sim.canvasHeight - Sim.fontSize * 3);

    }
    
  
	
	
	public void displayInfo(Graphics2D g) {
		
		g.setColor(Color.BLACK);
		g.setFont(Sim.plain);
		FontMetrics fontMetrics = g.getFontMetrics();
		if (this instanceof Adult) {
			Adult a = (Adult) this;
			String time = Sim.formatTime(a.age);
			g.drawString("Generation: " + a.gen, 0, fontMetrics.getAscent() + Sim.fontSize * 0);
			g.drawString("Age:        " + time, 0, fontMetrics.getAscent() + Sim.fontSize * 1);
			g.drawString("Mass:       " + Integer.toString((int) mass), 0,
					0 + fontMetrics.getAscent() + Sim.fontSize * 2);
			g.drawString("Offspring:  " + a.offspring.size(), 0, fontMetrics.getAscent() + Sim.fontSize * 3);
			g.setFont(Sim.bold);
			g.drawString("TRAITS", 0, fontMetrics.getAscent() + Sim.fontSize * 5);
			g.setFont(Sim.plain);
			g.drawString("Power:      " + (int) a.power.geno, 0, fontMetrics.getAscent() + Sim.fontSize * 6);
			g.drawString("Frequency:  " + (int) a.freq.geno, 0, fontMetrics.getAscent() + Sim.fontSize * 7);
			g.drawString("Friction:   " + (int) a.friction.geno, 0, fontMetrics.getAscent() + Sim.fontSize * 8);
			g.drawString("Vision:     " + (int) a.vision.geno, 0, fontMetrics.getAscent() + Sim.fontSize * 9);
			g.drawString("Smell:      " + (int) a.smell.geno, 0, fontMetrics.getAscent() + Sim.fontSize * 10);
			g.drawString("MelSizPref: " + (int) a.melSizePref.geno, 0, fontMetrics.getAscent() + Sim.fontSize * 11);
			g.drawString("Standards:  " + (int) a.standards.geno, 0, fontMetrics.getAscent() + Sim.fontSize * 12);
			g.drawString("ChildCount: " + (int) a.childCount.geno, 0, fontMetrics.getAscent() + Sim.fontSize * 13);
			g.drawString("Maturation: " + (int) a.maturation.geno, 0, fontMetrics.getAscent() + Sim.fontSize * 14);
			g.drawString("MutChance:  " + (int) a.mutationChance.geno, 0, fontMetrics.getAscent() + Sim.fontSize * 15);
			a.debug(g);
		} else if (this instanceof Egg) {
			Egg e = (Egg) this;
			String time = Sim.formatTime(e.maturity);
			g.drawString("Mass:       " + Integer.toString((int) mass), 0, 0 + fontMetrics.getAscent());
			g.drawString("Maturity:   " + time, 0, fontMetrics.getAscent() + Sim.fontSize);
			g.drawString("Generation: " + e.gen, 0, fontMetrics.getAscent() + Sim.fontSize * 2);

			g.drawString("Power:      " + (int) e.power.geno, 0, fontMetrics.getAscent() + Sim.fontSize * 6);
			g.drawString("Frequency:  " + (int) e.freq.geno, 0, fontMetrics.getAscent() + Sim.fontSize * 7);
			g.drawString("Friction:   " + (int) e.friction.geno, 0, fontMetrics.getAscent() + Sim.fontSize * 8);
			g.drawString("Vision:     " + (int) e.vision.geno, 0, fontMetrics.getAscent() + Sim.fontSize * 9);
			g.drawString("Smell:      " + (int) e.smell.geno, 0, fontMetrics.getAscent() + Sim.fontSize * 10);
			g.drawString("MelSizPref: " + (int) e.melSizePref.geno, 0, fontMetrics.getAscent() + Sim.fontSize * 11);
			g.drawString("Standards:  " + (int) e.standards.geno, 0, fontMetrics.getAscent() + Sim.fontSize * 12);
			g.drawString("ChildCount: " + (int) e.childCount.geno, 0, fontMetrics.getAscent() + Sim.fontSize * 13);
			g.drawString("Maturation: " + (int) e.maturation.geno, 0, fontMetrics.getAscent() + Sim.fontSize * 14);
			g.drawString("MutChance:  " + (int) e.mutationChance.geno, 0, fontMetrics.getAscent() + Sim.fontSize * 15);
			e.debug(g);
		}
	}
}