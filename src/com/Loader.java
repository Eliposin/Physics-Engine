package com;

import java.io.*;
import java.util.ArrayList;

public class Loader {

	public static void read(String inputFileName) {
		File f = new File("scenes\\" + inputFileName + ".obj");
		BufferedReader read = null;

		try {
			String currentLine;
			FileReader reader = new FileReader(f);
			read = new BufferedReader(reader);

			while ((currentLine = read.readLine()) != null) {
				// TODO read lines in
				System.out.println(currentLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (read != null)
					read.close();
				// System.out.println("Why doth tho worketh?");
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}
	
	
	private float[] fileParser(String fileText){
		float[] output = new float[3];
		String[] holder = new String[3];
		String dataType = "";
		ArrayList<Integer> whiteSpace = new ArrayList<Integer>();
		
		fileText = fileText.trim();
		
		for (int i = 0; i < fileText.length(); i++) {
			if (Character.isWhitespace(fileText.charAt(i))) {
				whiteSpace.add(i); 
			}
		}
		
		fileText.toLowerCase().getChars(0, whiteSpace.get(0) - 1,
				dataType.toCharArray(), 0);
		//check for v, vt, o
		
		switch (dataType) {
		
			case "#":	for (int i = 0; i < whiteSpace.size() - 2; i++) {
							fileText.getChars(whiteSpace.get(i + 1) + 1,
									whiteSpace.get(i + 2) - 1,
									holder[i].toCharArray(), 0);
						}
					break;
			case "o":  //TODO
						break;
			case "v":  //TODO
						break;
			case "vt": //TODO
						break;
			case "vn": //TODO
						break;
			case "vp": //TODO
						break;
			case "f":  //TODO
						break;
			case "g":  //TODO
						break;
			case "ka": //TODO
						break;
			case "kd": //TODO
						break;
			case "ks": //TODO
						break;
			case "d":  //TODO
				
		}
		
		return output;
	}
}
