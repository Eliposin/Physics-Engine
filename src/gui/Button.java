package gui;

import java.util.ArrayList;
import org.lwjgl.opengl.GL11;
import com.GameEngine;
import com.Input;
import com.DrawBuffer;
import com.Vector;

public class Button implements Runnable {

	static ArrayList<Button> buttons = new ArrayList<Button>();

	float[][] shape;
	float[] color = { 1f, 1f, 1f };
//	float[] color = {GameEngine.red, GameEngine.green, GameEngine.blue};
	float colorScale = 1f;

	public static int buttonHeight = 32;
	public static int buttonWidth = 64;
	public static int buttonPadding = 2;

	int locX = 0, locY = 0;
	boolean close = false;

	public Button(int locX, int locY) {

		this.locX = locX;
		this.locY = locY;

	}

	public void render() {
		color[0] = GameEngine.red * colorScale;
		color[1] = GameEngine.green * colorScale;
		color[2] = GameEngine.blue * colorScale;
		shape = new float[6][3];
		shape[0][0] = GL11.GL_QUADS;
		shape[1] = Vector.cScaleVector(color.clone(), colorScale);

		shape[2][0] = locX + buttonPadding;
		shape[2][1] = locY + buttonPadding;
		shape[3][0] = locX + buttonWidth - buttonPadding;
		shape[3][1] = locY + buttonPadding;
		shape[4][0] = locX + buttonWidth - buttonPadding;
		shape[4][1] = locY + buttonHeight - buttonPadding;
		shape[5][0] = locX + buttonPadding;
		shape[5][1] = locY + buttonHeight - buttonPadding;
		DrawBuffer.add(shape);

		shape = new float[5][3];
		shape[0][0] = GL11.GL_TRIANGLES;
		shape[1] = Vector.cScaleVector(color.clone(), 0.9f * colorScale);

		shape[2][0] = locX + buttonPadding;
		shape[2][1] = locY + buttonPadding;
		shape[3][0] = locX + buttonWidth - buttonPadding;
		shape[3][1] = locY + buttonPadding;
		shape[4][0] = locX + buttonWidth - buttonPadding;
		shape[4][1] = locY + buttonHeight - buttonPadding;
		DrawBuffer.add(shape);

		shape = new float[6][3];
		shape[0][0] = GL11.GL_LINE_LOOP;
		shape[1] = Vector.cScaleVector(color.clone(), 0.5f * colorScale);

		shape[2][0] = locX + buttonPadding;
		shape[2][1] = locY + buttonPadding;
		shape[3][0] = locX + buttonWidth - buttonPadding;
		shape[3][1] = locY + buttonPadding;
		shape[4][0] = locX + buttonWidth - buttonPadding;
		shape[4][1] = locY + buttonHeight - buttonPadding;
		shape[5][0] = locX + buttonPadding;
		shape[5][1] = locY + buttonHeight - buttonPadding;
		DrawBuffer.add(shape);

	}

	public void run() {

		if (locX < Input.mouseX && Input.mouseX < locX + buttonWidth
				&& locY < Input.mouseY
				&& Input.mouseY < locY + buttonHeight) {

			onHover();
			
			if (Input.mouse0Down == true) {
				onClick();
			} else {
				onRelease();
			}

		} else {
			onNothing();
		}

		render();

	}

	private void onHover() {
		buttonPadding = -1;
	}

	private void onClick() {
		colorScale = 0.8f;
	}
	
	private void onRelease() {
		colorScale = 1f;
		System.out.println("released");

	}

	private void onNothing() {
		buttonPadding = 0;
		colorScale = 1f;
	}

}
