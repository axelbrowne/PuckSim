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
        hitbox = new Rectangle((int)x, (int)y, (int)(radius*2), (int)(radius*2));
    }
    
    public void updateDynamicVars() {
        // Should uncomment this when these vars actually change w.r.t mass, age, etc.
        //translate();
        if (cooldown <= 0) {
            v += power.pheno*Sim.dt/mass;
            cooldown = freq.pheno;
        }
        cooldown -= Sim.dt;
        heading += Sim.dt;
        age(); 
    }
    
    public void translate() {
        // increase with cube of mass
        power.pheno = power.geno * 50000.0;
        // decrease with age
        freq.pheno = (1000.0 - freq.geno) / 600.0;
        // increase with square of mass
        friction.pheno = friction.geno * -600.0;
    }
    
    public void go() {
        updateDynamicVars();
        motion();
        //collisionCheck();
    }
    
    public void motion() {
        v = Math.max(0, v + (friction.pheno*Sim.dt)/mass);
        xv = Math.cos(heading)*v;
        yv = Math.sin(heading)*v;
        x = x + xv*(Sim.dt);
        y = y + yv*(Sim.dt);
    }
    
    public boolean collisionCheck() {
        if (Sim.melonList.size() == 0) {
            System.out.println("0");
            return false;
        }
        for (Melon m : Sim.melonList) {
            System.out.println("Bruh.");
            if (hitbox.intersects(m.hitbox)) {
                System.out.println("collision!");
                return true;
            }
        }
        return false;
    }

    
    public void draw(Graphics2D g) {
        g.setColor(Color.PINK);
        int[] xDraw = {(int)((x+radius)+Math.cos(heading)*radius*(3/4)),
                (int)((x+radius)-Math.cos(heading+Math.PI/6)*(radius+radius*(1.5*power.geno/1000)+radius*(cooldown/freq.pheno))),
                (int)((x+radius)-Math.cos(heading-Math.PI/6)*(radius+radius*(1.5*power.geno/1000)+radius*(cooldown/freq.pheno)))};
        int[] yDraw = {(int)((y+radius)+Math.sin(heading)*radius*(3/4)),
                (int)((y+radius)-Math.sin(heading+Math.PI/6)*(radius+radius*(1.5*power.geno/1000)+radius*(cooldown/freq.pheno))),
                (int)((y+radius)-Math.sin(heading-Math.PI/6)*(radius+radius*(1.5*power.geno/1000)+radius*(cooldown/freq.pheno)))};
        g.fillPolygon(xDraw, yDraw, 3);
        g.setColor(Color.BLACK);
        g.fillOval((int)x, (int)y, (int)radius*2, (int)radius*2);
        if (collisionCheck()) {
            g.setColor(Color.GREEN);
        } else {
            g.setColor(Color.RED);
        }
        g.drawRect((int)x, (int)y, (int)(radius*2), (int)(radius*2));
    }
}
