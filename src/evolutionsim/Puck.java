package evolutionsim;

import java.awt.Color;
import java.awt.Graphics2D;

//import evolutionsim.EvolutionSim.Puck.Gene;

public class Puck extends GameObject {

    public class Gene {
        String name;
        // 0-1000
        double value;
        
        Gene(String n, double v) {
            name = n;
            value = (double) v;
        }
    }
    
    double age;
    Gene[] traits;
    //Gene power;
    //Gene cooldown;
    //Gene friction;
    //Gene untilBored;
    //Gene durBored;
    //Gene numOffspring;
    //Gene maturationTime;
    //Gene sexualWillingness;
    //Gene chanceOfMutation;
    //Gene smell;
    
    Puck(double[] dnaValues, double xcor, double ycor, double mass) {
        super(xcor, ycor, mass);
        traits = new Gene[Sim.traitNames.length];
        Gene geneConstructor = new Gene(null, 0);
        for (int i = 0; i < traits.length; i++) {
            geneConstructor.name = Sim.traitNames[i];
            geneConstructor.value = dnaValues[i];
            traits[i] = geneConstructor;
        }
        
        age = 0;
    }
    
    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillOval((int)x, (int)y, (int)radius*2, (int)radius*2);
    }
    
    public void age() {
        age += Sim.dt;
    }
    
}