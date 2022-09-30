package me.ponlawat;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Random;

public class meteor extends JPanel implements Runnable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2213595086613327537L;
	static ArrayList<meteor> al = new ArrayList<>();
	Thread t = new Thread(this);
	JFrame cFrame;
//	JPanel cPan;
	byte b[] = new byte[8];
	String id;

	double x=0, y=0, rotate=0;
	int v=Math.random()>=0.5?-1:1,h=Math.random()>=0.5?-1:1, xf=-99, yf=-99;

	boolean ride = false;
	boolean destroyed = false;
	boolean boom = false;

	boolean click = false;

	boolean isColl = false;
	int collThr = 0;
	int nocollThr = 0;
	
	JLabel boomlab = new JLabel();

	public boolean isBoom() {
		return boom;
	}

	public void setBoom(boolean boom) {
		this.boom = boom;
	}
	double valo_x = 0.1 + new Random().nextDouble(1.0), valo_y = 0.1 + new Random().nextDouble(1.0);
	BufferedImage bi = null;

	private boolean randomPosition(int i){
		if(i >= 20) return false;

		x = new Random().nextInt(cFrame.getWidth()-60);
		y = new Random().nextInt(cFrame.getHeight()-60);

//		y = 0;

		ArrayList copy_mss = new ArrayList<>(meteor.getMeteors());
		Iterator<meteor> metlist = copy_mss.iterator();
		do {
			if(!metlist.hasNext()) return true;
			meteor m = metlist.next();
			if(m.isOverride(getMetX(), getMetY())){
				return randomPosition(i+1);
			}
		} while (metlist.hasNext());

		try {
			Thread.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return true;
	}

    meteor(JFrame frame){
    	cFrame = frame;
//    	cPan = panel;

		if(!randomPosition(1)) return;

    	new Random().nextBytes(b);
    	id = new String(b, Charset.forName("UTF-8"));
    	
    	int rand = new Random().nextInt(9) + 1;
    	try {
    		bi = ImageIO.read(new File("images"+File.separator+rand+".png"));
    	} catch(Exception e) {}
    	
    	bi = resize(bi, 50, 50);
    	al.add(this);
		cFrame.setTitle(getMeteors().stream().filter(meteor -> meteor != null && !meteor.isDestroyed()).toList().size() + " meteor left | Meteor Simulator");
    }
    
    public String getID() {
    	return id;
    }
    
    public Thread getThread() {
    	return t;
    }

	public double getRotate() {
		return rotate;
	}
	public BufferedImage getMeteorImg(){
		return bi;
	}

    @Override
    public Dimension getPreferredSize() {
    	return new Dimension(50, 50);
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

	public boolean isClick() {
		return click;
	}

	public void setClick(boolean click) {
		this.click = click;
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
		return ride;
	}

	public void setRide(boolean ride) {
		this.ride = ride;
	}

	public boolean isOverrideHorizontal(double ox) {
		return (ox < getMetX() && ox+50 >= getMetX()) || (ox >= getMetX() && ox <= getMetX()+50);
	}
	public boolean isOverrideVertical(double oy) {
		return (oy < getMetY() && oy+50 >= getMetY()) || (oy >= getMetY() && oy <= getMetY()+50);
	}

	public boolean isOverride(double ox, double oy) {
		boolean a = isOverrideHorizontal(ox) && isOverrideVertical(oy);

		return a;
	}
	
	public boolean isDestroyed() {
		return destroyed;
	}

	public void setDestroyed(boolean destroyed) {
		this.destroyed = destroyed;
	}
	
	public void killMeteor() {
		if(isDestroyed()) return;
		setCursor(null);
		setVisible(false);
		setDestroyed(true);
	}

	private void handleMeteorAttack(meteor target, boolean forward_or_up){
		if(getMetX() < target.getMetX() && getMetX()+50 >= target.getMetX()){
			setMetX(getMetX() - 1);
		} else if(getMetX() >= target.getMetX() && getMetX() <= target.getMetX()+50){
			setMetX(getMetX() + 1);
		} else if(getMetY() < target.getMetY() && getMetY()+50 >= target.getMetY()){
			setMetY(getMetY() - 1);
		} else if(getMetY() >= target.getMetY() && getMetY() <= target.getMetY()+50){
			setMetY(getMetY() + 1);
		}

		if(forward_or_up){
			if(isForward() != target.isForward()){
				setForward(!isForward());
				target.setForward(!target.isForward());
			} else {
				if(target.getMetValoX() > getMetValoX()){
					target.setForward(!target.isForward());
				} else {
					setForward(!isForward());
				}
			}
		} else {
			if(isUp() != target.isUp()){
				setUp(!isUp());
				target.setUp(!target.isUp());
			} else {
				if(target.getMetValoY() > getMetValoY()){
					target.setUp(!target.isUp());
				} else {
					setUp(!isUp());
				}
			}
		}
		randomValocity();
//		target.randomRotate();
//		randomRotate();
	}
	void randomValocity(){
		//สุ่มความเร็มใหม่เมื่อชน
		valo_x =  new Random().nextDouble(1.0)+0.1;
		valo_y =  new Random().nextDouble(1.0)+0.1;
	}

    @Override
    public void run() {
		while(!isDestroyed() || !isBoom()){
			if(isDestroyed()) {
				try {
					cFrame.setTitle(getMeteors().stream().filter(meteor -> meteor != null && !meteor.isDestroyed()).toList().size() + " meteor left | Meteor Simulator");
					Thread.sleep(600);
				} catch (Exception e) {
//					e.printStackTrace();
				}
				setBoom(true);

				Iterator<meteor> m = getMeteors().iterator();
				while(m.hasNext()) {
					meteor mn = m.next();
					if(mn != null && mn.getID() == id) {
						m.remove();
						return;
					}
				}
				cFrame.repaint();
				break;
			}
			if(isDestroyed() && !isBoom()) continue;


			ArrayList copy_mss = new ArrayList<>(getMeteors());
			Iterator<meteor> iMet = copy_mss.iterator();
			while(iMet.hasNext()) {
				meteor m = iMet.next();

				if(id == null || m == null || m == this || m.isDestroyed() || m.isBoom()) {
					continue;
				}

				if(m.isOverride(getMetX(), getMetY())) {
					if(m.isOverrideHorizontal(getMetX())) {
							handleMeteorAttack(m, true);
					}
					
					if(m.isOverrideVertical(getMetY())) {
							handleMeteorAttack(m, false);
					}
				}
			}
			//การเคลื่อนที่
			x += h * valo_x;
			y += v * valo_y;
			
			//ชนขอบเฟรม
			if(x+68 >= cFrame.getWidth() || x < 0) {
				h *= -1;
				randomValocity();
				if(x < 0) x = 0;
			}
			if(x+68 >= cFrame.getWidth()){
				x--;
			}
			if(y+88 >= cFrame.getHeight() || y < 0) {
				v *= -1;
				randomValocity();
				if(y < 0) y = 0;
			}
			if(y+88 >= cFrame.getHeight()){
				y--;
			}
//			setLocation((int)x, (int)y);

			cFrame.repaint();
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
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