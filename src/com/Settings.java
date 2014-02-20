package com;

import javax.swing.JFrame;
import javax.swing.JCheckBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import java.awt.GridLayout;

import javax.swing.JLabel;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.GameEngine;

public class Settings {
	private JFrame settingsFrame;

	static void editSettings() {
		try {
			Settings window = new Settings();
			window.settingsFrame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public Settings() throws LWJGLException {
		initSettings();
	}

	void initSettings() throws LWJGLException {
		settingsFrame = new JFrame();
		settingsFrame.setAutoRequestFocus(true);
		settingsFrame.setBounds(100, 100, 400, 300);
		settingsFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		settingsFrame.getContentPane().setLayout(new GridLayout(4, 4, 0, 0));

		JLabel lblFPS = new JLabel("Current FPS");
		settingsFrame.getContentPane().add(lblFPS);

		JLabel lblNumFPS = new JLabel(String.valueOf(GameEngine.setFPS));
		settingsFrame.getContentPane().add(lblNumFPS);

		JTextArea newFPS = new JTextArea();
		settingsFrame.getContentPane().add(newFPS);

		JLabel lblChkFullScreen = new JLabel("Toggle Full Screen");
		settingsFrame.getContentPane().add(lblChkFullScreen);
		JCheckBox checkFullScreen = new JCheckBox();
		settingsFrame.getContentPane().add(checkFullScreen);


		JLabel lblSoundOnColide = new JLabel("Toggle Full Screen");
		settingsFrame.getContentPane().add(lblChkFullScreen);
		JCheckBox checkSoundOnColide = new JCheckBox();
		settingsFrame.getContentPane().add(checkFullScreen);

	}

	// Checks to see if check box is selected. If selected, makes full screen.
	public void toggleFullscreen(JCheckBox aCheckBox) throws LWJGLException {
		Display.getDisplayMode();
		if (aCheckBox.isSelected() && !Display.isFullscreen()) {
			Display.setFullscreen(true);
		} else if (!aCheckBox.isSelected() && Display.isFullscreen())
			Display.setFullscreen(false);
	}

	public void soundOnColid() {

	}

		
		//Checks to see if check box is selected.  If selected, makes full screen.
//		Display.setDisplayMode();
//		Display.setFullscreen(checkFullScreen.isSelected());
//		Display.update();
		
		

	
	public void newFPS(int input){
		GameEngine.setSetFPS(input);
	}
}
