package physics;
import java.util.ArrayList;

public class ComplexPhys {

	static ArrayList<String> physName = new ArrayList<String>();
	static ArrayList<PhysObject> physObject = new ArrayList<PhysObject>();
	static ArrayList<Boolean> isActive = new ArrayList<Boolean>();

	public static int addPhysics(String name, PhysAttributes attributes) {

		int index;

		physName.add(name);
		index = physName.indexOf(name);
		isActive.add(index, true);
		physObject.add(new PhysObject(name, attributes, new Physics()));

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

	public static void setAttributes(String name, PhysAttributes attributes) {

		int index;

		index = physName.indexOf(name);
		physObject.get(index).setAttributes(attributes);

	}

	public static void UpdatePhysics(float delta) {

		int index = 0;

		while (index < physName.size()) {
			if (isActive.get(index) == true) {

				physObject.get(index).getAttributes().location = physObject
						.get(index).physics.Update(delta, physObject.get(index)
						.getAttributes().attributes[0], // mass
						physObject.get(index).getAttributes().attributes[1], // resistance
						physObject.get(index).getAttributes().attributes[2], // rebound
						physObject.get(index).getAttributes().acceleration, // acceleration
						physObject.get(index).getAttributes().location);
				
			}
			index++;
		}
		
	}
	
	public static float[] getLocation(String name) {
		
		int index;
		
		index = physName.indexOf(name);
		return physObject.get(index).getAttributes().location;
		
	}
	
	public static PhysObject getPhysObject(String name) {
		
		int index;
		index = physName.indexOf(name);
		
		return physObject.get(index);
		
	}
}
