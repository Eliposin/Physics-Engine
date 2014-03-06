package physics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import com.Entity;

import com.Engine;

/**
 * An all-seeing class that detects collisions and remedies them.
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
		float AABB1[] = ent1.getAABB();
		float AABB2[] = ent2.getAABB();
		float distance[] = new float[3];
		boolean overlap = false;
		for (int i = 0; i < 3; i++)
			distance[i] = Math.abs(AABB2[i] - AABB1[i])
					- (AABB1[i + 3] + AABB2[i + 3]);

		overlap = distance[0] <= 0.0F && distance[1] <= 0.0F
				&& distance[2] <= 0.0F;
		if (overlap)
			System.out.println("COLLISION!");
		return overlap;
	}

	public static void updateAABB(Entity entity) {
		for (int i = 0; i < 3; i++)
			entity.AABB[i] = entity.getLocation()[i] * scale;

	}

	public boolean AABBCollide(float[] vertices1, float[] vertices2) {

		return true;
	}

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

		float[] AABB = entity.getAABB();
		short[] index = new short[3];
		short[] radius = new short[6];

		for (int i = 0; i < 3; i++) {
			index[i] = (short) Math.floor(AABB[i] / scale);
			radius[i] = (short) (Math.floor((AABB[i] + AABB[i + 3]) / scale)
					- index[i] + 1);
			radius[i + 3] = (short) (Math
					.floor((AABB[i] - AABB[i + 3]) / scale) - index[i] - 1);
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
