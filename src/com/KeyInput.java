package com;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class KeyInput {

	public final static byte PRESSED = 1;
	public final static byte RELEASED = -1;
	public final static byte NO_CHANGE = 0;
	
	// List of all the possible keys.
	// TODO Create a method to parse through a file for saving custom controls.
	public int forward = Keyboard.KEY_W;
	public int backward = Keyboard.KEY_S;
	public int left = Keyboard.KEY_A;
	public int right = Keyboard.KEY_D;
	public int accept = Keyboard.KEY_RETURN;
	public int decline = Keyboard.KEY_PRIOR;
	public int modify = Keyboard.KEY_LSHIFT;
	public int debug = Keyboard.KEY_E;
	public int grid = Keyboard.KEY_G;
	public int settings = Keyboard.KEY_T;

	public static int mouseX, mouseY; // Mouse location.

	// Whether or not the keys are depressed.
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
	public static boolean settingsDown;

	public static byte mouse0Changed;
	public static byte mouse1Changed;
	public static byte forwardChanged;
	public static byte backwardChanged;
	public static byte leftChanged;
	public static byte rightChanged;
	public static byte acceptChanged;
	public static byte declineChanged;
	public static byte modifyChanged;
	public static byte debugChanged;
	public static byte gridChanged;
	public static byte settingsChanged;

	// Update the key's status
	public void refresh() {

		while (Keyboard.next()) {

			mouseX = Mouse.getX();
			mouseY = Mouse.getY();

			if (Mouse.isButtonDown(0)) {
				if (mouse0Down = false) {
					mouse0Changed = PRESSED;
				} else {
					mouse0Changed = NO_CHANGE;
				}
				mouse0Down = true;
			} else {
				if (mouse0Down = true) {
					mouse0Changed = RELEASED;
				} else {
					mouse0Changed = NO_CHANGE;
				}
				mouse0Down = false;
			}
			if (Mouse.isButtonDown(1)) {
				if (mouse1Down = false) {
					mouse1Changed = PRESSED;
				} else {
					mouse1Changed = NO_CHANGE;
				}
				mouse1Down = true;
			} else {
				if (mouse1Down = true) {
					mouse1Changed = RELEASED;
				} else {
					mouse1Changed = NO_CHANGE;
				}
				mouse1Down = false;
			}

			if (Keyboard.getEventKeyState()) {
				if (Keyboard.getEventKey() == forward) {
					if (forwardDown = false) {
						forwardChanged = PRESSED;
					} else {
						forwardChanged = NO_CHANGE;
					}
					forwardDown = true;
				}
				if (Keyboard.getEventKey() == backward) {
					if (backwardDown = false) {
						backwardChanged = PRESSED;
					} else {
						backwardChanged = NO_CHANGE;
					}
					backwardDown = true;
				}
				if (Keyboard.getEventKey() == left) {
					if (leftDown = false) {
						leftChanged = PRESSED;
					} else {
						leftChanged = NO_CHANGE;
					}
					leftDown = true;
				}
				if (Keyboard.getEventKey() == right) {
					if (rightDown = false) {
						rightChanged = PRESSED;
					} else {
						rightChanged = NO_CHANGE;
					}
					rightDown = true;
				}
				if (Keyboard.getEventKey() == accept) {
					if (acceptDown = false) {
						acceptChanged = PRESSED;
					} else {
						acceptChanged = NO_CHANGE;
					}
					acceptDown = true;
				}
				if (Keyboard.getEventKey() == decline) {
					if (declineDown = false) {
						declineChanged = PRESSED;
					} else {
						declineChanged = NO_CHANGE;
					}
					declineDown = true;
				}
				if (Keyboard.getEventKey() == modify) {
					if (modifyDown = false) {
						modifyChanged = PRESSED;
					} else {
						modifyChanged = NO_CHANGE;
					}
					modifyDown = true;
				}
				if (Keyboard.getEventKey() == debug) {
					if (debugDown = false) {
						debugChanged = PRESSED;
					} else {
						debugChanged = NO_CHANGE;
					}
					debugDown = true;
				}
				if (Keyboard.getEventKey() == grid) {
					if (gridDown = false) {
						gridChanged = PRESSED;
					} else {
						gridChanged = NO_CHANGE;
					}
					gridDown = true;
				}
				if (Keyboard.getEventKey() == settings) {
					if (settingsDown = false) {
						settingsChanged = PRESSED;
					} else {
						settingsChanged = NO_CHANGE;
					}
					settingsDown = true;
				}
			} else {
				if (Keyboard.getEventKey() == forward) {
					if (forwardDown = true) {
						forwardChanged = RELEASED;
					} else {
						forwardChanged = NO_CHANGE;
					}
					forwardDown = false;
				}
				if (Keyboard.getEventKey() == backward) {
					if (backwardDown = true) {
						backwardChanged = RELEASED;
					} else {
						backwardChanged = NO_CHANGE;
					}
					backwardDown = false;
				}
				if (Keyboard.getEventKey() == left) {
					if (leftDown = true) {
						leftChanged = RELEASED;
					} else {
						leftChanged = NO_CHANGE;
					}
					leftDown = false;
				}
				if (Keyboard.getEventKey() == right) {
					if (rightDown = true) {
						rightChanged = RELEASED;
					} else {
						rightChanged = NO_CHANGE;
					}
					rightDown = false;
				}
				if (Keyboard.getEventKey() == accept) {
					if (acceptDown = true) {
						acceptChanged = RELEASED;
					} else {
						acceptChanged = NO_CHANGE;
					}
					acceptDown = false;
				}
				if (Keyboard.getEventKey() == decline) {
					if (declineDown = true) {
						declineChanged = RELEASED;
					} else {
						declineChanged = NO_CHANGE;
					}
					declineDown = false;
				}
				if (Keyboard.getEventKey() == modify) {
					if (modifyDown = true) {
						modifyChanged = RELEASED;
					} else {
						modifyChanged = NO_CHANGE;
					}
					modifyDown = false;
				}
				if (Keyboard.getEventKey() == debug) {
					if (debugDown = true) {
						debugChanged = RELEASED;
					} else {
						debugChanged = NO_CHANGE;
					}
					debugDown = false;
				}
				if (Keyboard.getEventKey() == grid) {
					if (gridDown = true) {
						gridChanged = RELEASED;
					} else {
						gridChanged = NO_CHANGE;
					}
					gridDown = false;
				}
				if (Keyboard.getEventKey() == settings) {
					if (settingsDown = true) {
						settingsChanged = RELEASED;
					} else {
						settingsChanged = NO_CHANGE;
					}
					settingsDown = false;
				}
			}
		}
	}
}
