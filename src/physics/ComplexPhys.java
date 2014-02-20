package physics;

import java.util.ArrayList;

public class ComplexPhys {

	static ArrayList<String> physName = new ArrayList<String>();
	static ArrayList<Physics> physObject = new ArrayList<Physics>();
	static ArrayList<Boolean> isActive = new ArrayList<Boolean>();
	static ArrayList<Float[]> AABB = new ArrayList<Float[]>();

	public static int addPhysics(String name, float[] vertices, float mass, float drag,
			float restitution) {

		int index;

		physName.add(name);
		index = physName.indexOf(name);
		isActive.add(index, true);
		physObject.add(new Physics(vertices, mass, drag, restitution));

		return index;

	}

	public static void removePhysics(String name) {

		int index;

		index = physName.indexOf(name);
		isActive.remove(index);
		physObject.remove(index);

	}

	public static void enablePhysics(String name, boolean enable) {

		int index;

		index = physName.indexOf(name);

		if (enable == true) {
			isActive.set(index, true);
		} else if (enable == false) {
			isActive.set(index, false);
		}

	}

	public static void setAttributes(String name, float mass, float drag,
			float restitution) {

		int index;

		index = physName.indexOf(name);
		physObject.get(index).setAttributes(mass, drag, restitution);

	}

	public static void UpdatePhysics(float delta) {

		int index = 0;

		while (index < physName.size()) {
			if (isActive.get(index) == true) {

				physObject.get(index).update(delta);

			}
			index++;
		}

	}

	public static float[] getLocation(String name) {

		int index;

		index = physName.indexOf(name);
		return physObject.get(index).location;

	}

	public static Physics getPhysObject(String name) {

		int index;
		index = physName.indexOf(name);

		return physObject.get(index);

	}
}
