package evolutionsim;

import java.awt.*;
import java.lang.Object;

public class Melon extends GameObject {

    Melon(double xcor, double ycor, double m) {
        super(xcor, ycor, m);
        hitbox = new Rect((int)(x+radius*3), (int)(y+radius*3), (int)(radius*2), (int)(radius*2));
    }
    
    public void draw(Graphics2D g) {
        if (hitbox.intersects(Sim.population.get(0).hitbox)) {
            g.setColor(Color.GREEN);
            System.out.println("score!");
        } else {
            g.setColor(Color.RED);
        }
        g.drawRect((int)(x+radius*3), (int)(y+radius*3), (int)(radius*2), (int)(radius*2));
        Color melonGreen = new Color(38,189,123);
        Color melonRed = new Color(242,62,63);
        g.setColor(melonGreen);
        g.fillArc((int)x, (int)y, (int)(radius*6), (int)(radius*6), 225, 180);
        g.setColor(melonRed);
        g.fillArc((int)(x+radius/2), (int)(y+radius/2), (int)(radius*5), (int)(radius*5), 225, 180);
    }

}
