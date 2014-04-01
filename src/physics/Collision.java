package physics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import com.Entity;
import com.Profiler;
import com.Vector;
import com.Engine;

/**
 * A global class that detects collisions and remedies them.
 * 
 * @author Christopher Dombroski
 * 
 */

public class Collision {
	static float scale = Engine.scale;
	static short width = (short) Math.floor(Engine.width / scale);
	static short height = (short) Math.floor(Engine.height / scale);
	static short depth = (short) Math.floor(Engine.depth / scale);

	public static HashMap<short[], Sector> overlapMap = new HashMap<short[], Sector>();
	public static HashMap<Integer, short[]> overlapKeys = new HashMap<Integer, short[]>();

	public static boolean AABBCollide(Entity ent1, Entity ent2) {
		Profiler.prof("Collision.AABBCollide");
		
		float AABB1[] = ent1.getAABB();
		float AABB2[] = ent2.getAABB();
		float distance[] = new float[3];
		boolean overlap = false;
		for (int i = 0; i < 3; i++) {
			AABB1[i+3] *= Engine.scale;
			AABB2[i+3] *= Engine.scale;
			distance[i] = Math.abs(AABB2[i] - AABB1[i])
					- (AABB1[i + 3] + AABB2[i + 3]);
		}

		overlap = distance[0] <= 0.0F && distance[1] <= 0.0F
				&& distance[2] <= 0.0F;
		
		Profiler.prof("Collision.AABBCollide");
		return overlap;
	}
	
	/**
	 * 
	 * @param simplex ArrayList of float arrays
	 * @param d direction vector
	 * @return true if origin is contained, false if the origin is not contained
	 */
	public static boolean contains(ArrayList<float[]> simplex, float[] d) {
		
		float[] origin = new float[3];
		float[] AB = new float[3];
		float[] AC = new float[3];
		float[] AO = new float[3];
		float[] ABPerp = new float[3];
		
		AO = Vector.cSubVector(origin, simplex.get(0));
		AB = Vector.cSubVector(simplex.get(1), simplex.get(0));
		ABPerp = Vector.cTripleProduct(AB, AO, AB);
		
		switch(simplex.size()) {
		case 4:
			
			AC = Vector.cSubVector(simplex.get(2), simplex.get(0));
			float[] AD = Vector.cSubVector(simplex.get(3), simplex.get(0));
			float[] ACDPerp = Vector.cTripleProduct(AD, AC, AC);
			
			if (Vector.cDotVector(ACDPerp, AO) > 0) {
				
				float[] ABDPerp = Vector.cTripleProduct(AD, AB, AB);
				
				if (Vector.cDotVector(ABDPerp, AO) > 0) {
					
					return true;
					
				}
				
			}
			
			break;
		case 3:

			AC = Vector.cSubVector(simplex.get(2), simplex.get(0));
			
			ABPerp = Vector.cTripleProduct(AC, AB, AB);
			float[] ACPerp = Vector.cTripleProduct(AB, AC, AC);
			
			if (Vector.cDotVector(ABPerp, AO) > 0) {
				
				simplex.remove(3);
				d = ABPerp;
				
			} else { 
				
				if (Vector.cDotVector(ACPerp, AO) > 0) {
					
					simplex.remove(1);
					d = ACPerp;
					
				} else {
					
//					return true;
					
				}
				
			}

			break;
		case 2:
			
			d = ABPerp;
			break;
		}
		
		return false;
		
	}

	/**
	 * Builds AABB from the array given to it
	 * 
	 * @param vertices An array of vertices
	 * @return A float array representing the bounds of an AABB "hitbox"
	 */
	public static float[] buildAABB(float[] vertices) {
		float[] AABB = new float[6];
		float[] min = new float[3];
		float[] max = new float[3];

		for (int i = 0; i < vertices.length / 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (vertices[j + 3 * i] < min[j]) {
					min[j] = vertices[j + 3 * i];
				} else if (vertices[j + 3 * i] > max[j]) {
					max[j] = vertices[j + 3 * i];
				}

			}

		}

