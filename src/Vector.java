
public class Vector {
	
	public static float[] toCartesian(float magnitude, float azimuthal, float polar) {
		
		float [] coords;		
		coords = new float[3];
		
		coords[0] = magnitude * (float)Math.sin(polar) * (float)Math.cos(azimuthal); // X
		coords[1] = magnitude * (float)Math.sin(polar) * (float)Math.sin(azimuthal); // Y
		coords[2] = magnitude * (float)Math.cos(polar); // Z

		return coords;
	}
	
	public static float[] toCartesian(float magnitude, float azimuthal) {		
		
		float [] coords;		
		coords = new float[3];
		
		coords[0] = magnitude * (float)Math.PI/2 * (float)Math.cos(azimuthal); // X
		coords[1] = magnitude * (float)Math.PI/2 * (float)Math.sin(azimuthal); // Y
		coords[2] = magnitude * (float)Math.PI/2; // Z	

		return coords;
	}
	
	public static float[] toCartesian(float[] vector) {
		
		float [] coords;		
		coords = new float[3];
		
		coords[0] = vector[0] * (float)Math.sin(vector[2]) * (float)Math.cos(vector[1]); // X
		coords[1] = vector[0] * (float)Math.sin(vector[2]) * (float)Math.sin(vector[1]); // Y
		coords[2] = vector[0] * (float)Math.cos(vector[2]); // Z

		return coords;
	}
	
	public static float[] toSpherical(float x, float y, float z) {
		
		float[] vector;
		vector = new float[3];
		
		vector[0] = (float)Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2)); // magnitude
		vector[1] = (float)Math.atan(y / x); // azimuthal
		vector[2] = (float)Math.acos(z / vector[0]); // polar

		return vector;
	}
	
	public static float[] toSpherical(float x, float y) {
		
		float[] vector;
		vector = new float[3];
		
		vector[0] = (float)Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)); // magnitude
		vector[1] = (float)Math.atan(y / x); // azimuthal
		vector[2] = (float)Math.PI / 2; // polar
		
		return vector;
	}
	
	public static float[] toSpherical(float[] coords) {
		
		float[] vector;
		vector = new float[3];
		
		vector[0] = (float)Math.sqrt(Math.pow(coords[0], 2) + Math.pow(coords[1], 2) + Math.pow(coords[2], 2)); // magnitude
		vector[1] = (float)Math.atan(coords[1] / coords[0]); // azimuthal
		vector[2] = (float)Math.acos(coords[2] / vector[0]); // polar
		
		return vector;
	}
	
	public static float[] addVector(float[] vector1, float[] vector2) {
		
		float[] newVector;
		float[][] coords;
		newVector = new float[3];
		coords = new float[3][3];
		
		float x, y, z;
		
		coords[0] = toCartesian(vector1);
		coords[1] = toCartesian(vector2);
		
		x = coords[0][0] + coords[1][0];
		y = coords[0][1] + coords[1][1];
		z = coords[0][2] + coords[1][2];
		
		newVector = toSpherical(x, y, z);
		
		return newVector;
	}
	
	public static float[] subVector(float[] vector1, float[] vector2) {
		
		float[] newVector;
		float[][] coords;
		newVector = new float[3];
		coords = new float[3][3];
		
		float x, y, z;
		
		coords[0] = toCartesian(vector1);
		coords[1] = toCartesian(vector2);
		
		x = coords[0][0] - coords[1][0];
		y = coords[0][1] - coords[1][1];
		z = coords[0][2] - coords[1][2];
		
		newVector = toSpherical(x, y, z);
		
		return newVector;
	}
	
	public static float[] scaleVector(float[] vector, float scalar) {
		
		vector[0] *= scalar;
		
		return vector;
	}
	
	public static float toDegrees(float radians) {
		
		float degrees;
		
		degrees = (float)(radians * 180 / Math.PI);
		
		return degrees;
	}
	
	public static float toRadians(float degrees) {
		
		float radians;
		
		radians = (float)(degrees * Math.PI / 180);
		
		return radians;
	}
}
