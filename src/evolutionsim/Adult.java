package evolutionsim;

public class Adult extends Puck {
    
    double cooldown;
    Gene[] translation;

    Adult(double[] dnaValues, double xcor, double ycor, double mass) {
        super(dnaValues, xcor, ycor, mass);
        translation = new Gene[Sim.traitNames.length];
        for (int i = 0; i < translation.length; i++) {
            translation[i] = new Gene(Sim.traitNames[i], 0);
        }
        translate();
        cooldown = 0;
        
    }
    
    public void updateVars() {
        age();
        translate();

    }
    
    public void translate() {
        for (int i = 0; i < translation.length; i++) {
            if (translation[i].name.equals("power")) {
                double powScale = 75000.0 * Math.pow(mass, 3);
                //double powScale = 
                translation[i].value = (double) traits[i].value * 75000.0 * Math.pow(mass, 3);
            } else if (translation[i].name.equals("cooldown")) {
                double cdScale = 0.000015 * Math.pow(age, 2);
                translation[i].value = (double) traits[i].value / 500.0 + cdScale;
            } else if (translation[i].name.equals("friction")) {
                //double fricScale = Math.pow(mass, 2);
                double fricScale = 1;
                translation[i].value = (double) traits[i].value * -0.00025 * fricScale;
            }
        }
    }
    
    public void go() {
        updateVars();
        //System.out.println(v);
        //System.out.println(translation.get(0).name + ": " + translation.get(0).value);
        if (cooldown <= 0) {
            v = v + (translation[0].value*Sim.dt)/mass;
            //motion();
            cooldown = translation[1].value;
        }
        motion();
        cooldown -= Sim.dt;
    }
    
    public void motion() {
        
        System.out.println(v);
        //System.out.println((Sim.friction*Sim.dt)/mass);
        v = Math.max(0, v + (translation[2].value*mass*mass*Sim.dt)/mass);
        xv = Math.cos(heading)*v;
        yv = Math.sin(heading)*v;
        x = x + xv*(Sim.dt);
        y = y + yv*(Sim.dt);
    }

}
