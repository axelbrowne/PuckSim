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
    
    int gen;
    //Gene[] traits;
    
    Gene power;
    Gene freq;
    Gene friction;
    Gene vision;
    Gene smell;
    Gene melSizePref;
    //Gene untilBored;
    //Gene durBored;
    // sexual willingness
    Gene standards;
    // number of children per reproduction
    Gene childCount;
    // time until maturation
    Gene maturation;
    // chance that a gene will mutate after reproduction
    Gene mutationChance;
    
    Puck(double[] dnaValues, double xcor, double ycor, double mass, int generation) {
        super(xcor, ycor, mass);
        power = new Gene(dnaValues[0], 0.0);
        freq = new Gene(dnaValues[1], 0.0);
        friction = new Gene(dnaValues[2], 0.0);
        vision = new Gene(dnaValues[3], 0.0);
        smell = new Gene(dnaValues[4], 0.0);
        melSizePref = new Gene(dnaValues[5], 0.0);
        standards = new Gene(dnaValues[6], 0.0);
        childCount = new Gene(dnaValues[7], 0.0);
        maturation = new Gene(dnaValues[8], 0.0);
        mutationChance = new Gene(dnaValues[9], 0.0);
        //untilBored = new Gene(dnaValues[6], 0.0);
        gen = generation;
    }
    
    public void translate() {
        // increase with cube of mass
        power.pheno = power.geno * 30000.0;
        // decrease with age
        freq.pheno = (1000.0 - freq.geno) / 1200.0;
        // increase with square of mass
        friction.pheno = friction.geno * -50.0;
        //
        vision.pheno = vision.geno / 4;
        //
        smell.pheno = vision.pheno + smell.geno / 4;
        //
        melSizePref.pheno = (-500 + melSizePref.geno) / 500;
        //
        standards.pheno = standards.geno * 13;
        childCount.pheno = childCount.geno / 200;
        maturation.pheno = maturation.geno / 2000;
        // we do this because we use this in a few different spots
        // and i want to remember to keep it consistent in case\
        // a chance is made
        mutationChance.pheno = Sim.translateMutationChance(mutationChance.geno);
    }
}