package evolutionsim;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Egg extends Puck {
    
    ArrayList<Adult> parents;
    double[] genes;
    double maturity;
    
    Egg(double[] dnaValues, Adult a, Adult b, double xcor, double ycor, double mass, int generation) {
        super(dnaValues, xcor, ycor, mass, generation);
        genes = dnaValues;
        translate();
        parents = new ArrayList<Adult>();
        parents.add(a);
        parents.add(b);
        maturity = 0;
    }
    
    public void updateVars() {
        if (maturity >= maturation.pheno) {
            Sim.hatchEgg(this);
        }
        maturity += Sim.ticklength/60;
        updateRadius();
    }
    
    public void go() {
        updateVars();
    }
    
    public void debug(Graphics2D g) {
        g.setColor(Color.GREEN);
        for (int i = 0; i < parents.size(); i++) {
            Adult a = parents.get(i);
            g.drawLine((int)x, (int)y, (int)a.x, (int)a.y);
        }
    }
    
    public void draw(Graphics2D g) {
        drawX = x - radius;
        drawY = y - radius;
        //Color tail = new Color ((int)(power.geno * 0.255), (int)(freq.geno * 0.255), (int)(friction.geno * 0.255));
        //Color body = new Color ((int)(vision.geno * 0.255), (int)(smell.geno * 0.255), (int)(melSizePref.geno * 0.255));
        Color shell = new Color (255 - (int) (mutationChance.geno * 0.255), 255, 255 - (int) (mutationChance.geno * 0.255));
        Color one = new Color ((int) (power.geno * 0.255), (int) (freq.geno * 0.255),(int) (friction.geno * 0.255));
        Color two = new Color ((int) (vision.geno * 0.255), (int) (smell.geno * 0.255), (int) (melSizePref.geno * 0.255));
        Color three = new Color((int) (standards.geno * 0.255), (int) (childCount.geno * 0.255), (int) (maturation.geno * 0.255));
        //g.fillPolygon(xDraw, yDraw, 3);
        //g.setColor(body);
        g.setColor(shell);
        g.fillOval((int)drawX, (int)drawY, (int)radius*2, (int)radius*2);
        g.setColor(one);        
        g.fillOval((int)(drawX), (int)(drawY), (int)(radius), (int)(radius));
        g.setColor(two);
        g.fillOval((int)(x), (int)(drawY), (int)(radius), (int)(radius));
        g.setColor(three);
        g.fillOval((int)(x - radius/2), (int)(y), (int)radius, (int)radius);
        
    }
    
}
