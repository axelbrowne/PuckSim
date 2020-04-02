package evolutionsim;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;

public class InputListener implements KeyListener, MouseListener {

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() ==  ' ') {
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
    	int x = e.getX();
    	int y = e.getY();
        for (int i = 0; i < Sim.puckList.size(); i++) {
            Puck p = Sim.puckList.get(i);
            if (Math.hypot((x - p.x), (y - p.y)) < p.radius) {
                Sim.observed = p;
                break;
            }
            if (i == Sim.puckList.size() - 1) {
                Sim.observed = null;
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
