import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;

import javax.swing.JOptionPane;

//Alec Ibarra
public class MainLoop
{

    public static void main(String[] args)
    {
    	Logic game = new Logic();
		
		game.addWindowListener(new WindowAdapter()
		{public void windowClosing(WindowEvent e)
		{System.exit(0);}});
		game.setSize(1000,721);
		game.setResizable(false);
		game.setVisible(true);
    
		game.addComponentListener(new ComponentListener() {
        public void componentResized(ComponentEvent e) {}
		public void componentMoved(ComponentEvent e) {}
		public void componentShown(ComponentEvent e) {}
		public void componentHidden(ComponentEvent e) {}
    });
    }
}

@SuppressWarnings("serial")
class Logic extends Frame implements MouseListener,MouseMotionListener,KeyListener,MouseWheelListener
{
	
	//Game state control
	static boolean gameStarted = false;
	static boolean paused = false;
	
	//Game control
	static final int TARGET_FPS = 30;
	static final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;//in nanoseconds
	static final float TICK_TIME = 10 / (float)TARGET_FPS;
	static long lastLoopTime = System.nanoTime();
	static long now = System.nanoTime();
	static long updateLength = 0;
	static long lastFpsTime = 0;
	static long delay = 0;
	static double delta = 0;
	static int displayFPS = 0;
	static int fps = 0;
	static int lag = 0;
	
	static boolean up = false;
	static boolean down = false;
	static boolean right = false;
	static boolean left = false;
	static boolean debug = false;
	static boolean debugFlip = false;
	static boolean debug2 = false;
	static boolean debug2Flip = false;
	
	//Double buffering
	static int worldScale = 11;
	static int screenSizeX = 1000;
	static int screenSizeY = 721;
	static Graphics g2;
	static Image virtualMem = null;
	
	//Prepare class methods
	static ImageLoader im = new ImageLoader();
	static Player player = new Player();
	
	//Prepare temp instances
	static Player tempPlayer = new Player();
	static Entity tempEntity = new Entity();
	
	
	public Logic()
	{
		super("PerlinNoiseIsland");
        addMouseListener(this);//adds neccessary listener
		addMouseMotionListener(this);//adds neccessary listener
		addMouseWheelListener(this);//adds neccessary listener
		addKeyListener(this);//adds neccessary listener
        requestFocusInWindow();//calls focus for window
        
        EntityKeeper.prepare();
        gameStarted = true;//TODO temp game state control
        paused = false;//TODO temp game state control
        
        EntityKeeper.players.add(player);
        EntityKeeper.generateWorld();
	}
	
	public void update(Graphics g)
	{
		//prepare double buffer
		virtualMem = createImage(screenSizeX, screenSizeY);//create image with screen size
		g2 = virtualMem.getGraphics();
		
		if(gameStarted && !paused)
		{
			calcTime();
			while (lag >= TICK_TIME)
			{
			    gameLogic(delta);
			    lag -= TICK_TIME;
			}
			displayGame(g2);
		}
		else if(gameStarted && paused)
		{
			displayPauseScreen(g2);
		}
		else if(!gameStarted && !paused)
		{
			displayTitleScreen(g2);
		}
				
		g.drawImage(virtualMem, 0, 0, this);
		
		delay = (lastLoopTime-System.nanoTime() + OPTIMAL_TIME)/1000000;
		delay(delay);
		repaint();
	}
	
	/** --LOGIC METHODS-- **/
	
	public void gameLogic(double timeElapsed)
	{
		worldLogic(timeElapsed);
		objectLogic(timeElapsed);
		playerLogic(timeElapsed);
	}
	
	public void worldLogic(double timeElapsed)
	{
		
	}
	
	public void objectLogic(double timeElapsed)
	{
		
	}
	
	public void playerLogic(double timeElapsed)
	{
		player.move(timeElapsed);
	}
	
	
	/** --DISPLAY METHODS-- **/
	
	public void displayTitleScreen(Graphics g2)
	{
		
	}
	
	public void displayPauseScreen(Graphics g2)
	{
		
	}
	
	public void displayGame(Graphics g2)
	{
		displayWorld(g2);
		displayEntities(g2);
		displayHUD(g2);
	}
	
	public void displayWorld(Graphics g2)
	{	
		for(int k = 0; k < EntityKeeper.height; k++)//rows
			for(int j = 0; j < EntityKeeper.width; j++)//columns
			{
				AffineTransform at = new AffineTransform();
				at.translate(worldScale*j,(worldScale*k)+21);//position image
				at.scale((float)worldScale/25,(float)worldScale/25);//25s are the pic sizes
				
				if(EntityKeeper.world[j][k] == 0)
				{
					((Graphics2D) g2).drawImage(ImageLoader.getImage("water"), at, this);
				}
				else if(EntityKeeper.world[j][k] == 1)
				{
					((Graphics2D) g2).drawImage(ImageLoader.getImage("dirt"), at, this);
				}
				else if(EntityKeeper.world[j][k] == 2)
				{
					((Graphics2D) g2).drawImage(ImageLoader.getImage("grass"), at, this);
				}
			}
		
		if(debug && debug2)
		{
			for(int k = 0; k < EntityKeeper.height; k++)//rows
				for(int j = 0; j < EntityKeeper.width; j++)//columns
				{
					g2.drawLine(j*worldScale, 0, j*worldScale, 721);
					g2.drawLine(0, (k*worldScale+21), 1000, (k*worldScale)+21);
				}
		}
	}
	
