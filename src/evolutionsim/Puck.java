package evolutionsim;

//import evolutionsim.EvolutionSim.Puck.Gene;

public class Puck extends GameObject {

    public class Gene {
        double geno;
        // 0-1000
        double pheno;
        
        Gene(double g, double p) {
            geno = g;
            pheno = p;
        }
    }
    
    double age;
    //Gene[] traits;
    
    Gene power;
    Gene freq;
    Gene friction;
    Gene vision;
    Gene smell;
    Gene foodSizePreference;
    //Gene untilBored;
    //Gene durBored;
    //Gene numOffspring;
    //Gene maturationTime;
    //Gene sexualWillingness;
    //Gene chanceOfMutation;
    
    Puck(double[] dnaValues, double xcor, double ycor, double mass) {
        super(xcor, ycor, mass);
        power = new Gene(dnaValues[0], 0.0);
        freq = new Gene(dnaValues[1], 0.0);
        friction = new Gene(dnaValues[2], 0.0);
        vision = new Gene(dnaValues[3], 0.0);
        smell = new Gene(dnaValues[4], 0.0);
        foodSizePreference = new Gene(dnaValues[5], 0.0);
        //untilBored = new Gene(dnaValues[6], 0.0);
        age = 0;
    }
    
    public void age() {
        age += Sim.ticklength/60;
    }
    
}