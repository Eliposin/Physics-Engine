package gui;

import inout.Input;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.Engine;
import com.Vector;

public class Button implements ActionListener {

	static ArrayList<Button> buttons = new ArrayList<Button>();

//	float[] color = {Engine.red, Engine.green, Engine.blue};
	float[] color = {0.75f, 0.75f, 0.75f};
	float[] tempColor = color;
	float colorScale = 1f;
	
	String text;
	
	boolean hover = false;
	boolean pressed = false;

	public static int buttonHeight = 32;
	public static int buttonWidth = 64;
	public static int buttonPadding = 1;

	int[] location = new int[2];

	public Button(int locX, int locY) {

		location[0] = locX;
		location[1] = locY;

	}
	
	public Button(int[] location) {

		this.location = location;

	}

	public void render() {

		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glTranslatef(location[0], location[1], 0);
		
		tempColor = Vector.cScaleVector(color.clone(), colorScale);
		GL11.glColor3f(tempColor[0], tempColor[1], tempColor[2]);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2f(buttonPadding, buttonPadding);
			GL11.glVertex2f(buttonWidth - buttonPadding, buttonPadding);
			GL11.glVertex2f(buttonWidth - buttonPadding, buttonHeight - buttonPadding);
			GL11.glVertex2f(buttonPadding, buttonHeight - buttonPadding);
		GL11.glEnd();
		
		tempColor = Vector.cScaleVector(color.clone(), 0.9f * colorScale);
		GL11.glColor3f(tempColor[0], tempColor[1], tempColor[2]);
		GL11.glBegin(GL11.GL_TRIANGLES);
			GL11.glVertex2f(buttonPadding, buttonPadding);
			GL11.glVertex2f(buttonWidth - buttonPadding, buttonPadding);
			GL11.glVertex2f(buttonWidth - buttonPadding, buttonHeight - buttonPadding);
		GL11.glEnd();
		
		tempColor = Vector.cScaleVector(color.clone(), 0.5f * colorScale);
		GL11.glColor3f(tempColor[0], tempColor[1], tempColor[2]);
		GL11.glBegin(GL11.GL_LINE_LOOP);
			GL11.glVertex2f(buttonPadding, buttonPadding);
			GL11.glVertex2f(buttonWidth - buttonPadding, buttonPadding);
			GL11.glVertex2f(buttonWidth - buttonPadding, buttonHeight - buttonPadding);
			GL11.glVertex2f(buttonPadding, buttonHeight - buttonPadding);
		GL11.glEnd();
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glPopMatrix();

	}

	public void update() {

		if (location[0] < Input.mouseX && Input.mouseX < location[0] + buttonWidth
				&& location[1] < Input.mouseY
				&& Input.mouseY < location[1] + buttonHeight) {
			
			if (hover == false) {
				hover = true;
				onHover();
			}
			
			if (Input.mouse0Down == true) {
				
				if (pressed == false) {
					pressed = true;
					onClick();
				}
				
			} else {
				
				if (pressed == true) {
					pressed = false;
					onRelease();
				}
				
			}

		} else {
			
			if (Input.mouse0Down == false) {
				pressed = false;
				colorScale = 1;
				
				if (hover == true) {
				hover = false;
				onNothing();
			}
				
			}
			
		}

		render();

	}

	private void onHover() {
		buttonPadding = 0;
		System.out.println("hover");
	}

	private void onClick() {
		buttonPadding = 1;
		colorScale = 0.8f;
		System.out.println("clicked");
	}
	
	private void onRelease() {
		buttonPadding = 0;
		colorScale = 1f;
		System.out.println("released");
	}

	private void onNothing() {
		buttonPadding = 1;
//		colorScale = 1f;
		System.out.println("nothing");
	}
	
	public void setColor(float[] color) {
		this.color = color;
	}
	
	public float[] getColor() {
		return color;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
	
	public void setSize(int width, int height) {
		buttonWidth = width;
		buttonHeight = height;
	}
	
	public void setSize(int width, int height, int padding) {
		buttonWidth = width;
		buttonHeight = height;
		buttonPadding = padding;
	}
	
	public void setSize(int[] size) {
		buttonWidth = size[0];
		buttonHeight = size[1];
		buttonPadding = size[2];
	}
	
	public void setSize() {
		
	}
	
	public int[] getSize() {
		return new int[]{buttonWidth, buttonHeight, buttonPadding};
	}

	public void setLocation(int width, int height) {
		location[0] = width;
		location[1] = height;
	}
	
	public void setLocation(int[] location) {
		this.location = location;
	}
	
	public int[] getLocation() {
		return location;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
