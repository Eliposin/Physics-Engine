package com;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class KeyInput implements Runnable{
	
	public static int forward = Keyboard.KEY_W;
	public static int backward = Keyboard.KEY_S;
	public static int left = Keyboard.KEY_A;
	public static int right = Keyboard.KEY_D;
	public static int accept = Keyboard.KEY_RETURN;
	public static int decline  = Keyboard.KEY_PRIOR;
	public static int modify = Keyboard.KEY_LSHIFT;
	public static int debug = Keyboard.KEY_E;
	public static int Grid = Keyboard.KEY_G;
	
	public static int mouseX, mouseY;
	
	public static boolean mouse0Down;
	public static boolean mouse1Down;
	public static boolean forwardDown;
	public static boolean backwardDown;
	public static boolean leftDown;
	public static boolean rightDown;
	public static boolean acceptDown;
	public static boolean declineDown;
	public static boolean modifyDown;
	public static boolean debugDown;
	public static boolean GridDown;

	public void run() {
		
		mouseX = Mouse.getX();
		mouseY = Mouse.getY();

		if (Mouse.isButtonDown(0)) {
			mouse0Down = true;
		} else {
			mouse0Down = false;
		}
		if (Mouse.isButtonDown(0)) {
			mouse1Down = true;
		} else {
			mouse1Down = false;
		}

//		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
//		}

		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				if (Keyboard.getEventKey() == forward) {
					forwardDown = true;
				}
				if (Keyboard.getEventKey() == backward) {
					backwardDown = true;
				}
				if (Keyboard.getEventKey() == left) {
					leftDown = true;
				}
				if (Keyboard.getEventKey() == right) {
					rightDown = true;
				}
			} else {
				if (Keyboard.getEventKey() == forward) {
					forwardDown = false;
				}
				if (Keyboard.getEventKey() == backward) {
					backwardDown = false;
				}
				if (Keyboard.getEventKey() == left) {
					leftDown = false;
				}
				if (Keyboard.getEventKey() == right) {
					rightDown = false;
				}
			}
		}
		
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
