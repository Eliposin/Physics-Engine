package com;

/*
 * 	Physics Engine
 * 	Christopher Dombroski
 * 	
 * 	https://github.com/Nyrmburk/Physics-Engine
 */

import inout.Input;
import inout.Logger;
import inout.Dir;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListDataListener;

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
	
	Color guiColor = new Color(80, 80, 80, 255);
	
	int tool;
	
	final int MOVE = 0;
	final int SELECT = 1;
	final int ADD_ENTITY = 2;
	final int ADD_CONSTRAINT = 3;
	final int ADD_FORCE = 4;
	
	final int START = 5;
	final int USE = 6;
	final int END = 7;

	int[] graphData = new int[width]; // A list of frametime data.

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

			Profiler.prof("Display.sync");
			Display.sync(setFPS);
			Profiler.prof("Display.sync");
			Display.update();
			
		}
	}

	private void initialise() throws IOException, LWJGLException {
		
		initSystem();
		initDisplay();
		initGL();

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
	
	public int toolBarSize = 40;
	public int toolBarPadding = 2;
	public int workPanelSize = 180;
	public int workPanelPadding = 2;
	public int timelineSize = 40;
	public int timelinePadding = 2;
	
	private void initDisplay() throws LWJGLException {
		
		frmMain = new JFrame();
		frmMain.setResizable(false);
		frmMain.setTitle("Physics Engine");
		frmMain.setBackground(guiColor);
		frmMain.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmMain.setLayout(new BorderLayout());

		cnvsDisplay = new Canvas();
		cnvsDisplay.setBackground(guiColor);
		cnvsDisplay.setSize(width, height);
		frmMain.add(cnvsDisplay, BorderLayout.CENTER);
		
		initToolbar();
		
		initWorkPanel();
		
		initTimeline();
		
		initMenuBar();

		frmMain.pack();
		frmMain.setVisible(true);
		
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
	
	private AbstractButton makeImageButton(AbstractButton btn, String name, String ext, Dimension size) {
		
		String s = File.separator;
		File file = new File(Dir.gui + s + name + ext);
		File fileSelected = new File(Dir.gui + s + name + "Selected" + ext);
		File fileHover = new File(Dir.gui + s + name + "Hover" + ext);
		
		BufferedImage img;
		BufferedImage imgSelected;
		BufferedImage imgHover;
		
		ImageIcon icon = new ImageIcon();
		ImageIcon iconSelected = new ImageIcon();
		ImageIcon iconHover = new ImageIcon();
		
		try {
			img = ImageIO.read(file);
			imgSelected = ImageIO.read(fileSelected);
			imgHover = ImageIO.read(fileHover);
			
			icon = new ImageIcon(img.getScaledInstance(
					size.width, size.height, 
					java.awt.Image.SCALE_SMOOTH));
			iconSelected = new ImageIcon(imgSelected.getScaledInstance(
					size.width, size.height, 
					java.awt.Image.SCALE_SMOOTH));
			iconHover = new ImageIcon(imgHover.getScaledInstance(
					size.width, size.height, 
					java.awt.Image.SCALE_SMOOTH));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		btn.setIcon(icon);
		btn.setRolloverIcon(iconHover);
		btn.setSelectedIcon(iconSelected);
		
		btn.setBorder(BorderFactory.createEmptyBorder());
		btn.setContentAreaFilled(false);
		btn.setName(name);
		
		return btn;
		
	}
	
	private void initToolbar() {
		
		int btnwidth = toolBarSize - 2*toolBarPadding;;
		JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEADING, toolBarPadding, toolBarPadding));
		toolbar.setPreferredSize(new Dimension(toolBarSize, 0));
		toolbar.setBackground(guiColor);
		ButtonGroup btngrp = new ButtonGroup();
		
		JRadioButton move= (JRadioButton) makeImageButton(new JRadioButton(), 
				"move", ".png", new Dimension(btnwidth, btnwidth));
		move.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
            	tool = MOVE;
        		System.out.println("Tool move");
			}
		});
		toolbar.add(move);
		btngrp.add(move);
		
		JRadioButton select= (JRadioButton) makeImageButton(new JRadioButton(), 
				"select", ".png", new Dimension(btnwidth, btnwidth));
		select.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
            	tool = SELECT;
        		System.out.println("Tool select");
			}
		});
		toolbar.add(select);
		btngrp.add(select);
		
		JRadioButton addEntity= (JRadioButton) makeImageButton(new JRadioButton(), 
				"addEntity", ".png", new Dimension(btnwidth, btnwidth));
		addEntity.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	tool = ADD_ENTITY;
        		System.out.println("Tool addEntity");
            }
        });
		toolbar.add(addEntity);
		btngrp.add(addEntity);
		
		JRadioButton addConstraint= (JRadioButton) makeImageButton(new JRadioButton(), 
				"addConstraint", ".png", new Dimension(btnwidth, btnwidth));
		addConstraint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
            	tool = ADD_CONSTRAINT;
        		System.out.println("Tool addConstraint");
			}
		});
		toolbar.add(addConstraint);
		btngrp.add(addConstraint);
		
		JRadioButton addForce= (JRadioButton) makeImageButton(new JRadioButton(), 
				"addForce", ".png", new Dimension(btnwidth, btnwidth));
		addForce.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
            	tool = ADD_FORCE;
        		System.out.println("Tool addForce");
			}
		});
		toolbar.add(addForce);
		btngrp.add(addForce);

		frmMain.add(toolbar, BorderLayout.LINE_START);
		
	}
	
	int entityCount = 0;
	DefaultListModel<String> lm = new DefaultListModel<String>();
	JList<String> lstEntity = new JList<String>(lm);

	private void initWorkPanel() {
		
		JPanel panel = new JPanel(new GridLayout(4, 0));
		panel.setBackground(guiColor);
		
		JTabbedPane tabs = new JTabbedPane();
		tabs.setForeground(guiColor.darker());
		
		lstEntity.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		lstEntity.setLayoutOrientation(JList.VERTICAL);
		lstEntity.setBackground(guiColor.brighter());
		lstEntity.setVisibleRowCount(-1);
		JScrollPane listScroller = new JScrollPane(lstEntity);
		listScroller.setPreferredSize(new Dimension(workPanelSize, 80));
		
		tabs.add("Entity", listScroller);
		tabs.add("Constraints", new JPanel());
		panel.add(tabs);
		
		frmMain.add(panel, BorderLayout.LINE_END);
		
	}
	
	private JPanel makeLayer(String name, short type) {

		JPanel pnlLayer = new JPanel(new FlowLayout(FlowLayout.LEADING));
		pnlLayer.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		pnlLayer.setBackground(guiColor);
		pnlLayer.setPreferredSize(new Dimension(80, 40));
		
		Checkbox chkEnabled = new Checkbox();
		chkEnabled.setFocusable(false);
		pnlLayer.add(chkEnabled);
		
		ImageIcon icon = new ImageIcon("res/GUI/gear_icon.png");
		JButton btn = new JButton(icon);
		btn.setBorder(BorderFactory.createLineBorder(guiColor.darker()));
		btn.setContentAreaFilled(false);
		btn.setFocusPainted(false);
		btn.setPreferredSize(new Dimension(32, 32));
		pnlLayer.add(btn);
		
		JLabel lblName = new JLabel();
		lblName.setText(name);
		lblName.setBackground(guiColor.brighter());
		lblName.setForeground(guiColor.brighter().brighter());
		pnlLayer.add(lblName);
		
		return pnlLayer;
	}
	
	private void initTimeline() {
		
		JPanel pnlTimeline = new JPanel(new BorderLayout());
		JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.LEADING, toolBarPadding, toolBarPadding));
		pnlButtons.setBackground(guiColor);
		
		int btnSize = timelineSize - 2*timelinePadding;
		
		ButtonGroup time = new ButtonGroup();
		
		JRadioButton record= (JRadioButton) makeImageButton(new JRadioButton(), 
				"record", ".png", new Dimension(btnSize, btnSize));
		pnlButtons.add(record);
		time.add(record);

		JRadioButton stop= (JRadioButton) makeImageButton(new JRadioButton(), 
				"stop", ".png", new Dimension(btnSize, btnSize));
		pnlButtons.add(stop);
		time.add(stop);
		
		JRadioButton pFrame= (JRadioButton) makeImageButton(new JRadioButton(), 
				"pFrame", ".png", new Dimension(btnSize, btnSize));
		pnlButtons.add(pFrame);
		time.add(pFrame);
		
		JRadioButton nFrame= (JRadioButton) makeImageButton(new JRadioButton(), 
				"nFrame", ".png", new Dimension(btnSize, btnSize));
		pnlButtons.add(nFrame);
		time.add(nFrame);
		
		JRadioButton fBackward= (JRadioButton) makeImageButton(new JRadioButton(), 
				"fBackward", ".png", new Dimension(btnSize, btnSize));
		pnlButtons.add(fBackward);
		time.add(fBackward);
		
		JRadioButton fForward= (JRadioButton) makeImageButton(new JRadioButton(), 
				"fForward", ".png", new Dimension(btnSize, btnSize));
		pnlButtons.add(fForward);
		time.add(fForward);

		JRadioButton pause= (JRadioButton) makeImageButton(new JRadioButton(), 
				"pause", ".png", new Dimension(btnSize, btnSize));
		pnlButtons.add(pause);
		time.add(pause);
		
		JRadioButton play= (JRadioButton) makeImageButton(new JRadioButton(), 
				"play", ".png", new Dimension(btnSize, btnSize));
		pnlButtons.add(play);
		time.add(play);
		
		pnlTimeline.add(pnlButtons, BorderLayout.LINE_START);
		
		JSlider sldr = new JSlider();
		sldr.setBackground(guiColor);
		sldr.setPreferredSize(new Dimension(width/2, btnSize));
		pnlTimeline.add(sldr);
		
		pnlTimeline.setBackground(guiColor);
		pnlTimeline.setPreferredSize(new Dimension(500, timelineSize));
		frmMain.add(pnlTimeline, BorderLayout.PAGE_START);
		
	}
	
	private void initMenuBar() {
		
		MenuBar mb = new MenuBar();
		
		Menu menuFile = new Menu("File");
		menuFile.add(new MenuItem("New"));
		menuFile.add(new MenuItem("Open"));
		menuFile.add(new MenuItem("Save"));
		menuFile.add(new MenuItem("Save As..."));
		menuFile.add(new MenuItem("Import"));
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
		menuView.add(new MenuItem("Top"));
		menuView.add(new MenuItem("Bottom"));
		menuView.add(new MenuItem("Left"));
		menuView.add(new MenuItem("Right"));
		menuView.add(new MenuItem("Front"));
		menuView.add(new MenuItem("Back"));
		menuView.addSeparator();
		menuView.add(new MenuItem("Camera 1"));
		menuView.add(new MenuItem("Camera 2"));
		menuView.add(new MenuItem("Camera n"));
		menuView.add(new MenuItem("Camera \" name\""));
		menuView.addSeparator();
		menuView.add(new MenuItem("Zoom"));
		menuView.add(new MenuItem("Zoom All"));
		menuView.add(new MenuItem("Zoom Selection"));
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
		GL11.glEnable(GL11.GL_DEPTH_TEST);
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

		Profiler.prof("Engine.renderGL");
		
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

		Profiler.prof("Engine.renderGL");
		
		GL11.glColor3f(red, green, blue);
		GL11.glEnable(GL11.GL_VERTEX_ARRAY);
		GL11.glEnable(GL11.GL_NORMAL_ARRAY);
		for (int i = 0; i < Manager.Entity.size(); i++) {
			Manager.Entity.get(i).draw();
		}
		GL11.glDisable(GL11.GL_VERTEX_ARRAY);
		GL11.glDisable(GL11.GL_NORMAL_ARRAY);
		
		Profiler.prof("Engine.renderGL");

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
		
		Profiler.prof("Engine.renderGL");
		
	}

	public void update(int delta) throws IOException {

		Input.refresh();
		
		switch (Input.mouse0Changed) {
			case Input.PRESSED:
				tool(START);
				break;
			case Input.RELEASED:
				tool(END);
				break;
			default:
				if (Input.mouse0Down) {
					tool(USE);
				}
		}
		
		if (Input.deleteChanged == Input.RELEASED) {
			java.util.List<String> lst = lstEntity.getSelectedValuesList();
			System.out.println(lst);
			for (int i = 0; i < lst.size(); i++) {
				Manager.removeEntity(lst.get(i));
				lm.removeElement(lst.get(i));
			}
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
		Manager.update(delta);

	}
	
	public void tool(int action) {
		switch(tool) {
		case MOVE: move(action);
			break;
		case SELECT: select(action);
			break;
		case ADD_ENTITY: addEntity(action);
			break;
		case ADD_CONSTRAINT: addConstraint(action);
			break;
		case ADD_FORCE: addForce(action);
			break;
		}
	}
	
	private void move(int action) {
		
		switch (action) {
		case START:
			break;
		case USE:
			
			java.util.List<String> lst = lstEntity.getSelectedValuesList();
			for (int i = 0; i < lst.size(); i++) {
				Entity e = Manager.getEntity(lst.get(i));
				e.setLocation(Vector.cAddVector(e.location.clone(), new float[]{Input.deltaX, Input.deltaY, 0}));
			}
			
			break;
		case END:
			break;
		}
		
	}
	
	private void select(int action) {
		
		switch (action) {
		case START:
			break;
		case USE:
			break;
		case END:
			break;
		}
		
	}
	
	private void addEntity(int action) {
		
		String name;
		
		switch (action) {
		case START:
			entityCount++;
			name = "Entity " + entityCount;
			Manager.addEntity(name, Entity.SHAPE, new float[]{Input.mouseX, Input.mouseY, 0}, "icosahedron");
			lm.addElement(name);
			
			break;
		case USE:
			name = "Entity " + entityCount;
			Entity e = Manager.getEntity(name);
			e.setLocation(Vector.cAddVector(e.location.clone(), new float[]{Input.deltaX, Input.deltaY, 0}));
			
			break;
		case END:
			break;
		}
		
	}
	
	private void addConstraint(int action) {
		
		switch (action) {
		case START:
			break;
		case USE:
			break;
		case END:
			break;
		}
		
	}
	
	private void addForce(int action) {
		
		switch (action) {
		case START:
			break;
		case USE:
			break;
		case END:
			break;
		}
		
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
			System.out.println(Profiler.getString());
			Profiler.clear();
			fps = 0; // reset the FPS counter
			lastFPS += 1000; // add 1 second
		}
		fps++;
	}

}
