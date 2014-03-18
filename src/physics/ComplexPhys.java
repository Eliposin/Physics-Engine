package physics;

import java.util.ArrayList;
/**
 * 
 * @author Christopher Dombroski and Bradley Pellegrini
 *
 */
public class ComplexPhys {

	static ArrayList<String> physName = new ArrayList<String>();
	static ArrayList<Physics> physObject = new ArrayList<Physics>();
	static ArrayList<Boolean> isActive = new ArrayList<Boolean>();
//	static ArrayList<Float[]> AABB = new ArrayList<Float[]>();

	public static int addPhysics(String name, float mass, float drag,
			float restitution) {

		int index;

		physName.add(name);
		index = physName.indexOf(name);
		isActive.add(index, true);
		physObject.add(new Physics(mass, drag, restitution));

		return index;

	}

	public static void removePhysics(String name) {

		int index;

		index = physName.indexOf(name);
		isActive.remove(index);
		physObject.remove(index);

	}

	/**
	 * Turns on or off the physics of the object
	 * @param name name of the obejct to apply physics to
	 * @param enable true to turn on physics, false to turn off.
	 */
	public static void enablePhysics(String name, boolean enable) {

		int index;

		index = physName.indexOf(name);

		if (enable == true) {
			isActive.set(index, true);
		} else if (enable == false) {
			isActive.set(index, false);
		}

	}

	/**
	 * 
	 * @param name object name
	 * @param mass mass of object
	 * @param drag drag of object
	 * @param restitution restitution of object
	 */
	public static void setAttributes(String name, float mass, float drag,
			float restitution) {

		int index;

		index = physName.indexOf(name);
		physObject.get(index).setAttributes(mass, drag, restitution);

	}

	/**
	 * Updates the Physics
	 * @param delta change in time.
	 */
	public static void UpdatePhysics(float delta) {

		int index = 0;

		while (index < physName.size()) {
			if (isActive.get(index) == true) {

				physObject.get(index).update(delta);

			}
			index++;
		}

	}

	/**
	 * gets the location of a physics object with the specified name
	 * @param name the string representation of the object
	 * @return the location in array form
	 */
	public static float[] getLocation(String name) {

		int index;

		index = physName.indexOf(name);
		return physObject.get(index).location;

	}

	/**
	 * Gets the physics object
	 * @param name the name of the object
	 * @return Physics
	 */
	public static Physics getPhysObject(String name) {

		int index;
		index = physName.indexOf(name);

		return physObject.get(index);

	}
}
