public class Criminality {

	public static void main(String argv[]) {

		//GameEngine gameEngine = new GameEngine();
		//gameEngine.start();
		
		float[] vector1;
		float[] vector2;
		float[] vector3;
		
		float x, y, z;
		
		vector1 = new float[3];
		vector2 = new float[3];
		vector3 = new float[3];
		
		vector1[0] = 200;
		vector1[1] = Vector.toRadians(60);
		vector1[2] = (float)Math.PI/2;
		vector2[0] = 120;
		vector2[1] = Vector.toRadians(-45);
		vector2[2] = (float)Math.PI/2;
		
		vector3 = Vector.addVector(vector1, vector2);
//		
//		vector1[0] = 6.708204f;
//		vector1[1] = -1.19029f;
//		vector1[2] = 0.93193114f;
//		
//		vector3 = Vector.toCoords(6.708204f, -1.19029f, 0.93193114f);
//		vector3 = Vector.toCoords(vector1);
//		vector3 = Vector.toCoords(vector1); // 2, -5, 4 and 6.708204, -68.19859/-1.19029, 53.39572/0.93193114
		x = vector3[0];
		y = vector3[1];
		z = vector3[2];
		
		System.out.println("Magnitude is " + x + " Azimuthal angle is " + Vector.toDegrees(y) + " Polar angle is " + Vector.toDegrees(z));
		System.out.println("Magnitude is " + x + " Azimuthal angle is " + y + " Polar angle is " + z);
		System.out.println("X is " + x + " Y is " + y + " Z is " + z);

	}
}
