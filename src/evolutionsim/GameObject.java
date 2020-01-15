package evolutionsim;

public class GameObject {
    
    double x, y, v, xv, yv, heading, mass, radius, age;
    
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
}
