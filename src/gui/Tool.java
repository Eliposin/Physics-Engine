package gui;

import inout.Input;

import com.Entity;
import com.Manager;
import com.Vector;

public class Tool {
	
	static int currentTool;
	
	final static int MOVE = 0;
	final static int SELECT = 1;
	final static int ADD_ENTITY = 2;
	final static int ADD_CONSTRAINT = 3;
	final static int ADD_FORCE = 4;
	
	public final static int START = 5;
	public final static int USE = 6;
	public final static int END = 7;

	public static void useTool(int action) {
		switch(currentTool) {
		case MOVE: move(action);
			break;
		case SELECT: select(action);
			break;
		case ADD_ENTITY: addEntity(action);
			break;
		case ADD_CONSTRAINT: addConstraint(action);
			break;
		case ADD_FORCE: addForce(action);
			break;
		}
	}
	
	private static void move(int action) {
		
		switch (action) {
		case START:
			break;
		case USE:
			
			java.util.List<String> lst = GUI.lstEntity.getSelectedValuesList();
			for (int i = 0; i < lst.size(); i++) {
				Entity e = Manager.getEntity(lst.get(i));
				e.setLocation(Vector.cAddVector(e.location.clone(), new float[]{Input.deltaX, Input.deltaY, 0}));
			}
			
			break;
		case END:
			break;
		}
		
	}
	
	private static void select(int action) {
		
		switch (action) {
		case START:
			break;
		case USE:
			break;
		case END:
			break;
		}
		
	}
	
	private static void addEntity(int action) {
		
		String name;
		
		switch (action) {
		case START:
			GUI.entityCount++;
			name = "Entity " + GUI.entityCount;
			Manager.addEntity(name, Entity.SHAPE, new float[]{Input.mouseX, Input.mouseY, 0}, "icosahedron");
			GUI.lm.addElement(name);
			
			break;
		case USE:
			name = "Entity " + GUI.entityCount;
			Entity e = Manager.getEntity(name);
			e.setLocation(Vector.cAddVector(e.location.clone(), new float[]{Input.deltaX, Input.deltaY, 0}));
			
			break;
		case END:
			break;
		}
		
	}
	
	private static void addConstraint(int action) {
		
		switch (action) {
		case START:
			break;
		case USE:
			break;
		case END:
			break;
		}
		
	}
	
	private static void addForce(int action) {
		
		switch (action) {
		case START:
			break;
		case USE:
			break;
		case END:
			break;
		}
		
	}
	
}