		for (int i = 0; i < 3; i++) {
			AABB[i] = (min[i] + max[i]) / 2;
			AABB[i + 3] = max[i] - AABB[i];
		}
		return AABB;

	}


	/**
	 * 
	 * @param vector1
	 *            array representation of a vector
	 * @param vector2
	 *            array representation of a vector
	 * @param stride
	 *            number of elements in one vector e.g. {(0,1),(1,1),(2,1)}
	 *            stride = 2.
	 * @param addition
	 *            boolean value if true does minkowski sum, if false minkowski
	 *            difference.
	 * @return output the sum or difference of the two vectors
	 */
	static float[] minkowski(float[] vector1, float[] vector2, int stride,
			boolean addition) {
		float[] output = new float[vector1.length * vector2.length / stride];

		for (int i = 0; i < vector1.length / stride; i++) {
			for (int j = 0; j < vector2.length / stride; j++) {
				for (byte k = 0; k < stride; k++) {
					if (addition) {
						output[i * vector2.length + j * stride + k] = vector1[i
								* stride + k]
								+ vector2[j * stride + k];
					} else {
						output[i * vector2.length + j * stride + k] = vector1[i
								* stride + k]
								- vector2[j * stride + k];
					}
				}
			}
		}

		return output;
	}

	/**
	 * Take an Entity and find out where on the collision array it exists. Once
	 * it finds an overlap, it makes the corresponding byte array (overlapCount)
	 * add 1. If any section is 2 or greater, there is a chance there may be a
	 * collision. Use the index of overlapCount to get the elements from
	 * overlapEntity.
	 * 
	 * @param entity
	 *            The entity to map onto the collision array
	 */

	public static void mapSectors(Entity entity) {
		
		Profiler.prof("Collision.mapSectors");

		float[] AABB = entity.getAABB();
		short[] index = new short[3];
		short[] radius = new short[6];

		for (int i = 0; i < 3; i++) {
			index[i] = (short) Math.floor(AABB[i] / scale);
			AABB[i+3] *= Engine.scale; 
			radius[i] = (short) (Math.floor((AABB[i] + AABB[i + 3]) / scale)
					- index[i] + 0);
			radius[i + 3] = (short) (Math
					.floor((AABB[i] - AABB[i + 3]) / scale)
					- index[i] - 0);
		}

		for (short i = (short) (index[0] + radius[3]); i <= index[0]
				+ radius[0]; i++) {
			for (short j = (short) (index[1] + radius[4]); j <= index[1]
					+ radius[1]; j++) {
				for (short k = (short) (index[2] + radius[5]); k <= index[2]
						+ radius[2]; k++) {
					if ((i >= 0 && j >= 0 && k >= 0)
							&& (i < width && j < height && k < depth)) {
						short[] sectorIndex = { i, j, k };
						short temp;
						if ((temp = contains(sectorIndex)) > -1) {
							overlapMap.get(getKey(temp)).entity.add(entity);
						} else {
							overlapMap.put(sectorIndex, new Sector(entity));
							overlapKeys.put(overlapKeys.size(), sectorIndex);
						}
					}
				}
			}
		}
		
		Profiler.prof("Collision.mapSectors");
		
	}

	public static void clearSectors() {
		// overlapSector.clear();
		overlapMap.clear();
		overlapKeys.clear();
		// System.out.println("Destroyed all");
	}

	public static Entity[] getEntities(int index) {
		short[] key = getKey(index);
		Sector sector = overlapMap.get(key);
		Entity[] entity = new Entity[sector.entity.size()];
		for (int i = 0; i < entity.length; i++) {
			entity[i] = sector.entity.get(i);
		}
		return entity;
	}

	public static short[] getKey(int index) {
		return overlapKeys.get(index);
	}

	private static short contains(short[] index) {
		short contains = -1;
		for (short i = 0; i < overlapMap.size(); i++) {
			if (Arrays.equals(getKey(i), index)) {
				contains = i;
				break;
			}
		}

		return contains;
	}
}

class Sector {

	public ArrayList<Entity> entity = new ArrayList<Entity>();

	Sector(Entity entity) {
		this.entity.add(entity);
	}

	public Entity getEntity(int index) {
		return entity.get(index);
	}
}
