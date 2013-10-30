package physics;
import com.Vector;

public class Physics  {
	
	float[] location = {0, 0, 0};
	float[] velocity = {0, 0, 0};
	float[] acceleration = {0, 0, 0};

	public Physics() {
		
	}
	
	public float[] getlocation() {
		return velocity;
	}
	
	public float[] getVelocity() {
		return velocity;
	}
	
	public float[] getAcceleration() {
		return velocity;
	}
	
	public float getSpeed() {
		
		float speed;
		
		speed = (float) Math.abs(Math.sqrt(Math.pow(velocity[0], 2) + 
				Math.pow(velocity[1], 2) + Math.pow(velocity[2], 2)));
		
		System.out.println("Speed is " + speed + " m/s");
		
		return speed;
	}
	

	public float[] Update(float delta, float mass, float resistance, float rebound, float[] acceleration, float[] location) {
		
		float[] deltaX = Vector.cSubVector(location, this.location);
		float[] deltaV = Vector.cSubVector(acceleration, this.acceleration);

//		deltaX = Resistance(deltaX, resistance, mass);
		deltaX = Acceleration(deltaX, acceleration, delta);
//		deltaX = Delta(deltaX, delta);
		
		this.location = location.clone();
		this.velocity = deltaX.clone();
		this.acceleration = deltaV.clone();
		
		location = Vector.cAddVector(location, deltaX);
		
		return location;
		
	}

	public float[] Delta(float[] vDelta, int delta) {
		// Attempt to fix issues with varying framerates and physics. This is
		// not perfect.

		int lastDelta = -1;

		// Correct for special case in beginning.
		if (lastDelta != -1) {

			vDelta = Vector.sScaleVector(vDelta, delta, 0);

		}

		vDelta = Vector.sScaleVector(vDelta, delta, 0);

		lastDelta = delta;

		return vDelta;

	}
	
	public float[] Acceleration(float[] deltaX, float[] acceleration, float delta) {
		
		deltaX = Vector.cAddVector(deltaX, Vector.cScaleVector(acceleration.clone(), ((float) delta / 1000)));
		
		return deltaX;
	}
	
	public float[] Resistance(float[] deltaX, float resistance, float mass) {
		
		deltaX = Vector.cSubVector(deltaX, Vector.cScaleVector(deltaX, resistance / mass));
		
//		vDelta[0] -= vDelta[0] * resistance / mass;
		
		return deltaX;
	}
}
