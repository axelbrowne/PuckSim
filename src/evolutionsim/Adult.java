package evolutionsim;

import java.awt.*;


public class Adult extends Puck {
    
    double cooldown;
    //double timeUnitlBored;
    //Gene[] translation;

    Adult(double[] dnaValues, double xcor, double ycor, double mass) {
        super(dnaValues, xcor, ycor, mass);
        translate();
        cooldown = 0;
        age = 0;
        //timeUntilBored = untilBored.geno;
    }
    
    public void translate() {
        // increase with cube of mass
        power.pheno = power.geno * 50000.0;
        // decrease with age
        freq.pheno = (1000.0 - freq.geno) / 600.0;
        // increase with square of mass
        friction.pheno = friction.geno * -600.0;
        //
        vision.pheno = vision.geno / 4;
        //
        smell.pheno = vision.pheno + smell.geno / 4;
        //
        foodSizePreference.pheno = 1 + foodSizePreference.geno / 1000;
        //
        //untilBored.pheno = untilBored.geno * 6;
    }
    
    public void updateDynamicVars() {
        /**
        if (timeUntilBored <= 0) {
            heading = 2*Math.PI*Math.random();
        }
        timeUntilBored -= Sim.ticklength;
        **/
        if (cooldown <= 0) {
            v += power.pheno*Sim.ticklength/mass;
            seekFood();
            cooldown = freq.pheno;
        }
        cooldown -= Sim.ticklength;
        if (mass <= 0) { die(); }
        else { mass -= Sim.ticklength * (mass / 100); }
        age += Sim.ticklength/60;
        updateRadius();
        //age(); 
    }
    

    
    public void go() {
        updateDynamicVars();
        motion();
        collisionCheck();
    }
    
    public void motion() {
        v = Math.max(0, v + (friction.pheno*Sim.ticklength)/mass);
        xv = Math.cos(heading)*v;
        yv = Math.sin(heading)*v;
        x = x + xv*(Sim.ticklength);
        y = y + yv*(Sim.ticklength);
        if ((int) x < 0 || (int) y < 0 || (int) x > Sim.canvasSize || (int) y > Sim.canvasSize) {
            heading += Math.PI;
        }
    }
    
    public void seekFood() {
        if (Sim.melonList.size() == 0) { return; }
        if (see() != null) {
            towardsVision();
        } else if (smell() != null) {
            towardsSmell();
        } else {
            heading = Math.random() * 2 * Math.PI;
        }
    }
    
    public void towardsSmell() {
        if (smell() != null) {
            heading = smell()[3];
        }
    }
    
    public void towardsVision() {
        Melon m = see();
        if (m != null) {
            heading = getHeadingTowards(m);
        }
    }
    
    public Melon see() {
        if (Sim.melonList.size() == 0) { return null; }
        double target, potential;
        Melon t = Sim.melonList.get(0);
        for (Melon m : Sim.melonList) {
            target = melonScore(t);
            potential = melonScore(m);
            if (potential > target) {
                t = m;
            }
        }
        if (getDistanceTo(t) > vision.pheno) {
            return null;
        } else {
            return t;
        }
    }
    
    public double melonScore(Melon m) {
        return (Math.pow(m.mass, foodSizePreference.pheno) / (getDistanceTo(m) * getDistanceTo(m)));
    }
    
    public double[] smell() {
        //double xVector = 0.0;
        //double yVector = 0.0;
        /**
        double smell = 0.0;
        double angle = 0.0;
        double newSmell = 0.0;
        double newAngle = 0.0;
        **/
        
        double xcor, ycor, newX, newY, strength, newStrength;
        xcor = ycor = newX = newY = strength = newStrength = 0.0;
        for (Melon m : Sim.melonList) {
            newStrength = melonScore(m);
            strength += newStrength;
            newX = (m.x - x) * newStrength;
            newY = (m.y - y) * newStrength;
            xcor += newX;
            ycor += newY;
            
            
            /**
            newSmell = (Math.pow(m.mass, foodSizePreference.pheno))/((Math.hypot(m.x - x, m.y - y) * (Math.hypot(m.x - x, m.y - y))));
            newAngle = (Math.atan2(m.y - y, m.x - x));
            smell += newSmell;
            System.out.println((int)((180 / Math.PI)*(newAngle-angle)));
            angle += (newSmell/(smell))*(newAngle - angle);
            **/
        }
        // [xcor, ycor, dist, heading]
        double[] output = new double[]{xcor, ycor, Math.hypot(xcor, ycor), Math.atan2(ycor, xcor)};
        if (output[0] > smell.pheno) {
            return null;
        }
        return output;
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
    
    public void debug(Graphics2D g) {
        if (Sim.melonList.size() == 0) { return; }
        if (see() != null) {
            g.setStroke(new BasicStroke(2));
            g.setColor(Color.GREEN);
            g.drawLine((int)x, (int)y,
                    (int) (x + 5 * Math.cos(getHeadingTowards(see()))*melonScore(see())),
                    (int) (y + 5 * Math.sin(getHeadingTowards(see()))*melonScore(see())));   
            g.setStroke(new BasicStroke(1));
            g.setColor(Color.CYAN);
            g.drawLine((int)x, (int)y, (int) see().x, (int) see().y);  
        } else if (smell() != null) {
            g.setColor(Color.MAGENTA);
            g.drawLine((int)x, (int)y, (int)(x + 5 * smell()[0]), (int)(y + 5 * smell()[1]));
        } else {
            heading = Math.random() * 2 * Math.PI;
        }
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
        //g.setColor(Color.GREEN);
        //g.fillOval((int)x - 1, (int)y - 1, 2, 2);
        //debug(g);
    }
}
