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
	 private String helpMessage0;
	 private String helpMessage1;
	 private String helpMessage2;
	 private String helpMessage3;
	 private String helpMessage4;
	 private String helpMessage5;
	 private String helpPrompt;
	 private String replayMessage;
	 private String retryMessage;
	 private String replayInstructions;
	
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
		
		//color bar yellow
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
		textRenderer.setColor(Color.BLACK);
		textRenderer.draw(message, 5, 565);
		textRenderer.endRendering();
		
	}
	
	public void drawHelpPrompt(final GL2 gl2){
		helpPrompt = "Press [h] for help";
		
		helpPromptRenderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 14));
		
		helpPromptRenderer.beginRendering(600, 600);
		helpPromptRenderer.setColor(Color.WHITE);
		helpPromptRenderer.draw(helpPrompt, 5, 25);
		helpPromptRenderer.endRendering();
	}
	
	public void drawHelpMessage(final GL2 gl2){
		helpMessage0 = "-------------------------------------------------------";
		helpMessage1 = "Use [w, a, s, d] to move";
		helpMessage2 = "Click and drag mouse to change angle";
		helpMessage3 = "Use spacebar to start shot";
		helpMessage4 = "Use spacebar again to shoot";
		helpMessage5 = "Use [h] to close help prompt";

		
		helpMessageRenderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 14));
		
		helpMessageRenderer.beginRendering(600, 600);
		helpMessageRenderer.setColor(Color.YELLOW);
		helpMessageRenderer.draw(helpMessage0, 165, 345);
		helpMessageRenderer.endRendering();
		
		helpMessageRenderer.beginRendering(600, 600);
		helpMessageRenderer.setColor(Color.WHITE);
		helpMessageRenderer.draw(helpMessage1, 220, 330);
		helpMessageRenderer.endRendering();
		
		helpMessageRenderer.beginRendering(600, 600);
		helpMessageRenderer.setColor(Color.WHITE);
		helpMessageRenderer.draw(helpMessage2, 170, 315);
		helpMessageRenderer.endRendering();
		
		helpMessageRenderer.beginRendering(600, 600);
		helpMessageRenderer.setColor(Color.WHITE);
		helpMessageRenderer.draw(helpMessage3, 205, 300);
		helpMessageRenderer.endRendering();
		
		helpMessageRenderer.beginRendering(600, 600);
		helpMessageRenderer.setColor(Color.WHITE);
		helpMessageRenderer.draw(helpMessage4, 197, 285);
		helpMessageRenderer.endRendering();
		
		helpMessageRenderer.beginRendering(600, 600);
		helpMessageRenderer.setColor(Color.WHITE);
		helpMessageRenderer.draw(helpMessage5, 197, 270);
		helpMessageRenderer.endRendering();
		
		helpMessageRenderer.beginRendering(600, 600);
		helpMessageRenderer.setColor(Color.YELLOW);
		helpMessageRenderer.draw(helpMessage0, 165, 255);
		helpMessageRenderer.endRendering();
		
	}
	
	public void drawReplayPrompt(final GL2 gl2){
		replayMessage = "Do you want to see a replay? [y/n]";
		
		//Add text renderer
		textRenderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 20));

		textRenderer.beginRendering(600, 600);
		textRenderer.setColor(Color.BLACK);
		textRenderer.draw(replayMessage, 170, 315);
		textRenderer.endRendering();		
	}
	
	public void drawRetryPrompt(final GL2 gl2){
		retryMessage = "Do you want to retry your shot? [y/n]";
		
		//Add text renderer
		textRenderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 20));

		textRenderer.beginRendering(600, 600);
		textRenderer.setColor(Color.BLACK);
		textRenderer.draw(retryMessage, 170, 315);
		textRenderer.endRendering();	
	}
	
	public void drawReplayInstructions(final GL2 gl2){
		replayInstructions = "Press [z] to slow down, [x] to speed up";
		
		//Add text renderer
		textRenderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 20));

		textRenderer.beginRendering(600, 600);
		textRenderer.setColor(Color.BLACK);
		textRenderer.draw(replayInstructions, 5, 25);
		textRenderer.endRendering();		
	}

	public float getBall_Power() {
		return ball_Power;
	}

	public void setBall_Power(int ball_Power) {
		this.ball_Power = ball_Power;
	}
}