	public void displayEntities(Graphics g2)
	{
		for(int k = 0; k < EntityKeeper.everything.size(); k++)
		{
			for(int j = 0; j < EntityKeeper.everything.get(k).size(); j++)
			{
				tempEntity = (Entity) EntityKeeper.everything.get(k).get(j);
				drawEntity(g2,tempEntity);
			}
		}
	}
	
	public void displayHUD(Graphics g2)
	{
		g2.setColor(new Color(0,0,0,100));
		g2.fillRect(0, 21, 46, 14);
		g2.setColor(Color.yellow);
		g2.setFont(new Font("Ariel",Font.BOLD,12));
		g2.drawString("FPS: " + String.valueOf(displayFPS), 1, 33);
			
		if(debug)
		{
			g2.setColor(new Color(0,0,0,100));
			g2.fillRect(46, 21, 37, 14);
			g2.fillRect(0, 35, 83, 38);
			g2.setColor(Color.yellow);
			g2.setFont(new Font("Ariel",Font.BOLD,12));
			g2.drawString("TickTime: "+delay, 1, 45);
			g2.drawString("Players: "+EntityKeeper.everything.get(1).size(), 1, 57);
			g2.drawString("Objects: "+EntityKeeper.everything.get(0).size(), 1, 69);
		}
	}
	
	public void drawEntity(Graphics g2, Entity tempEntity)
	{
		//for each player with correct orientation and location
		AffineTransform at = new AffineTransform();
		//translate image to use center as point of rotation
		at.translate(tempEntity.getXPos()+(tempEntity.getIconWidth()/2)*worldScale/tempEntity.getIconWidth(),
				tempEntity.getYPos()+(tempEntity.getIconHeight()/2)*worldScale/tempEntity.getIconHeight());

		at.rotate(Math.toRadians(tempEntity.getRotation()));
		at.translate(-(tempEntity.getIconWidth()/2)*worldScale/tempEntity.getIconWidth(), -(tempEntity.getIconHeight()/2)*worldScale/tempEntity.getIconHeight());//reposition image
		// draw the image
		at.scale((float)worldScale/tempEntity.getIconWidth(),(float)worldScale/tempEntity.getIconHeight());
		((Graphics2D) g2).drawImage(tempEntity.getIcon(), at, this);
		at.rotate(Math.toRadians(-tempEntity.getRotation()));
		
		if (debug)
		{
			((Graphics2D) g2).draw(tempEntity.getHitBox());
		}
	}
	

	/** --LISTENERS-- **/
	
	public void mouseDragged(MouseEvent e) {}
	
	public void keyPressed(KeyEvent e) 
	{
		switch (e.getKeyCode()) 
		{ 
			case KeyEvent.VK_W:
			{		
				up = true;
				break;
			}
			case KeyEvent.VK_S:
			{
				down = true;
				break;
			}
			case KeyEvent.VK_A:
			{		
				left = true;
				break;
			}
			case KeyEvent.VK_D:
			{
				right = true;
				break;
			}
			case KeyEvent.VK_F2:
			{
				if(debugFlip)
				{
					debug = false;
					debugFlip = false;
				}	
				else
				{
					debug = true;
					debugFlip = true;
				}
				break;
			}
			case KeyEvent.VK_F3:
			{
				if(debug2Flip)
				{
					debug2 = false;
					debug2Flip = false;
				}	
				else
				{
					debug2 = true;
					debug2Flip = true;
				}
				break;
			}
			case KeyEvent.VK_SPACE:
			{
				EntityKeeper.generateWorld();
				break;
			}
		}
	}

	public void keyReleased(KeyEvent e)
	{
		switch (e.getKeyCode())
		{
			case KeyEvent.VK_W:
			{		
				up = false;
				break;
			}
			case KeyEvent.VK_S:
			{
				down = false;
				break;
			}
			case KeyEvent.VK_A:
			{		
				left = false;
				break;
			}
			case KeyEvent.VK_D:
			{
				right = false;
				break;
			}
		}
	}
	
	/** --UTIL / EXTRA-- **/
	
	public void paint(Graphics g){
		update(g);
	}
	
	public void calcTime()
	{
		now = System.nanoTime();
	    updateLength = now - lastLoopTime;
	    lastLoopTime = now;
	    delta = updateLength / ((double)OPTIMAL_TIME);

	    lastFpsTime += updateLength;
	    fps++;
	      
	    if (lastFpsTime >= 1000000000)
	    {
	       displayFPS = fps;
	       lastFpsTime = 0;
	       fps = 0;
	    }
	    lag += delta;
	}	
	
	public static void delay(double nt)//better time delay method
	{
		try {
			if (nt < 0)
				nt = .000001;
			Thread.sleep((long)nt);
		} catch (InterruptedException e) {}
	}
	
	public void mousePressed(MouseEvent e) {
		mouseDragged(e);
	}
	
	/** --UNUSED LISTENERS-- **/
	
	public void mouseWheelMoved(MouseWheelEvent e) 
	{
		int notches = e.getWheelRotation();
	       if (notches < 0) {
	           worldScale++;
	       } else {
	           worldScale--;
	       }
	       //TODO make this method more versitile also fix limitations for map sizes
	       if(worldScale < 11)
	       {
	    	   worldScale = 11;
	       }
	       else if(worldScale > 100)
		   {
	    	   worldScale = 100;
		   }
	    	   
	}

	public void keyTyped(KeyEvent e) {}

	public void mouseMoved(MouseEvent e) {}

	public void mouseClicked(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}
	
}