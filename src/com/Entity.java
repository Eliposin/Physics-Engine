package com;

import physics.Collision;
import physics.ComplexPhys;
import physics.Physics;

public class Entity {
	float[] vertices;
	float[] normals;
	float[] textures; // float??
	
	float[] AABB = new float[6];
	
	public String name;
	Loader loader = new Loader();

	short type;
	
	Entity(String name, short type, float[] vertices, float[] normals, float[] textures) {
		this.name = name;
		this.vertices = vertices;
		this.normals = normals;
		this.textures = textures;
		ComplexPhys.addPhysics(name, 1000, 0, 0);
		AABB = Collision.buildAABB(vertices);
	}
	
	Entity(String name, short type, String fileName) {
		this.name = name;
		loader.read(fileName);
		this.vertices = loader.vertices;
		this.normals = loader.normals;
		this.textures = loader.textureCoords;
		AABB = Collision.buildAABB(vertices);
	}

	public void addVertexBuffer() {
	}
	
	public Physics getPhysics() {
		return ComplexPhys.getPhysObject(name);
	}
	
	public float[] getAABB() {
		return AABB.clone();
	}
	
	public float[] getLocation() {
		return getPhysics().getLocation().clone();
	}

}
