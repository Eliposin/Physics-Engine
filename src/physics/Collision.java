package physics;

public class Collision {

<<<<<<< HEAD
	float[] AABB = new float[6];
	float[] distance = new float[3];
	boolean overlap = false;

	public boolean AABBCollide(float[] AABB) {

		for (int i = 0; i < 3; i++) {
			distance[i] = Math.abs(AABB[i] - this.AABB[i])
					- (this.AABB[i + 3] + AABB[i + 3]);
		}

		overlap = (distance[0] < 0 && distance[1] < 0 && distance[2] < 0);
		
		if (overlap == true) {
			System.out.println("COLLISION!");
		}

		return overlap;
=======
	public boolean AABBCollide(float[] vertciesOne, float[] vertciesTwo) {
		
		return true;
>>>>>>> 6c39114cbe4d335f7fe41c819497cdb2be4d4954
	}

	public void buildAABB(float[] vertices) {
		float[] min = new float[3];
		float[] max = new float[3];

		for (int i = 0; i < vertices.length / 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (vertices[j + 3 * i] < min[j]) {
					min[j] = vertices[j + 3 * i];
				} else if (vertices[j + 3 * i] > max[j]) {
					max[j] = vertices[j + 3 * i];
				}

			}

		}

		for (int i = 0; i < 3; i++) {
			AABB[i] = (min[i] + max[i]) / 2;
			AABB[i + 3] = max[i] - AABB[i];
		}

	}

	
	
	
}
