package com;

import inout.Model;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

import physics.Collision;
import physics.Physics;

public class Entity {
	
	public String name;
	
	public boolean isActive = true;

	public float[] AABB = new float[6];
	public float[] rotation = new float[3]; // position, rotation
	public float[] location = new float[3];
	
	public Model mdl = new Model();
	
	Physics phys = null;
	
	short type;
	
	public final static short SHAPE = 0;
	public final static short CONSTRAINT_STRING = 1;
	public final static short CONSTRAINT_ELASTIC = 2;
	public final static short CONSTRAINT_ROD = 3;
	public final static short CONSTRAINT_SPRING = 4;
	
	int VAOID;
	int VBOID;
	int NBOID;
	int VBOIID;
	
	Entity(String name, short type, float[] location, String fileName) {
		this.name = name;
		this.type = type;
		this.location = location;
		if (type == SHAPE) {
			phys = new Physics(Vector.cScaleVector(location, 1/Engine.scale));
		}
		mdl.load(fileName);
		AABB = Collision.buildAABB(mdl.vertices);
		initModel();
	}
	
	private void initModel() {
		
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
		
		Profiler.prof("Entity.draw");
		
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
		
		if(Engine.debugToggle) {
			GL11.glLineWidth(2);
			
			//Draw the AABB			
			GL11.glColor3f(0.8f, 0.2f, 0.2f);
			GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex3f(-AABB[3], -AABB[4], -AABB[5]);//0
			GL11.glVertex3f(AABB[3], -AABB[4], -AABB[5]);//1
			GL11.glVertex3f(AABB[3], -AABB[4], -AABB[5]);//1
			GL11.glVertex3f(AABB[3], AABB[4], -AABB[5]);//2
			GL11.glVertex3f(AABB[3], AABB[4], -AABB[5]);//2
			GL11.glVertex3f(-AABB[3], AABB[4], -AABB[5]);//3
			
			
			GL11.glVertex3f(-AABB[3], -AABB[4], AABB[5]);//4
			GL11.glVertex3f(-AABB[3], -AABB[4], -AABB[5]);//0
			GL11.glVertex3f(-AABB[3], -AABB[4], -AABB[5]);//0
			GL11.glVertex3f(-AABB[3], AABB[4], -AABB[5]);//3
			GL11.glVertex3f(-AABB[3], AABB[4], -AABB[5]);//3
			GL11.glVertex3f(-AABB[3], AABB[4], AABB[5]);//7
			
			GL11.glVertex3f(AABB[3], -AABB[4], AABB[5]);//5
			GL11.glVertex3f(-AABB[3], -AABB[4], AABB[5]);//4
			GL11.glVertex3f(-AABB[3], -AABB[4], AABB[5]);//4
			GL11.glVertex3f(-AABB[3], AABB[4], AABB[5]);//7
			GL11.glVertex3f(-AABB[3], AABB[4], AABB[5]);//7
			GL11.glVertex3f(AABB[3], AABB[4], AABB[5]);//6
			
			GL11.glVertex3f(AABB[3], -AABB[4], -AABB[5]);//1
			GL11.glVertex3f(AABB[3], -AABB[4], AABB[5]);//5
			GL11.glVertex3f(AABB[3], -AABB[4], AABB[5]);//5
			GL11.glVertex3f(AABB[3], AABB[4], AABB[5]);//6
			GL11.glVertex3f(AABB[3], AABB[4], AABB[5]);//6
			GL11.glVertex3f(AABB[3], AABB[4], -AABB[5]);//2
			GL11.glEnd();
			
			//Draw the forces on the object
			GL11.glColor3f(0.2f, 0.8f, 0.2f);
			GL11.glBegin(GL11.GL_LINES);
			float[] acceleration = phys.getAcceleration();
			acceleration = Vector.cScaleVector(acceleration, 1f/10);
			GL11.glVertex3f(0, 0, 0);
			GL11.glVertex3f(acceleration[0], acceleration[1], acceleration[2]);
			GL11.glEnd();
			
			//Draw the velocity of the object
			GL11.glColor3f(0.8f, 0.8f, 0.2f);
			float[] velocity = phys.getVelocity();
			velocity = Vector.cScaleVector(velocity, 1f/10);
			GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex3f(0, 0, 0);
			GL11.glVertex3f(velocity[0], velocity[1], velocity[2]);
			GL11.glEnd();
			
			GL11.glLineWidth(1);
		}
		GL11.glColor3f(Engine.red, Engine.green, Engine.blue);
		GL11.glPopMatrix();
		
		Profiler.prof("Entity.draw");
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
	
	public float[] getAABB() {
		return AABB.clone();
	}
	
	public void updateOrientation(int delta) {
		if (phys != null) {
			phys.update(delta);
			AABB[0] = location[0] - phys.getLocation()[0];
			AABB[1] = location[1] - phys.getLocation()[1];
			AABB[2] = location[2] - phys.getLocation()[2];
			location = Vector.cScaleVector(phys.getLocation(), Engine.scale);
			//		rotation = phys.getRotation().clone;

		}

	}
	
	public void setLocation(float[] location) {
		
		this.location = location;
		phys.location = Vector.cScaleVector(location, 1/Engine.scale);
		
	}
	
}
