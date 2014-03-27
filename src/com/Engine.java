package com;

/*
 * 	Physics Engine
 * 	Christopher Dombroski
 * 	
 * 	https://github.com/Nyrmburk/Physics-Engine
 */

import inout.Input;
import inout.Logger;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.Random;

import javax.swing.*;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.Sys;

import effects.Trail;
import physics.*;

public class Engine {
	
	JFrame frmMain;
	Canvas cnvsDisplay;
	
	static boolean closing = false;

	int delta; // change in milliseconds since the last frame
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

	public static float red = 0.5f;
	public static float green = 0.5f;
	public static float blue = 1.0f;
	
	Color guiColor = new Color(70, 70, 70, 255);

	float[] location = { 400, 600, 0 }; // location of the first object. need to
										// change soon.
	float[] location2 = new float[3]; // location of the second object. ^^

	String obj1 = "Circle";
	String obj2 = "Ring";

	int[] graphData = new int[width]; // A list of frametime data.

	float[] attr = { 1000, 0f, 1f }; // The attributes of the objects for
										// physics. {Mass, Drag, Restitution}

	static Logger circleLogger;
	static Logger ringLogger;

	int trailLength = 250; // max positions to use for creating a trail.
	Trail trail = new Trail(trailLength); // create a trail
	// Trail trail2 = new Trail(trailLength);

//	Button button = new Button(0, 0);

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

