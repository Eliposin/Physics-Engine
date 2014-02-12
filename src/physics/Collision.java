package physics;

public class Collision {

	public boolean AABBCollide(float[] vertciesOne, float[] vertciesTwo) {
		
		return true;
	}

	public void AABBGenerator(float[] vertices) {
		float[] min = new float[3];
		float[] max = new float[3];

		for (int i = 0; i < vertices.length; i++) {
			for (int j = 0; j < 3; j++) {
				if (vertices[j + 3 * i] < min[j]) {
					min[j] = vertices[j + 3 * i];
				} else if (vertices[j + 3 * i] > max[j]) {
					max[j] = vertices[j + 3 * i];
				}

			}

		}

	}

	
	
	
}
