package game;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

public class HUD {
	
	private float x_position, y_position;
	
	public HUD(float x, float y){
		this.x_position = x;
		this.y_position = y;
	}
	
	public void draw(final GL2 gl2, float barSize){
		drawPBBackground(gl2);
		drawPB(gl2, barSize);
	}
	
	//draw yellow background bar for power bar
	public void drawPBBackground(final GL2 gl2){
		
		//color bar white
		gl2.glColor3f(1f, 1f, 0f);
		
		gl2.glBegin(GL2.GL_QUADS);
		gl2.glVertex2f(0f, 0f);
		gl2.glVertex2f(300f, 0f);
		gl2.glVertex2f(300f, 20f);
		gl2.glVertex2f(0f, 20f);
		gl2.glEnd();
		
		
	}
	
	//draw red power bar
	public void drawPB(final GL2 gl2, float barSize){
		
		//color bar red
		gl2.glColor3f(1f, 0f, 0f);
		
		gl2.glBegin(GL2.GL_QUADS);
		gl2.glVertex2f(0f, 0f);
		gl2.glVertex2f(barSize, 0f);
		gl2.glVertex2f(barSize, 20f);
		gl2.glVertex2f(0f,  20f);
		gl2.glEnd();
		
	}
}