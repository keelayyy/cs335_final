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

	
	public Ball(){
		try { 
	        //bind ball texture to GL2 objects
			ball_texture = TextureIO.newTexture(new File("basketball_skin.jpg"), false);
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
	}
	
	public void draw(final GL2 gl2, final GLU glu){
		
		//bind floor texture to GL2 object
		//ball_texture.bind(gl2);
		
		//enable texture mapping
//		gl2.glTexParameteri(gl2.GL_TEXTURE_2D, gl2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP);
//		gl2.glTexParameteri(gl2.GL_TEXTURE_2D, gl2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP);
//		gl2.glTexParameteri(gl2.GL_TEXTURE_2D, gl2.GL_TEXTURE_MIN_FILTER, gl2.GL_LINEAR);
//		gl2.glTexParameteri(gl2.GL_TEXTURE_2D, gl2.GL_TEXTURE_MAG_FILTER, gl2.GL_LINEAR);
//		gl2.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE);
//		gl2.glBlendFunc(GL2.GL_SRC_ALPHA,GL2.GL_ONE);

//        gl2.glEnable(GL.GL_TEXTURE_2D);
//		
//		GLUquadric ball = glu.gluNewQuadric();
//		glu.gluQuadricDrawStyle(ball, GLU.GLU_FILL);
//		glu.gluQuadricTexture(ball, true);
//		glu.gluQuadricNormals(ball, GLU.GLU_SMOOTH);
//		ball_texture.bind(gl2);
//		glu.gluSphere(ball, 10.0, 20, 20);
//		glu.gluDeleteQuadric(ball);
		
		GLUquadric qobj = glu.gluNewQuadric(); 

		glu.gluQuadricTexture(qobj,true); 

		gl2.glEnable(GL.GL_TEXTURE_2D);
		ball_texture.bind(gl2);

		glu.gluSphere(qobj,1.5,50,50); 

		glu.gluDeleteQuadric(qobj); 
		gl2.glDisable(GL.GL_TEXTURE_2D);
		
		
        
		//disable texture mapping
		gl2.glDisable(GL.GL_TEXTURE_2D);
	}
}
