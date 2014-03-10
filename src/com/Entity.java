package com;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

import physics.Collision;
import physics.ComplexPhys;
import physics.Physics;

public class Entity {
	
	public String name;
	int VAOID;
	int VBOID;
	int NBOID;
	int VBOIID;
	
	public Model mdl = new Model();
	
	public float[] AABB = new float[6];
	public float[] rotation = new float[3]; // position, rotation
	public float[] location = new float[3];
	
	short type;
	
	Entity(String name, short type, String fileName) {
		this.name = name;
		mdl.load(fileName);
		AABB = Collision.buildAABB(mdl.vertices);
		initModel();
	}
	
	private void initModel() {
		
		boolean normals = false;
		
		if (mdl.normals != null) {
			normals = true;
		}
		
		FloatBuffer verticesBuffer = toFloatBuffer(mdl.vertices);
		FloatBuffer normalsBuffer = toFloatBuffer(mdl.normals);;
		IntBuffer indicesBuffer = toIntBuffer(mdl.indices);
		
		VAOID = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(VAOID);
		VBOID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0);
		
		NBOID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, NBOID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, normalsBuffer, GL15.GL_STATIC_DRAW);
		GL11.glNormalPointer(GL11.GL_FLOAT, 0, 0);

		VBOIID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOIID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
		
	}
	
	public void draw() {
		
		GL11.glPushMatrix();
		
		GL11.glTranslatef(location[0], location[1], location[2]);
		GL11.glScalef(Engine.scale, Engine.scale, Engine.scale);
		
		GL30.glBindVertexArray(VAOID);
		
		GL11.glEnable(GL11.GL_VERTEX_ARRAY);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOID);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, VBOIID);
		
		// Draw the vertices
		GL11.glDrawElements(GL11.GL_TRIANGLES, mdl.indices.length, GL11.GL_UNSIGNED_INT, 0);
		
		// Put everything back to default (deselect)
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
		
		GL11.glDisable(GL11.GL_VERTEX_ARRAY);
		GL11.glDisable(GL11.GL_NORMAL_ARRAY);
		
		GL11.glPopMatrix();
		
	}
	
	private FloatBuffer toFloatBuffer(float[] floatArray) {

		FloatBuffer buffer = BufferUtils.createFloatBuffer(floatArray.length);
		buffer.put(floatArray);
		buffer.flip();

		return buffer;
	}
	
	private IntBuffer toIntBuffer(int[] intArray) {

		IntBuffer buffer = BufferUtils.createIntBuffer(intArray.length);
		buffer.put(intArray);
		buffer.flip();

		return buffer;
	}
	
	public Physics getPhysics() {
		return ComplexPhys.getPhysObject(name);
	}
	
	public float[] getAABB() {
		return AABB.clone();
	}
	
	public void updateOrientation() {
		Physics phys = getPhysics();
		AABB[0] = location[0] - phys.getLocation()[0];
		AABB[1] = location[1] - phys.getLocation()[1];
		AABB[2] = location[2] - phys.getLocation()[2];
		location = Vector.cScaleVector(phys.getLocation().clone(), Engine.scale);
//		rotation = phys.getRotation().clone;
	}

}
