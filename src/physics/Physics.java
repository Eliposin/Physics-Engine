package physics;

import java.util.ArrayList;
import com.GameEngine;
import com.Vector;

public class Physics  {
	
	public float mass = 1000f; //grams
	public float drag = 1f; //coefficient of drag
	public float restitution = 1f; // coefficient of restitution
	public float[] location = {0, 0, 0}; // centimeters
	private float[] lastLocation = {0, 0, 0};
	public float[] velocity = {0, 0, 0}; // centimeters per second
	public float[] acceleration = {0, 0, 0}; // centimeters per second squared

	ArrayList<float[]> forceBuffer = new ArrayList<float[]>(); // hold all the possible momentums acting on the object
	ArrayList<float[]> momentumBuffer = new ArrayList<float[]>(); // hold all the possible forces acting on the object
	
	public Physics() {
		
	}
	
	public Physics(float mass, float drag, float restitution) {
		
		this.mass = mass;
		this.drag = drag;
		this.restitution = restitution;
		
	}
	
	public void addForce(float[] f) {
		forceBuffer.add(f);
	}
	
	public void addMomentum(float[] p) {
		forceBuffer.add(p);
	}
	
	public void setAttributes(float mass, float drag, float restitution) {
		
		this.mass = mass;
		this.drag = drag;
		this.restitution = restitution;
	}
	
	public float[] getlocation() {
		return location;
	}
	
	public float[] getVelocity() {
		return velocity;
	}
	
	public float[] getAcceleration() {
		return acceleration;
	}
	
	public float getSpeed() {
		
		float speed;
		
		speed = (float) Math.abs(Math.sqrt(Math.pow(velocity[0], 2) + 
				Math.pow(velocity[1], 2) + Math.pow(velocity[2], 2)));
		
		System.out.println("Speed is " + speed + " m/s");
		
		return speed;
	}
	
	public float[] getMomentum() {
		
		float[] momentum;
		
		momentum = Vector.cScaleVector(velocity.clone(), mass);
		
		return momentum;
	}
	

	public float[] update(float delta) {
		
		
		velocity = force(velocity, delta);
		
		lastLocation = location.clone();
		location = Vector.cAddVector(location, Vector.cScaleVector(velocity, delta / GameEngine.timeScale));
		
		float[] deltaX = Vector.cSubVector(location, lastLocation);
		velocity = Vector.cScaleVector(deltaX, GameEngine.timeScale / delta);
		
		return location;
		
	}
	
	public float[] force(float[] velocity, float delta) {
//		 deltaX += a*deltaT/1000
//		deltaX = Vector.cAddVector(deltaX, Vector.cScaleVector(acceleration.clone(), ((float) delta / 1000)));
		
		float[] force;		
		for (int i = 0; i < forceBuffer.size(); i++) {
			
			force = forceBuffer.get(i);
			velocity = Vector.cAddVector(velocity,
					Vector.cScaleVector(force.clone(),
							((float) delta / GameEngine.timeScale) / mass));
			
		}
		
		forceBuffer.clear();
		
		return velocity;
	}
	
	public float[] drag(float[] deltaX, float resistance, float mass) {
		
		deltaX = Vector.cSubVector(deltaX, Vector.cScaleVector(deltaX, resistance / mass));
		
//		vDelta[0] -= vDelta[0] * resistance / mass;
		
		return deltaX;
	}
}
