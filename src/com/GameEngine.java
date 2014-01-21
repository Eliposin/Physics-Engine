package com;

/*
 * 	Physics Engine
 * 	Christopher Dombroski
 * 	
 * 	https://github.com/Nyrmburk/Physics-Engine
 */

import java.io.IOException;
import java.util.Random;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.Sys;
import com.Logger;
import effects.Trail;
import com.KeyInput;
import com.Loader;
import gui.Button;
import physics.ComplexPhys;
import physics.Physics;

public class GameEngine {
	
	int delta;	// change in milliseconds since the last frame
	public static float timeScale = 1000;	// 1 second real-time is 1000 / (timeScale) seconds in engine
	
	public static int width = 1200;	// window width
	public static int height = 800;	// window height
	
	public static float scale = 100f;	// number of pixels in 1 meter

	int fps;	// Actual frames per second
	int setFPS = 120;	// Desired frames per second
	long lastFPS = 0;	// last frame's fps
	long lastFrame = getTime();	// last frame's time of creation
	int frame = 0;	// current frame number
	
	boolean debugToggle = true; 	// use the debug menu

	public static float red = 0.5f;
	public static float green = 0.5f;
	public static float blue = 1.0f;
	
	float[] location = {400, 600, 0};	// location of the first object. need to change soon.
	float[] location2 = new float[3];	// location of the second object. ^^
	
	int[] graphData = new int[width];	// A list of frametime data.
	
	float[] attr = {1000, 0f, 1f};	// The attributes of the objects for physics. {Mass, Drag, Restitution} 
	
	Logger boxLogger;
	Logger squareLogger;
	
	int trailLength = 250;	// max positions to use for creating a trail.
	Trail trail = new Trail(trailLength);	// create a trail
//	Trail trail2 = new Trail(trailLength);
	
	KeyInput keyInput = new KeyInput();
	Button button = new Button(400, 600);

	Random random = new Random();

	public void start() throws IOException {

		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		
		initialise();

		lastFPS = getTime(); // set lastFPS to current Time

		while (!Display.isCloseRequested()) {

			delta = getDelta();

			update(delta);
			renderGL();

			Display.sync(setFPS);
			Display.update();
		}
		
		close();
	}
	
	private void initialise() throws IOException {
		initGL();
//		GLButton button = new GLButton(KeyInput.mouseX, KeyInput.mouseY);
//		button.initGL();
		
		Loader.read("Object_Test");
		
		ComplexPhys.addPhysics("Box", attr[0], attr[1], attr[2]);
		ComplexPhys.addPhysics("Square", attr[0], attr[1], attr[2]);
		
		boxLogger = new Logger("Box");
		squareLogger = new Logger("Square");
		
	}
	
	public void close() {
		
		try {
			boxLogger.close();
			squareLogger.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("closed");
		
	}

	public void initGL() {

		// init opengl
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, width, 0, height, 100, -100);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		

	}

