package physics;

import com.Entity;

import com.GameEngine;

/**
 * An all-seeing class that detects collisions and remedies them.
 * 
 * @author Christopher Dombroski
 *
 */

public class Collision {
	static int width = GameEngine.width;
	static int height = GameEngine.height;
	static int depth = GameEngine.depth;
	static float scale = GameEngine.scale;
	float[] distance = new float[3];
	static byte[][][] overlapCount = new byte[(int) (width/scale)]
									  [(int) (height/scale)]
									  [(int) (depth/scale)];
	static Entity[][][][] overlapEntity = new Entity[(int) (width/scale)]
											 [(int) (height/scale)]
											 [(int) (depth/scale)]
											 [10]; //[x][y][z][index]
	boolean overlap = false;

	public boolean AABBCollide(float[] AABB) {

//		for (int i = 0; i < 3; i++) {
//			distance[i] = Math.abs(AABB[i] - this.AABB[i])
//					- (this.AABB[i + 3] + AABB[i + 3]);
//		}

		overlap = (distance[0] < 0 && distance[1] < 0 && distance[2] < 0);
		
		if (overlap == true) {
			System.out.println("COLLISION!");
		}

		return overlap;
	}
		
	public boolean AABBCollide(float[] vertices1, float[] vertices2) {
		
		return true;
	}

//	public void buildAABB(float[] vertices) {
//		float[] min = new float[3];
//		float[] max = new float[3];
//
//		for (int i = 0; i < vertices.length / 3; i++) {
//			for (int j = 0; j < 3; j++) {
//				if (vertices[j + 3 * i] < min[j]) {
//					min[j] = vertices[j + 3 * i];
//				} else if (vertices[j + 3 * i] > max[j]) {
//					max[j] = vertices[j + 3 * i];
//				}
//
//			}
//
//		}
//
//		for (int i = 0; i < 3; i++) {
//			AABB[i] = (min[i] + max[i]) / 2;
//			AABB[i + 3] = max[i] - AABB[i];
//		}
//
//	}

	/**
	 * Take an Entity and find out where on the collision array it exists.
	 * Once it finds an overlap, it makes the corresponding byte array 
	 * (overlapCount) add 1. If any section is 2 or greater, there is a 
	 * chance there may be a collision. Use the index of overlapCount to
	 * get the elements from overlapEntity.
	 * 
	 * @param entity The entity to map onto the collision array
	 */
	public static void mapSectors(Entity entity) {
		
		float[] AABB = entity.getAABB();
		int[] index = new int[3];
		int[] radius = new int[3];
		
		for (int i = 0; i < 3; i++) {
			index[i] = (int) Math.floor(AABB[i] / scale);
			radius[i] = (int) Math.floor((AABB[i] + AABB[i+3]) / scale) - index[i];
		}
		
		for (int i = index[0] - radius[0]; i < index[0] + radius[0]; i++) {
			for (int j = index[1] - radius[1]; j < index[1] + radius[1]; j++) {
				for (int k = index[2] - radius[2]; k < index[2] + radius[2]; k++) {
					overlapEntity[i][j][k][overlapCount[i][j][k]] = entity;
					overlapCount[i][j][k] += 1;
				}
			}
		}
	}
}
