package com;

import java.util.ArrayList;
import physics.Collision;
import physics.ComplexPhys;
import physics.GJKCollision;

public class Manager {

	static ArrayList<String> EntityName = new ArrayList<String>();
	static ArrayList<Entity> Entity = new ArrayList<Entity>();
	static ArrayList<Boolean> isActive = new ArrayList<Boolean>();

	public final static short SHAPE = 0;
	public final static short CONSTRAINT_STRING = 1;
	public final static short CONSTRAINT_ELASTIC = 2;
	public final static short CONSTRAINT_ROD = 3;
	public final static short CONSTRAINT_SPRING = 4;

	public static int addEntity(String name, short type, String fileName) {
		int index;
		EntityName.add(name);
		index = EntityName.indexOf(name);
		isActive.add(index, true);
		Entity.add(new Entity(name, type, fileName));
		ComplexPhys.addPhysics(name, 1000, 0, 0);

		return index;

	}

	public static void removeEntity(String name) {

		int index;

		index = EntityName.indexOf(name);
		isActive.remove(index);
		Entity.remove(index);

	}

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

	public static Entity getEntity(String name) {

		int index;
		index = EntityName.indexOf(name);

		return Entity.get(index);

	}

	public static void update() {
		Collision.clearSectors();
		for (int i = 0; i < Entity.size(); i++) {
			Entity.get(i).updateOrientation();
			// Collision.mapSectors((Entity) Entity.get(i));
		}

		if (Entity.size() > 1) {
			for (int i = 0; i < Entity.size(); i++) {
				for (int j = i + 1; j < Entity.size(); j++) {
					if (Collision.AABBCollide((Entity) Entity.get(i),
							(Entity) Entity.get(j))) {

						if (GJKCollision.GJKCollide(Entity.get(i), Entity
								.get(j))) {
							System.out.println("Actual Collision!!!!!");
							if (GJKCollision.GJKCollide(Entity.get(i), Entity
									.get(j))) {
								System.out.println("Acutal Collision!!!!!");
							}

						}
					}
				}
			}
		}
	}
}
