package physics;

import java.util.ArrayList;

import com.Engine;
import com.Entity;
import com.Profiler;
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
		
		Profiler.prof("GJKCollision.GJKCollide");

		direction = Vector.cSubVector(entity2.location, entity1.location);
		
		simplex.clear();

		simplex.add(support(entity1, entity2, direction));

		direction = Vector.cScaleVector(direction, -1f);

		int iterations = 0;
		while (iterations < 10) {

			simplex.add(support(entity1, entity2, direction));

			if (Vector.cDotVector(simplex.get(simplex.size() - 1), direction) <= 0) {
				Profiler.prof("GJKCollision.GJKCollide");
				return false;
			} else {
				if (contains()) {
					Profiler.prof("GJKCollision.GJKCollide");
					return true;
				}
				else{
					getDirection();
				}
			}
			
			iterations++;
		}
		Profiler.prof("GJKCollision.GJKCollide");
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
		
	}
	/**
	 * finds direction to the origin froma triangle
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
