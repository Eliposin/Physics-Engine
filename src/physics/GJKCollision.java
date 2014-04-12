package physics;

import java.util.ArrayList;

import com.Engine;
import com.Entity;
import com.Vector;
/**
 * 
 * @author Bradley Pellegrini and Christopher Dombroski
 * 
 * Uses the GJK algorithm to find if two objects collide
 *
 */
public class GJKCollision {
	
	static float[] direction = new float[3];
	static ArrayList<float[]> simplex = new ArrayList<float[]>(4);
	
	/**
	 * 
	 * @param entity1 an object of the entity class
	 * @param entity2 an object of the entity class
	 * @return true if objects intersect, false if they do not.
	 */
	public static boolean GJKCollide(Entity entity1, Entity entity2) {

		direction = Vector.cSubVector(entity2.location, entity1.location);
		
		simplex.clear();

		simplex.add(support(entity1, entity2, direction));

		direction = Vector.cScaleVector(direction, -1f);
		
//		if (Engine.debugToggle) {
//			GL11.glPointSize(8);
//			GL11.glBegin(GL11.GL_POINTS);
//			GL11.glVertex3f(Engine.width/2, Engine.height/2, 0);
//			GL11.glEnd();
//			
//			GL11.glColor3f(1, 1, 0);
//			GL11.glPointSize(3);
//			float[] vert1 = entity1.mdl.vertices.clone();
//			float[] vert2 = entity2.mdl.vertices.clone();
//			vert1 = Vector.cScaleVector(vert1.clone(), Engine.scale);
//			vert2 = Vector.cScaleVector(vert2.clone(), Engine.scale);
//			
//			for (int i = 0; i < vert1.length; i = i+3) {
//				vert1[i] = vert1[i] + entity1.location[0];
//				vert1[i+1] = vert1[i+1] + entity1.location[1];
//				vert1[i+2] = vert1[i+2] + entity1.location[2];
//			}
//			for (int i = 0; i < vert2.length; i = i+3) {
//				vert2[i] = vert2[i] + entity2.location[0];
//				vert2[i+1] = vert2[i+1] + entity2.location[1];
//				vert2[i+2] = vert2[i+2] + entity2.location[2];
//			}
//			GL11.glBegin(GL11.GL_POINTS);
//			float[] mink = Collision.minkowski(vert1, vert2, 3, false);
//			for (int i = 0; i < mink.length; i = i+3) {
//				GL11.glVertex3f(Engine.width/2+mink[i], Engine.height/2+mink[i+1], mink[i+2]);
//			}
//			GL11.glEnd();
//		}

		int iterations = 0;
		while (iterations < 100) {

			simplex.add(support(entity1, entity2, direction));
			
//			switch (simplex.size()) {
//			case 2:
//				GL11.glPointSize(4);
//				GL11.glColor3f(1, 0, 0);
//				break;
//			case 3:
//				GL11.glPointSize(8);
//				GL11.glColor3f(0, 0, 1);
//				break;
//			case 4: 
//				GL11.glPointSize(12);
//				GL11.glColor3f(0, 1, 0);
//			}
//			
//			if (Engine.debugToggle) {
//				GL11.glBegin(GL11.GL_POINTS);
//				for (float[] p : simplex) {
//					GL11.glVertex3f(Engine.width/2+p[0], Engine.height/2+p[1], p[2]);
//				}
//				GL11.glEnd();
//			}

			if (Vector.cDotVector(simplex.get(simplex.size() - 1), direction) <= 0) {
				return false;
			} else {
				if (contains()) {
					return true;
				}
				else{
					getDirection();
				}
			}
			
			iterations++;
		}
		return false;
	}

