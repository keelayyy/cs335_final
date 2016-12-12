package game;

import java.util.Vector;

import javax.vecmath.Vector3f;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;


public class BallPath {
	
	Vector<Vector3f> path = null;
	private boolean scored;
    private int scoredLocation;
	
	public BallPath(Vector3f start, 
			float force, float angle_X, float angle_Y,
			Court court, Basket basket, Ball ball) {
		path = new Vector<Vector3f>();
		scoredLocation = -1;
	    scored = false;
	    System.out.println("X: " + start.x);
	    System.out.println("Y: " + start.y);
	    System.out.println("Z: " + start.z);
	    int collideNextPoint = 0;
	    float step = (force / 1000);
	    float xChange = start.x;
	    float yChange = start.y;
	    float zChange = start.z;
	    
	    float velocity = (float) Math.sqrt(Math.pow(zChange, 2) + Math.pow(xChange, 2) + Math.pow(yChange, 2));
	    float zStep = (float) (7 * (step * zChange / velocity) * Math.sin(angle_X));
	    float xStep = (float) (7 * (step * xChange / velocity) * Math.cos(angle_X));
	    float yStep = (float) (7 * (step) / Math.cos(angle_Y));
	    
	    Vector3f curPos = new Vector3f(xChange,yChange,zChange);
	    Vector3f accel = new Vector3f(0.0f, -0.01f, 0.0f);
	    Vector3f speed = new Vector3f(xStep,yStep,zStep);
	    
	    int rollingFrames = 0;

	    while ((path.size() < 4500)) {
	    	if (speed.length() < 0.005){
	    		rollingFrames++;
	    	}
	    	else {
	    		rollingFrames = 0;
	    	}
	    	if (rollingFrames > 10){
	    		accel.x = -0.1f;
	    		accel.z = -0.1f;
	    	}
	    	if (speed.x < 0) {
	    		speed.x = 0;
	    	}
	    	if (speed.z < 0){
	    		speed.z = 0;
	    	}
	    	path.add(new Vector3f(curPos));
	        speed.add(accel);
	        curPos.add(speed);
	        float[] xyzCurPos = new float[3];
	        float[] xyzSpeed = new float[3];
	        curPos.get(xyzCurPos);
	        speed.get(xyzSpeed);

	        if ((Math.abs(xyzCurPos[0] - Basket.BACKBOARD_MAX_X) < ball.getRadius()) 
	        		&& (xyzCurPos[1] >= Basket.BACKBOARD_MIN_Y && xyzCurPos[1] <= Basket.BACKBOARD_MAX_Y) 
	        		&& (xyzCurPos[2] >= -1 * Basket.BACKBOARD_MAX_Z && xyzCurPos[2] <= Basket.BACKBOARD_MAX_Z)) { //backboard
	        	xyzSpeed[0] *= 0.9;
	        	xyzSpeed[2] *= 0.9;
	        }
	        
	        if (xyzCurPos[0] - ball.getRadius() <= -1 * Court.MAX_X_WIDTH 
	        		|| xyzCurPos[0] + ball.getRadius() >= Court.MAX_X_WIDTH) { //front and back walls
	        	if (xyzCurPos[0] - ball.getRadius() < -1 * Court.MAX_X_WIDTH){
	        		xyzCurPos[0] = -1 * Court.MAX_X_WIDTH + ball.getRadius();
	        	}
	        	else {
	        		xyzCurPos[0] = Court.MAX_X_WIDTH - ball.getRadius();
	        	}
	        	xyzSpeed[0] *= -0.9;
	        }
			if (xyzCurPos[1] - ball.getRadius() <= Court.MIN_Y_HEIGHT 
					|| xyzCurPos[1] + ball.getRadius() >= Court.MAX_Y_HEIGHT) { //ceiling and floor
				if (xyzCurPos[1] - ball.getRadius() < Court.MIN_Y_HEIGHT){
	        		xyzCurPos[1] = Court.MIN_Y_HEIGHT + ball.getRadius();
	        	}
	        	else {
	        		xyzCurPos[1] = Court.MAX_Y_HEIGHT - ball.getRadius();
	        	}
	        	xyzSpeed[1] *= -0.7;
	        }
	        if (xyzCurPos[2] - ball.getRadius() <= -1 * Court.MAX_Z_WIDTH 
	        		|| xyzCurPos[2] + ball.getRadius() >= Court.MAX_Z_WIDTH) { //side walls
				if (xyzCurPos[2] - ball.getRadius() < -1 * Court.MAX_Z_WIDTH){
	        		xyzCurPos[2] = -1 * Court.MAX_Z_WIDTH + ball.getRadius();
	        	}
	        	else {
	        		xyzCurPos[2] = Court.MAX_Z_WIDTH - ball.getRadius();
	        	}
	        	xyzSpeed[2] *= -0.9;
	        }
	        curPos.set(xyzCurPos);
	        speed.set(xyzSpeed);
			//Collision
	        if (collideNextPoint == 0) {
	            if (xyzCurPos[0] - basket.getX() < basket.getHoopRadius() - basket.getHoopRadius() * 0.1
	            		&& xyzCurPos[1] - basket.getY() < ball.getRadius()
	            		&& xyzCurPos[2] - basket.getZ() < basket.getHoopRadius() - basket.getHoopRadius() * 0.1) { //ring area
	                Vector3f ringLoc = new Vector3f(basket.getX(), basket.getY(), basket.getZ());
	                float closestLength = ball.getRadius();
	                int closestLocation = -1;
	                for (int i = 0; i < Basket.RIM_FACES * Basket.RIM_FACES; i++) {
	                    Vector3f tempVector = basket.getRimVertexList().get(i + 1);
	                    tempVector.add(ringLoc);
	                    float[] points = new float[]{curPos.x - tempVector.x,curPos.y - tempVector.y,curPos.z - tempVector.z};
	                    float testDistance = (float) Math.sqrt(Math.pow(points[0], 2) + Math.pow(points[1], 2) + Math.pow(points[2], 2));
	                    if (testDistance < closestLength) {
	                        closestLocation = i;
	                        closestLength = testDistance;
	                    }
	                }
	                if (closestLocation > 0) {
	                    float mag = (float) Math.sqrt(Math.pow(xyzSpeed[0], 2) + Math.pow(xyzSpeed[1], 2) + Math.pow(xyzSpeed[2], 2));
	                    speed.normalize();
	                    speed.get(xyzSpeed);
	                    Vector3f normalVec = basket.getNormalVertexList().get(closestLocation);
	                    float dotProd = -2 * (speed.dot(normalVec));
	                    float[] xyzNormal = new float[3];
	                    normalVec.get(xyzNormal);
	                    xyzNormal[0] *= dotProd;
	                    xyzNormal[1] *= dotProd;
	                    xyzNormal[2] *= dotProd;
	                    normalVec.set(xyzNormal);
	                    normalVec.add(speed);
	                    normalVec.get(xyzNormal);
	                    xyzNormal[0] *= (mag * 0.8);
	                    xyzNormal[1] *= (mag * 0.8);
	                    xyzNormal[1] *= (mag * 0.8);
	                    normalVec.set(xyzNormal);
	                    speed = normalVec;
	                    if (xyzCurPos[1] - ball.getRadius() < Court.MIN_Y_HEIGHT){
	                    	collideNextPoint = 10;
	                    }
//	                    else {
//	                    	collideNextPoint = 3;
//	                    }
	                }
	            }
	        } else {
	            collideNextPoint--;
	        }
	        //Scored???
	        if (((xyzCurPos[1] - basket.getY() < 0.15 && xyzCurPos[0] - basket.getX() < 0.15 && xyzCurPos[2] - basket.getZ() < 0.15))) {
	            scored = true;
	            if (scoredLocation < 0) {
	            	scoredLocation = path.size();
	            }
	        }
	    }
	}
	
	public Vector3f getPathPosition(int position){
		if (position >= 0 && position < path.size()){
			return path.get(position);
		}
		else return null;
	}
	
	public int getNumSteps(){
		return path.size();
	}
	
	public boolean isScored(){
		return scored;
	}
	
	//actually displays the path
	public void showPath(final GL2 gl2, final GLU glu, Ball ball, int shootFrame){
		gl2.glPushMatrix();
		ball.draw(gl2, glu, path.get(shootFrame));
		gl2.glPopMatrix();
	}
}
