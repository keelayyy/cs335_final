package game;

import java.io.File;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

public class Court {

	private Texture floor_texture;
	private Texture wall_texture;
	private Texture ceiling_texture;

	public static float MAX_X_WIDTH = 40f;
	public static float MAX_Z_WIDTH = 20f;
	public static float WALL_BUFFER = 2f;
	
	public Court(){
		try { 
	        //bind floor and wall texture to GL2 objects
			floor_texture = TextureIO.newTexture(new File("src/textures/court_floor.jpg"), false);
			wall_texture = TextureIO.newTexture(new File("src/textures/court_wall.jpg"), false);
			ceiling_texture = TextureIO.newTexture(new File("src/textures/court_ceiling.jpg"), false);
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
	}
	
	public void draw(final GL2 gl2){
		
		//bind floor texture to GL2 object
		floor_texture.bind(gl2);
		
		//enable texture mapping
		gl2.glTexParameteri(gl2.GL_TEXTURE_2D, gl2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP);
		gl2.glTexParameteri(gl2.GL_TEXTURE_2D, gl2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP);
		gl2.glTexParameteri(gl2.GL_TEXTURE_2D, gl2.GL_TEXTURE_MIN_FILTER, gl2.GL_LINEAR);
		gl2.glTexParameteri(gl2.GL_TEXTURE_2D, gl2.GL_TEXTURE_MAG_FILTER, gl2.GL_LINEAR);
		gl2.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE);
		gl2.glBlendFunc(GL2.GL_SRC_ALPHA,GL2.GL_ONE);
		

        gl2.glEnable(GL.GL_TEXTURE_2D);
		
		//draw floor texture
        gl2.glBegin(GL2.GL_QUADS);
        gl2.glTexCoord2f(0,0);	gl2.glVertex3f(-40.0f, -5.0f, 20.0f);    
        gl2.glTexCoord2f(1,0);	gl2.glVertex3f(40.0f, -5.0f, 20.0f);
        gl2.glTexCoord2f(1,1);	gl2.glVertex3f(40.0f, -5.0f, -20.0f);   
        gl2.glTexCoord2f(0,1);	gl2.glVertex3f(-40.0f, -5.0f, -20.0f); 
		gl2.glEnd();
		
		//bind wall texture to GL2 object
        wall_texture.bind(gl2);
		
        //draw wall texture
        //Front
        gl2.glBegin(GL2.GL_QUADS);
        gl2.glTexCoord2f(0,0);	gl2.glVertex3f(-40.0f, -5.0f, -20.0f);  
        gl2.glTexCoord2f(1,0);	gl2.glVertex3f(40.0f, -5.0f, -20.0f);
        gl2.glTexCoord2f(1,1);	gl2.glVertex3f(40.0f, 10.0f, -20.0f);   
        gl2.glTexCoord2f(0,1);	gl2.glVertex3f(-40.0f, 10.0f, -20.0f); 

        //Back
        gl2.glTexCoord2f(0,0);	gl2.glVertex3f(40.0f, -5.0f, 20.0f);    
        gl2.glTexCoord2f(1,0);	gl2.glVertex3f(-40.0f, -5.0f, 20.0f);
        gl2.glTexCoord2f(1,1);	gl2.glVertex3f(-40.0f, 10.0f, 20.0f);   
        gl2.glTexCoord2f(0,1);	gl2.glVertex3f(40.0f, 10.0f, 20.0f); 

        //Left
        gl2.glTexCoord2f(0,0);	gl2.glVertex3f(-40.0f, -5.0f, 20.0f);    
        gl2.glTexCoord2f(1,0);	gl2.glVertex3f(-40.0f, -5.0f, -20.0f);
        gl2.glTexCoord2f(1,1);	gl2.glVertex3f(-40.0f, 10.0f, -20.0f);   
        gl2.glTexCoord2f(0,1);	gl2.glVertex3f(-40.0f, 10.0f, 20.0f); 

        //Right
        gl2.glTexCoord2f(0,0);	gl2.glVertex3f(40.0f, -5.0f, -20.0f);    
        gl2.glTexCoord2f(1,0);	gl2.glVertex3f(40.0f, -5.0f, 20.0f);
        gl2.glTexCoord2f(1,1);	gl2.glVertex3f(40.0f, 10.0f, 20.0f);   
        gl2.glTexCoord2f(0,1);	gl2.glVertex3f(40.0f, 10.0f, -20.0f); 
        gl2.glEnd();
        
        //bind ceiling texture to GL2 object
        ceiling_texture.bind(gl2);
        
        //draw ceiling texture (in a loop, to preserve square tiles)
        gl2.glBegin(GL2.GL_QUADS);
        for (int delta_x = -30; delta_x < 40; delta_x += 20){
        	for (int delta_z = -10; delta_z < 20; delta_z += 20){
        		//delta_x and _z are at the center point of each desired tile mapping
        		gl2.glTexCoord2f(0,0);	gl2.glVertex3f(delta_x - 10, 10.0f, delta_z + 10);    
                gl2.glTexCoord2f(1,0);	gl2.glVertex3f(delta_x + 10, 10.0f, delta_z + 10);
                gl2.glTexCoord2f(1,1);	gl2.glVertex3f(delta_x + 10, 10.0f, delta_z - 10);   
                gl2.glTexCoord2f(0,1);	gl2.glVertex3f(delta_x - 10, 10.0f, delta_z - 10); 
        	}
        }
        gl2.glEnd();
        
		//disable texture mapping
		gl2.glDisable(GL.GL_TEXTURE_2D);
	}
}