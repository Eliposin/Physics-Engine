package com;

import javax.swing.JFrame;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JTextPane;

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
		
		JLabel lblNumFPS = new JLabel(String.valueOf(GameEngine.setFPS));
		settingsFrame.getContentPane().add(lblNumFPS);
		
		JTextPane newFPS = new JTextPane();
		settingsFrame.getContentPane().add(newFPS);
		
		JLabel lblChkFullScreen = new JLabel("Toggle Full Screen");
		settingsFrame.getContentPane().add(lblChkFullScreen);
		JCheckBox checkFullScreen = new JCheckBox();
		settingsFrame.getContentPane().add(checkFullScreen);
		
		//Checks to see if check box is selected.  If selected, makes full screen.
		Display.setDisplayMode();
		Display.setFullscreen(checkFullScreen.isSelected());
		Display.update();
		}
		
		
	
}
