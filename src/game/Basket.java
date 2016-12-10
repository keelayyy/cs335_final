package game;

import java.io.File;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

public class Basket {
	
	//define any textures you'll be using here
	private Texture backboard_texture;
	private Texture net_texture;
	
	//position of the center of the hoop
	private float x_position, y_position, z_position, hoop_radius;
	
	public Basket(float x, float y, float z, float r){
		this.x_position = x;
		this.y_position = y;
		this.z_position = z;
		this.hoop_radius = r;
		try { 
	        //bind backboard texture to GL2 objects
			backboard_texture = TextureIO.newTexture(new File("src/textures/backboard.jpg"), false);
			net_texture = TextureIO.newTexture(new File("src/textures/net.png"), false);
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
	}
	
	//draw the hoop components relative to the x_ and y_position. make them collidable, too.
	public void draw(final GL2 gl2, final GLU glu){
		//draw backboard box
		drawBackboard(gl2);
		
		//draw post holding up the backboard
		drawPost(gl2);

		gl2.glTranslatef(x_position, y_position, z_position);
		
		//draw rim
		drawRim(gl2, hoop_radius * 0.1f, hoop_radius, 50, 50);
		
		//draw net
		drawNet(gl2,glu,hoop_radius,hoop_radius * 2.5f);
		
		gl2.glTranslatef(-x_position, -y_position, -z_position);
		
	}
	
	public void drawBackboard(final GL2 gl2){
		//bind backboard texture to GL2 object
		backboard_texture.bind(gl2);
		
		//enable texture mapping
		gl2.glTexParameteri(gl2.GL_TEXTURE_2D, gl2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP);
		gl2.glTexParameteri(gl2.GL_TEXTURE_2D, gl2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP);
		gl2.glTexParameteri(gl2.GL_TEXTURE_2D, gl2.GL_TEXTURE_MIN_FILTER, gl2.GL_LINEAR);
		gl2.glTexParameteri(gl2.GL_TEXTURE_2D, gl2.GL_TEXTURE_MAG_FILTER, gl2.GL_LINEAR);
		gl2.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE);
		gl2.glBlendFunc(GL2.GL_SRC_ALPHA,GL2.GL_ONE);
		

        gl2.glEnable(gl2.GL_TEXTURE_2D);
		
		//draw backboard texture
        gl2.glBegin(GL2.GL_QUADS);
        gl2.glTexCoord2f(0,0);	gl2.glVertex3f(33f, 1.5f, -4.5f);    
        gl2.glTexCoord2f(1,0);	gl2.glVertex3f(33f, 1.5f, 4.5f);
        gl2.glTexCoord2f(1,1);	gl2.glVertex3f(33f, 6.0f, 4.5f);   
        gl2.glTexCoord2f(0,1);	gl2.glVertex3f(33f, 6.0f, -4.5f); 
		gl2.glEnd();
		gl2.glDisable(gl2.GL_TEXTURE_2D);
		
		//draw thin white box behind backboard (to simulate 3D backboard)
		//gl2.glColor3f(1, 0.6824f, 0);
		gl2.glColor3f(0.9f,0.9f,0.9f);
		//Back 
		gl2.glBegin(GL2.GL_QUADS);
        gl2.glVertex3f(34f, 1.5f, 4.5f);       
        gl2.glVertex3f(34f, 1.5f, -4.5f);   
        gl2.glVertex3f(34f, 6.0f, -4.5f);      
        gl2.glVertex3f(34f, 6.0f, 4.5f); 
        gl2.glEnd();
        
        //Left
		gl2.glBegin(GL2.GL_QUADS);
        gl2.glVertex3f(33f, 1.5f, 4.5f);      
        gl2.glVertex3f(34f, 1.5f, 4.5f);   
        gl2.glVertex3f(34f, 6.0f, 4.5f);      
        gl2.glVertex3f(33f, 6.0f, 4.5f); 
        gl2.glEnd();
        
        //Right 
		gl2.glBegin(GL2.GL_QUADS);
		gl2.glVertex3f(34f, 1.5f, -4.5f);    
        gl2.glVertex3f(33f, 1.5f, -4.5f); 
        gl2.glVertex3f(33f, 6.0f, -4.5f);     
        gl2.glVertex3f(34f, 6.0f, -4.5f); 
        gl2.glEnd();
        
        //Top
		gl2.glBegin(GL2.GL_QUADS);
        gl2.glVertex3f(34f, 6.0f, -4.5f);       
        gl2.glVertex3f(33f, 6.0f, -4.5f);  
        gl2.glVertex3f(33f, 6.0f, 4.5f);      
        gl2.glVertex3f(34f, 6.0f, 4.5f); 
        gl2.glEnd();
        
        //Bottom 
		gl2.glBegin(GL2.GL_QUADS);
        gl2.glVertex3f(34f, 1.5f, 4.5f);      
        gl2.glVertex3f(33f, 1.5f, 4.5f);   
        gl2.glVertex3f(33f, 1.5f, -4.5f);      
        gl2.glVertex3f(34f, 1.5f, -4.5f); 
        gl2.glEnd();
	}
	