	/**
	 * 
	 * @return true if the origin is contained.
	 */
	private static boolean contains() {

		if (simplex.size() < 4) {
			return false;
		}
		
		float[] a = simplex.get(0);
		float[] b = simplex.get(1);
		float[] c = simplex.get(2);
		float[] d = simplex.get(3);
		
		//compute edges
		float[] ac = Vector.cSubVector(c, a);
		float[] ab = Vector.cSubVector(b, a);
		float[] bc = Vector.cSubVector(c, b);
		float[] bd = Vector.cSubVector(d, b);
		float[] ad = Vector.cSubVector(d, a);
		float[] ba = Vector.cScaleVector(ab, -1);
		float[] ao = Vector.cScaleVector(a, -1);
		float[] bo = Vector.cScaleVector(b, -1);
		
		float[] abc = Vector.cCrossVector(ac, ab);
		float[] bcd = Vector.cCrossVector(bc, bd);
		float[] adb = Vector.cCrossVector(ab, ad);
		float[] acd = Vector.cCrossVector(ad, ac);
		
		boolean contains = (
				(Vector.cDotVector(abc, ad) * Vector.cDotVector(abc, ao) >= 0) &&
				(Vector.cDotVector(bcd, ba) * Vector.cDotVector(bcd, bo) >= 0) &&
				(Vector.cDotVector(adb, ac) * Vector.cDotVector(adb, ao) >= 0) &&
				(Vector.cDotVector(acd, ab) * Vector.cDotVector(acd, ao) >= 0));
		return contains;
		
	}
	/**
	 * finds the direction of the origin from a line
	 */
	private static void getLineDirection() {
		
		float[] a = simplex.get(0);
		float[] b = simplex.get(1);
		
		float[] ab = Vector.cSubVector(b, a.clone());
		float[] ao = Vector.cScaleVector(a.clone(), -1);
		
		if (Vector.cDotVector(ab, ao) > 0) {
			direction = Vector.cCrossVector(Vector.cCrossVector(ab, ao), ab);
		} else {
			direction = ao;
		}
		
//		if (Engine.debugToggle) {
//			GL11.glColor3f(1, 0, 0);
//			GL11.glBegin(GL11.GL_LINES);
//			GL11.glVertex3f(Engine.width/2+simplex.get(0)[0], Engine.height/2+simplex.get(0)[1], simplex.get(0)[2]);
//			GL11.glVertex3f(Engine.width/2+simplex.get(1)[0], Engine.height/2+simplex.get(1)[1], simplex.get(1)[2]);
//			GL11.glEnd();
//		}
	}
	/**
	 * finds direction to the origin from a triangle
	 */
	private static void getTriDirection() {
		
		float[] a = simplex.get(0);
		float[] b = simplex.get(1);
		float[] c = simplex.get(2);
		
		float[] ac = Vector.cSubVector(c, a);
		float[] ab = Vector.cSubVector(b, a);
		float[] ao = Vector.cScaleVector(a, -1);
		
		float[] abc = Vector.cCrossVector(ac, ab);
		
		if(Vector.cDotVector(Vector.cCrossVector(abc, ac), ao) > 0) {
			
			if (Vector.cDotVector(ac, ao) > 0) {
				simplex.clear();
				simplex.add(a);
				simplex.add(c);
				direction = Vector.cCrossVector(Vector.cCrossVector(ac, ao), ac);
			} else {
				
				if (Vector.cDotVector(ab, ao) > 0) {
					simplex.clear();
					simplex.add(a);
					simplex.add(b);
					direction = Vector.cCrossVector(Vector.cCrossVector(ab, ao), ab);
				} else {
					
					simplex.clear();
					simplex.add(a);
					direction = ao;
					
				}
				
			}
			
		} else {
			
			if (Vector.cDotVector(Vector.cCrossVector(ab, abc), ao) > 0) {
				
				if (Vector.cDotVector(ab, ao) > 0) {
					
					simplex.clear();
					simplex.add(a);
					simplex.add(b);
					direction = Vector.cCrossVector(Vector.cCrossVector(ab, ao), ab);
					
				} else {
					
					simplex.clear();
					simplex.add(a);
					direction = ao;
					
				}
				
			} else { 
				
				if (Vector.cDotVector(abc, ao) > 0) {
					
					direction = abc;
					
				} else {
					
					simplex.clear();
					simplex.add(a);
					simplex.add(c);
					simplex.add(b);
					
					direction = Vector.cScaleVector(direction, -1);
					
				}
				
			}
			
		}
		
//		if (Engine.debugToggle) {
//			if (simplex.size() == 3) {
//				GL11.glColor3f(0, 0, 1);
//				GL11.glBegin(GL11.GL_LINE_LOOP);
//				GL11.glVertex3f(Engine.width/2+simplex.get(0)[0], Engine.height/2+simplex.get(0)[1], simplex.get(0)[2]);
//				GL11.glVertex3f(Engine.width/2+simplex.get(1)[0], Engine.height/2+simplex.get(1)[1], simplex.get(1)[2]);
//				GL11.glVertex3f(Engine.width/2+simplex.get(2)[0], Engine.height/2+simplex.get(2)[1], simplex.get(2)[2]);
//				GL11.glEnd();
//			}
//		}
	}
	/**
	 * finds the direction of the origin from a Tetrahedron
	 */
	private static void getTetraDirection() {
		
		float[] a = simplex.get(0);
		float[] b = simplex.get(1);
		float[] c = simplex.get(2);
		float[] d = simplex.get(3);
		
		float[] ac = Vector.cSubVector(c, a);
		float[] ab = Vector.cSubVector(b, a);
		float[] ad = Vector.cSubVector(d, a);
		float[] ao = Vector.cScaleVector(a, -1);
		
//		float[] abc = Vector.cCrossVector(ac, ab); //??? not used?
		float[] adb = Vector.cCrossVector(ab, ad);
		float[] acd = Vector.cCrossVector(ad, ac);
		
		int BsideOnACD = Vector.cDotVector(acd, ab) > 0.0f ? 1 : 0;
		int CsideOnADB = Vector.cDotVector(adb, ac) > 0.0f ? 1 : 0;
		
		boolean ABsameAsOrigin = (Vector.cDotVector(acd, ao) > 0 ? 1 : 0) == BsideOnACD;
		boolean ACsameAsOrigin = (Vector.cDotVector(adb, ao) > 0 ? 1 : 0) == CsideOnADB;
		
		if (!ABsameAsOrigin) {
			
			simplex.remove(b);
			
		} else if (!ACsameAsOrigin) {
			
			simplex.remove(c);
			
		} else {
			
			simplex.remove(d);
			
		}
		
//		if (Engine.debugToggle) {
//			if (simplex.size() == 4) {
//				GL11.glColor3f(0, 1, 0);
//				GL11.glBegin(GL11.GL_LINES);
//				GL11.glVertex3f(Engine.width/2+simplex.get(3)[0], Engine.height/2+simplex.get(3)[1], simplex.get(3)[2]);
//				GL11.glVertex3f(Engine.width/2+simplex.get(0)[0], Engine.height/2+simplex.get(0)[1], simplex.get(0)[2]);
//				GL11.glVertex3f(Engine.width/2+simplex.get(3)[0], Engine.height/2+simplex.get(3)[1], simplex.get(3)[2]);
//				GL11.glVertex3f(Engine.width/2+simplex.get(1)[0], Engine.height/2+simplex.get(1)[1], simplex.get(1)[2]);
//				GL11.glVertex3f(Engine.width/2+simplex.get(3)[0], Engine.height/2+simplex.get(3)[1], simplex.get(3)[2]);
//				GL11.glVertex3f(Engine.width/2+simplex.get(2)[0], Engine.height/2+simplex.get(2)[1], simplex.get(2)[2]);
//				GL11.glEnd();
//			}
//		}
		
	}
	/**
	 * calls the above methods to get the direction
	 */
	private static void getDirection() {

		switch(simplex.size()) {
		
		case 4:
			getTetraDirection();
			break;
		case 3:
			getTriDirection();
			break;
		case 2:
			getLineDirection();
			break;
		}
		
	}

