package com;
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
import gui.Button;


import physics.ComplexPhys;

public class GameEngine {
	
	int frame = 0;
	int delta;
	
	public static int height = 800;
	public static int width = 1200;

	int fps;
	int setFPS = 120;
	long lastFPS = 0;
	long lastFrame = getTime();
	boolean debugToggle = true;

	float x = 400, y = 300;

	public static float red = 0.5f;
	public static float green = 0.5f;
	public static float blue = 1.0f;
	
	float[] location = new float[3];
	float[] location2 = new float[3];
	float[] vector = new float[3];
	float[] vector2 = new float[3];
	float speed;
	
	int[] graphData = new int[width];
	
	float[] gravity = {0, -9.8f, 0};
	float[] gravity2 = {0, -9.8f, 0};
	float[] attr = {1000, 0f, 1f};
	
	Logger boxLogger;
	Logger squareLogger;
	int trailLength = 250;
	Trail trail = new Trail(trailLength);
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
	
	public void initialise() throws IOException {
		initGL();
//		GLButton button = new GLButton(KeyInput.mouseX, KeyInput.mouseY);
//		button.initGL();
		
//		float[] attr = new float[3];
//		attr[0] = 1000;
//		attr[1] = 1.15f;
//		attr[2] = 1;
		
		(new Thread(keyInput)).start();
		
//		attributes = new PhysAttributes(location, attr, gravity);
//		attributes2 = new PhysAttributes(location2, attr, gravity2);
		
		ComplexPhys.addPhysics("Box", attr[0], attr[1], attr[2]);
		ComplexPhys.addPhysics("Square", attr[0], attr[1], attr[2]);
//		ComplexPhys.enablePhysics("Square", false);
		
		boxLogger = new Logger("Box");
		squareLogger = new Logger("Square");
		
	}
	
	public void close() {
		
		try {
			boxLogger.close();
			squareLogger.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		keyInput.close();
		
		System.out.println("closed");
		
	}

	public void initGL() {

		// init opengl
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, width, 0, height, 100, -100);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

	}

