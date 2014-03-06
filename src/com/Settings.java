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

import com.Engine;

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

	/**
	 * initialized Settings window.
	 * 
	 * @throws LWJGLException
	 */
	void initSettings() throws LWJGLException {
		settingsFrame = new JFrame();
		settingsFrame.setAutoRequestFocus(true);
		settingsFrame.setBounds(100, 100, 400, 300);
		settingsFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		settingsFrame.getContentPane().setLayout(new GridLayout(4, 4, 0, 0));

		JLabel lblFPS = new JLabel("Current FPS");
		settingsFrame.getContentPane().add(lblFPS);

		JLabel lblNumFPS = new JLabel(String.valueOf(Engine.setFPS));
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
	// Display.setDisplayMode();
	// Display.setFullscreen(checkFullScreen.isSelected());
	// Display.update();// Checks to see if check box is selected. If selected,
	// makes full screen.
	/**
	 * 
	 * @param aCheckBox
	 *            a JCheckBox object checks state of JCheckBox to determine if
	 *            full screen is enabled.
	 * @throws LWJGLException
	 */
	public void toggleFullscreen(JCheckBox aCheckBox) throws LWJGLException {

	}

	/**
	 * 
	 * @param on
	 *            boolean value to determine if sound on colid is played
	 *            possibly gotten from check box or a list box with multip
	 *            options.
	 */
	public void soundOnCollide(boolean on) {

	}

	/**
	 * Used to set new FPS.
	 * 
	 * @param input
	 *            input passed in from a text field used to set new FPS
	 * 
	 */
	public void newFPS(int input) {
		Engine.setFPS = input;
	}
}
