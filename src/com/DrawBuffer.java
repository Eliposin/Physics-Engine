package com;

import java.util.ArrayList;


/*	Christopher Dombroski
 * 	12/2/2013
 * 	
 * 	Hold all the drawing information for the game engine to read.
 * 	All the data is to be held in the following format
 * 	
 * 	{0}{display type, 0, 0}
 * 	{1}{red, green, blue}
 * 	{2-10,000}{x, y, z}
 */

public class DrawBuffer {
	
	static ArrayList<float[][]> drawBuffer = new ArrayList<float[][]>();
	static ArrayList<Integer> idBuffer = new ArrayList<Integer>();
	
	public static void add(float[][] shape, int id) {
		// Add an element to the drawBuffer and idBuffer.
		
		drawBuffer.add(shape);
		idBuffer.add(id);
	}
	
	public static void add(float[][] shape) {
		// Add an element to the drawBuffer and idBuffer.
		
		drawBuffer.add(shape);
		idBuffer.add(0);
	}

	public static void remove(int id) {
		//remove an item based on its id.
		
		int index = idBuffer.indexOf(id);
		drawBuffer.remove(index);
		idBuffer.remove(index);
		
	}
	
	public static void clear() {
		drawBuffer.clear();
		idBuffer.clear();
	}
	
}
