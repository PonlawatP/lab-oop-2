package me.ponlawat;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3542183766389063737L;
	JFrame cFrame;
	int amount;
    Frame(int amount){
    	cFrame = this;
    	this.amount = amount;
    	
    	setTitle(amount + " meteor left | Meteor Simulator");
    	
        setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1200, 600);
//		setPreferredSize(new Dimension(1200, 720));
		setLocationRelativeTo(null);
        setContentPane(comp());
        setVisible(true);
    }
    
    public JFrame getcFrame() {
    	return cFrame;
    }


    public JPanel comp() {
    	JPanel pan = new JPanel(null);
    	pan.setBackground(Color.BLACK);

    	JLabel l = new JLabel("Click on meteor to destroy");
    	l.setForeground(Color.WHITE);
    	l.setBounds(30,0,200,50);
    	pan.add(l);
    	
    	for(int i = 0; i < amount; i++) {
    		new meteor(getcFrame(), pan);
    	}
    	
    	for(meteor m : meteor.getMeteors()) {
    		m.start();
    	}
    	
    	return pan;
    }
}