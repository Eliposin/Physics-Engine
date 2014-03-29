package com;

import java.util.HashMap;

public class Profiler {
	
	static HashMap<String, Long> profile = new HashMap<String, Long>();
	
	/**
	 * Call this at the beginning and end of an operation. It signals the 
	 * profiler to subtract the current time from the buffered time. It must 
	 * ALWAYS be called in pairs of start and end.
	 * @param name The name of the operation
	 */
	public static void prof(String name) {
		
		if (profile.containsKey(name)) {
			profile.put(name, Engine.getTime() - profile.get(name));
		} else {
			profile.put(name, Engine.getTime());
		}
	}

	public static String getString() {
		
		return profile.toString();
	}
	
	/**
	 * Clear the profile buffer. Call this every frame.
	 */
	public static void clear() {
		
		profile.clear();
		
	}

}
