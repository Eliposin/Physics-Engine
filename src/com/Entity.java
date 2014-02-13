package com;

import physics.Physics;

public class Entity {
	float[] vertices;
	float[] normals;
<<<<<<< HEAD
	float[] textures; // float??

=======
	float[] textures; //float??
	
//	float charge;
//	float density;
//	
//	Not sure if these should go in Entity or PhysObject. But both should be implemented in some form
>>>>>>> 6c39114cbe4d335f7fe41c819497cdb2be4d4954
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
