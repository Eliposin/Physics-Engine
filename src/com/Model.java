package com;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.opengl.Texture;

public class Model {

	public List<float[]> verticesList = new ArrayList<float[]>(5000);
	public List<float[]> normalsList = new ArrayList<float[]>(5000);
	public List<float[]> textureCoordsList = new ArrayList<float[]>(5000);
	public List<float[]> paramVerticesList = new ArrayList<float[]>(500);
	public List<float[]> colorAmbientList = new ArrayList<float[]>(100);
	public List<float[]> colorDiffuseList = new ArrayList<float[]>(100);
	public List<float[]> colorSpecularList = new ArrayList<float[]>(100);
	public List<int[]> indicesList = new ArrayList<int[]>(500);
	public List<Integer> shadingList = new ArrayList<Integer>(100);
	public List<Integer> illuminationList = new ArrayList<Integer>(100);
	public List<Float> materialDissolveList = new ArrayList<Float>(100);
	public List<String> materialList = new ArrayList<String>(20);
	public List<String> objectList = new ArrayList<String>(10);
	public List<String> groupList = new ArrayList<String>(10);

	public float[] vertices;
	public float[] normals;
	public float[] textureCoords;
	public float[] paramVertices;
	public float[] colorAmbient;
	public float[] colorDiffuse;
	public float[] colorSpecular;
	public int[] indices;

	Texture texture;

	byte indicesFormat = -1;
	byte indicesStride;
	byte vertexStride;
	byte normalStride = 3;
	byte textureStride;

	final byte V = 0;
	final byte VN = 1;
	final byte VT = 2;
	final byte VTN = 3;

