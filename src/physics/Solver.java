package physics;

//import com.Engine;
import com.Vector;

public class Solver {

	public static void collision(Physics phys1, Physics phys2, int delta) {

		float[] momentum = Vector.cAddVector(phys1.getMomentum(), phys2.getMomentum());
		float restitution = 1 + (phys1.restitution + phys2.restitution) / 2;
//		float magnitude = (Vector.cLength(momentum) / (Engine.timeScale / delta)) * restitution;
		float magnitude = (Vector.cLength(momentum) * (100 / delta)) * restitution;
//		float magnitude = (Vector.cLength(momentum) * delta) * restitution;
//		float magnitude = Vector.cLength(momentum) * restitution;
		float[] normal = Vector.cSubVector(phys1.getLocation(), phys2.getLocation());
		normal = Vector.normalize(normal.clone());
		float[] force = Vector.cScaleVector(normal.clone(), magnitude);
		
		phys1.addForce(force.clone());
		force = Vector.cScaleVector(force.clone(), -1);
		phys2.addForce(force.clone());
		
	}

}
