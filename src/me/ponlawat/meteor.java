package me.ponlawat;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.charset.Charset;
import java.security.DrbgParameters.NextBytes;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class meteor extends JPanel implements Runnable
{
	static ArrayList<meteor> al = new ArrayList<>();
	Thread t = new Thread(this);
	JFrame cFrame;
	JPanel cPan;
	byte b[] = new byte[8];
	String id;

	double x=0, y=0, rotate=0;
	int v=Math.random()>=0.5?-1:1,h=Math.random()>=0.5?-1:1;

	boolean isRide = false;
	
//	double valo_x = 0, valo_y = 0.1 + new Random().nextDouble(0.6);
	double valo_x = 0.1 + new Random().nextDouble(0.6), valo_y = 0.1 + new Random().nextDouble(0.6);
	BufferedImage bi = null;
	BufferedImage bif = new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB);

	private void randomPosition(){
		x = new Random().nextInt(cFrame.getWidth()-60);
		y = new Random().nextInt(cFrame.getHeight()-60);

		Iterator<meteor> metlist = getMeteors().iterator();
		do {
			if(!metlist.hasNext()) return;
			meteor m = metlist.next();
			if(m.isOverride(getX(), getY())){
				randomPosition();
			}
		} while (metlist.hasNext());
	}

    meteor(JFrame frame, JPanel panel){
    	cFrame = frame;
    	cPan = panel;
    	new Random().nextBytes(b);
    	id = new String(b, Charset.forName("UTF-8"));
    	
    	int rand = new Random().nextInt(9) + 1;
    	try {
    		bi = ImageIO.read(new File("res/images/"+rand+".png"));	
    	} catch(Exception e) {}
    	
    	bi = resize(bi, 30, 30);

		randomPosition();
    	
    	setLocation((int)x,(int)y);
    	setBackground(null);
    	setForeground(null);
    	
    	setSize(30, 30);
    	
    	cPan.add(this);
    	
    	al.add(this);
    }
    
    public String getID() {
    	return id;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	Graphics2D g2 = (Graphics2D) g;
    	g2.rotate(Math.PI / 4, bi.getWidth() / 2, bi.getHeight() / 2);
    	g2.drawImage(bi, 0, 0, null);
    }

    @Override
    public Dimension getPreferredSize() {
    	return new Dimension(30, 30);
    }
    
    public BufferedImage resize(BufferedImage img, int newW, int newH) { 
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }  

    public double getMetX() {
    	return x;
    }
    public double getMetY() {
    	return y;
    }
    public void setMetX(double x) {
    	this.x = x;
    }
    public void setMetY(double y) {
    	this.y = y;
    }

    public double getMetValoX() {
    	return valo_x;
    }
    public double getMetValoY() {
    	return valo_y;
    }
    public void setMetValoX(double x) {
    	this.valo_x = x;
    }
    public void setMetValoY(double y) {
    	this.valo_y = y;
    }
    public boolean isForward() {
    	return h==1;
    }
    public boolean isUp() {
    	return v==-1;
    }
    public void setForward(boolean var) {
    	if(!var) h = -1; else h = 1;
    }
    public void setUp(boolean var) {
    	if(var) v = -1; else v = 1;
    }

	public boolean isRide() {
		return isRide;
	}

	public void setRide(boolean ride) {
		isRide = ride;
	}

	public boolean isOverrideHorizontal(int ox) {
		return (ox < getX() && ox+30 > getX()) || (ox > getX() && ox < getX()+30);
	}
	public boolean isOverrideVertical(int oy) {
		return (oy < getY() && oy+30 > getY()) || (oy > getY() && oy < getY()+30);
	}

	public boolean isOverride(int ox, int oy) {
		return isOverrideHorizontal(ox) && isOverrideVertical(oy);
	}

	private void handleMeteorAttack(meteor tarket, boolean forward_or_up){
//			อัตราเร็ว | ทิศทาง
		if(forward_or_up){
			tarket.setForward(!tarket.isForward());
			setForward(!isForward());
			System.out.print("1\t");
		} else {
			tarket.setUp(!tarket.isUp());
			setUp(!isUp());
			System.out.print("2\t");
		}
	}

    @Override
    public void run() {
		while(true){
			Iterator<meteor> iMet = getMeteors().iterator();
			while(iMet.hasNext()) {
				meteor m = iMet.next();
				if(m.id == this.id) {
					continue;
				}

				if(m.isOverride(getX(), getY())) {
					if(m.isOverrideHorizontal(getX())) {
//						if(m.isRide() || isRide()) continue;
//						m.setRide(!m.isRide());
//						setRide(!isRide());

						handleMeteorAttack(m, true);
					}
					if(m.isOverrideVertical(getY())) {
//						if(m.isRide() || isRide()) continue;
//						m.setRide(!m.isRide());
//						setRide(!isRide());

						handleMeteorAttack(m, false);
					}
					System.out.print("\n");
				} else {
//					if(m.isRide()) m.setRide(false);
//					if(isRide()) setRide(false);
				}
			}

			x += h * valo_x;
			y += v * valo_y;

			if(x+45 > cFrame.getWidth() || x < 0) h *= -1;
			if(y+60 > cFrame.getHeight() || y < 0) v *= -1;
			setLocation((int)x, (int)y);
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
    
    public void start() {
    	t.start();
    }
    
    public static ArrayList<meteor> getMeteors() {
    	return al;
    }
}