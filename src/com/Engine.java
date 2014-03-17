package com;

/*
 * 	Physics Engine
 * 	Christopher Dombroski
 * 	
 * 	https://github.com/Nyrmburk/Physics-Engine
 */

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Random;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.Sys;

import com.Logger;
import com.Input;
//import com.Settings;


import effects.Trail;
import gui.Button;
import physics.*;

public class Engine {

	int delta; // change in milliseconds since the last frame
	public static float timeScale = 1000; // 1 second real-time is 1000 /
											// (timeScale) seconds in engine

	public static int width = 1200; // window width
	public static int height = 800; // window height
	public static int depth = 1200;

	public static float scale = 50; // number of pixels in 1 meter

	int fps; // Actual frames per second
	static int setFPS = 120; // Desired frames per second
	long lastFPS = 0; // last frame's fps
	long lastFrame = getTime(); // last frame's time of creation
	int frame = 0; // current frame number

	public static boolean debugToggle = true; // use the debug menu

	public static float red = 0.5f;
	public static float green = 0.5f;
	public static float blue = 1.0f;

	float[] location = { 400, 600, 0 }; // location of the first object. need to
										// change soon.
	float[] location2 = new float[3]; // location of the second object. ^^

	String obj1 = "Circle";
	String obj2 = "Ring";

	int[] graphData = new int[width]; // A list of frametime data.

	float[] attr = { 1000, 0f, 1f }; // The attributes of the objects for
										// physics. {Mass, Drag, Restitution}

	Logger circleLogger;
	Logger ringLogger;

	int trailLength = 250; // max positions to use for creating a trail.
	Trail trail = new Trail(trailLength); // create a trail
	// Trail trail2 = new Trail(trailLength);

	Button button = new Button(400, 600);

	Random random = new Random();

	public void start() throws IOException {

		try {
			Display.setDisplayMode(new DisplayMode(width - 1, height));
			Display.setTitle("Physics Engine");
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
		// GLButton button = new GLButton(KeyInput.mouseX, KeyInput.mouseY);
		// button.initGL();
		
		Manager.addEntity(obj1, Manager.SHAPE, "icosahedron");
		Manager.addEntity(obj2, Manager.SHAPE, "icosahedron");

		circleLogger = new Logger(obj1);
		ringLogger = new Logger(obj2);

	}

	public void close() {

		try {
			circleLogger.close();
			ringLogger.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("closed");

	}

	public void initGL() {

		// init opengl
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, width, 0, height, depth, 0);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);

	}

	public void renderGL() {

		// Clear the screen and depth buffer
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		GL11.glColor3f(red, green, blue);

		// draw a grid every 10 pixels
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

		if (debugToggle) {

			GL11.glBegin(GL11.GL_QUADS);
			for (int i = 0; i < Collision.overlapMap.size(); i++) {
				int intensity = Collision.getEntities(i).length;
				short[] sectorIndex = Collision.getKey(i);
				GL11.glColor3f(intensity * 0.2f, intensity * 0.2f, intensity * 0.2f);
				GL11.glVertex2f(sectorIndex[0] * scale, sectorIndex[1] * scale);
				GL11.glVertex2f(sectorIndex[0] * scale, sectorIndex[1] * scale
						+ scale);
				GL11.glVertex2f(sectorIndex[0] * scale + scale, sectorIndex[1]
						* scale + scale);
				GL11.glVertex2f(sectorIndex[0] * scale + scale, sectorIndex[1]
						* scale);
			}
			GL11.glEnd();
		}


		GL11.glColor3f(red, green, blue);

		GL11.glEnable(GL11.GL_VERTEX_ARRAY);
		GL11.glEnable(GL11.GL_NORMAL_ARRAY);
		for (int i = 0; i < Manager.Entity.size(); i++) {
			Manager.Entity.get(i).draw();
		}
		GL11.glDisable(GL11.GL_VERTEX_ARRAY);
		GL11.glDisable(GL11.GL_NORMAL_ARRAY);

		float[] trailf = { 0, 0, 0 };
		GL11.glBegin(GL11.GL_LINE_STRIP);
		for (int i = 0; i < trailLength - 1; i++) {

			trailf = trail.getTrail(i);

			GL11.glVertex3f(trailf[0], trailf[1], trailf[2]);

		}
		GL11.glEnd();

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

		// GL11.glPopMatrix();
	}

