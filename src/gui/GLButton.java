package gui;

import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import com.KeyInput;

public class GLButton implements Runnable {
	
	static ArrayList<GLButton> buttons = new ArrayList<GLButton>();
	
	Thread t = new Thread(this, "button");
	
	public static int buttonHeight = 32;
	public static int buttonWidth = 64;
	public static int buttonPadding = 2;
	
	int locX = 0, locY = 0;
	
	public GLButton(int locX, int locY) {
		
		initGL();
		t.start();
		
		this.locX = locX;
		this.locY = locY;
		
	}
	
	public void initGL() {
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, com.GameEngine.width, 0, com.GameEngine.height, 100, -100);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
	}
	
	public void renderGL() {
		GL11.glColor3f(1, 0, 0);
		
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(locX + buttonPadding, locY + buttonPadding);
		GL11.glVertex2f(locX + buttonWidth - buttonPadding, locY + buttonPadding);
		GL11.glVertex2f(locX + buttonWidth - buttonPadding, locY + buttonHeight - buttonPadding);
		GL11.glVertex2f(locX + buttonPadding, locY + buttonHeight - buttonPadding);
		GL11.glEnd();
		
		GL11.glColor3f(0.8f, 0, 0);
		
		GL11.glBegin(GL11.GL_TRIANGLES);
		GL11.glVertex2f(locX + buttonPadding, locY + buttonPadding);
		GL11.glVertex2f(locX + buttonWidth - buttonPadding, locY + buttonPadding);
		GL11.glVertex2f(locX + buttonWidth - buttonPadding, locY + buttonHeight - buttonPadding);
		GL11.glEnd();
		
	}
	
	public void run() {
		
		while (true) {
			
			if (locX < KeyInput.mouseX && KeyInput.mouseX < locX + buttonWidth && locY < KeyInput.mouseY && KeyInput.mouseY < locY + buttonHeight) {
			
				onHover();
			
				if (KeyInput.mouse0Down == true) {
				onClick();
				}
			
			} else {
				onNothing();
			}
			
			renderGL();
			
		}
		
	}
	
	private void onHover() {
		buttonPadding = 0;
	}
	
	private void onClick() {
		buttonPadding = 4;
	}
	
	private void onNothing() {
		buttonPadding = 2;
	}

}
