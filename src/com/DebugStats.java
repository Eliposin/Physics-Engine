package com;
import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridLayout;
import javax.swing.JLabel;


public class DebugStats {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public void start() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DebugStats window = new DebugStats();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		while (true) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Create the application.
	 */
	public DebugStats() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setAutoRequestFocus(false);
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(4, 4, 0, 0));
		
		JLabel lblFPS = new JLabel("FPS");
		frame.getContentPane().add(lblFPS);
		
		JLabel lblNumFPS = new JLabel("25 fps");
		frame.getContentPane().add(lblNumFPS);
		
		JLabel lblNewLabel_2 = new JLabel("New label");
		frame.getContentPane().add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("New label");
		frame.getContentPane().add(lblNewLabel_3);
		
		JLabel lblFrameTime = new JLabel("Frame Time");
		frame.getContentPane().add(lblFrameTime);
		
		JLabel lblNumFrameTime = new JLabel("12 ms");
		frame.getContentPane().add(lblNumFrameTime);
		
		JLabel lblNewLabel_5 = new JLabel("New label");
		frame.getContentPane().add(lblNewLabel_5);
		
		JLabel lblNewLabel_8 = new JLabel("New label");
		frame.getContentPane().add(lblNewLabel_8);
		
		JLabel lblNewLabel_7 = new JLabel("12 ms");
		frame.getContentPane().add(lblNewLabel_7);
		
		JLabel lblNewLabel_9 = new JLabel("New label");
		frame.getContentPane().add(lblNewLabel_9);
		
		JLabel lblNewLabel_10 = new JLabel("New label");
		frame.getContentPane().add(lblNewLabel_10);
		
		JLabel lblNewLabel_12 = new JLabel("New label");
		frame.getContentPane().add(lblNewLabel_12);
		
		JLabel lblNewLabel_11 = new JLabel("New label");
		frame.getContentPane().add(lblNewLabel_11);
		
		JLabel lblNewLabel_13 = new JLabel("New label");
		frame.getContentPane().add(lblNewLabel_13);
		
		JLabel lblNewLabel_14 = new JLabel("New label");
		frame.getContentPane().add(lblNewLabel_14);
		
		JLabel lblNewLabel_15 = new JLabel("New label");
		frame.getContentPane().add(lblNewLabel_15);
	}

}
