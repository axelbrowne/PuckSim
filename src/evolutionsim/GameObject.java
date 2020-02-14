package evolutionsim;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Color;

public class GameObject {
    
    double x, y, v, xv, yv, heading, mass, radius, age;
    double drawX, drawY;

    GameObject (double xcor, double ycor, double m) {
        x = xcor;
        y = ycor;
        v = 0;
        heading = 0;
        xv = Math.cos(heading)*v;
        yv = Math.sin(heading)*v;
        mass = m;
        radius = Math.cbrt(3*(mass)/(4*Math.PI));
    }
    
    public void die() {
        Sim.objectList.remove(this);
        if (this instanceof Melon) {
            Sim.melonList.remove(this);
        } else if (this instanceof Adult) {
            Sim.adultList.remove(this);
        }
    }
    
    protected double getDistanceTo(GameObject target) {
        return Math.hypot((this.x - target.x), (this.y - target.y));
    }
    
    protected double getHeadingTowards(GameObject target) {
        return Math.atan2((target.y - y), (target.x - x));
    }
    
    protected void updateRadius() {
        radius = Math.cbrt(3*(mass)/(4*Math.PI));
    }
    
    public void draw(Graphics2D g) {
        this.draw(g);
    }

    public void displayInfo(Graphics2D g) {
        if (this instanceof Adult) {
            Adult a = (Adult) this;
            int fontsize = 12;
            g.setFont(new Font("Courier", Font.PLAIN, fontsize));
            g.setColor(Color.BLACK);
            FontMetrics fontMetrics = g.getFontMetrics();
            
            int hr = (int) a.getAge() / 60;
            int min = (int) a.getAge() % 60;
            int sec = (int) (60 * (a.getAge() - Math.floor(a.getAge())));
            String time = Sim.formatTime(hr, min, sec);            
            g.drawString("Mass:       " + Integer.toString((int)mass), 0, 0 + fontMetrics.getAscent());
            g.drawString("Age:        " + time, 0, fontMetrics.getAscent() + fontsize);
            g.drawString("Power:      " + (int) a.power.geno, 0, fontMetrics.getAscent() + fontsize*3);
            g.drawString("Frequency:  " + (int) a.freq.geno, 0, fontMetrics.getAscent() + fontsize*4);
            g.drawString("Friction:   " + (int) a.friction.geno, 0, fontMetrics.getAscent() + fontsize*5);
            g.drawString("Vision:     " + (int) a.vision.geno, 0, fontMetrics.getAscent() + fontsize*6);
            g.drawString("Smell:      " + (int) a.smell.geno, 0, fontMetrics.getAscent() + fontsize*7);
            g.drawString("MelSizPref: " + (int) a.foodSizePreference.geno, 0, fontMetrics.getAscent() + fontsize*8);
            a.debug(g);
        }
    }
}
