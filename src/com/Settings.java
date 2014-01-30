package com;

import javax.swing.JFrame;
import javax.swing.JCheckBox;

import java.awt.GridLayout;

import javax.swing.JLabel;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

import com.GameEngine;

public class Settings {
	private JFrame settingsFrame;
	
	static void editSettings(){
		try {
			Settings window = new Settings();
			window.settingsFrame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	public Settings() throws LWJGLException{
		initialize();
	}

	void initialize() throws LWJGLException {
		settingsFrame = new JFrame();
		settingsFrame.setAutoRequestFocus(true);
		settingsFrame.setBounds(100, 100, 450, 300);
		settingsFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		settingsFrame.getContentPane().setLayout(new GridLayout(4, 4, 0, 0));
		
		JLabel lblFPS = new JLabel("Current FPS");
		settingsFrame.getContentPane().add(lblFPS);
		
		JLabel lblNumFPS = new JLabel("25 fps");
		settingsFrame.getContentPane().add(lblNumFPS);
		
		JLabel lblChkFullScreen = new JLabel("Toggle Full Screen");
		settingsFrame.getContentPane().add(lblChkFullScreen);
		JCheckBox chkBox = new JCheckBox();
		settingsFrame.getContentPane().add(chkBox);
		//Checks to see if check box is selected.  If selected, makes full screen.
		if(chkBox.isSelected()){
		
				;
				Display.update();
				if(Display.isFullscreen()){
					System.out.println("It worked");
				}
				else{
					System.out.println("something went wrong");
				}
			} 
			
		}
		
		
	
}
