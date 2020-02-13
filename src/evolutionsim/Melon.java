package evolutionsim;

import java.awt.*;

public class Melon extends GameObject {

    Melon(double xcor, double ycor, double m) {
        super(xcor, ycor, m);
    }
    
    public void draw(Graphics2D g) {
        drawX = x - radius*3.5;
        drawY = y - radius*3.5;
        Color melonGreen = new Color(38,189,123);
        Color melonRed = new Color(242,62,63);
        g.setColor(melonGreen);
        g.fillArc((int)(drawX), (int)(drawY), (int)(radius*6), (int)(radius*6), 225, 180);
        g.setColor(melonRed);
        g.fillArc((int)(drawX + radius/2), (int)(drawY + radius/2), (int)(radius*5), (int)(radius*5), 225, 180);
        //g.setColor(Color.GREEN);
        //g.fillOval((int)x - 1, (int)y - 1, 2, 2);
    }

}
