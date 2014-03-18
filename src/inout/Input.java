package inout;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 * Get all the keyboard and mouse data. It can tell if a key state has 
 * changed, what its current state is now. It can read and write from a key 
 * mapping file and is fully configurable. 
 * @author Christopher Dombroski
 *
 */
public class Input {

	public final static byte PRESSED = 1;
	public final static byte RELEASED = -1;
	public final static byte NO_CHANGE = 0;
	
	// List of all the possible keys.
	// TODO Create a method to parse through a file for saving custom controls.
	public static int forward = Keyboard.KEY_W;
	public static int backward = Keyboard.KEY_S;
	public static int left = Keyboard.KEY_A;
	public static int right = Keyboard.KEY_D;
	public static int action = Keyboard.KEY_SPACE;
	public static int accept = Keyboard.KEY_RETURN;
	public static int decline = Keyboard.KEY_PRIOR;
	public static int modify = Keyboard.KEY_LSHIFT;
	public static int debug = Keyboard.KEY_E;
	public static int grid = Keyboard.KEY_G;
	public static int settings = Keyboard.KEY_T;

	public static int mouseX, mouseY; // Mouse location.

	// Whether or not the keys are depressed.
	public static boolean mouse0Down;
	public static boolean mouse1Down;
	public static boolean mouse2Down;
	public static boolean forwardDown;
	public static boolean backwardDown;
	public static boolean leftDown;
	public static boolean rightDown;
	public static boolean actionDown;
	public static boolean acceptDown;
	public static boolean declineDown;
	public static boolean modifyDown;
	public static boolean debugDown;
	public static boolean gridDown;
	public static boolean settingsDown;

	public static byte mouse0Changed;
	public static byte mouse1Changed;
	public static byte mouse2Changed;
	public static byte forwardChanged;
	public static byte backwardChanged;
	public static byte leftChanged;
	public static byte rightChanged;
	public static byte actionChanged;
	public static byte acceptChanged;
	public static byte declineChanged;
	public static byte modifyChanged;
	public static byte debugChanged;
	public static byte gridChanged;
	public static byte settingsChanged;

