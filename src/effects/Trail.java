package effects;

import java.util.ArrayList;

public class Trail {
	
	int count;
	float[] error = {100,0,0}; 
	
	static ArrayList<float[]> trail = new ArrayList<float[]>();
	
	public Trail(int count) {
		
		this.count = count;
		trail.ensureCapacity(count);
		
	}
	
	public void updateTrail(float[] location) {
		
		trail.add(0, location);
		
		if (trail.size() == count){
			trail.remove(count - 1);
		}
		
		
	}
	
	public ArrayList<float[]> getTrail() {
		
		return trail;
		
	}
	
	public float[] getTrail(int index) {
		//Return the element at the provided index.
		
		if (index < trail.size()) {
			
			return trail.get(index);
			
		} else {
			
			return error;
			
		}
		
		
		
	}

}
