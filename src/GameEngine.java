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
	long lastFrame;

	float x = 400, y = 300;
	float rotation = 0.0f;

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
		GL11.glColor3f(0.5f, 0.5f, 1.0f);

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
		GL11.glPopMatrix();

	}

	public void update(int delta) {

		rotation += 0.3f * delta;

		if (Mouse.isButtonDown(0)) {
			int mouseX = Mouse.getX();
			int mouseY = Mouse.getY();

			System.out.println("MOUSE DOWN @ X: " + mouseX + " Y: " + mouseY);
			x = mouseX;
			y = mouseY;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			System.out.println("SPACE KEY IS DOWN");
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			System.out.println("W Key Pressed");
			y += 1 * delta;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			System.out.println("A Key Pressed");
			x -= 1 * delta;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			System.out.println("S Key Pressed");
			y -= 1 * delta;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			System.out.println("D Key Pressed");
			x += 1 * delta;
		}
		
		if (x > 1279) {
			x = 1279;
		}
		if (x < 0) {
			x = 0;
		}
		if (y > 719) {
			y = 719;
		}
		if (y < 0) {
			y = 0;
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