	/**
	 * 
	 * @param shape1 Entity one
	 * @param shape2 Entity two
	 * @param vector a vector
	 * @return a third point
	 */
	public static float[] support(Entity shape1,
			Entity shape2, float[] vector) {

		float[] p1 = farthestPoint(shape1.mdl.verticesList, vector.clone());
		float[] p2 = farthestPoint(shape2.mdl.verticesList, Vector.cScaleVector(vector.clone(), -1));
		p1 = Vector.cAddVector(p1, shape1.location);
		p2 = Vector.cAddVector(p2, shape2.location);
		float[] p3 = Vector.cSubVector(p1, p2);

		return p3;

	}

	/**
	 * 
	 * @param shape an arraylist of the shape
	 * @param normal the normals
	 * @return the fartherest point in a direction.
	 */
	private static float[] farthestPoint(ArrayList<float[]> shape, float[] normal) {
		// TODO Sort the vertices by direction and use the commented out
		// algorithm

		int index = 0;
		// int nextIndex = shape.size();
		float max = Float.MIN_NORMAL;
		// float nextMax = Float.MIN_NORMAL;
		// int start = index;
		// int end = nextIndex;
		float temp = 0;
		// int n = 3;
		// int skip = shape.size() / n;
		// int remainder = shape.size() % n;

		for (int i = 0; i < shape.size(); i++) {

			temp = Vector.cDotVector(normal, shape.get(i));

			if (temp > max) {
				max = temp;
				index = i;
			}

		}

		// int iterations = 0;

		// FIXME After the vertices are sorted, fix the implementation of this
		// algorithm.

		// while (skip != 0) {
		//
		// for (int i = start; i <= end+remainder; i += skip) {
		//
		// if ( i < end) {
		// temp = Vector.cDotVector(normal, shape.get(i));
		// } else {
		// temp = Vector.cDotVector(normal, shape.get(end-1));
		// }
		//
		// iterations++;
		// if (temp >= max) {
		// nextMax = max;
		// max = temp;
		// nextIndex = index;
		// index = i;
		// } else if (temp >= nextMax) {
		// nextMax = temp;
		// nextIndex = i;
		// }
		// }
		//
		// if (index < nextIndex) {
		// start = index;
		// end = nextIndex;
		// } else {
		// start = nextIndex;
		// end = index;
		// }
		//
		// if (end-start < n && end-start > 1) {
		// skip = 1;
		// } else if (end-start <= 1) {
		// skip = 0;
		// } else {
		// skip = (end - start) / n;
		// remainder = (end - start) % n;
		// }
		//
		// }
		//
		// System.out.println(iterations + " iterations out of " + shape.size()
		// + " vertices");
		// System.out.println(Arrays.toString(mdl.shape.get(index)));
		return Vector.cScaleVector(shape.get(index).clone(), Engine.scale);
	}
}
