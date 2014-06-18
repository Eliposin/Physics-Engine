package physics;

import java.util.Arrays;

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
		
		System.out.println(Arrays.toString(momentum1) + "momentum 1");

		for(int i = 0; i < momentum2.length; i++) {
			if (momentum2[i] < 0) {
				momentum2[i] = -momentum2[i];
			}
		}
		System.out.println(Arrays.toString(momentum2) + "momentum 2");

		
		momentum = Vector.cAddVector(momentum1.clone(), momentum2.clone());
		System.out.println(Arrays.toString(momentum) + "momentum total");

		float restitution = (phys1.restitution + phys2.restitution) / 2;
		float magnitude = (Vector.cLength(momentum) / ((float)delta / 1000)) * restitution;
		
		float[] force = Vector.cScaleVector(normal.clone(), magnitude * otherScale);
		//System.out.println(Arrays.toString(force) + " first entity");

		phys1.addForce(force.clone());
		force = Vector.cScaleVector(force.clone(), -1);
		//System.out.println(Arrays.toString(force) + " second entity");
		phys2.addForce(force.clone());
		
	}

}
