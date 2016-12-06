package game;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;

public class Basket {
	
	//define any textures you'll be using here
	private Texture backboard_texture;
	
	//position of the center of the hoop
	private float x_position, y_position;
	
	public Basket(float x, float y){
		this.x_position = x;
		this.y_position = y;
		try { 
	        //bind floor and wall texture to GL2 objects
			//ball_texture = TextureIO.newTexture(new File("basketball_skin.jpg"), false);
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
	}
	
	//draw the hoop components relative to the x_ and y_position. make them collidable, too.
	public void draw(final GL2 gl2){
		
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
	
}
