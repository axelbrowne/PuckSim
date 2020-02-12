package evolutionsim;

import java.awt.*;


public class Adult extends Puck {
    
    double cooldown;
    //Gene[] translation;

    Adult(double[] dnaValues, double xcor, double ycor, double mass) {
        super(dnaValues, xcor, ycor, mass);
        translate();
        cooldown = 0;
        age = 0;
    }
    
    public void updateDynamicVars() {
        // Should uncomment this when these vars actually change w.r.t mass, age, etc.
        //translate();
        if (cooldown <= 0) {
            v += power.pheno*Sim.dt/mass;
            towardsMelon();
            cooldown = freq.pheno;
        }
        cooldown -= Sim.dt;
        //heading += Sim.dt;
        if (mass <= 0) { die(); }
        else { mass -= Sim.dt * (mass / 100); }
        age += Sim.dt/60;
        updateRadius();
        //age(); 
    }
    
    public void translate() {
        // increase with cube of mass
        power.pheno = power.geno * 50000.0;
        // decrease with age
        freq.pheno = (1000.0 - freq.geno) / 600.0;
        // increase with square of mass
        friction.pheno = friction.geno * -600.0;
        //
        smell.pheno = smell.geno / 2;
    }
    
    public void go() {
        updateDynamicVars();
        motion();
        collisionCheck();
    }
    
    public void motion() {
        v = Math.max(0, v + (friction.pheno*Sim.dt)/mass);
        xv = Math.cos(heading)*v;
        yv = Math.sin(heading)*v;
        x = x + xv*(Sim.dt);
        y = y + yv*(Sim.dt);
    }
    
    public void towardsMelon() {
        Melon m = targetMelon();
        if (m != null) {
            heading = getHeadingTowards(m);
        }
    }
    
    public Melon targetMelon() {
        if (Sim.melonList.size() == 0) {
            return null;
        }
        double targetSmell, potentialSmell;
        Melon target = Sim.melonList.get(0);
        for (Melon m : Sim.melonList) {
            targetSmell = ((target.mass * 0.1) / (getDistanceTo(target) * getDistanceTo(target)));
            potentialSmell = ((m.mass * 0.1) / (getDistanceTo(m) * getDistanceTo(m)));
            if (potentialSmell > targetSmell) {
                target = m;
            }
        }
        if (getDistanceTo(target) > smell.pheno) {
            return null;
        } else {
            return target;
        }
    }

    public boolean collisionCheck() {
        if (Sim.melonList.size() == 0) {
            return false;
        }
        for (Melon m : Sim.melonList) {
            if (getDistanceTo(m) <= radius + m.radius) {
                mass += m.mass;
                m.die();
                return true;
            }
        }
        return false;
    }
    
    public double getAge() {
        return age;
    }
    
    public void draw(Graphics2D g) {
        drawX = x - radius;
        drawY = y - radius;
        g.setColor(Color.PINK);
        int[] xDraw = {
                (int)((x)+Math.cos(heading)*radius*(3/4)),
                (int)((x)-Math.cos(heading+Math.PI/6)*(radius+radius*(1.5*power.geno/1000)+radius*(cooldown/freq.pheno))),
                (int)((x)-Math.cos(heading-Math.PI/6)*(radius+radius*(1.5*power.geno/1000)+radius*(cooldown/freq.pheno)))};
        int[] yDraw = {
                (int)((y)+Math.sin(heading)*radius*(3/4)),
                (int)((y)-Math.sin(heading+Math.PI/6)*(radius+radius*(1.5*power.geno/1000)+radius*(cooldown/freq.pheno))),
                (int)((y)-Math.sin(heading-Math.PI/6)*(radius+radius*(1.5*power.geno/1000)+radius*(cooldown/freq.pheno)))};
        g.fillPolygon(xDraw, yDraw, 3);
        g.setColor(Color.BLACK);
        g.fillOval((int)drawX, (int)drawY, (int)radius*2, (int)radius*2);
        g.setColor(Color.GREEN);
        g.fillOval((int)x - 1, (int)y - 1, 2, 2);
        if (targetMelon() != null) {
            g.drawLine((int)x, (int)y, (int)targetMelon().x, (int)targetMelon().y);
        }
    }
}
