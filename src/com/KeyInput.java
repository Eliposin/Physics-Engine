package com;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class KeyInput implements Runnable {

	public static int forward = Keyboard.KEY_W;
	public static int backward = Keyboard.KEY_S;
	public static int left = Keyboard.KEY_A;
	public static int right = Keyboard.KEY_D;
	public static int accept = Keyboard.KEY_RETURN;
	public static int decline = Keyboard.KEY_PRIOR;
	public static int modify = Keyboard.KEY_LSHIFT;
	public static int debug = Keyboard.KEY_E;
	public static int grid = Keyboard.KEY_G;

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
	public static boolean gridDown;
	
	boolean close = false;
	
	
	public void close() {
		
		close = true;
		
	}

	public void run() {

		while (close == false) {

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
					if (Keyboard.getEventKey() == accept) {
						acceptDown = true;
					}
					if (Keyboard.getEventKey() == decline) {
						declineDown = true;
					}
					if (Keyboard.getEventKey() == modify) {
						modifyDown = true;
					}
					if (Keyboard.getEventKey() == debug) {
						debugDown = true;
					}
					if (Keyboard.getEventKey() == grid) {
						gridDown = true;
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
					if (Keyboard.getEventKey() == accept) {
						acceptDown = true;
					}
					if (Keyboard.getEventKey() == decline) {
						declineDown = true;
					}
					if (Keyboard.getEventKey() == modify) {
						modifyDown = true;
					}
					if (Keyboard.getEventKey() == debug) {
						debugDown = true;
					}
					if (Keyboard.getEventKey() == grid) {
						gridDown = true;
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
}
