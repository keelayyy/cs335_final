package game;

import java.util.Vector;

import javax.vecmath.Vector3f;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;


public class BallPath {
	
	Vector<Vector3f> path = null;
	private boolean scored;
    private int scoredLocation;
	
	public BallPath(Vector3f start, Vector3f startDirection,
			float force, float angle_X, float angle_Y,
			Court court, Basket basket, Ball ball) {
		path = new Vector<Vector3f>();
		scoredLocation = -1;
	    scored = false;
	    float step = 7 * (force / 1000);
	    Vector3f startPos = new Vector3f(new float[]{start.x,start.y,start.z});
	    start.normalize();
	    
	    float d = (float) Math.sqrt(Math.pow(startDirection.z, 2) + Math.pow(startDirection.x, 2));
	    float zStep;
	    float xStep;
	    zStep = (float) ((step * startDirection.z / d) * Math.cos(angle_Y));
	    xStep = (float) ((step * startDirection.x / d) * Math.cos(angle_Y));
	    float yStep = (float) ((step) / Math.cos(angle_Y) / 1.35);
	    
	    Vector3f curPos = new Vector3f(startPos.x,startPos.y,startPos.z);
	    Vector3f accel = new Vector3f(0.0f, -0.01f, 0.0f);
	    Vector3f speed = new Vector3f(xStep,yStep,zStep);

	    while ((path.size() < 4500)) {
 	    	path.add(new Vector3f(curPos));
	        speed.add(accel);
	        curPos.add(speed);
	        float[] xyzCurPos = new float[3];
	        float[] xyzSpeed = new float[3];
	        curPos.get(xyzCurPos);
	        speed.get(xyzSpeed);

	        if (((Math.abs(xyzCurPos[0] - Basket.BACKBOARD_MIN_X) < ball.getRadius()) || (Math.abs(xyzCurPos[0] - Basket.BACKBOARD_MAX_X) < ball.getRadius()))
	        		&& (xyzCurPos[1] >= Basket.BACKBOARD_MIN_Y && xyzCurPos[1] <= Basket.BACKBOARD_MAX_Y) 
	        		&& (xyzCurPos[2] >= -1 * Basket.BACKBOARD_MAX_Z && xyzCurPos[2] <= Basket.BACKBOARD_MAX_Z)) { //backboard
	        	if ((Math.abs(xyzCurPos[0] - Basket.BACKBOARD_MIN_X) < ball.getRadius())
	        			|| (Math.abs(xyzCurPos[0] - Basket.BACKBOARD_MAX_X) < ball.getRadius())) { //hit front or back
	        		xyzSpeed[0] *= -0.5;
	        	}
	        	else if (xyzCurPos[1] >= Basket.BACKBOARD_MIN_Y || xyzCurPos[1] <= Basket.BACKBOARD_MAX_Y) { //hit top or bottom
	        		xyzSpeed[1] *= -0.5;
	        	}
	        	else { //hit sides
	        		xyzSpeed[2] *= -0.5;
	        	}
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

            if (Math.abs(xyzCurPos[0] - basket.getX() + ball.getRadius()) < basket.getHoopRadius()
            		&& Math.abs(xyzCurPos[1] - basket.getY() + ball.getRadius()) < ball.getRadius()
            		&& Math.abs(xyzCurPos[2] - basket.getZ() + ball.getRadius()) < basket.getHoopRadius()) { //ring area
                Vector3f ringLoc = new Vector3f(basket.getX(), basket.getY(), basket.getZ());
                float closestLength = ball.getRadius() * 2;
                int closestLocation = -1;
                for (int i = 0; i < Basket.RIM_FACES * Basket.RIM_FACES; i++) {
                    Vector3f tempVector = basket.getRimVertexList().get(i + 1);
                    tempVector.add(ringLoc);
                    float[] points = new float[]{curPos.x - tempVector.x,curPos.y - tempVector.y,curPos.z - tempVector.z};
                    float testDistance = (float) Math.sqrt(Math.pow(points[0], 2) + Math.pow(points[1], 2) + Math.pow(points[2], 2));
                    if (testDistance < closestLength) {
                        closestLocation = i;
                        closestLength = testDistance;
                        speed.x *= -0.9;
                        speed.y *= -0.9;
                        speed.z *= -0.9;
                        break;
                    }
                }
//                if (closestLocation > 0) {
//                    float mag = (float) Math.sqrt(Math.pow(xyzSpeed[0], 2) + Math.pow(xyzSpeed[1], 2) + Math.pow(xyzSpeed[2], 2));
//                    speed.normalize();
//                    speed.get(xyzSpeed);
//                    Vector3f normalVec = basket.getNormalVertexList().get(closestLocation);
//                    float dotProd = -2 * (speed.dot(normalVec));
//                    float[] xyzNormal = new float[3];
//                    normalVec.get(xyzNormal);
//                    xyzNormal[0] *= dotProd;
//                    xyzNormal[1] *= dotProd;
//                    xyzNormal[2] *= dotProd;
//                    normalVec.set(xyzNormal);
//                    normalVec.add(speed);
//                    normalVec.get(xyzNormal);
//                    xyzNormal[0] *= (mag * 0.8);
//                    xyzNormal[1] *= (mag * 0.8);
//                    xyzNormal[2] *= (mag * 0.8);
//                    normalVec.set(xyzNormal);
//                    speed = normalVec;
//                }
            }
	        //Scored???
	        if (((Math.abs(xyzCurPos[1] - basket.getY()) < 0.5 
	        		&& Math.abs(xyzCurPos[0] - basket.getX()) < 0.5 
	        		&& Math.abs(xyzCurPos[2] - basket.getZ()) < 0.5))) {
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

	public int getScoredLocation() {
		return scoredLocation;
	}
}
