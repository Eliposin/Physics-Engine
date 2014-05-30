package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

import com.Engine;

import gui.Tool;
import inout.Dir;

public class GUI {

	public static JFrame frmMain;
	public static Canvas cnvsDisplay;

	static int entityCount = 0;

	static Color guiColor = new Color(80, 80, 80, 255);

	public static int toolBarSize = 40;
	public static int toolBarPadding = 2;
	public static int workPanelSize = 180;
	public static int workPanelPadding = 2;
	public static int timelineSize = 40;
	public static int timelinePadding = 2;
	
	static ScriptEngineManager engineFactory = new ScriptEngineManager();

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws LWJGLException
	 */
	public static void initialize() throws LWJGLException {

		frmMain = new JFrame();
//		frmMain.setResizable(false);
		frmMain.setTitle("Physics Engine");
		frmMain.setBackground(guiColor);
		frmMain.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmMain.setLayout(new BorderLayout());

		cnvsDisplay = new Canvas();
		cnvsDisplay.setBackground(guiColor);
		cnvsDisplay.setSize(Engine.width, Engine.height);
		frmMain.add(cnvsDisplay, BorderLayout.CENTER);

		initToolbar();

		initWorkPanel();

//		initTimeline();

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
					Engine.close();
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				}

			}

		});

	}

	private static void initToolbar() {

		int btnwidth = toolBarSize - 2 * toolBarPadding;

		JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEADING,
				toolBarPadding, toolBarPadding));
		toolbar.setPreferredSize(new Dimension(toolBarSize, 0));
		toolbar.setBackground(guiColor);
		ButtonGroup btngrp = new ButtonGroup();

		JRadioButton move = (JRadioButton) makeImageButton(new JRadioButton(),
				"move", ".png", new Dimension(btnwidth, btnwidth));
		move.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Tool.currentTool = Tool.MOVE;
				System.out.println("Tool move");
			}
		});
		toolbar.add(move);
		btngrp.add(move);

		JRadioButton select = (JRadioButton) makeImageButton(
				new JRadioButton(), "select", ".png", new Dimension(
						btnwidth, btnwidth));
		select.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Tool.currentTool = Tool.SELECT;
				System.out.println("Tool select");
			}
		});
		toolbar.add(select);
		btngrp.add(select);
		
		JRadioButton drag = (JRadioButton) makeImageButton(
				new JRadioButton(), "drag", ".png", new Dimension(
						btnwidth, btnwidth));
		drag.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Tool.currentTool = Tool.DRAG;
				System.out.println("Tool drag");
			}
		});
		toolbar.add(drag);
		btngrp.add(drag);

		JRadioButton addEntity = (JRadioButton) makeImageButton(
				new JRadioButton(), "addEntity", ".png", new Dimension(
						btnwidth, btnwidth));
		addEntity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Tool.currentTool = Tool.ADD_ENTITY;
				System.out.println("Tool addEntity");
			}
		});
		toolbar.add(addEntity);
		btngrp.add(addEntity);

		JRadioButton addConstraint = (JRadioButton) makeImageButton(
				new JRadioButton(), "addConstraint", ".png", new Dimension(
						btnwidth, btnwidth));
		addConstraint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Tool.currentTool = Tool.ADD_CONSTRAINT;
				System.out.println("Tool addConstraint");
			}
		});
		toolbar.add(addConstraint);
		btngrp.add(addConstraint);

		JRadioButton addForce = (JRadioButton) makeImageButton(
				new JRadioButton(), "addForce", ".png", new Dimension(btnwidth,
						btnwidth));
		addForce.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Tool.currentTool = Tool.ADD_FORCE;
				System.out.println("Tool addForce");
			}
		});
		toolbar.add(addForce);
		btngrp.add(addForce);

		frmMain.add(toolbar, BorderLayout.LINE_START);

	}

	public static DefaultListModel<String> lm = new DefaultListModel<String>();
	public static JList<String> lstEntity = new JList<String>(lm);

	private static void initWorkPanel() {

		JPanel workPanel = new JPanel(new GridLayout(4, 0));
		workPanel.setBackground(guiColor);

		JTabbedPane entityTabs = new JTabbedPane();
		entityTabs.setForeground(guiColor.darker());

		lstEntity
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		lstEntity.setLayoutOrientation(JList.VERTICAL);
		lstEntity.setBackground(guiColor.brighter());
		lstEntity.setForeground(guiColor.darker().darker().darker());
		lstEntity.setVisibleRowCount(-1);
		JScrollPane listScroller = new JScrollPane(lstEntity);
		listScroller.setPreferredSize(new Dimension(workPanelSize, 80));

		entityTabs.add("Entity", listScroller);
		entityTabs.add("Constraints", new JPanel());
		workPanel.add(entityTabs);

		JTabbedPane attributes = new JTabbedPane();
		JPanel pnlAtttributes = new JPanel(new FlowLayout(FlowLayout.LEADING,
				2, 2));
		pnlAtttributes.setBackground(guiColor.brighter());
		attributes.add("Attributes", pnlAtttributes);
		workPanel.add(attributes);

		frmMain.add(workPanel, BorderLayout.LINE_END);

	}

