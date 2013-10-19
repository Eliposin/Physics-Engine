package physics;
import com.Vector;

public class Physics  {

	float[] lastLocation = new float[3];
	float[] vector = new float[3];
	float speed;

	public Physics() {
		
	}
	
	public float[] getVector() {
		return vector;
	}
	
	public float getSpeed() {
		return speed;
	}
	

	public float[] Update(int delta, float mass, float resistance, float rebound, float[] acceleration, float[] location) {

		float[] vDelta = new float[3];

		vDelta[0] = location[0] - lastLocation[0];
		vDelta[1] = location[1] - lastLocation[1];
		vDelta[2] = location[2] - lastLocation[2];

		vDelta = Vector.toSpherical(vDelta);

		vDelta = Resistance(vDelta, resistance, mass);
		vDelta = Acceleration(vDelta, acceleration, delta);
//		vDelta = Delta(vDelta, delta);
		
//		System.out.println("Speed is: " + vDelta[0]);
		speed = vDelta[0];

		vDelta = Vector.toCartesian(vDelta);
		
		vector = vDelta;

		lastLocation[0] = location[0];
		lastLocation[1] = location[1];
		lastLocation[2] = location[2];

		location[0] += vDelta[0];
		location[1] += vDelta[1];
		location[2] += vDelta[2];
		
		return location;
		
	}

	public float[] Delta(float[] vDelta, int delta) {
		// Attempt to fix issues with varying framerates and physics. This is
		// not perfect.

		int lastDelta = -1;

		// Correct for special case in beginning.
		if (lastDelta != -1) {

			vDelta = Vector.scaleVector(vDelta, delta, 0);

		}

		vDelta = Vector.scaleVector(vDelta, delta, 0);

		lastDelta = delta;

		return vDelta;

	}
	
	public float[] Acceleration(float[] vDelta, float[] direction, int delta) {
		
		vDelta = Vector.addVector(vDelta, direction);
		
		return vDelta;
	}
	
	public float[] Resistance(float[] vDelta, float resistance, float mass) {
		
		vDelta[0] -= vDelta[0] * resistance / mass;
		
		return vDelta;
	}
}