	public void renderGL() {

		// Clear the screen and depth buffer
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		GL11.glColor3f(red, green, blue);
		
		//draw a grid every 10 pixels
		GL11.glBegin(GL11.GL_LINES);
		for (int i = 0; i < height / scale; i++) {
			GL11.glVertex2f(0, i * scale);
			GL11.glVertex2f(width, i * scale);
		}
		GL11.glEnd();
		
		GL11.glBegin(GL11.GL_LINES);
		for (int i = 0; i < width / scale; i++) {
			GL11.glVertex2f(i * scale, 0);
			GL11.glVertex2f(i * scale, height);
		}
		GL11.glEnd();
		
		// Circle 1
		//TODO Create a vertex buffer and simplify all this into one draw call.
		GL11.glBegin(GL11.GL_POLYGON);
		GL11.glVertex2f(location[0] + 24.620193f, location[1] + 4.3412046f);
		GL11.glVertex2f(location[0] + 23.492315f, location[1] + 8.550504f);
		GL11.glVertex2f(location[0] + 21.650635f, location[1] + 12.5f);
		GL11.glVertex2f(location[0] + 19.151112f, location[1] + 16.06969f);
		GL11.glVertex2f(location[0] + 16.06969f, location[1] + 19.151112f);
		GL11.glVertex2f(location[0] + 12.5f, location[1] + 21.650635f);
		GL11.glVertex2f(location[0] + 8.550504f, location[1] + 23.492315f);
		GL11.glVertex2f(location[0] + 4.3412046f, location[1] + 24.620193f);
		GL11.glVertex2f(location[0] + 1.5308084E-15f, location[1] + 25.0f);
		GL11.glVertex2f(location[0] + -4.3412046f, location[1] + 24.620193f);
		GL11.glVertex2f(location[0] + -8.550504f, location[1] + 23.492315f);
		GL11.glVertex2f(location[0] + -12.5f, location[1] + 21.650635f);
		GL11.glVertex2f(location[0] + -16.06969f, location[1] + 19.151112f);
		GL11.glVertex2f(location[0] + -19.151112f, location[1] + 16.06969f);
		GL11.glVertex2f(location[0] + -21.650635f, location[1] + 12.5f);
		GL11.glVertex2f(location[0] + -23.492315f, location[1] + 8.550504f);
		GL11.glVertex2f(location[0] + -24.620193f, location[1] + 4.3412046f);
		GL11.glVertex2f(location[0] + -25.0f, location[1] + 3.0616169E-15f);
		GL11.glVertex2f(location[0] + -24.620193f, location[1] + -4.3412046f);
		GL11.glVertex2f(location[0] + -23.492315f, location[1] + -8.550504f);
		GL11.glVertex2f(location[0] + -21.650635f, location[1] + -12.5f);
		GL11.glVertex2f(location[0] + -19.151112f, location[1] + -16.06969f);
		GL11.glVertex2f(location[0] + -16.06969f, location[1] + -19.151112f);
		GL11.glVertex2f(location[0] + -12.5f, location[1] + -21.650635f);
		GL11.glVertex2f(location[0] + -8.550504f, location[1] + -23.492315f);
		GL11.glVertex2f(location[0] + -4.3412046f, location[1] + -24.620193f);
		GL11.glVertex2f(location[0] + -4.5924254E-15f, location[1] + -25.0f);
		GL11.glVertex2f(location[0] + 4.3412046f, location[1] + -24.620193f);
		GL11.glVertex2f(location[0] + 8.550504f, location[1] + -23.492315f);
		GL11.glVertex2f(location[0] + 12.5f, location[1] + -21.650635f);
		GL11.glVertex2f(location[0] + 16.06969f, location[1] + -19.151112f);
		GL11.glVertex2f(location[0] + 19.151112f, location[1] + -16.06969f);
		GL11.glVertex2f(location[0] + 21.650635f, location[1] + -12.5f);
		GL11.glVertex2f(location[0] + 23.492315f, location[1] + -8.550504f);
		GL11.glVertex2f(location[0] + 24.620193f, location[1] + -4.3412046f);
		GL11.glVertex2f(location[0] + 25.0f, location[1] + -6.1232338E-15f);
		GL11.glEnd();
		
		
		// Circle 2
		//TODO Create a vertex buffer and simplify all this into one draw call.
		GL11.glBegin(GL11.GL_LINE_LOOP);
		GL11.glVertex2f(location2[0] + 24.620193f, location2[1] + 4.3412046f);
		GL11.glVertex2f(location2[0] + 23.492315f, location2[1] + 8.550504f);
		GL11.glVertex2f(location2[0] + 21.650635f, location2[1] + 12.5f);
		GL11.glVertex2f(location2[0] + 19.151112f, location2[1] + 16.06969f);
		GL11.glVertex2f(location2[0] + 16.06969f, location2[1] + 19.151112f);
		GL11.glVertex2f(location2[0] + 12.5f, location2[1] + 21.650635f);
		GL11.glVertex2f(location2[0] + 8.550504f, location2[1] + 23.492315f);
		GL11.glVertex2f(location2[0] + 4.3412046f, location2[1] + 24.620193f);
		GL11.glVertex2f(location2[0] + 1.5308084E-15f, location2[1] + 25.0f);
		GL11.glVertex2f(location2[0] - 4.3412046f, location2[1] + 24.620193f);
		GL11.glVertex2f(location2[0] - 8.550504f, location2[1] + 23.492315f);
		GL11.glVertex2f(location2[0] - 12.5f, location2[1] + 21.650635f);
		GL11.glVertex2f(location2[0] - 16.06969f, location2[1] + 19.151112f);
		GL11.glVertex2f(location2[0] - 19.151112f, location2[1] + 16.06969f);
		GL11.glVertex2f(location2[0] - 21.650635f, location2[1] + 12.5f);
		GL11.glVertex2f(location2[0] - 23.492315f, location2[1] + 8.550504f);
		GL11.glVertex2f(location2[0] - 24.620193f, location2[1] + 4.3412046f);
		GL11.glVertex2f(location2[0] - 25.0f, location2[1] + 3.0616169E-15f);
		GL11.glVertex2f(location2[0] - 24.620193f, location2[1] - 4.3412046f);
		GL11.glVertex2f(location2[0] - 23.492315f, location2[1] - 8.550504f);
		GL11.glVertex2f(location2[0] - 21.650635f, location2[1] - 12.5f);
		GL11.glVertex2f(location2[0] - 19.151112f, location2[1] - 16.06969f);
		GL11.glVertex2f(location2[0] - 16.06969f, location2[1] - 19.151112f);
		GL11.glVertex2f(location2[0] - 12.5f, location2[1] - 21.650635f);
		GL11.glVertex2f(location2[0] - 8.550504f, location2[1] - 23.492315f);
		GL11.glVertex2f(location2[0] - 4.3412046f, location2[1] - 24.620193f);
		GL11.glVertex2f(location2[0] - 4.5924254E-15f, location2[1] - 25.0f);
		GL11.glVertex2f(location2[0] + 4.3412046f, location2[1] - 24.620193f);
		GL11.glVertex2f(location2[0] + 8.550504f, location2[1] - 23.492315f);
		GL11.glVertex2f(location2[0] + 12.5f, location2[1] - 21.650635f);
		GL11.glVertex2f(location2[0] + 16.06969f, location2[1] - 19.151112f);
		GL11.glVertex2f(location2[0] + 19.151112f, location2[1] - 16.06969f);
		GL11.glVertex2f(location2[0] + 21.650635f, location2[1] - 12.5f);
		GL11.glVertex2f(location2[0] + 23.492315f, location2[1] - 8.550504f);
		GL11.glVertex2f(location2[0] + 24.620193f, location2[1] - 4.3412046f);
		GL11.glVertex2f(location2[0] + 25.0f, location2[1] - 6.1232338E-15f);
		GL11.glEnd();
		
		float[] trailf = {0,0,0};
		GL11.glBegin(GL11.GL_LINE_STRIP);
		for (int i = 0; i < trailLength-1; i++) {
			
			trailf = trail.getTrail(i);
			
			GL11.glVertex3f(trailf[0], trailf[1], trailf[2]);
			
		}
		GL11.glEnd();
		
//		float[] trailf2 = {0,0,0};
//		GL11.glBegin(GL11.GL_LINE_STRIP);
//		for (int i = 0; i < trailLength-1; i++) {
//			
//			trailf2 = trail2.getTrail(i);
//			
//			GL11.glVertex3f(trailf2[0], trailf2[1], trailf2[2]);
//			
//		}
//		GL11.glEnd();
		
		// Draw more stuff if debug toggle is on
		if (debugToggle == true) {
			
			// Draw a frametime graph at the top of the screen
			if (frame <= width) {
				
				if (frame == width) {
					frame = 0;
				}
				
				graphData[frame] = delta * 2;
				
				GL11.glBegin(GL11.GL_LINES);
				for (int i = 0; i < width; i++) {
					GL11.glVertex2f(i, height);
					GL11.glVertex2f(i, height - graphData[i]);
				}
				GL11.glEnd();
				
				if (frame < width - 50) {
					graphData[frame + 50] = 0;
				} else {
					graphData[frame - (width - 50)] = 0;
				}
			}
			
			frame++;
		}
		
		
		// Have any number of objects from any thread get drawn.
		//TODO check and see if opengl has anything like this built-in
		float[][] shape;
		float[] color;
		int type;
		for (int i = 0; i < DrawBuffer.drawBuffer.size(); i++) {
			shape = DrawBuffer.drawBuffer.get(i);
			type = (int) shape[0][0];
			color = shape[1];
			
			GL11.glColor3f(color[0], color[1], color[2]);
			GL11.glBegin(type);
			for (int j = 2; j < shape.length ; j++) {
				
				GL11.glVertex3f(shape[j][0], shape[j][1], shape[j][2]);
				
			}
			GL11.glEnd();
		}
		
		DrawBuffer.clear();
		
		
//		GL11.glPopMatrix();
	}