	public void update(int delta) throws IOException {

		// button.run(); // test out a button I've been working on

		// Set the location of the physics objects to something the renderer can
		// easily get at
		// location =
		// Vector.cScaleVector(ComplexPhys.getLocation("Box").clone(), scale);
		// location2 =
		// Vector.cScaleVector(ComplexPhys.getLocation("Square").clone(),
		// scale);

		location = Vector.cScaleVector(Manager.getEntity(obj1).getPhysics()
				.getLocation().clone(), scale);
		location2 = Vector.cScaleVector(Manager.getEntity(obj2).getPhysics()
				.getLocation().clone(), scale);

		// log the object's current location
		Physics phys2 = ComplexPhys.getPhysObject(obj1);
		float log[] = { phys2.getVelocity()[0], phys2.getVelocity()[1],
				phys2.getVelocity()[2], delta };
		circleLogger.LogLine(log);
		phys2 = ComplexPhys.getPhysObject(obj2);
		float log2[] = { phys2.getLocation()[0], phys2.getLocation()[1],
				phys2.getLocation()[2], delta };
		ringLogger.LogLine(log2);
		trail.updateTrail(location);
		// trail2.updateTrail(location2);
		
		

		if (debugToggle) {
			// TODO put stuff here
//			IntBuffer viewport = BufferUtils.createIntBuffer(16);
//			GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);
//			
//			FloatBuffer modelMatrix = BufferUtils.createFloatBuffer(16);
//			GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelMatrix);
//			
//			FloatBuffer projMatrix = BufferUtils.createFloatBuffer(16);
//			GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projMatrix);
//			
//			FloatBuffer obj_pos = BufferUtils.createFloatBuffer(16);
//			
//			boolean unProject = GLU.gluUnProject(Input.mouseX, Input.mouseY, 0.5f, modelMatrix, projMatrix, viewport, obj_pos);
//			
//			System.out.println(unProject);
//			System.out.println(obj_pos.get(0) + ", " + obj_pos.get(1) + ", " + obj_pos.get(2));
		}

		// give the object a force of gravity
		float[] f2 = { 0, (float) (-9.8 * 1000), 0 };
		ComplexPhys.getPhysObject(obj1).addForce(f2);
		ComplexPhys.getPhysObject(obj2).addForce(f2);

		Input.refresh();

		if (Input.mouse0Down == true) {

			int mouseX = Mouse.getX();
			int mouseY = Mouse.getY();

			float[] f = new float[3];
			Physics phys = ComplexPhys.getPhysObject(obj1);

			f[0] = (mouseX - location[0] - phys.velocity[0]) * timeScale
					* phys.mass / delta;
			f[1] = (mouseY - location[1] - phys.velocity[1]) * timeScale
					* phys.mass / delta;

			phys.addForce(f);
		}

		if (Input.mouse1Down == true) {

			int mouseX = Mouse.getX();
			int mouseY = Mouse.getY();

			float[] f = new float[3];
			Physics phys = ComplexPhys.getPhysObject(obj2);

			f[0] = (mouseX - location2[0] - phys.velocity[0]) * timeScale
					* phys.mass / delta;
			f[1] = (mouseY - location2[1] - phys.velocity[1]) * timeScale
					* phys.mass / delta;

			phys.addForce(f);
		}

		if (Input.debugChanged == Input.PRESSED) {

			if (debugToggle == false) {
				debugToggle = true;
				System.out.println("debug is ON");
			} else {
				debugToggle = false;
				System.out.println("debug is OFF");
			}

		}

		if (Input.actionChanged == Input.PRESSED) {

			blue = random.nextFloat();
			red = random.nextFloat();
			green = random.nextFloat();

		}

		if (Input.forwardChanged == Input.PRESSED) {

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

		if (Input.backwardChanged == Input.PRESSED) {

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

		if (Input.settingsChanged == Input.PRESSED) {

			com.Settings.editSettings();

		}

		updateFPS();
		ComplexPhys.UpdatePhysics(delta);
		Manager.update();

	}

	public long getTime() {

		return (Sys.getTime() * 1000) / Sys.getTimerResolution();

	}

	public int getDelta() {
		// Get the amount of milliseconds that has passed since the last frame.
		long time = getTime();
		int delta = (int) (time - lastFrame);
		lastFrame = time;

		if (delta == 0) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			delta = 1;
		}

		// System.out.println("delta: " + delta);
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
