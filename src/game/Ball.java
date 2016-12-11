package game;

import java.io.File;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

public class Ball {
	
	private Texture ball_texture;
	
	private float radius;

	
	public Ball(float r){
		this.radius = r;
		try { 
	        //bind ball texture to GL2 objects
			ball_texture = TextureIO.newTexture(new File("src/textures/basketball_skin.jpg"), false);
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
	}
	
	public void draw(final GL2 gl2, final GLU glu){
		GLUquadric qobj = glu.gluNewQuadric(); 
		glu.gluQuadricTexture(qobj,true); 

		gl2.glEnable(GL.GL_TEXTURE_2D);
		ball_texture.bind(gl2);

		glu.gluSphere(qobj,radius,50,50); 

		glu.gluDeleteQuadric(qobj); 
		gl2.glDisable(GL.GL_TEXTURE_2D);
	}

/////////////////////////////////////Getters and Setters Start Here
	
	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}
}