	public void update(int delta) throws IOException {
		
//		button.run();	// test out a button I've been working on

//		keyInput.refresh();
		
		//	Set the location of the physics objects to something the renderer can easily get at
		location = ComplexPhys.getLocation("Box");
		location2 = ComplexPhys.getLocation("Square");
		
		//	log the object's current location
		boxLogger.LogLine(ComplexPhys.getLocation("Box"));
		squareLogger.LogLine(ComplexPhys.getLocation("Square"));
		trail.updateTrail(location);
//		trail2.updateTrail(location2);
		
		if (debugToggle == true) {
			//TODO put stuff here
		}

		// give the object a force of gravity
		float[] f2 = {0, (float) (-9.8 * 1000), 0};
		ComplexPhys.getPhysObject("Box").addForce(f2);
		ComplexPhys.getPhysObject("Square").addForce(f2);
		
		// move the object using force; precisely calculated to the pixel
		if (Mouse.isButtonDown(0)) {
			int mouseX = Mouse.getX();
			int mouseY = Mouse.getY();
			
			float[] f = new float[3];
			Physics phys = ComplexPhys.getPhysObject("Box");
			
			f[0] = (mouseX - location[0] - phys.velocity[0]) * timeScale * phys.mass / delta;
			f[1] = (mouseY - location[1] - phys.velocity[1]) * timeScale * phys.mass / delta;
			
			phys.addForce(f);
			
		}
		
		if (Mouse.isButtonDown(1)) {
			int mouseX = Mouse.getX();
			int mouseY = Mouse.getY();
			
			float[] f = new float[3];
			Physics phys = ComplexPhys.getPhysObject("Square");
			
			f[0] = (mouseX - location2[0] - phys.velocity[0]) * timeScale * phys.mass / delta;
			f[1] = (mouseY - location2[1] - phys.velocity[1]) * timeScale * phys.mass / delta;
			
			phys.addForce(f);
		}
		
		//TODO Make KeyInput work better.
		if (KeyInput.debugDown == true) {
			
			if (debugToggle == false) {
				debugToggle = true;
				System.out.println("debug is ON");
			} else {
				debugToggle = false;
				System.out.println("debug is OFF");
			}
			
		}

		while (Keyboard.next()) {

			if (Keyboard.getEventKeyState()) {
				
				if (Keyboard.getEventKey() == Keyboard.KEY_E) {

					System.out.println("E Key Pressed");
					
					if (debugToggle == false) {
						debugToggle = true;
						System.out.println("debug is ON");
					} else {
						debugToggle = false;
						System.out.println("debug is OFF");
					}
					
				}

				if (Keyboard.getEventKey() == Keyboard.KEY_SPACE) {

					System.out.println("Space Key Pressed");
					blue = random.nextFloat();
					red = random.nextFloat();
					green = random.nextFloat();
				}
				
				if (Keyboard.getEventKey() == Keyboard.KEY_UP) {
					System.out.println("Up is Down");
					if (setFPS < 10) {
						setFPS += 1;
						if (setFPS == 0) {
							setFPS = 1;
						}
					} else if (setFPS < 60) {
						setFPS += 5;
					} else if (setFPS < 120) {
						setFPS += 10;
					} else if (setFPS < 480) {
						setFPS += 30;
					} else {
						setFPS += 120;
						if (setFPS > 2000) {
							setFPS = 2000;
						}
					} 
				}
				
				if (Keyboard.getEventKey() == Keyboard.KEY_DOWN) {
					System.out.println("Down is Down");
					if (setFPS < 10) {
						setFPS -= 1;
						if (setFPS == 0) {
							setFPS = 1;
						}
					} else if (setFPS < 60) {
						setFPS -= 5;
					} else if (setFPS < 120) {
						setFPS -= 10;
					} else if (setFPS < 480) {
						setFPS -= 30;
					} else {
						setFPS -= 120;
						if (setFPS > 2000) {
							setFPS = 2000;
						}
					} 
				}
			} else {
				
				if (Keyboard.getEventKey() == Keyboard.KEY_E) {
					
					System.out.println("E Key Released");
				}

				if (Keyboard.getEventKey() == Keyboard.KEY_SPACE) {

					System.out.println("Space Key Released");
				}
			}
		}

		updateFPS();
		ComplexPhys.UpdatePhysics(delta);
		
	}

	public long getTime() {

		return (Sys.getTime() * 1000) / Sys.getTimerResolution();

	}

	public int getDelta() {
		// Get the amount of milliseconds that has passed since the last frame.
		long time = getTime();
		int delta = (int) (time - lastFrame);
		lastFrame = time;
		
//		System.out.println("delta: " + delta);
		return delta;

	}

	public void updateFPS() {
		// Calculate the FPS
		if (getTime() - lastFPS > 1000) {
			System.out.println("FPS: " + fps);
			fps = 0; // reset the FPS counter
			lastFPS += 1000; // add 1 second
		}
		fps++;
	}
}
