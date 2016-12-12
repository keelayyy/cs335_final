package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Rectangle2D;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.awt.TextRenderer;

public class HUD {
	
	private float x_position, y_position;
	private float ball_Power = 0;
	
	//Text renderer and message
	 private TextRenderer textRenderer;
	 private TextRenderer helpPromptRenderer;
	 private TextRenderer helpMessageRenderer;
	 private String message;
	 private String helpMessage;
	 private String helpPrompt;
	
	public HUD(float x, float y, float ballPower){
		this.x_position = x;
		this.y_position = y;
		this.ball_Power = ballPower;
	}
	
	public void draw(final GL2 gl2, float barSize){
		drawPBBackground(gl2);
		drawPB(gl2, barSize);
		//drawBallPower(gl2);
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
	
	public void drawBallPower(final GL2 gl2){
		message = "Ball Power: " + Float.toString(ball_Power);
		
		//Add text renderer
		textRenderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 14));

		textRenderer.beginRendering(600, 600);
		textRenderer.setColor(Color.RED);
		textRenderer.draw(message, 5, 565);
		textRenderer.endRendering();
		
	}
	
	public void drawHelpPrompt(final GL2 gl2){
		helpPrompt = "Press [h] for help";
		
		helpPromptRenderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 14));
		
		helpPromptRenderer.beginRendering(600, 600);
		helpPromptRenderer.setColor(Color.RED);
		helpPromptRenderer.draw(helpPrompt, 5, 25);
		helpPromptRenderer.endRendering();
	}
	
	public void drawHelpMessage(final GL2 gl2){
		helpMessage = "Use [w, a, s, d] to move.";
		
		helpMessageRenderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 14));
		
		helpMessageRenderer.beginRendering(600, 600);
		helpMessageRenderer.setColor(Color.RED);
		helpMessageRenderer.draw(helpMessage, 5, 25);
		helpMessageRenderer.endRendering();
	}

	public float getBall_Power() {
		return ball_Power;
	}

	public void setBall_Power(int ball_Power) {
		this.ball_Power = ball_Power;
	}
}