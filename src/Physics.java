
public class Physics {
	static float lastLocX = 800;
	static float lastLocY = 600;
	
	static float boundX = 1.0f;
	static float boundY = 1.0f; 
	
	public float[] physics(float delta, float acceleration, float resistance, float rebound, float[] location) { 
		
		
		
		return location;
	}

	public static float deltaX(float delta, float acceleration, float resistance, float maxSpeed, float locX) {
		float speed;
		
		collideX(locX, 100, 1180);
		
		speed = (lastLocX - locX);
		speed += acceleration;
		speed -= speed * resistance / 10;
		speed *= boundX;
		
		//speed = speed + 2;
		System.out.println("speed is " + speed);
		
		if (maxSpeed != -1) {
			if (Math.abs(speed) >= maxSpeed) {
				if (speed >= 0) {
					speed = maxSpeed;
				} else {
					speed = - maxSpeed;
				}
			}
		}
		
		lastLocX = locX;
		locX -= speed;		
		return locX;		
	}
	
	public static float deltaY(float acceleration, float resistance, float maxSpeed, float locY) {
		float speed;
		
		collideY(locY, 100, 620);
		
		speed = (lastLocY - locY);
		speed += acceleration;
		speed -= speed * resistance / 10;
		speed *= boundY;
		
		if (maxSpeed != -1) {
			if (Math.abs(speed) >= maxSpeed) {
				if (speed >= 0) {
					speed = maxSpeed;
				} else {
					speed = - maxSpeed;
				}
			}
		}
		
		
		
		locY -= speed;
		lastLocY = locY + speed;
		return locY;		
	}	
	
	public static void collideX(float locX, float boundLeft, float boundRight) {
		
		if (locX <= boundLeft || locX > boundRight) {
			boundX = -1;
		} else {
			boundX = 1;
		}
	}
	
	public static void collideY(float locY, float boundBot, float boundTop) {
		
		if (locY <= boundBot || locY > boundTop) {
			boundY = -1;
		} else {
			boundY = 1;
		}
	}
}
	