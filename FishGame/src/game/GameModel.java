package game;
import java.util.*;
import java.io.*;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * This is a simple model for a game with objects that
 * move around the screen and the user needs to 
 * catch them by clicking on them. This class
 * represents the state of the game
 * @author tim
 *
 */
public class GameModel {
	public double width;
	public double height;
	public double size;
	public List<GameActor> actors = new ArrayList<GameActor>();
	protected Random rand = new Random();
	public GameActor avatar;
	private int numActors;
	public boolean gameOver = false;
	public boolean paused = true;
	public int numActive;
	private long startTime=System.nanoTime();
	private long lastUpdate=startTime;
	// mRate is the minimum time between fish, in tenths of a second
	public int mRate = 30;
	// sRate is the variable rate added to the minimum rate, creating a range of random numbers from 3 to 7 seconds.
	public int sRate = 40;
	public int score;
	public String typescript = "lb 100 lb 100 rg 100 rb 100 lb 100"; 
	public boolean scripted=true;
	public Scanner scan = new Scanner(typescript);


	public String log = "";
	public int herz=4;
	PrintWriter writer = new PrintWriter("log.txt", "UTF-8");
	public int mode = 1;
	// 0 for random, 1 for scripted
	
	
	public GameModel(double size, int numActors) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		this.width =size;
		this.height = size;
		this.size=size;
		this.numActors = numActors;

		this.avatar = new GameActor(0,0);
		avatar.species = Species.avatar;
		this.avatar.radius=6;
		this.gameOver = false;
	}

	// this method will spawn an actor, accepting two int parameters. The first parameter determines the species (1 for good, anything else for bad) and
	// the second parameter determines the side it will spawn on (0 for left, 1 for right)
	public void keySpawnActor(int side, int m)throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		
		double vx=0,vy=0,speed = 10;
		double x=0,y = height/2;
		if (side == 0)
		{
			x = 1;
			vx=speed;
		} 
		else if (side == 1)
		{
			x=width-1;
			vx= -speed;
		}
		else
		{
			return;
		}
		GameActor a = new GameActor(x,y);
		this.actors.add(a);
		a.speed = speed;
		a.radius = 4;
		a.vx=vx;
		a.vy=vy;
	
	//	if (s
			a.species = Species.good;
	//	else{
			a.species = Species.bad;
	} 

	
	/*
	 * spawns an actor from a script
	 */

	public void scriptSpawnActor(Scanner s) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		if(s.hasNext()){
		String t=s.next();
		mRate=s.nextInt();

//			startTime=now;
		double x=0,y = height/2;
		Species spec = Species.good;
		int vx=0;
		int or=0;
		if (t.charAt(0)=='r')
		{
			x = width-1;
			vx= -10;
			or=1;
		} else if (t.charAt(0)=='l'){
			x =1;
			vx=10;
			or=0;
		}
		
		if (t.charAt(1) == 'g')
		{
			spec=Species.good;
		}
		else if (t.charAt(1)=='b'){
			spec=Species.bad;
		}
		
		GameActor a = new GameActor(x,y,true,spec);

		if (actors.size()>0){
			this.actors.get(this.actors.size()-1).active=false;
			this.actors.get(this.actors.size()-1).ct.stop();	
		}
			this.actors.add(a);
			a.ct.play();
			a.speed = 10;
		a.radius = 4;
		a.vx=vx;
		a.vy=0;
		a.species=spec;
		a.origin=or;
		a.colorHerz=this.herz;
		log+="Actor event.BirthTime:"+a.birthTime+" Type:"+a.species+" Side:"+a.origin+"\n";
		writer.println("Actor event.BirthTime:"+a.birthTime+" Type:"+a.species+" Side:"+a.origin+"\n");
		
		}
	}
	
	// spawn an actor randomly
	public void randomSpawnActor(){
		
	}
	
	public void start(){
		paused = false;
	}
	
	public void stop(){
		paused = true;
	}
	
	/**
	 * if an actor moves off the board, in the x (or y) direction, 
	 * it is bounced back into the board and its velocity in the
	 * offending direction is reversed
	 * @param a
	 */
	public void keepOnBoard(GameActor a){
		if (a.x<0) {
			a.x = -a.x;a.vx = -a.vx;
		}else if (a.x> width){
			a.x = width - (a.x-width);
			a.vx = -a.vx;
		}
		if (a.y<0) {
			a.y = -a.y;a.vy = -a.vy;
		}else if (a.y > height){
			a.y = height - (a.y-height);
			a.vy=-a.vy;
		}
	}
	
	/**
	 * update moves all actors one step and if
	 * any fireflies that intersect with the avatar
	 * are remove, while if a wasp intersects the avatar,
	 * the game is restarted
	 * update will check if the difference between the lastUpdate and the current time is greater than the sRate plus a random number from 1 to 4, and spawn a fish if so.
	 */
	public void update() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		if (paused || gameOver) return;
		long now=System.nanoTime();
		long interval =  (long) ((mRate)*100000000l);
		if (now>interval+startTime){
		
			startTime=now;
			switch(mode){
			case 0:
				randomSpawnActor();
			case 1:
				if (scan.hasNext())
					scriptSpawnActor(scan);
			
			}
		}
		for(GameActor a:this.actors){
			a.update();
			if (a.active && intersects(a,avatar)) {
				a.active=false;
				numActive--;
			}
			keepOnBoard(a);
		}
	}
	
	public boolean intersects(GameActor a, GameActor b){
		double dx=a.x-b.x;
		double dy = a.y-b.y;
		double d = Math.sqrt(dx*dx+dy*dy);
		return (d < a.radius + b.radius);
	}
	public void fileWrite(){
		try {
	          File file = new File("example.txt");
	          BufferedWriter output = new BufferedWriter(new FileWriter(file));
	          output.write(log);
	          output.close();
	        } catch ( IOException e ) {
	           e.printStackTrace();
	        }
	}
}

