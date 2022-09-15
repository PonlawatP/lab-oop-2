package me.ponlawat;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
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
	JPanel cPan;
	byte b[] = new byte[8];
	String id;

	double x=0, y=0, rotate=0;
	int v=Math.random()>=0.5?-1:1,h=Math.random()>=0.5?-1:1, xf=-99, yf=-99;

	boolean ride = false;
	boolean destroyed = false;
	boolean boom = false;
	
	JLabel boomlab = new JLabel();

	public boolean isBoom() {
		return boom;
	}

	public void setBoom(boolean boom) {
		this.boom = boom;
	}

	//	double valo_x = 0.1 + new Random().nextDouble(0.6), valo_y = 0;
	double valo_x = 0.1 + new Random().nextDouble(0.6), valo_y = 0.1 + new Random().nextDouble(0.6), valo_r = new Random().nextDouble(0.03);
	BufferedImage bi = null;

	private void randomPosition(){
		x = new Random().nextInt(cFrame.getWidth()-60);
		y = new Random().nextInt(cFrame.getHeight()-60);
		
//		y = 0;

		Iterator<meteor> metlist = getMeteors().iterator();
		do {
			if(!metlist.hasNext()) return;
			meteor m = metlist.next();
			if(m.isOverride(getX(), getY())){
				randomPosition();
			}
		} while (metlist.hasNext());

		try {
			Thread.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

    meteor(JFrame frame, JPanel panel){
    	cFrame = frame;
    	cPan = panel;
    	new Random().nextBytes(b);
    	id = new String(b, Charset.forName("UTF-8"));
    	
    	int rand = new Random().nextInt(9) + 1;
    	try {
    		bi = ImageIO.read(new File("images/"+rand+".png"));	
    	} catch(Exception e) {}
    	
    	bi = resize(bi, 50, 50);

		randomPosition();
    	
    	setLocation((int)x,(int)y);
    	setBackground(null);
    	setForeground(null);
    	setOpaque(true);
    	setCursor(new Cursor(Cursor.HAND_CURSOR));
    	
    	setSize(50, 50);
    	
    	cPan.add(this);
    	
    	al.add(this);
    	
    }
    
    public void randomRotate() {
    	valo_r = new Random().nextDouble(0.05);
    }
    
    public String getID() {
    	return id;
    }
    
    public Thread getThread() {
    	return t;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
    	if(isDestroyed()) return;
    	super.paintComponent(g);
    	
    	Graphics2D g2 = (Graphics2D) g;
    	g2.rotate(rotate, bi.getWidth() / 2, bi.getHeight() / 2);
    	g2.drawImage(bi, 0, 0, null);
		
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

	public boolean isOverrideHorizontal(int ox) {
		return (ox < getX() && ox+50 >= getX()) || (ox >= getX() && ox < getX()+50);
	}
	public boolean isOverrideVertical(int oy) {
		return (oy < getY() && oy+50 >= getY()) || (oy >= getY() && oy < getY()+50);
	}

	public boolean isOverride(int ox, int oy) {
		return isOverrideHorizontal(ox) && isOverrideVertical(oy);
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
//		setBackground(Color.RED);
		
		setDestroyed(true);

		ImageIcon ic = new ImageIcon("images/bomb.gif");
    	boomlab.setIcon(ic);
    	boomlab.setSize(80, 80);
    	boomlab.setPreferredSize(new Dimension(80, 80));
    	boomlab.setLocation(getX()-20, getY()-20);
    	boomlab.setVisible(true);
		cPan.add(boomlab);
		
		Iterator<meteor> m = getMeteors().iterator();
		while(m.hasNext()) {
			if(m.next().getID() == id) {
				m.remove();

				cFrame.setTitle(getMeteors().size() + " meteor left | Meteor Simulator");
				return;
			}
		}
		
	}

	private void handleMeteorAttack(meteor target, boolean forward_or_up){
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
		target.randomRotate();
		randomRotate();
	}

    @Override
    public void run() {
		while(!isDestroyed() || !isBoom()){
			if(isDestroyed()) {
				try {
					Thread.sleep(700);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				setBoom(true);
				boomlab.setVisible(false);
//				cPan.remove(cPan.getComponentCount()-1);
				break;
			}
			xf += h * valo_x;
			yf += v * valo_y;
	    	
	    	rotate += valo_r;
			
			Iterator<meteor> iMet = getMeteors().iterator();
			while(iMet.hasNext()) {
				meteor m = iMet.next();
				if(m.id == this.id) {
					continue;
				}

				if(m.isOverride(getX(), getY())) {
					if(m.isOverrideHorizontal(getX())) {
						if(!m.isOverrideHorizontal(xf)) {
							handleMeteorAttack(m, true);
						}

					}
					
					if(m.isOverrideVertical(getY())) {
						if(!m.isOverrideVertical(yf)) {
							handleMeteorAttack(m, false);
						}
					}
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
				e.printStackTrace();
			}
		}
    }
   
    public void start() {
    	t.start();
    	
    	addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				if(!isDestroyed()) killMeteor();
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
			}
		});
    }
    
    public static ArrayList<meteor> getMeteors() {
    	return al;
    }
}