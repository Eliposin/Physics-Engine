package com;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.opengl.Texture;

public class Loader {

	public List<float[]> verticesList = new ArrayList<float[]>(5000);
	public List<float[]> normalsList = new ArrayList<float[]>(5000);
	public List<float[]> textureCoordsList = new ArrayList<float[]>(5000);
	public List<float[]> paramVerticesList = new ArrayList<float[]>(500);
	public List<float[]> colorAmbientList = new ArrayList<float[]>(100);
	public List<float[]> colorDiffuseList = new ArrayList<float[]>(100);
	public List<float[]> colorSpecularList = new ArrayList<float[]>(100);
	public List<Integer> shadingList = new ArrayList<Integer>(100);
	public List<Integer> illuminationList = new ArrayList<Integer>(100);
	public List<Float> materialDissolveList = new ArrayList<Float>(100);
	public List<String> materialList = new ArrayList<String>(20);
	public List<String> objectList = new ArrayList<String>(10);
	public List<String> groupList = new ArrayList<String>(10);
	
	float[] vertices;
	float[] normals;
	float[] textureCoords;
	float[] paramVertices;
	float[] colorAmbient;
	float[] colorDiffuse;
	float[] colorSpecular;

	public void read(String inputFileName) {
		File f = new File("scenes\\" + inputFileName + ".obj");
		BufferedReader read = null;

		try {
			String currentLine;
			FileReader reader = new FileReader(f);
			read = new BufferedReader(reader);
			
			while ((currentLine = read.readLine()) != null) {
				fileParser(currentLine);
			}
			
			initAll();

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
	
	/**
	 * Parse through a .obj file.
	 * 
	 * @param fileText
	 * @return
	 */
	private void fileParser(String fileText) {

		String[] line = fileText.split(" ");
		
		switch (line[0].toLowerCase()) {

		case "#":
			break;
		case "o":
			if (line.length > 1) {
				objectList.add(line[1]);
			} else {
				objectList.add("object");
			}
			break;
		case "v":
			verticesList.add(floats(line));
			break;
		case "vt":
			textureCoordsList.add(floats(line));
			break;
		case "vn":
			normalsList.add(floats(line));
			break;
		case "vp":
			paramVerticesList.add(floats(line));
			break;
		case "f": // TODO
			break;
		case "g":
			if (line.length > 1) {
				groupList.add(line[1]);
			} else {
				groupList.add("group");
			}
			break;
		case "newmtl":
			materialList.add(line[1]);
			break;
		case "ka":
			colorAmbientList.add(floats(line));
			break;
		case "kd":
			colorDiffuseList.add(floats(line));
			break;
		case "ks":
			colorSpecularList.add(floats(line));
			break;
		case "s":
			if (line[1].equals("off") == false) {
				shadingList.add(Integer.valueOf(line[1]));
			} else {
				shadingList.add(-1);
			}
			break;
		case "d":
			materialDissolveList.add(Float.parseFloat(line[1]));
			break;
		case "tr":
			materialDissolveList.add(Float.parseFloat(line[1]));
			break;
		case "illum":
			illuminationList.add(Integer.valueOf(line[1]));
			break;
		}
	}

	
	private float[] floats(String[] line) {
		float[] f = new float [line.length - 1];
		for (byte i = 0; i < f.length; i++) {
			f[i] = Float.parseFloat(line[i + 1]);
		}
		return f;
	}
	
	private static float[] toArray(List<float[]> list, int stride) {
		if (stride < 1) {
			return null;
		}
		float[] output = new float[list.size() * stride];
		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < stride; j ++) {
				output[i * stride + j] = list.get(i)[j];
			}
		}
		return output;
	}
	
	private void initAll() {
		vertices = toArray(verticesList, verticesList.get(0).length);
//		normals = toArray(normalsList, normalsList.get(0).length);
//		textureCoords = toArray(textureCoordsList, textureCoordsList.get(0).length);
//		paramVertices = toArray(paramVerticesList, paramVerticesList.get(0).length);
//		colorAmbient = toArray(colorAmbientList, colorAmbientList.get(0).length);
//		colorDiffuse = toArray(colorDiffuseList, colorDiffuseList.get(0).length);
//		colorSpecular = toArray(colorSpecularList, colorSpecularList.get(0).length);
	}
}
