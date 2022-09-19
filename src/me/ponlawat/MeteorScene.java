package me.ponlawat;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Iterator;

public class MeteorScene extends JPanel {
    JFrame cFrame;

    MeteorScene(JFrame frame){
        cFrame = frame;

        setSize(cFrame.getWidth(), cFrame.getHeight());
    }

    Image boom = Toolkit.getDefaultToolkit().createImage(System.getProperty("user.dir")+File.separator+"images"+File.separator+"bomb.gif");

    @Override
    protected void paintComponent(Graphics g) {
        Iterator<meteor> mss = meteor.getMeteors().iterator();
        while (mss.hasNext()) {
            meteor m = mss.next();
            if(m.isBoom()) continue;

            Graphics2D g2 = (Graphics2D) g;

            if(m.isDestroyed()) g2.drawImage(boom, (int)m.getMetX()-20, (int)m.getMetY()-20, cFrame); else g2.drawImage(m.getMeteorImg(), (int)m.getMetX(), (int)m.getMetY(), cFrame);
//            g2.rotate(m.getRotate(), m.getMeteorImg().getWidth() / 2, m.getMeteorImg().getHeight() / 2);
        }
    }
}
