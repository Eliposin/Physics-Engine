import java.util.Random;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.Sys;

public class GameEngine {

	int fps;
	long lastFPS = 0;
	long lastFrame = getTime();

	float x = 400, y = 300;	
	float rotation = 0.0f;
	
	float locX = 800, locY = 600;
	float lastLocX = locX, lastLocY = locY;

	float red = 0.5f;
	float green = 0.5f;
	float blue = 1.0f;

	Random random = new Random();

	public void start() {

		try {
			Display.setDisplayMode(new DisplayMode(1280, 720));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		initGL();

		lastFPS = getTime(); // set lastFPS to current Time

		while (!Display.isCloseRequested()) {

			int delta = getDelta();

			update(delta);
			renderGL();

			Display.sync(120);
			Display.update();
		}
	}

	public void initGL() {

		// init opengl
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 1280, 0, 720, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

	}

	public void renderGL() {

		// Clear the screen and depth buffer
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		// set the color of the quad (R,G,B,A)
		GL11.glColor3f(red, green, blue);

		// draw quad
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 0);
		GL11.glRotatef(rotation, 0f, 0f, 1f);
		GL11.glTranslatef(-x, -y, 0);

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(x - 50, y - 50);
		GL11.glVertex2f(x + 50, y - 50);
		GL11.glVertex2f(x + 50, y + 50);
		GL11.glVertex2f(x - 50, y + 50);
		GL11.glEnd();

		GL11.glBegin(GL11.GL_LINE_LOOP);
		GL11.glVertex2f(locX - 100, locY - 100);
		GL11.glVertex2f(locX + 100, locY - 100);
		GL11.glVertex2f(locX + 100, locY + 100);
		GL11.glVertex2f(locX - 100, locY + 100);
		GL11.glEnd();
		GL11.glPopMatrix();

	}

	public void update(int delta) {

		//rotation += 0.3f * delta;
		
		//locX = Physics.deltaX(delta, 0.0f, 0.2f, -1f, locX);
		//locY = Physics.deltaY(0.0f, 0.2f, -1f, locY);
		
		

		if (Mouse.isButtonDown(0)) {
			int mouseX = Mouse.getX();
			int mouseY = Mouse.getY();

			System.out.println("MOUSE DOWN @ X: " + mouseX + " Y: " + mouseY);
			locX = mouseX;
			locY = mouseY;
		}
		
		

		while (Keyboard.next()) {

			if (Keyboard.getEventKeyState()) {

				if (Keyboard.getEventKey() == Keyboard.KEY_SPACE) {

					System.out.println("Space Key Pressed");
					blue = random.nextFloat();
					red = random.nextFloat();
					green = random.nextFloat();
				}
			} else {

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

		if (x > 1230) {
			x = 1230;
		}
		if (x < 50) {
			x = 50;
		}
		if (y > 670) {
			y = 670;
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
