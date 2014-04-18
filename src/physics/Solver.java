package physics;

import com.Vector;

public class Solver {
	
	public static void collision(Physics phys1, Physics phys2) {
		
		float mass1 = phys1.mass;
		float mass2 = phys2.mass;
		float restitution = 1;
		
		float[] normal = Vector.cSubVector(phys2.location, phys1.location);
		
		float[] velocity = Vector.cSubVector(phys1.velocity.clone(), phys2.velocity.clone());
		
		float[] impulse = Vector.cScaleVector(normal.clone(), 1 + restitution);
		float temp = Vector.cDotVector(velocity, normal);
		impulse = Vector.cScaleVector(impulse.clone(), temp);
//		temp = (1/mass1 + 1/mass2);
//		impulse = Vector.cScaleVector(impulse.clone(), temp);
		
		float[] force1 = Vector.cScaleVector(impulse.clone(), mass1 * 75);
		float[] force2 = Vector.cScaleVector(impulse.clone(), -mass2 * 75);
		
		phys1.addForce(force2);
		phys2.addForce(force1);
		
	}

}
