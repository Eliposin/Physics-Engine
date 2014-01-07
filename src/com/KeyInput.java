package com;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class KeyInput {

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

	// Update the key's status
	public void refresh() {

		while (Keyboard.next()) {

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
					acceptDown = false;
				}
				if (Keyboard.getEventKey() == decline) {
					declineDown = false;
				}
				if (Keyboard.getEventKey() == modify) {
					modifyDown = false;
				}
				if (Keyboard.getEventKey() == debug) {
					debugDown = false;
				}
				if (Keyboard.getEventKey() == grid) {
					gridDown = false;
				}
			}
		}
	}
}