//	private static void initTimeline() {
//
//		JPanel pnlTimeline = new JPanel(new BorderLayout());
//		JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.LEADING,
//				toolBarPadding, toolBarPadding));
//		pnlButtons.setBackground(guiColor);
//
//		int btnSize = timelineSize - 2 * timelinePadding;
//
//		ButtonGroup time = new ButtonGroup();
//
//		JRadioButton record = (JRadioButton) makeImageButton(
//				new JRadioButton(), "record", ".png", new Dimension(btnSize,
//						btnSize));
//		pnlButtons.add(record);
//		time.add(record);
//
//		JRadioButton stop = (JRadioButton) makeImageButton(new JRadioButton(),
//				"stop", ".png", new Dimension(btnSize, btnSize));
//		pnlButtons.add(stop);
//		time.add(stop);
//
//		JRadioButton pFrame = (JRadioButton) makeImageButton(
//				new JRadioButton(), "pFrame", ".png", new Dimension(btnSize,
//						btnSize));
//		pnlButtons.add(pFrame);
//		time.add(pFrame);
//
//		JRadioButton nFrame = (JRadioButton) makeImageButton(
//				new JRadioButton(), "nFrame", ".png", new Dimension(btnSize,
//						btnSize));
//		pnlButtons.add(nFrame);
//		time.add(nFrame);
//
//		JRadioButton fBackward = (JRadioButton) makeImageButton(
//				new JRadioButton(), "fBackward", ".png", new Dimension(btnSize,
//						btnSize));
//		pnlButtons.add(fBackward);
//		time.add(fBackward);
//
//		JRadioButton fForward = (JRadioButton) makeImageButton(
//				new JRadioButton(), "fForward", ".png", new Dimension(btnSize,
//						btnSize));
//		pnlButtons.add(fForward);
//		time.add(fForward);
//
//		JRadioButton pause = (JRadioButton) makeImageButton(new JRadioButton(),
//				"pause", ".png", new Dimension(btnSize, btnSize));
//		pnlButtons.add(pause);
//		time.add(pause);
//
//		JRadioButton play = (JRadioButton) makeImageButton(new JRadioButton(),
//				"play", ".png", new Dimension(btnSize, btnSize));
//		pnlButtons.add(play);
//		time.add(play);
//
//		pnlTimeline.add(pnlButtons, BorderLayout.LINE_START);
//
//		JSlider sldr = new JSlider();
//		sldr.setBackground(guiColor);
//		sldr.setPreferredSize(new Dimension(Engine.width / 2, btnSize));
//		pnlTimeline.add(sldr);
//
//		pnlTimeline.setBackground(guiColor);
//		pnlTimeline.setPreferredSize(new Dimension(500, timelineSize));
//		frmMain.add(pnlTimeline, BorderLayout.PAGE_START);
//
//	}

	private static void initMenuBar(){

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
		MenuItem exit = new MenuItem();
		exit.setName("Exit");
		exit.setLabel(exit.getName());
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);;
			}
		});
		menuFile.add(exit);
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
		MenuItem pluginManager = new MenuItem();
		pluginManager.setName("Plugin Manager");
		pluginManager.setLabel(pluginManager.getName());
		pluginManager.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					pluginDialog();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		menuPlugin.add(pluginManager);
		mb.add(menuPlugin);
		frmMain.setMenuBar(mb);

		Menu menuHelp = new Menu("Help");
		menuHelp.add(new MenuItem("Manual"));
		menuHelp.add(new MenuItem("Updates"));
		menuHelp.add(new MenuItem("Report a Bug"));
		menuHelp.addSeparator();
		menuHelp.add(new MenuItem("About"));
		mb.add(menuHelp);
		
	}

	/**
	 * Processes a simple JavaScript call; future
	 * functionality will process external
	 * scripts as plugins
	 * @throws IOException 
	 */
	protected static void pluginDialog() throws IOException {
		/////////////////////////////////////////////////////
		////////We should try to take all scripting /////////
		////////out of GUI and move it to the 		/////////
		////////Plugin class.						/////////
		/////////////////////////////////////////////////////

		JFileChooser fc = new JFileChooser(Dir.paths.get("plugins"));
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"JavaScript files", "js");
		fc.setFileFilter(filter);
		int returnVal = fc.showOpenDialog(fc);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			Charset charset = Charset.forName("UTF-8");
			try (BufferedReader reader = Files.newBufferedReader(fc
					.getSelectedFile().toPath(), charset)) {
				String line = null;
				StringBuilder sb = new StringBuilder();
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\r\n");
				}
				ScriptEngineManager factory = new ScriptEngineManager();
				ScriptEngine engine = factory.getEngineByName("JavaScript");
				try {
					engine.eval(sb.toString());
				} catch (ScriptException e) {
					e.printStackTrace();
					JOptionPane
							.showMessageDialog(frmMain,
									"There is an error with the script file",
									"Plugin Manager Error",
									JOptionPane.WARNING_MESSAGE);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unused")
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

	private static AbstractButton makeImageButton(AbstractButton btn, String name,
			String ext, Dimension size) {

		String res = File.separator;
		File directory = Dir.paths.get("gui");
		File file = new File(directory + res + name + ext);
		File fileSelected = new File(directory + res + name + "Selected" + ext);
		File fileHover = new File(directory + res + name + "Hover" + ext);

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

			icon = new ImageIcon(img.getScaledInstance(size.width, size.height,
					java.awt.Image.SCALE_SMOOTH));
			iconSelected = new ImageIcon(imgSelected.getScaledInstance(
					size.width, size.height, java.awt.Image.SCALE_SMOOTH));
			iconHover = new ImageIcon(imgHover.getScaledInstance(size.width,
					size.height, java.awt.Image.SCALE_SMOOTH));

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

}
