package evolutionsim;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Egg extends Puck {
    
    ArrayList<Adult> parents;
    double maturity;
    
    Egg(double[] dnaValues, Adult a, Adult b, double xcor, double ycor, double mass) {
        super(dnaValues, xcor, ycor, mass);
        gen = Math.max(a.gen, b.gen) + 1;
        parents = new ArrayList<Adult>();
        parents.add(a);
        parents.add(b);
        maturity = 0;
    }
    
    public void draw(Graphics2D g) {
        System.out.println("egg");
        drawX = x - radius;
        drawY = y - radius;
        //Color tail = new Color ((int)(power.geno * 0.256), (int)(freq.geno * 0.256), (int)(friction.geno * 0.256));
        //Color body = new Color ((int)(vision.geno * 0.256), (int)(smell.geno * 0.256), (int)(melSizePref.geno * 0.256));
        g.setColor(Color.BLUE);
        //g.fillPolygon(xDraw, yDraw, 3);
        //g.setColor(body);
        g.fillOval((int)drawX, (int)drawY, (int)10, (int)10);
    }
    
}
