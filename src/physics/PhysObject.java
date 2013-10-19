package physics;
public class PhysObject {
	
	PhysAttributes attributes;
	Physics physics;

	public PhysObject(String name, PhysAttributes attributes,
			Physics physics) {
		
		this.attributes = attributes;
		this.physics = physics;

	}

	public PhysObject(String name, Physics physics) {

		this.physics = physics;
		
	}
	
	public void setAttributes(PhysAttributes attributes) {
		
		this.attributes = attributes;
		
	}
	
	public PhysAttributes getAttributes() {
		
		return attributes;
		
	}
	
	public Physics getPhysics() {
		
		return physics;
		
	}

}
