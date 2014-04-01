package com;

import java.util.ArrayList;
import physics.Collision;
import physics.GJKCollision;

/**
 * A global class that has the index of all the Entities. It controls updating 
 * the Entities and multithreading.
 * @author Christopher Dombroski
 *
 */
public class Manager {

	static ArrayList<String> entityID = new ArrayList<String>();
	static ArrayList<Entity> entity = new ArrayList<Entity>();

	/**
	 * Add an Entity into the buffer.
	 * @param name The name of the Entity
	 * @param type What type the Entity is. e.g. "shape" or "spring"
	 * @param fileName The model filename
	 * @return The index of the Entity
	 */
	public static int addEntity(String name, short type, float[] location, String fileName) {
		
		Entity ntt = new Entity(name, type, location, fileName);
		
		entity.add(ntt);
		entityID.add(name);

		return ntt.hashCode();
	}

	/**
	 * Remove an Entity from the buffer.
	 * @param name The name of the Entity
	 */
	public static void removeEntity(String name) {

		int index;

		index = entityID.indexOf(name);
		entityID.remove(index);
		entity.remove(index);

	}

	/**
	 * Return the Entity with the supplied name.
	 * @param name the name of the Entity
	 * @return The Entity
	 */
	public static Entity getEntity(String name) {

		int index;
		index = entityID.indexOf(name);

		return entity.get(index);

	}
	
	public static void render() {
		
		for (Entity ntt : entity) {
			ntt.draw();
		}
		
	}

	/**
	 * Update all the Entities. Updating includes orientation, sector mapping, 
	 * and collision detection.
	 */
	public static void update(int delta) {
		
		Collision.clearSectors();
		for (int i = 0; i < entity.size(); i++) {
			entity.get(i).updateOrientation(delta);
			// Collision.mapSectors((Entity) Entity.get(i));
		}

		if (entity.size() > 1) {
			for (int i = 0; i < entity.size(); i++) {
				for (int j = i + 1; j < entity.size(); j++) {
					if (Collision.AABBCollide(entity.get(i), entity.get(j))) {

						if (GJKCollision.GJKCollide(entity.get(i), entity
								.get(j))) {

						}
					}
				}
			}
		}
	}
}
