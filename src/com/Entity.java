package com;

import physics.Physics;

public class Entity {
	float[] vertices;
	float[] normals;
	float[] textures; // float??

	String name;

	short type;
	
	Entity(String name, short type, float[] vertices, float[] normals, float[] textures) {
		this.name = name;
		this.vertices = vertices;
		this.normals = normals;
		this.textures = textures;
	}

	Physics physObject;

}
