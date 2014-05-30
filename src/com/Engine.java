package com;

/*
 * 	Physics Engine
 * 	Christopher Dombroski
 * 	
 * 	https://github.com/Nyrmburk/Physics-Engine
 */

import gui.GUI;
import gui.Tool;
import inout.Input;
import inout.Dir;

import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.Random;

import javax.swing.UIManager;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.Sys;

import physics.*;

public class Engine {
	
	static boolean closing = false;

	public static int delta; // change in milliseconds since the last frame
	public static float timeScale = 1000; // 1 second real-time is 1000 /
											// (timeScale) seconds in engine

	public static int width = 1200; // window width
	public static int height = 800; // window height
	public static int depth = 1200;

	public static float scale = 100; // number of pixels in 1 meter

	int fps; // Actual frames per second
	static int setFPS = 120; // Desired frames per second
	long lastFPS = 0; // last frame's fps
	long lastFrame = 0; // last frame's time of creation
	int frame = 0; // current frame number

	public static boolean debugToggle = true; // use the debug menu

	public static float red = 0.95f;
	public static float green = 0.95f;
	public static float blue = 0.95f;
	int[] graphData = new int[300]; // A list of frametime data.

	float[] attr = { 1000, 0f, 1f }; // The attributes of the objects for
										// physics. {Mass, Drag, Restitution}

	Random random = new Random();

	public void start() throws IOException {

		try {
			initialise();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		lastFPS = getTime(); // set lastFPS to current Time

		while (!closing) {
			
			delta = getDelta();
			
			update(delta);
			renderGL();
			
			if (Display.wasResized()) {
				width = GUI.cnvsDisplay.getWidth();
				height = GUI.cnvsDisplay.getHeight();
				GL11.glViewport(0, 0, width, height);
				initGL();
			}
			
			Display.update();
			Display.sync(setFPS);
			
		}
	}

	private void initialise() throws IOException, LWJGLException {
		
		initSystem();
		initDisplay();
		initGL();
		Input.load();

	}

	public static void close() {

		closing = true;

		System.out.println("closed");
	}
	
	private void initSystem() {
		String os = System.getProperty("os.name").toLowerCase();
		
		if (os.startsWith("win")) {
			os = "win";
		} else if (os.startsWith("mac")) {
			os = "mac";
		} else if (os.startsWith("linux")) {
			os = "linux";
		}
		
		String natives = System.getProperty("user.dir") + File.separator + "lib" + File.separator + "natives-" + os;
		System.setProperty("org.lwjgl.librarypath", natives);
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Unable to load default look and feel");
		}
		
		Dir.initPaths();
		
		lastFrame = getTime();
		
	}
	
	private void initDisplay() throws LWJGLException {
		
		GUI.initialize();

	}
	
	private void initGL() {
		
		float ratio = (float)width/height;
		float near = 1;
		float far = 2*depth;
		float fov = (float) Math.toDegrees(2 * Math.atan2(height, far - near));

		// init opengl
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
//		GL11.glOrtho(0, width, 0, height, -depth, depth);
		GLU.gluPerspective(fov, ratio, near, far);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);

//		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		
		FloatBuffer ambient = asFloatBuffer(new float[]{0.2f, 0.2f, 0.2f, 1f});
		FloatBuffer light = asFloatBuffer(new float[]{1f, 1f, 1f, 1f});
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_LIGHT0);
		GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, ambient);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, light);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, asFloatBuffer(new float[]{width/2, height/2, depth/2, 1}));
		GL11.glLoadIdentity();
		GLU.gluLookAt(width/2, height/2, depth, width/2, height/2, 0, 0, 1, 0);

	}
	
	static FloatBuffer asFloatBuffer(float[] floatArray) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(floatArray.length);
		buffer.put(floatArray);
		buffer.flip();
		return buffer;
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
			
			// Draw a frametime graph at the top of the screen
			if (frame <= graphData.length) {

				if (frame == graphData.length) {
					frame = 0;
				}

				graphData[frame] = delta * 2;

				GL11.glBegin(GL11.GL_LINES);
				for (int i = 0; i < graphData.length; i++) {
					
					if (graphData[i] < 34) {
						GL11.glColor3f(0, 0.75f, 0);
					} else if (graphData[i] < 68) {
						GL11.glColor3f(0.9f, 0.9f, 0);
					} else {
						GL11.glColor3f(0.75f, 0, 0);
					}
					
					GL11.glVertex2f(i, height);
					GL11.glVertex2f(i, height - graphData[i]);
				}
				GL11.glEnd();

				if (frame < graphData.length - 50) {
					graphData[frame + 50] = 0;
				} else {
					graphData[frame - (graphData.length - 50)] = 0;
				}
			}

			frame++;
			
		}
		
		GL11.glColor3f(red, green, blue);
		Manager.render();
		
//		Draw the triangle of death?
//		GL11.glBegin(GL11.GL_TRIANGLES);
//		GL11.glColor3f(1, 0, 0);
//		GL11.glVertex3f(450, 660, 1);
//		GL11.glColor3f(0, 1, 0);
//		GL11.glVertex3f(450, 140, 1);
//		GL11.glColor3f(0, 0, 1);
//		GL11.glVertex3f(900, 400, 1);
//		GL11.glEnd();
		
	}

	public void update(int delta) throws IOException {

		Input.refresh();
		
		switch (Input.mouseChanged[0]) {
			case Input.PRESSED:
				Tool.useTool(Tool.START);
				break;
			case Input.RELEASED:
				Tool.useTool(Tool.END);
				break;
			default:
				if (Input.mouseDown[0]) {
					Tool.useTool(Tool.USE);
				}
		}
		
		if (Input.keyChanged.get("delete") == Input.RELEASED) {
			java.util.List<String> lst = GUI.lstEntity.getSelectedValuesList();
			System.out.println(lst);
			for (String current : lst) {
				Manager.removeEntity(current);
				GUI.lm.removeElement(current);
			}
		}
		
		if (Input.keyChanged.get("debug") == Input.PRESSED) {

			if (debugToggle == false) {
				debugToggle = true;
			} else {
				debugToggle = false;
			}

		}

		if (Input.keyChanged.get("action") == Input.PRESSED) {

			blue = random.nextFloat();
			red = random.nextFloat();
			green = random.nextFloat();

		}

		if (Input.keyChanged.get("forward") == Input.PRESSED) {

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

		if (Input.keyChanged.get("backward") == Input.PRESSED) {

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

		updateFPS();
		Manager.update(delta);

	}

	public static long getTime() {

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
