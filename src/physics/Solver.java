package physics;

//import com.Engine;
import com.Vector;

public class Solver {

	public static void collision(Physics phys1, Physics phys2, int delta) {

		float[] momentum1 = phys1.getMomentum();
		float[] momentum2 = phys2.getMomentum();
		
		float[] momentum = Vector.cAddVector(momentum1.clone(), momentum2.clone());
		
		float[] normal = Vector.cSubVector(phys1.getLocation(), phys2.getLocation());
		normal = Vector.normalize(normal.clone());
		
		float dot = Vector.cDotVector(momentum.clone(), normal.clone());
		float size = Vector.cLength(momentum.clone());
		float angle = (float) Math.abs(Math.acos(dot / (size + 1)) - Math.PI/2);
		float scaleFactor = (float) (angle / (Math.PI/2));
		float otherScale = 1 - (float) (1 / (Math.tan(scaleFactor) * Math.tan(angle) + 1));
		System.out.println("scaleFactor: " + scaleFactor + " otherScale: " + otherScale);
		
		for(int i = 0; i < momentum1.length; i++) {
			if (momentum1[i] < 0) {
				momentum1[i] = -momentum1[i];
			}
		}
		for(int i = 0; i < momentum2.length; i++) {
			if (momentum2[i] < 0) {
				momentum2[i] = -momentum2[i];
			}
		}
		
		momentum = Vector.cAddVector(momentum1.clone(), momentum2.clone());
		float restitution = (phys1.restitution + phys2.restitution) / 2;
		float magnitude = (Vector.cLength(momentum) / ((float)delta / 1000)) * restitution;
		
		float[] force = Vector.cScaleVector(normal.clone(), magnitude * otherScale);
		
		phys1.addForce(force.clone());
		force = Vector.cScaleVector(force.clone(), -1);
		phys2.addForce(force.clone());
		
	}

}
