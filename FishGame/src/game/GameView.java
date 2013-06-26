package game;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.*;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;
import java.awt.*;
import java.io.*;
import java.io.IOException;

import javax.swing.*;
import javax.swing.event.*;
import java.util.ArrayList;


/**
 * a GameView is a 2D view of a GameModel
 * the aspect of the view and model may be different
 * Since the GameModel is a square space, the GameView takes
 * the minimum of the width and height of the JPanel and uses
 * that to scale the GameModel to the Viewing window.
 * Calling repaint() on the GameView will cause it to render
 * the current state of the Model to the JPanel canvas...
 * @author tim
 *
 */
public class GameView extends JPanel{
	private static final long serialVersionUID = 1L;
	public AudioClip goodclip,badclip;
	private GameModel gm = null;
	ArrayList<String> keylog = new ArrayList<String>();
	public boolean gameActive = false;
	public GameView(final GameModel gm) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		super();
		this.gm = gm;
		goodclip = new AudioClip("src/good.wav");
		badclip = new AudioClip("src/bad.wav");
	//	MouseInputListener ml =
	//			new CanvasMouseInputListener();
	//	this.addMouseListener(ml);
	//	this.addMouseMotionListener(ml);
		System.out.println("hello");
		this.requestFocus();
		KeyListener kl = new KeyListener()
		{
			@Override
			public void keyPressed(KeyEvent e)
				{
				
			//	feedBackText.append("Key Pressed: " + e.getKeyChar() + "\n");
			}
			
			@Override
			public void keyReleased(KeyEvent e)
			{
				//feedBackText.append("Key Released: " + e.getKeyChar() + "\n");
			}
			
			@Override
			public void keyTyped(KeyEvent e)
			{
				System.out.println("You pressed "+e.getKeyChar());
				//Rightside goodfish response key is p
				if (e.getKeyChar() == 'p') 
				{
					gm.log+="Key:"+e.getKeyChar()+" Time: " + System.nanoTime()+"Right Good\n";
					System.out.println(gm.log);
					if (gm.actors.size()>0){
					
						if (gm.actors.get(gm.actors.size()-1).species.toString().equals("good") && gm.actors.get(gm.actors.size()-1).origin==1){
							gm.score+=2;
							gm.actors.get(gm.actors.size()-1).ct.stop();
							gm.actors.get(gm.actors.size()-1).active=false;
							goodclip.play();
						} 
						else {
							badclip.play();
						}
					
					}
				} 
				else if (e.getKeyChar() == 'q'){
					gm.log+="Key:"+e.getKeyChar()+" Time: " + System.nanoTime()+"Left Good\n";
					System.out.println(gm.log);
					if (gm.actors.size()>0){
						if (gm.actors.get(gm.actors.size()-1).species.toString().equals("good") && gm.actors.get(gm.actors.size()-1).origin!=1){
							gm.score+=2;
							gm.actors.get(gm.actors.size()-1).ct.stop();
				
							gm.actors.get(gm.actors.size()-1).active=false;
							goodclip.play();
						} 
						else{
							gm.actors.get(gm.actors.size()-1).ct.stop();
							
							gm.actors.get(gm.actors.size()-1).active=false;
							badclip.play();
						}
					}
				
				} 
				else if (e.getKeyChar() == 'a') {
					//left bad fish
				
					gm.log+="Key:"+e.getKeyChar()+" Time: " + System.nanoTime()+"Left Bad\n";
					System.out.println(gm.log);
					if (gm.actors.size()>0){
						if (gm.actors.get(gm.actors.size()-1).species.toString().equals("bad") && gm.actors.get(gm.actors.size()-1).origin!=1){
							gm.score+=2;
							gm.actors.get(gm.actors.size()-1).ct.stop();
							gm.actors.get(gm.actors.size()-1).active=false;
							goodclip.play();
							
						} 
						else{
							gm.actors.get(gm.actors.size()-1).ct.stop();
							
							gm.actors.get(gm.actors.size()-1).active=false;
							badclip.play();
						}
					}
				}
				else if (e.getKeyChar() == 'l') {
					// right bad fish				
					gm.log+="Key:"+e.getKeyChar()+" Time: " + System.nanoTime()+"Right Bad\n";
					System.out.println(gm.log);
					if (gm.actors.size()>0){
						if (gm.actors.get(gm.actors.size()-1).species.toString().equals("bad") && gm.actors.get(gm.actors.size()-1).origin==1){
							gm.score+=2;
							gm.actors.get(gm.actors.size()-1).ct.stop();
							gm.actors.get(gm.actors.size()-1).active=false;
							goodclip.play();
						} 
						else {
							gm.actors.get(gm.actors.size()-1).ct.stop();
							
							gm.actors.get(gm.actors.size()-1).active=false;
							badclip.play();
						}
					}
				}
			}
		};
			this.addKeyListener(kl);
	}
	
	/**
	 * toViewCoords(x) converts from model coordinates to pixels
	 * on the screen so that objects can be drawn to scale, i.e.
	 * as the screen is resized the objects change
	 * size proportionately.  
	 * @param x the unit in model coordinates 
	 * @return the corresponding value in pixel based on window-size
	 */
	public int toViewCoords(double x){
		int width = this.getWidth();
		int height = this.getHeight();
		int viewSize = (width<height)?width:height;
		return (int) Math.round(x/gm.size*viewSize);
	}
	
	/**
	 * toModelCoords(x) is used to convert mouse locations
	 * to positions in the model so that the avatar position
	 * in the model can be changed correctly
	 * @param x position in pixels in view
	 * @return position in model coordinates
	 */
	public double toModelCoords(int x){
		int width = this.getWidth();
		int height = this.getHeight();
		int viewSize = (width<height)?width:height;
		return x*gm.size/viewSize;
	}

	/**
	 * paintComponent(g) draws the current state of the model
	 * onto the component. It first repaints it in blue, 
	 * then draws the avatar,
	 * then draws each of the other actors, i.e. fireflies and wasps...
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if (gm==null) return;
		int width = this.getWidth();
		int height = this.getHeight();
		g.setColor(Color.BLUE);
		g.fillRect(0,0,width,height);
	//	drawActor(g,gm.avatar,Color.GREEN);
		java.util.List<GameActor> gaList = new ArrayList<GameActor>(gm.actors);
		for(GameActor a:gaList){
			drawActor(g,a,Color.RED);
		}
		g.setFont(new Font("Helvetica",Font.BOLD,20));
		
		g.drawString("Score:"+gm.score, width/10, height/10);
		

	}
	
	/**
	 * drawActor(g,a,c) - draws a single actor a 
	 * using the Graphics object g. The color c is the
	 * default color used for new species, but is ignored
	 * for avatars, wasps, and fireflies
	 * 
	 * @param g - the Graphics object used for drawing
	 * @param a - the Actor to be drawn
	 * @param c - the default color for actors of unknown species
	 */
	private void drawActor(Graphics g, GameActor a,Color c){
		if (!a.active) return;
		int theRadius = toViewCoords(a.radius);
		int x = toViewCoords(a.x);
		int y = toViewCoords(a.y);
		
		switch (a.species){
		case good:
			c = interpolate(a.color3, a.color4,a.birthTime,System.nanoTime(),a.colorHerz);break;
		case bad:
			c = interpolate(a.color1, a.color2,a.birthTime,System.nanoTime(),a.colorHerz);break;
		
		case avatar: c=Color.BLACK; break;
		}
		g.setColor(c);
		if (a.species==Species.avatar){
			g.drawOval(x-theRadius, y-theRadius, 2*theRadius, 2*theRadius);
		} else
			g.fillOval(x-theRadius, y-theRadius, 2*theRadius, 2*theRadius);

	}
	
	private Color interpolate(Color c1, Color c2, long birth, long now, double freq){
		double t = ((now-birth)/1000000000.0)*freq;
		double y = 0.5*(Math.sin(Math.PI*2*t)+1);
		float red = (float) (c1.getRed()/255.0*y + c2.getRed()/255.0*(1-y));
		float green = (float) (c1.getGreen()/255.0*y + c2.getGreen()/255.0*(1-y));
		float blue = (float) (c1.getBlue()/255.0*y + c2.getBlue()/255.0*(1-y));
		return new Color(red,green,blue);
	}
	
	/**
	 * this listens for mouse clicks (which unpauses the game)
	 * and mouse movements which move the avatar
	 * @author tim
	 *
	 */
	private class CanvasMouseInputListener extends MouseInputAdapter{
		public void mouseClicked(MouseEvent e){
			gm.paused= !(gm.paused); //false;
		}
		public void mouseMoved(MouseEvent e){
			Point p = e.getPoint();
			double x = toModelCoords(p.x); 
			double y = toModelCoords(p.y);
			gm.avatar.x =x;
			gm.avatar.y =y;
			//System.out.println("x="+p.x+" y="+p.y);
			e.getComponent().repaint();		
		}
	}
	
}
