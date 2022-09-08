package me.ponlawat;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Frame extends JFrame
{
	JFrame cFrame;
    Frame(){
    	cFrame = this;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(400, 400);
        setContentPane(comp());
        setVisible(true);
    }
    
    public JFrame getcFrame() {
    	return cFrame;
    }


    public JPanel comp() {
    	JPanel pan = new JPanel(null);
    	pan.setBackground(Color.BLACK);

    	
    	for(int i = 0; i < 10; i++) {
    		new meteor(getcFrame(), pan);
    	}
    	
    	for(meteor m : meteor.getMeteors()) {
    		m.start();
    	}
    	
    	return pan;
    }
}