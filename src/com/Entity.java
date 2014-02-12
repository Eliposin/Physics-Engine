package com;

import physics.Physics;

public class Entity {
	float[] vertices;
	float[] normals;
	float[] textures; //float??
	
//	float charge;
//	float density;
//	
//	Not sure if these should go in Entity or PhysObject. But both should be implemented in some form
	String name;
	
	short type;
	
//	final short SHAPE = 0;
//	final short CONSTRAINT_STRING = 1;
//	final short CONSTRAINT_ELASTIC = 2;
//	final short CONSTRAINT_ROD = 3;
//	final short CONSTRAINT_SPRING = 4;
	
	
	
	Physics physObject;
	
}