			Display.sync(setFPS);
			Display.update();
		}
	}

	private void initialise() throws IOException, LWJGLException {
		
		initSystem();
		initDisplay();
		initGL();
		
		Manager.addEntity(obj1, Manager.SHAPE, "icosahedron");
		Manager.addEntity(obj2, Manager.SHAPE, "icosahedron");

		circleLogger = new Logger(obj1);
		ringLogger = new Logger(obj2);

	}

	public static void close() {

		closing = true;
		try {
			circleLogger.close();
			ringLogger.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

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
		
		lastFrame = getTime();
		
	}
	
	private void initDisplay() throws LWJGLException {
		
		frmMain = new JFrame();
		frmMain.setResizable(false);
		frmMain.setTitle("Physics Engine");
		frmMain.setBackground(guiColor);
		frmMain.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		LayoutManager manager = new BorderLayout();
		frmMain.setLayout(manager);
		
		JPanel pnlToolBar = new JPanel(new GridLayout(0,2));
		pnlToolBar.setBackground(guiColor);
		pnlToolBar.setPreferredSize(new Dimension(40, height));
		frmMain.add(pnlToolBar, BorderLayout.LINE_START);
		
		cnvsDisplay = new Canvas();
		cnvsDisplay.setSize(width, height);
		frmMain.add(cnvsDisplay, BorderLayout.CENTER);
		
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(guiColor);
		panel.setPreferredSize(new Dimension(180, height));
		frmMain.add(panel, BorderLayout.LINE_END);
		
		
		JPanel pnlTimeline = new JPanel(new FlowLayout());
		JButton btnRecord = new JButton("Record");
		btnRecord.setBackground(guiColor);
		pnlTimeline.add(btnRecord);
		JButton btnStop = new JButton("Stop");
		btnStop.setBackground(guiColor);
		pnlTimeline.add(btnStop);
		JButton btnPause = new JButton("Pause");
		btnPause.setBackground(guiColor);
		pnlTimeline.add(btnPause);
		JButton btnPlay = new JButton("Play");
		btnPlay.setBackground(guiColor);
		pnlTimeline.add(btnPlay);
		JSlider sldr = new JSlider();
		sldr.setBackground(guiColor);
		sldr.setPreferredSize(new Dimension(width/2, 20));
		pnlTimeline.add(sldr);
		pnlTimeline.setBackground(guiColor);
		pnlTimeline.setPreferredSize(new Dimension(0, 40));
//		frmMain.add(pnlTimeline, BorderLayout.PAGE_END);
		
		initMenuBar();

		frmMain.pack();
		frmMain.setVisible(true);
		
//		Display.setDisplayMode(new DisplayMode(width - 1, height));
		Display.setParent(cnvsDisplay);
		Display.create();

		frmMain.addWindowListener(new WindowAdapter() {
			
			public void windowClosing(WindowEvent e) {

				JFrame frame = (JFrame) e.getSource();

				int result = JOptionPane.showConfirmDialog(frame,
						"Are you sure you want to exit the application?",
						"Exit Application", JOptionPane.YES_NO_OPTION);

				if (result == JOptionPane.YES_OPTION) {
					close();
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				}

			}
			
		});

	}
	
	private void initMenuBar() {
		
		MenuBar mb = new MenuBar();
		
		Menu menuFile = new Menu("File");
		menuFile.add(new MenuItem("New"));
		menuFile.add(new MenuItem("Open"));
		menuFile.add(new MenuItem("Save"));
		menuFile.add(new MenuItem("Save As..."));
		menuFile.add(new MenuItem("Import"));
//		menuFile.add(new MenuItem("Export"));
		menuFile.add(new MenuItem("Reset"));
		menuFile.addSeparator();
		menuFile.add(new MenuItem("Record"));
		menuFile.add(new MenuItem("Replay"));
		menuFile.addSeparator();
		menuFile.add(new MenuItem("Exit"));
		mb.add(menuFile);
		
		Menu menuEdit = new Menu("Edit");
		menuEdit.add(new MenuItem("Undo"));
		menuEdit.add(new MenuItem("Redo"));
		menuEdit.addSeparator();
		menuEdit.add(new MenuItem("Copy"));
		menuEdit.add(new MenuItem("Paste"));
		menuEdit.add(new MenuItem("Cut"));
		menuEdit.addSeparator();
		menuEdit.add(new MenuItem("Scene"));
		menuEdit.add(new MenuItem("Settings"));
		mb.add(menuEdit);
		
		Menu menuEntity = new Menu("Entity");
		menuEntity.add(new MenuItem("Add"));
		menuEntity.add(new MenuItem("Remove"));
		menuEntity.add(new MenuItem("Disable"));
		menuEntity.addSeparator();
		menuEntity.add(new MenuItem("Model"));
		mb.add(menuEntity);
		
		Menu menuConstraint = new Menu("Constraint");
		menuConstraint.add(new MenuItem("Add"));
		menuConstraint.add(new MenuItem("Remove"));
		menuConstraint.add(new MenuItem("Disable"));
		menuConstraint.add(new MenuItem("Type"));
		mb.add(menuConstraint);
		
		Menu menuSelect = new Menu("Select");
		menuSelect.add(new MenuItem("All"));
		menuSelect.add(new MenuItem("Deselect"));
		menuSelect.add(new MenuItem("Reselect"));
		menuSelect.add(new MenuItem("Inverse"));
		menuSelect.add(new MenuItem("Entities"));
		menuSelect.add(new MenuItem("Constraints"));
		mb.add(menuSelect);
		
		Menu menuView = new Menu("View");
		menuView.add(new MenuItem("Zoom"));
		menuView.addSeparator();
		menuView.add(new MenuItem("Fill"));
		menuView.add(new MenuItem("Wireframe"));
		menuView.add(new MenuItem("Points"));
		menuView.addSeparator();
		menuView.add(new MenuItem("Debug"));
		mb.add(menuView);
		
		Menu menuPlugin = new Menu("Plugin");
		menuPlugin.add(new MenuItem("generated1"));
		menuPlugin.add(new MenuItem("generated2"));
		menuPlugin.add(new MenuItem("generated3"));
		menuPlugin.addSeparator();
		menuPlugin.add(new MenuItem("Plugin Manager"));
		mb.add(menuPlugin);
		
		Menu menuHelp = new Menu("Help");
		menuHelp.add(new MenuItem("Manual"));
		menuHelp.add(new MenuItem("Updates"));
		menuHelp.add(new MenuItem("Report a Bug"));
		menuHelp.addSeparator();
		menuHelp.add(new MenuItem("About"));
		mb.add(menuHelp);
		frmMain.setMenuBar(mb);
		
	}

	private void initGL() {

		// init opengl
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
//		GL11.glOrtho(0, width, 0, height, depth, 0);
		GLU.gluPerspective(37, (float)width/height, 0.03f, 2*depth);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);

//		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		
//		FloatBuffer ambient;
//		FloatBuffer brightAmbient;
//		FloatBuffer light;
//		ambient = asFloatBuffer(new float[]{0.15f, 0.15f, 0.15f, 1f});
//		brightAmbient = asFloatBuffer(new float[]{0.5f, 0.5f, 0.5f, 1f});
//		light = asFloatBuffer(new float[]{1.5f, 1.5f, 1.5f, 1f});
//		GL11.glShadeModel(GL11.GL_SMOOTH);
//		GL11.glEnable(GL11.GL_DEPTH_TEST);
//		GL11.glEnable(GL11.GL_LIGHTING);
//		GL11.glEnable(GL11.GL_LIGHT0);
//		GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, ambient);
//		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, light);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
//		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
//		GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE);
//		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, asFloatBuffer(new float[]{width/2, height/2, depth/2, 1}));
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
			if (frame <= graphData.length) {

				if (frame == graphData.length) {
					frame = 0;
				}

				graphData[frame] = delta * 2;

				GL11.glBegin(GL11.GL_LINES);
				for (int i = 0; i < graphData.length; i++) {
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
		
//		button.update();

		// GL11.glPopMatrix();
	}

	public void update(int delta) throws IOException {

		location = Vector.cScaleVector(Manager.getEntity(obj1).getPhysics()
				.getLocation().clone(), scale);
		location2 = Vector.cScaleVector(Manager.getEntity(obj2).getPhysics()
				.getLocation().clone(), scale);

		// log the object's current location
		Physics phys2 = ComplexPhys.getPhysObject(obj1);
		float log[] = { phys2.getVelocity()[0], phys2.getVelocity()[1],
				phys2.getVelocity()[2], delta };
		circleLogger.LogLine(log);
		
		if (50 > location[0]) {
			if (phys2.velocity[0] < 0) {
				phys2.velocity[0] *= -1;
			}
		}
		if (location[0] >  width-50) {
			if (phys2.velocity[0] > 0) {
				phys2.velocity[0] *= -1;
			}
		}
		
		if (50 > location[1]) {
			if (phys2.velocity[1] < 0) {
				phys2.velocity[1] *= -1;
			}
		}
		if (location[1] >  height-50) {
			if (phys2.velocity[1] > 0) {
				phys2.velocity[1] *= -1;
			}
		}
		
		phys2 = ComplexPhys.getPhysObject(obj2);
		float log2[] = { phys2.getLocation()[0], phys2.getLocation()[1],
				phys2.getLocation()[2], delta };
		ringLogger.LogLine(log2);
		trail.updateTrail(location);
//		 trail2.updateTrail(location2);
		
		if (50 > location2[0]) {
			if (phys2.velocity[0] < 0) {
				phys2.velocity[0] *= -1;
			}
		}
		if (location2[0] >  width-50) {
			if (phys2.velocity[0] > 0) {
				phys2.velocity[0] *= -1;
			}
		}
		
		if (50 > location2[1]) {
			if (phys2.velocity[1] < 0) {
				phys2.velocity[1] *= -1;
			}
		}
		if (location2[1] >  height-50) {
			if (phys2.velocity[1] > 0) {
				phys2.velocity[1] *= -1;
			}
		}

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
			float[] mouse = {Input.mouseX, Input.mouseY, 0};

			float[] f = new float[3];
			Physics phys = ComplexPhys.getPhysObject(obj1);

			f[0] = (mouseX - location[0] - phys.velocity[0]) * timeScale *1000 / delta;
			f[1] = (mouseY - location[1] - phys.velocity[1]) * timeScale *1000 / delta;

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
