package com;

import physics.Physics;

public class Entity {
	float[] vertices;
	float[] normals;
	float[] textures; // float??
	
	float[] AABB = new float[6];

	Physics physObject;
	
	String name;

	short type;
	
	Entity(String name, short type, float[] vertices, float[] normals, float[] textures) {
		this.name = name;
		this.vertices = vertices;
		this.normals = normals;
		this.textures = textures;
	}

	public void addVertexBuffer() {
		
	}
	
	public Physics getPhysics() {
		return physObject;
	}
	
	public float[] getAABB() {
		return AABB;
	}

}
