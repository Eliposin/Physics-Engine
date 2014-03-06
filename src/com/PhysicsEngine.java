package com;

import java.io.IOException;


public class PhysicsEngine implements Runnable {
	
	Thread t = new Thread(this, "GUI");
	
	PhysicsEngine () {
		t.start();
	}

	public static void main(String args[]) {

		new PhysicsEngine();
	    
//	    DebugStats debugStats = new DebugStats();
//		debugStats.start();
		
	}
	
	public void run() {
		
		Engine gameEngine = new Engine();
		try {
			gameEngine.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}
