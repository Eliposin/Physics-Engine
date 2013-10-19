package com;

import java.io.IOException;


public class Criminality implements Runnable {
	
	Thread t = new Thread(this, "GUI");
	
	Criminality () {
		t.start();
	}

	public static void main(String argv[]) {

		new Criminality();
	    
//	    DebugStats debugStats = new DebugStats();
//		debugStats.start();
		
	}
	
	public void run() {
		
		GameEngine gameEngine = new GameEngine();
		try {
			gameEngine.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}