	public void drawPost(final GL2 gl2){
		//draw base layer (black)
		gl2.glColor3f(0.1f,0.1f,0.1f);
        //Left
		gl2.glBegin(GL2.GL_QUADS);
        gl2.glVertex3f(34f, 3.0f, 1.0f);      
        gl2.glVertex3f(40f, 8.0f, 1.0f);   
        gl2.glVertex3f(40f, 10.0f, 1.0f);      
        gl2.glVertex3f(34f, 5.0f, 1.0f); 
        gl2.glEnd();
        
        //Right 
        gl2.glBegin(GL2.GL_QUADS);
        gl2.glVertex3f(34f, 3.0f, -1.0f);      
        gl2.glVertex3f(40f, 8.0f, -1.0f);   
        gl2.glVertex3f(40f, 10.0f, -1.0f);      
        gl2.glVertex3f(34f, 5.0f, -1.0f); 
        gl2.glEnd();
        
        //Top
		gl2.glBegin(GL2.GL_QUADS);
        gl2.glVertex3f(34f, 5.0f, -1.0f);       
        gl2.glVertex3f(40f, 10.0f, -1.0f);  
        gl2.glVertex3f(40f, 10.0f, 1.0f);      
        gl2.glVertex3f(34f, 5.0f, 1.0f); 
        gl2.glEnd();
        
		//Bottom
		gl2.glBegin(GL2.GL_QUADS);
		gl2.glVertex3f(34f, 3.0f, -1.0f);       
		gl2.glVertex3f(40f, 8.0f, -1.0f);  
		gl2.glVertex3f(40f, 8.0f, 1.0f);      
		gl2.glVertex3f(34f, 3.0f, 1.0f); 
		gl2.glEnd();
	}
	
	//draws a ring. original implementation taken from
	//http://www.java2s.com/Code/Java/SWT-JFace-Eclipse/drawarotatingtorususingtheJOGLOpenGLbinding.htm
	public void drawRim(final GL2 gl2, float r, float R, int nsides, int rings) {
		gl2.glColor3f(0.6784f,0.4196f,0);
		float ringDelta = 2.0f * (float) Math.PI / rings;
		float sideDelta = 2.0f * (float) Math.PI / nsides;
		float theta = 0.0f, cosTheta = 1.0f, sinTheta = 0.0f;
		for (int i = rings - 1; i >= 0; i--) {
			float theta1 = theta + ringDelta;
			float cosTheta1 = (float) Math.cos(theta1);
			float sinTheta1 = (float) Math.sin(theta1);
			gl2.glBegin(GL2.GL_QUAD_STRIP);
			float phi = 0.0f;
			for (int j = nsides; j >= 0; j--) {
				phi += sideDelta;
				float cosPhi = (float) Math.sin(phi);
				float sinPhi = (float) Math.cos(phi);
				float dist = R + r * cosPhi;
				gl2.glNormal3f(-sinTheta1 * cosPhi,sinPhi,  cosTheta1 * cosPhi);
				gl2.glVertex3f( -sinTheta1 * dist,r * sinPhi, cosTheta1 * dist);
				gl2.glNormal3f( -sinTheta * cosPhi,sinPhi, cosTheta * cosPhi);
				gl2.glVertex3f( -sinTheta * dist,r * sinPhi, cosTheta * dist);
			}
			gl2.glEnd();
			theta = theta1;
			cosTheta = cosTheta1;
			sinTheta = sinTheta1;
		}
	}
	
	public void drawNet(final GL2 gl2, final GLU glu, float radius, float height) {
		gl2.glEnable(GL.GL_BLEND);
		gl2.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		gl2.glRotatef(90f, 1, 0, 0);
		GLUquadric qobj = glu.gluNewQuadric(); 
		glu.gluQuadricTexture(qobj,true); 

		gl2.glEnable(GL.GL_TEXTURE_2D);
		net_texture.bind(gl2);
		glu.gluCylinder(qobj,radius,radius * 0.8f,height,50,50); 

		glu.gluDeleteQuadric(qobj); 
		gl2.glRotatef(-90f, 1, 0, 0);
		gl2.glDisable(GL.GL_TEXTURE_2D);
		gl2.glDisable(GL.GL_BLEND);
	}

/////////////////////////////////////Getters and Setters Start Here

	public float getX() {
		return x_position;
	}

	public void setX(float x_position) {
		this.x_position = x_position;
	}

	public float getY() {
		return y_position;
	}

	public void setY(float y_position) {
		this.y_position = y_position;
	}

	public float getZ_position() {
		return z_position;
	}

	public void setZ_position(float z_position) {
		this.z_position = z_position;
	}

	public float getHoop_radius() {
		return hoop_radius;
	}

	public void setHoop_radius(float hoop_radius) {
		this.hoop_radius = hoop_radius;
	}
}
