package physics;

import com.Entity;
import com.GameEngine;

public class Collision {
	int width = GameEngine.width;
	int height = GameEngine.height;
	int depth = GameEngine.depth;
	float scale = GameEngine.scale;
	float[] distance = new float[3];
	byte[][][] overlapCount = new byte[(int) (width/scale)][(int) (height/scale)][(int) (depth/scale)];
	Entity[][][][] overlapEntitiy; //[x][y][z][index]
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

	private void mapSectors(Entity entity) {
		
		float[] AABB = entity.getAABB();
		
		
		
	}
	
}
