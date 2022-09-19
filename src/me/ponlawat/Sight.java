package me.ponlawat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

public class Sight extends JPanel implements MouseMotionListener, MouseListener, MouseWheelListener {
    JFrame cFrame;
    int x=0,y=0;
    Image img = Toolkit.getDefaultToolkit().createImage(System.getProperty("user.dir")+ File.separator+"images"+File.separator+"sight.png");

    Sight(JFrame frame){
        cFrame = frame;
        setSize(cFrame.getWidth(), cFrame.getHeight());

        addMouseMotionListener(this);
        addMouseListener(this);

        addMouseWheelListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(img, x-30,y-30,x+30,y+30,0,0,576,576, cFrame);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        cFrame.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        cFrame.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        for(meteor m : meteor.getMeteors()) {
            if (m.isOverride(e.getX(), e.getY()) && !m.isDestroyed()) {
                m.killMeteor();
                break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        System.out.println(e.getWheelRotation());
        if(e.getWheelRotation() == -1){
            meteor m = new meteor(cFrame);
            m.start();
        }
    }
}
