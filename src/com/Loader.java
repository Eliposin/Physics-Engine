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
				fileParser(currentLine);
				// System.out.println(currentLine);
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

	private static float[] fileParser(String fileText) {

		/*
		 * Parse through a .obj file. This system needs some attention because
		 * certain formats will break it. It also currently only supports float
		 * formats, no strings of other such things.
		 */

		float[] output = new float[3];
		@SuppressWarnings("unused")
		String[] holder = new String[5];
		String dataType = "";
		char[] charHolder = new char[100];
		ArrayList<Integer> whiteSpace = new ArrayList<Integer>();

		fileText = fileText.trim();
		fileText += " ";

		for (int i = 0; i < fileText.length(); i++) {
			if (Character.isWhitespace(fileText.charAt(i))) {
				whiteSpace.add(i);
			}
		}

		fileText.toLowerCase().getChars(0, whiteSpace.get(0), charHolder, 0);
		dataType = new String(charHolder).trim();

		// check for v, vt, o

		switch (dataType) {

		case "#":
			break;
		case "o":
			output = getFloatArray(fileText, whiteSpace);
			break;
		case "v":
			output = getFloatArray(fileText, whiteSpace);
			break;
		case "vt": // TODO
			break;
		case "vn":
			output = getFloatArray(fileText, whiteSpace);
			break;
		case "vp":
			output = getFloatArray(fileText, whiteSpace);
			break;
		case "f": // TODO
			break;
		case "g": // TODO
			break;
		case "ka":
			output = getFloatArray(fileText, whiteSpace);
			break;
		case "kd":
			output = getFloatArray(fileText, whiteSpace);
			break;
		case "ks":
			output = getFloatArray(fileText, whiteSpace);
			break;
		case "d":
			output = getFloatArray(fileText, whiteSpace);

		}

		return output;
	}

	private static float[] getFloatArray(String text,
			ArrayList<Integer> whiteSpace) {
		float[] output = new float[whiteSpace.size() - 1];

		for (int i = 0; i < whiteSpace.size() - 1; i++) {
			char[] characterHolder = new char[whiteSpace.get(i + 1)
					- whiteSpace.get(i) - 1];
			text.getChars(whiteSpace.get(i) + 1, whiteSpace.get(i + 1),
					characterHolder, 0);
			output[i] = Float.parseFloat(new String(characterHolder));
		}

		return output;
	}

	@SuppressWarnings("unused")
	private static String[] getStringArray(String text,
			ArrayList<Integer> whiteSpace) {
		String[] output = new String[whiteSpace.size() - 1];

		for (int i = 0; i < whiteSpace.size() - 1; i++) {
			char[] characterHolder = new char[whiteSpace.get(i + 1)
					- whiteSpace.get(i) - 1];
			text.getChars(whiteSpace.get(i) + 1, whiteSpace.get(i + 1),
					characterHolder, 0);
			output[i] = new String(characterHolder);
		}

		return output;
	}
}
