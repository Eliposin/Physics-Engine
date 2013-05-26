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

	public void start() {
		
		try {
			Display.setDisplayMode(new DisplayMode(1280, 720));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		initGL();
		
		lastFPS = getTime(); //set lastFPS to current Time

		while (!Display.isCloseRequested()) {

			int delta = getDelta();
			
			update(delta);
			renderGL();
		    
		    Display.sync(120);
			Display.update();
		}
	}
	
	public void initGL() {
		
		//init opengl
				GL11.glMatrixMode(GL11.GL_PROJECTION);
				GL11.glLoadIdentity();
				GL11.glOrtho(0, 800, 0, 600, 1, -1);
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
	}
	
	public void renderGL() {
		
		// Clear the screen and depth buffer
	    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);	
		
	    // set the color of the quad (R,G,B,A)
	    GL11.glColor3f(0.5f,0.5f,1.0f);
	    	
	    // draw quad
	    GL11.glBegin(GL11.GL_QUADS);
	        GL11.glVertex2f(100,100);
			GL11.glVertex2f(100+200,100);
			GL11.glVertex2f(100+200,100+200);
			GL11.glVertex2f(100,100+200);
	    GL11.glEnd();
		
	}
	
	public void update(int delta) {
		
		updateFPS();
		pollInput();
		
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
	        fps = 0; //reset the FPS counter
	        lastFPS += 1000; //add 1 second
	    }
	    fps++;
	}
	
	public void pollInput() {

		if (Mouse.isButtonDown(0)) {
			int x = Mouse.getX();
			int y = Mouse.getY();

			System.out.println("MOUSE DOWN @ X: " + x + " Y: " + y);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			System.out.println("SPACE KEY IS DOWN");
		}

		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				if (Keyboard.getEventKey() == Keyboard.KEY_A) {
					System.out.println("A Key Pressed");
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_S) {
					System.out.println("S Key Pressed");
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_D) {
					System.out.println("D Key Pressed");
				}
			} else {
				if (Keyboard.getEventKey() == Keyboard.KEY_A) {
					System.out.println("A Key Released");
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_S) {
					System.out.println("S Key Released");
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_D) {
					System.out.println("D Key Released");
				}
			}
		}
	}
}
