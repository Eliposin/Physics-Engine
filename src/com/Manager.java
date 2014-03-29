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

	static ArrayList<String> EntityName = new ArrayList<String>();
	static ArrayList<Entity> Entity = new ArrayList<Entity>();
	static ArrayList<Boolean> isActive = new ArrayList<Boolean>();

	/**
	 * Add an Entity into the buffer.
	 * @param name The name of the Entity
	 * @param type What type the Entity is. e.g. "shape" or "spring"
	 * @param fileName The model filename
	 * @return The index of the Entity
	 */
	
	public static int addEntity(String name, short type, float[] location, String fileName) {
		int index;
		EntityName.add(name);
		index = EntityName.indexOf(name);
		isActive.add(index, true);
		Entity.add(new Entity(name, type, location, fileName));

		return index;

	}

	/**
	 * Remove an Entity from the buffer.
	 * @param name The name of the Entity
	 */
	public static void removeEntity(String name) {

		int index;

		index = EntityName.indexOf(name);
		EntityName.remove(index);
		isActive.remove(index);
		Entity.remove(index);

	}

	/**
	 * Enable the Entity. If it is disabled, it will still be present in RAM 
	 * but not used
	 * @param name The name of the Entity
	 * @param enable
	 */
	public static void enableEntity(String name, boolean enable) {

		int index;

		index = EntityName.indexOf(name);

		if (enable == true) {
			isActive.set(index, true);
		} else if (enable == false) {
			isActive.set(index, false);
		}

	}

	// public static void setAttributes(String name, float mass, float drag,
	// float restitution) {
	//
	// int index;
	//
	// index = EntityName.indexOf(name);
	// Entity.get(index).setAttributes(mass, drag, restitution);
	//
	// }
	//
	// public static void UpdatePhysics(float delta) {
	//
	// int index = 0;
	//
	// while (index < EntityName.size()) {
	// if (isActive.get(index) == true) {
	//
	// Entity.get(index).update(delta);
	//
	// }
	// index++;
	// }
	//
	// }
	//
	// public static float[] getLocation(String name) {
	//
	// int index;
	//
	// index = EntityName.indexOf(name);
	// return Entity.get(index).location;
	//
	// }

	/**
	 * Return the Entity with the supplied name.
	 * @param name the name of the Entity
	 * @return The Entity
	 */
	public static Entity getEntity(String name) {

		int index;
		index = EntityName.indexOf(name);

		return Entity.get(index);

	}

	/**
	 * Update all the Entities. Updating includes orientation, sector mapping, 
	 * and collision detection.
	 */
	public static void update(int delta) {
		
		Collision.clearSectors();
		for (int i = 0; i < Entity.size(); i++) {
			Entity.get(i).updateOrientation(delta);
			// Collision.mapSectors((Entity) Entity.get(i));
		}

		if (Entity.size() > 1) {
			for (int i = 0; i < Entity.size(); i++) {
				for (int j = i + 1; j < Entity.size(); j++) {
					if (Collision.AABBCollide((Entity) Entity.get(i),
							(Entity) Entity.get(j))) {

						if (GJKCollision.GJKCollide(Entity.get(i), Entity
								.get(j))) {

						}
					}
				}
			}
		}
		
	}
}
