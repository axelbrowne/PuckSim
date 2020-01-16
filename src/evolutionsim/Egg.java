package evolutionsim;

public abstract class Egg extends Puck {

    Egg(double[] dnaValues, double xcor, double ycor, double mass) {
        super(dnaValues, xcor, ycor, mass);
        //randomizeGenes(dnaValues);
    }

    /*
    private void randomizeGenes(int[] dnaValues) {
        for (int i : dnaValues) {
            
        }
        for (Gene g : dna) {
            g.value = (int) Math.random()*1000;
        }
    }
    */
}