	public void load(String inputFileName) {
		File f = new File("scenes\\" + inputFileName + ".obj");
		BufferedReader read = null;

		try {
			String currentLine;
			FileReader reader = new FileReader(f);
			read = new BufferedReader(reader);

			long time = System.currentTimeMillis();
			while ((currentLine = read.readLine()) != null) {
				fileParser(currentLine);
			}
			System.out.println(System.currentTimeMillis() - time);

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
		line = removeEmpty(line);

		if (line.length == 0) {
			return;
		}

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
			vertexStride = (byte) (line.length - 1);
			verticesList.add(floats(line));
			break;
		case "vt":
			textureStride = (byte) (line.length - 1);
			textureCoordsList.add(floats(line));
			break;
		case "vn":
			normalStride = (byte) (line.length - 1);
			normalsList.add(floats(line));
			break;
		case "vp":
			paramVerticesList.add(floats(line));
			break;
		case "f":
			indicesList.add(face(line));
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
		float[] f = new float[line.length - 1];
		for (byte i = 0; i < f.length; i++) {
			f[i] = Float.parseFloat(line[i + 1]);
		}
		return f;
	}

	private int[] face(String[] line) {
		int sides = line.length - 1;
		String[] working;

		if (indicesFormat == -1) {
			if (line[1].contains("//")) {
				indicesFormat = VN;
			} else if (line[1].contains("/")) {
				working = line[1].split("/");
				if (working.length == 2) {
					indicesFormat = VT;
				} else if (working.length == 3) {
					indicesFormat = VTN;
				}

			} else {
				indicesFormat = V;
			}
		}

		switch (indicesFormat) {
		case (V):
			indicesStride = (byte) sides;
			break;
		case (VN):
			indicesStride = (byte) (sides * 2);
			break;
		case (VT):
			indicesStride = (byte) (sides * 2);
			break;
		case (VTN):
			indicesStride = (byte) (sides * 3);
			break;
		}

		int[] indices = new int[indicesStride];

		switch (indicesFormat) {
		case (V):
			for (int i = 0; i < sides; i++) {
				indices[i] = Integer.parseInt(line[i + 1]) - 1;
			}
			break;
		case (VN):
			for (int i = 0; i < sides; i++) {
				working = line[i + 1].split("//");
				indices[i * 2] = Integer.parseInt(working[0]) - 1;
				indices[i * 2 + 1] = Integer.parseInt(working[1]) - 1;
			}
			break;
		case (VT):
			for (int i = 0; i < sides; i++) {
				working = line[i + 1].split("/");
				indices[i * 2] = Integer.parseInt(working[0]) - 1;
				indices[i * 2 + 1] = Integer.parseInt(working[1]) - 1;
			}
			break;
		case (VTN):
			for (int i = 0; i < sides; i++) {
				working = line[i + 1].split("/");
				indices[i * 3] = Integer.parseInt(working[0]) - 1;
				indices[i * 3 + 1] = Integer.parseInt(working[1]) - 1;
				indices[i * 3 + 2] = Integer.parseInt(working[2]) - 1;
			}
			break;
		}

		return indices;
	}
	
	private ArrayList<float[]> genNormals() {
		
		long time = System.currentTimeMillis();

		ArrayList<ArrayList<float[]>> normalBuffer = new ArrayList<ArrayList<float[]>>(verticesList.size());
		ArrayList<float[]> normals = new ArrayList<float[]>(verticesList.size());
		
		while (normalBuffer.size() <= verticesList.size()) {
			normalBuffer.add(new ArrayList<float[]>());
		}

		for( int i = 0; i < indicesList.size(); i++ ) {
		  // get the three vertices that make the faces
		  float[] p1 = verticesList.get(indicesList.get(i)[0]);
		  float[] p2 = verticesList.get(indicesList.get(i)[1]);
		  float[] p3 = verticesList.get(indicesList.get(i)[2]);

		  float[] v1 = Vector.cSubVector(p2, p1);
		  float[] v2 = Vector.cSubVector(p3, p1);
		  float[] normal = Vector.cCrossVector(v1, v2);

		  normal = Vector.toSpherical(normal);
		  normal[0] = 1;
		  normal = Vector.toCartesian(normal);

		  // Store the face's normal for each of the vertices that make up the face.
		  normalBuffer.get(indicesList.get(i)[0]).add(normal);
		  normalBuffer.get(indicesList.get(i)[1]).add(normal);
		  normalBuffer.get(indicesList.get(i)[2]).add(normal);
		}

		// Now loop through each vertex vector, and average out all the normals stored.

		for( int i = 0; i < verticesList.size(); i++) {
			float[] normal = new float[3];
			for( int j = 0; j < normalBuffer.get(i).size(); j++ ) {
				normal = Vector.cAddVector(normal, normalBuffer.get(i).get(j));
			}
			
			normal = Vector.toSpherical(normal);
			normal[0] = 1;
			normal = Vector.toCartesian(normal);
			
			normals.add(i, normal);
				
		}

		System.out.println(System.currentTimeMillis() - time + " milliseconds");

		return normals;
	}

	private static float[] toArray(List<float[]> list, int stride) {
		if (stride < 1) {
			return null;
		}
		float[] output = new float[list.size() * stride];
		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < stride; j++) {
				output[i * stride + j] = list.get(i)[j];
			}
		}
		return output;
	}

	private static int[] toArrayI(List<int[]> list, int stride) {
		if (stride < 1) {
			return null;
		}
		int[] output = new int[list.size() * stride];
		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < stride; j++) {
				output[i * stride + j] = list.get(i)[j];
			}
		}
		return output;
	}

	private String[] removeEmpty(String[] line) {
		List<String> clean = new ArrayList<String>(line.length);
		for (int i = 0; i < line.length; i++) {
			if (!line[i].isEmpty()) {
				clean.add(line[i]);
			}
		}

		String[] output = clean.toArray(new String[0]);

		return output;
	}

	private void initAll() {
		if (verticesList.size() != 0) {
			vertices = toArray(verticesList, vertexStride);
		}
		if (textureCoordsList.size() != 0) {
			textureCoords = toArray(textureCoordsList, textureStride);
		}
		if (indicesList.size() != 0) {
			indices = toArrayI(indicesList, indicesStride);
		}
		if (normalsList.size() != 0) {
			normals = toArray(normalsList, normalStride);
		} else {
			normals = toArray(genNormals(), normalStride);;
		}
		if (paramVerticesList.size() != 0) {
			paramVertices = toArray(paramVerticesList,
					paramVerticesList.get(0).length);
		}
		if (colorAmbientList.size() != 0) {
			colorAmbient = toArray(colorAmbientList,
					colorAmbientList.get(0).length);
		}
		if (colorDiffuseList.size() != 0) {
			colorDiffuse = toArray(colorDiffuseList,
					colorDiffuseList.get(0).length);
		}
		if (colorSpecularList.size() != 0) {
			colorSpecular = toArray(colorSpecularList,
					colorSpecularList.get(0).length);
		}

	}
}
