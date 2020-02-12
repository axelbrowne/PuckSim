package evolutionsim;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class InputListener implements KeyListener, MouseListener {

    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println(e);
        if (e.getKeyChar() ==  'a') {
            System.out.println("pause");
            Sim.pause = !Sim.pause;
        }
        // TODO Auto-generated method stub

    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //System.out.println("click!");
        Point p = MouseInfo.getPointerInfo().getLocation();
        for (Adult a : Sim.adultList) {
            //System.out.println("mouse: (" + p.x + "," + p.y + ")  adult: (" + a.x + "," + a.y + ")");
            //System.out.println(Math.hypot((p.x - a.x), (p.y - a.y)));
            if (Math.hypot((p.x - a.x), (p.y - 23 - a.y)) < a.radius) {
                Sim.observed = a;
            }
        }        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

}
