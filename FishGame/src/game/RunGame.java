package game;

import java.awt.event.ActionListener;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;

public class RunGame {
	public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		GameModel gm = new GameModel(100,10);
		DrawDemo myDemo = new DrawDemo(gm);
		myDemo.frame.setVisible(true);
		myDemo.params.setVisible(true);
		GameLoop gl = new GameLoop(gm,myDemo.gameboard);
		Thread t = new Thread(gl);
		System.out.println("gameloop");
		t.start();
		myDemo.gameboard.requestFocus();
	}
}