	/**
	 *  Update the key and mouse status.
	 */
	public static void refresh() {
		
		mouseX = Mouse.getX();
		mouseY = Mouse.getY();
		
		forwardChanged = NO_CHANGE;
		backwardChanged = NO_CHANGE;
		leftChanged = NO_CHANGE;
		rightChanged = NO_CHANGE;
		actionChanged = NO_CHANGE;
		acceptChanged = NO_CHANGE;
		declineChanged = NO_CHANGE;
		modifyChanged = NO_CHANGE;
		debugChanged = NO_CHANGE;
		gridChanged = NO_CHANGE;
		settingsChanged = NO_CHANGE;

		if (Mouse.isButtonDown(0)) {
			if (mouse0Down == false) {
				mouse0Changed = PRESSED;
			} else {
				mouse0Changed = NO_CHANGE;
			}
			mouse0Down = true;
		} else {
			if (mouse0Down == true) {
				mouse0Changed = RELEASED;
			} else {
				mouse0Changed = NO_CHANGE;
			}
			mouse0Down = false;
		}
		if (Mouse.isButtonDown(1)) {
			if (mouse1Down == false) {
				mouse1Changed = PRESSED;
			} else {
				mouse1Changed = NO_CHANGE;
			}
			mouse1Down = true;
		} else {
			if (mouse1Down == true) {
				mouse1Changed = RELEASED;
			} else {
				mouse1Changed = NO_CHANGE;
			}
			mouse1Down = false;
		}
		if (Mouse.isButtonDown(2)) {
			if (mouse2Down == false) {
				mouse2Changed = PRESSED;
			} else {
				mouse2Changed = NO_CHANGE;
			}
			mouse2Down = true;
		} else {
			if (mouse2Down == true) {
				mouse2Changed = RELEASED;
			} else {
				mouse2Changed = NO_CHANGE;
			}
			mouse2Down = false;
		}
		
		while (Keyboard.next()) {

			if (Keyboard.getEventKeyState()) {
				if (Keyboard.getEventKey() == forward) {
					if (forwardDown == false) {
						forwardChanged = PRESSED;
					} else {
						forwardChanged = NO_CHANGE;
					}
					forwardDown = true;
					System.out.println("forwardChanged = " + forwardChanged);
				}
				if (Keyboard.getEventKey() == backward) {
					if (backwardDown == false) {
						backwardChanged = PRESSED;
					} else {
						backwardChanged = NO_CHANGE;
					}
					backwardDown = true;
				}
				if (Keyboard.getEventKey() == left) {
					if (leftDown == false) {
						leftChanged = PRESSED;
					} else {
						leftChanged = NO_CHANGE;
					}
					leftDown = true;
				}
				if (Keyboard.getEventKey() == right) {
					if (rightDown == false) {
						rightChanged = PRESSED;
					} else {
						rightChanged = NO_CHANGE;
					}
					rightDown = true;
				}
				if (Keyboard.getEventKey() == action) {
					if (actionDown == false) {
						actionChanged = PRESSED;
					} else {
						actionChanged = NO_CHANGE;
					}
					actionDown = true;
				}
				if (Keyboard.getEventKey() == accept) {
					if (acceptDown == false) {
						acceptChanged = PRESSED;
					} else {
						acceptChanged = NO_CHANGE;
					}
					acceptDown = true;
				}
				if (Keyboard.getEventKey() == decline) {
					if (declineDown == false) {
						declineChanged = PRESSED;
					} else {
						declineChanged = NO_CHANGE;
					}
					declineDown = true;
				}
				if (Keyboard.getEventKey() == modify) {
					if (modifyDown == false) {
						modifyChanged = PRESSED;
					} else {
						modifyChanged = NO_CHANGE;
					}
					modifyDown = true;
				}
				if (Keyboard.getEventKey() == debug) {
					if (debugDown == false) {
						debugChanged = PRESSED;
					} else {
						debugChanged = NO_CHANGE;
					}
					debugDown = true;
				}
				if (Keyboard.getEventKey() == grid) {
					if (gridDown == false) {
						gridChanged = PRESSED;
					} else {
						gridChanged = NO_CHANGE;
					}
					gridDown = true;
				}
				if (Keyboard.getEventKey() == settings) {
					if (settingsDown == false) {
						settingsChanged = PRESSED;
					} else {
						settingsChanged = NO_CHANGE;
					}
					settingsDown = true;
				}
			} else {
				if (Keyboard.getEventKey() == forward) {
					if (forwardDown == true) {
						forwardChanged = RELEASED;
					} else {
						forwardChanged = NO_CHANGE;
					}
					forwardDown = false;
					System.out.println("forwardChanged = " + forwardChanged);
				}
				if (Keyboard.getEventKey() == backward) {
					if (backwardDown == true) {
						backwardChanged = RELEASED;
					} else {
						backwardChanged = NO_CHANGE;
					}
					backwardDown = false;
				}
				if (Keyboard.getEventKey() == left) {
					if (leftDown == true) {
						leftChanged = RELEASED;
					} else {
						leftChanged = NO_CHANGE;
					}
					leftDown = false;
				}
				if (Keyboard.getEventKey() == right) {
					if (rightDown == true) {
						rightChanged = RELEASED;
					} else {
						rightChanged = NO_CHANGE;
					}
					rightDown = false;
				}
				if (Keyboard.getEventKey() == action) {
					if (actionDown == true) {
						actionChanged = RELEASED;
					} else {
						actionChanged = NO_CHANGE;
					}
					actionDown = false;
				}
				if (Keyboard.getEventKey() == accept) {
					if (acceptDown == true) {
						acceptChanged = RELEASED;
					} else {
						acceptChanged = NO_CHANGE;
					}
					acceptDown = false;
				}
				if (Keyboard.getEventKey() == decline) {
					if (declineDown == true) {
						declineChanged = RELEASED;
					} else {
						declineChanged = NO_CHANGE;
					}
					declineDown = false;
				}
				if (Keyboard.getEventKey() == modify) {
					if (modifyDown == true) {
						modifyChanged = RELEASED;
					} else {
						modifyChanged = NO_CHANGE;
					}
					modifyDown = false;
				}
				if (Keyboard.getEventKey() == debug) {
					if (debugDown == true) {
						debugChanged = RELEASED;
					} else {
						debugChanged = NO_CHANGE;
					}
					debugDown = false;
				}
				if (Keyboard.getEventKey() == grid) {
					if (gridDown == true) {
						gridChanged = RELEASED;
					} else {
						gridChanged = NO_CHANGE;
					}
					gridDown = false;
				}
				if (Keyboard.getEventKey() == settings) {
					if (settingsDown == true) {
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
