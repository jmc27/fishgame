package game;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class ParamsUI extends JFrame {

			/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
			JLabel header,fish,good,bad,sound,color;
		JPanel matrix;
		JButton button1;
		JPanel col1,col2,col3;
		JTextField gs,bs,gc,bc;
	
	public ParamsUI(){
		super("Parameters");
		this.setSize(500,500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new FlowLayout());
		
		header = new JLabel("Settings");
		fish=new JLabel("Fish Type:");
		good=new JLabel("Good");
		bad=new JLabel("Bad");
		sound=new JLabel("Sound");
		color=new JLabel("Color");
		
		gs=new JTextField();
		bs=new JTextField();
		gc=new JTextField();
		bc=new JTextField();
		
		col1 = new JPanel();
		col1.setLayout(new GridLayout(3,1));
		col2 = new JPanel();
		col2.setLayout(new GridLayout(3,1));
		col3 = new JPanel();
		col3.setLayout(new GridLayout(3,1));
		
		matrix = new JPanel();
		matrix.setLayout(new GridLayout(1,3));
		
		button1 = new JButton();
		button1.setText("Push Me");
		button1.addActionListener(
				new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.out.println("Yay I love being pushed");
			}
		});
		
		
		col1.add(fish);
		col1.add(good);
		col1.add(bad);
		
		col2.add(sound);
		col2.add(gs);
		col2.add(bs);
		
		col3.add(color);
		col3.add(gc);
		col3.add(bc);
		
		matrix.add(col1);
		matrix.add(col2);
		matrix.add(col3);
		
		
		this.add(matrix);
		
	}
}