	public void renderGL() {

		// Clear the screen and depth buffer
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		GL11.glColor3f(red, green, blue);
		

//		 draw quad
//		GL11.glPushMatrix();
//		GL11.glTranslatef(x, y, 0);
//		GL11.glRotatef(rotation, 0f, 0f, 1f);
//		GL11.glTranslatef(-x, -y, 0);
		
		//draw a grid every 10 pixels
//		GL11.glBegin(GL11.GL_LINES);
//		for (int i = 0; i < height / 100; i++) {
//			GL11.glVertex2f(0, i * 100);
//			GL11.glVertex2f(width, i * 100);
//		}
//		GL11.glEnd();
//		
//		GL11.glBegin(GL11.GL_LINES);
//		for (int i = 0; i < width / 100; i++) {
//			GL11.glVertex2f(i * 100, 0);
//			GL11.glVertex2f(i * 100, height);
//		}
//		GL11.glEnd();

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(x - 50, y - 50);
		GL11.glVertex2f(x + 50, y - 50);
		GL11.glVertex2f(x + 50, y + 50);
		GL11.glVertex2f(x - 50, y + 50);
		GL11.glEnd();

		GL11.glBegin(GL11.GL_LINE_LOOP);
		GL11.glVertex2f(location[0] - 100, location[1] - 100);
		GL11.glVertex2f(location[0] + 100, location[1] - 100);
		GL11.glVertex2f(location[0] + 100, location[1] + 100);
		GL11.glVertex2f(location[0] - 100, location[1] + 100);
		GL11.glEnd();
		
		GL11.glBegin(GL11.GL_POLYGON);
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
		
		
//		Numbers
//		int place = 0;
//		int x = 20, y = 500;
//		switch (3) {
//		
//		case 0: GL11.glBegin(GL11.GL_QUADS);
//		
//				GL11.glVertex2f(2 + x + (12 * place),0 + y);
//				GL11.glVertex2f(8 + x + (12 * place),0 + y);
//				GL11.glVertex2f(8 + x + (12 * place),2 + y);
//				GL11.glVertex2f(2 + x + (12 * place),2 + y);
//				
//				GL11.glVertex2f(8 + x + (12 * place),2 + y);
//				GL11.glVertex2f(10 + x + (12 * place),2 + y);
//				GL11.glVertex2f(10 + x + (12 * place),12 + y);
//				GL11.glVertex2f(8 + x + (12 * place),12 + y);
//				
//				GL11.glVertex2f(2 + x + (12 * place),12 + y);
//				GL11.glVertex2f(8 + x + (12 * place),12 + y);
//				GL11.glVertex2f(8 + x + (12 * place),14 + y);
//				GL11.glVertex2f(2 + x + (12 * place),14 + y);
//				
//				GL11.glVertex2f(0 + x + (12 * place),2 + y);
//				GL11.glVertex2f(2 + x + (12 * place),2 + y);
//				GL11.glVertex2f(2 + x + (12 * place),12 + y);
//				GL11.glVertex2f(0 + x + (12 * place),12 + y);
//				
//				GL11.glVertex2f(2 + x + (12 * place),4 + y);
//				GL11.glVertex2f(4 + x + (12 * place),4 + y);
//				GL11.glVertex2f(4 + x + (12 * place),6 + y);
//				GL11.glVertex2f(2 + x + (12 * place),6 + y);
//				
//				GL11.glVertex2f(4 + x + (12 * place),6 + y);
//				GL11.glVertex2f(6 + x + (12 * place),6 + y);
//				GL11.glVertex2f(6 + x + (12 * place),8 + y);
//				GL11.glVertex2f(4 + x + (12 * place),8 + y);
//				
//				GL11.glVertex2f(6 + x + (12 * place),8 + y);
//				GL11.glVertex2f(8 + x + (12 * place),8 + y);
//				GL11.glVertex2f(8 + x + (12 * place),10 + y);
//				GL11.glVertex2f(6 + x + (12 * place),10 + y);
//				
//				GL11.glEnd();
//				break;
//		
//		case 1:	GL11.glBegin(GL11.GL_QUADS);
//		
//				GL11.glVertex2f(0 + x + (12 * place),0 + y);
//				GL11.glVertex2f(10 + x + (12 * place),0 + y);
//				GL11.glVertex2f(10 + x + (12 * place),2 + y);
//				GL11.glVertex2f(0 + x + (12 * place),2 + y);
//				
//				GL11.glVertex2f(6 + x + (12 * place),2 + y);
//				GL11.glVertex2f(6 + x + (12 * place),14 + y);
//				GL11.glVertex2f(4 + x + (12 * place),14 + y);
//				GL11.glVertex2f(4 + x + (12 * place),2 + y);
//				
//				GL11.glVertex2f(4 + x + (12 * place),12 + y);
//				GL11.glVertex2f(2 + x + (12 * place),12 + y);
//				GL11.glVertex2f(2 + x + (12 * place),10 + y);
//				GL11.glVertex2f(4 + x + (12 * place),10 + y);
//				
//				GL11.glEnd();
//				break;
//				
//		case 2: GL11.glBegin(GL11.GL_QUADS);
//		
//				GL11.glVertex2f(0 + x + (12 * place),0 + y);
//				GL11.glVertex2f(10 + x + (12 * place),0 + y);
//				GL11.glVertex2f(10 + x + (12 * place),2 + y);
//				GL11.glVertex2f(0 + x + (12 * place),2 + y);
//		
//				GL11.glVertex2f(0 + x + (12 * place),2 + y);
//				GL11.glVertex2f(2 + x + (12 * place),2 + y);
//				GL11.glVertex2f(2 + x + (12 * place),4 + y);
//				GL11.glVertex2f(0 + x + (12 * place),4 + y);
//				
//				GL11.glVertex2f(2 + x + (12 * place),4 + y);
//				GL11.glVertex2f(4 + x + (12 * place),4 + y);
//				GL11.glVertex2f(4 + x + (12 * place),6 + y);
//				GL11.glVertex2f(2 + x + (12 * place),6 + y);
//				
//				GL11.glVertex2f(4 + x + (12 * place),6 + y);
//				GL11.glVertex2f(8 + x + (12 * place),6 + y);
//				GL11.glVertex2f(8 + x + (12 * place),8 + y);
//				GL11.glVertex2f(4 + x + (12 * place),8 + y);
//				
//				GL11.glVertex2f(8 + x + (12 * place),8 + y);
//				GL11.glVertex2f(10 + x + (12 * place),8 + y);
//				GL11.glVertex2f(10 + x + (12 * place),12 + y);
//				GL11.glVertex2f(8 + x + (12 * place),12 + y);
//				
//				GL11.glVertex2f(2 + x + (12 * place),12 + y);
//				GL11.glVertex2f(8 + x + (12 * place),12 + y);
//				GL11.glVertex2f(8 + x + (12 * place),14 + y);
//				GL11.glVertex2f(2 + x + (12 * place),14 + y);
//				
//				GL11.glVertex2f(0 + x + (12 * place),10 + y);
//				GL11.glVertex2f(2 + x + (12 * place),10 + y);
//				GL11.glVertex2f(2 + x + (12 * place),12 + y);
//				GL11.glVertex2f(0 + x + (12 * place),12 + y);
//				
//				GL11.glEnd();
//				break;
//				
//		case 3: GL11.glBegin(GL11.GL_QUADS);
//		
//				GL11.glVertex2f(2 + x + (12 * place),0 + y);
//				GL11.glVertex2f(8 + x + (12 * place),0 + y);
//				GL11.glVertex2f(8 + x + (12 * place),2 + y);
//				GL11.glVertex2f(2 + x + (12 * place),2 + y);
//				
//				GL11.glVertex2f(8 + x + (12 * place),2 + y);
//				GL11.glVertex2f(10 + x + (12 * place),2 + y);
//				GL11.glVertex2f(10 + x + (12 * place),6 + y);
//				GL11.glVertex2f(8 + x + (12 * place),6 + y);
//				
//				GL11.glVertex2f(4 + x + (12 * place),6 + y);
//				GL11.glVertex2f(8 + x + (12 * place),6 + y);
//				GL11.glVertex2f(8 + x + (12 * place),8 + y);
//				GL11.glVertex2f(4 + x + (12 * place),8 + y);
//				
//				GL11.glVertex2f(8 + x + (12 * place),8 + y);
//				GL11.glVertex2f(10 + x + (12 * place),8 + y);
//				GL11.glVertex2f(10 + x + (12 * place),12 + y);
//				GL11.glVertex2f(8 + x + (12 * place),12 + y);
//				
//				GL11.glVertex2f(2 + x + (12 * place),12 + y);
//				GL11.glVertex2f(8 + x + (12 * place),12 + y);
//				GL11.glVertex2f(8 + x + (12 * place),14 + y);
//				GL11.glVertex2f(2 + x + (12 * place),14 + y);
//				
//				GL11.glVertex2f(0 + x + (12 * place),10 + y);
//				GL11.glVertex2f(2 + x + (12 * place),10 + y);
//				GL11.glVertex2f(2 + x + (12 * place),12 + y);
//				GL11.glVertex2f(0 + x + (12 * place),12 + y);
//				
//				GL11.glVertex2f(0 + x + (12 * place),2 + y);
//				GL11.glVertex2f(2 + x + (12 * place),2 + y);
//				GL11.glVertex2f(2 + x + (12 * place),4 + y);
//				GL11.glVertex2f(0 + x + (12 * place),4 + y);
//				
//				GL11.glEnd();
//				break;
//		case 4:
//		case 5:
//		case 6:
//		case 7:
//		case 8:
//		case 9:
//		}
		
		if (debugToggle == true) {
			
			GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex3f(100, 100, 100);
			GL11.glVertex3f(vector[0] + 100, vector[1] + 100, vector[2] + 100);
			GL11.glEnd();
			
			GL11.glBegin(GL11.GL_LINE_LOOP);
			GL11.glVertex2f(0, 0);
			GL11.glVertex2f(width - 1, 0);
			GL11.glVertex2f(width - 1, height - 1);
			GL11.glVertex2f(0, height - 1);
			GL11.glEnd();
			
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
		
		button.run();
		
		ComplexPhys.UpdatePhysics(delta);
		location = ComplexPhys.getLocation("Box");
		location2 = ComplexPhys.getLocation("Square");
		vector = ComplexPhys.getPhysObject("Box").getVelocity();
		vector2 = ComplexPhys.getPhysObject("Square").getVelocity();
		
		
		boxLogger.LogLine(ComplexPhys.getLocation("Box"));
		squareLogger.LogLine(ComplexPhys.getLocation("Square"));
		trail.updateTrail(location);
		
		float boxRebound = 2 * ComplexPhys.getPhysObject("Box").restitution;
		if (location[0] < 100) {
			vector[0] *= -1;
			location[0] += boxRebound * vector[0];
		} else if ( location[0] > width - 100) {
			vector[0] *= -1;
			location[0] += boxRebound * vector[0];
		}
		
		if (location[1] < 100) {
			vector[1] *= -1;
			location[1] += boxRebound * vector[1];
		} else if (location[1] > height - 100) {
			vector[1] *= -1;
			location[1] += boxRebound * vector[1];
		}
		
		float squareRebound = 2f * ComplexPhys.getPhysObject("Square").restitution;
		if (location2[0] < 25) {
			vector2[0] *= -1;
			location2[0] += squareRebound * vector2[0];
		} else if ( location2[0] > width - 25) {
			vector2[0] *= -1;
			location2[0] += squareRebound * vector2[0];
		}
		
		if (location2[1] < 25) {
			vector2[1] *= -1;
			location2[1] += squareRebound * vector2[1];
		} else if (location2[1] > height - 25) {
			vector2[1] *= -1;
			location2[1] += squareRebound * vector2[1];
		}
		
		if (debugToggle == true) {
			
		}
		
//		System.out.println("delta is: " + delta);

		if (Mouse.isButtonDown(0)) {
			int mouseX = Mouse.getX();
			int mouseY = Mouse.getY();

//			System.out.println("MOUSE DOWN @ X: " + mouseX + " Y: " + mouseY);
			
//			if (((location[0] - 100) < mouseX && mouseX < (location[0] + 100))
//					&& ((location[1] - 100) < mouseY && mouseY < (location[1] + 100))) {
//				
//				location[0] = mouseX;
//				location[1] = mouseY;
//				
//			}
//			
//			if (((location2[0] - 25) < mouseX && mouseX < (location2[0] + 25))
//					&& ((location2[1] - 25) < mouseY && mouseY < (location2[1] + 25))) {
//				
//				location2[0] = mouseX;
//				location2[1] = mouseY;
//				
//			}
			
			location[0] = mouseX;
			location[1] = mouseY;
			
		}
		
		if (Mouse.isButtonDown(1)) {
			int mouseX = Mouse.getX();
			int mouseY = Mouse.getY();
			
			location2[0] = mouseX;
			location2[1] = mouseY;
		}
		
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
					
//					if (debugToggle == false) {
//						debugToggle = true;
//						System.out.println("debug is ON");
//					} else {
//						debugToggle = false;
//						System.out.println("debug is OFF");
//					}
					
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

		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			System.out.println("W is Down");
			y += 1 * delta;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			System.out.println("A is Down");
			x -= 1 * delta;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			System.out.println("S is Down");
			y -= 1 * delta;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			System.out.println("D is Down");
			x += 1 * delta;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		}		

		if (x > width - 50) {
			x = width - 50;
		}
		if (x < 50) {
			x = 50;
		}
		if (y > height - 50) {
			y = height - 50;
		}
		if (y < 50) {
			y = 50;
		}

		updateFPS();
	}

	public long getTime() {

		return (Sys.getTime() * 1000) / Sys.getTimerResolution();

	}

	public int getDelta() {

		long time = getTime();
		int delta = (int) (time - lastFrame);
		lastFrame = time;

		return delta;

	}

	public void updateFPS() {
		if (getTime() - lastFPS > 1000) {
			System.out.println("FPS: " + fps);
			fps = 0; // reset the FPS counter
			lastFPS += 1000; // add 1 second
		}
		fps++;
	}
}
