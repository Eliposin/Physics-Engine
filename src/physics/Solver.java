package physics;

//import com.Engine;
import com.Vector;

public class Solver {

	public static void collision(Physics phys1, Physics phys2, int delta) {

		float[] momentum = Vector.cAddVector(phys1.getMomentum(), phys2.getMomentum());
		float restitution = 1 + (phys1.restitution + phys2.restitution) / 2;
//		float magnitude = (Vector.cLength(momentum) / (Engine.timeScale / delta)) * restitution;
		float magnitude = (Vector.cLength(momentum) * (100 / delta)) * restitution;
		float[] normal = Vector.cSubVector(phys1.getLocation(), phys2.getLocation());
		normal = Vector.normalize(normal.clone());
		
		float dot = Vector.cDotVector(momentum.clone(), normal.clone());
		float size = Vector.cLength(momentum);
		float angle = (float) Math.abs(Math.acos(dot / (size + 1)) - Math.PI/2);
		float scaleFactor = (float) (angle / (Math.PI/2));
		
		float[] force = Vector.cScaleVector(normal.clone(), magnitude * scaleFactor);
		
		phys1.addForce(force.clone());
		force = Vector.cScaleVector(force.clone(), -1);
		phys2.addForce(force.clone());
		
	}

}
