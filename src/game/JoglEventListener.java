package game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.awt.TextRenderer;




public class JoglEventListener extends GLCanvas implements GLEventListener, KeyListener, MouseListener, MouseMotionListener {
	
	private static Animator animator;

    private GLU glu = new GLU();
    
    private boolean check = false;
    private boolean helpCheck = false;
    
    //size of power bar and power bar counter to count twice
    private float barSize = 0;
    private float barCounter = 0;
    private float ballPower = 0;
    
    //Text renderer for ballPower
    private TextRenderer textRenderer;
    
    //declare court, basket, and ball objects
    Court court = null;
    Ball ball = null;
    Basket basket = null;
    HUD hud = null;
	
	private float camera_angle_X = 0;
	private float camera_angle_Y = 0.25f;
	private float camera_X = 0;
	private float camera_Y = 0;
	private float camera_Z = 0;
	private float camera_lookat_X = 0;
	private float camera_lookat_Y = 0;
	private float camera_lookat_Z = 0;
	
	private float mouseX0;
	private float mouseY0;
	
	private float dragX;
	private float dragY;
	
	private boolean ShootMode = false;
	

	float windowWidth, windowHeight;
	
	public JoglEventListener(int width, int height, GLCapabilities capabilities) {
		super(capabilities);
		setSize(width, height);
		addGLEventListener(this);
	}

	public void reshape(GLAutoDrawable glDrawable, int x, int y, int width, int height) {
		final GL2 gl2 = glDrawable.getGL().getGL2();
		this.windowWidth = width;
		this.windowHeight = height;
		gl2.glViewport(0, 0, width, height);
	}
	
	
	public void displayChanged(GLAutoDrawable gLDrawable, boolean modeChanged, boolean deviceChanged) {
		
	}

	/** Called by the drawable immediately after the OpenGL context is
	 * initialized for the first time. Can be used to perform one-time OpenGL
	 * initialization such as setup of lights and display lists.
	 * @param gLDrawable The GLAutoDrawable object.
	 */
	public void init(GLAutoDrawable gLDrawable) {
		final GL2 gl2 = gLDrawable.getGL().getGL2();
		gl2.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);    // Black Background
		gl2.glClearDepth(1.0f);                      // Depth Buffer Setup

		//Add event listeners for interactive functionality
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		
		court = new Court();
		ball = new Ball(0.75f);
		basket = new Basket(31.5f,2f,0f,1.0f);
		hud = new HUD(0f, 0f, ballPower);
		
		//Start animator
    	animator = new Animator(this);
    	animator.start();
	}

	public void drawCourt(GL2 gl2){
		court.draw(gl2);
	}
	
	public void drawBasket(GL2 gl2){
		basket.draw(gl2,glu);
	}
	
	public void drawBall(GL2 gl2){
		//getting there...
		//set ball position based on where we're looking
		//gl2.glTranslated(Math.sin(camera_angle_X),Math.sin(camera_angle_Y) - 3.0,Math.cos(camera_angle_X));
		gl2.glTranslatef(-camera_X, -camera_Y, -camera_Z);
//		float ball_X = camera_lookat_X + 2;
//		float ball_Y = camera_lookat_Y - 0.1f;
//		float ball_Z = camera_lookat_Z + 2;
//		gl2.glTranslatef(ball_X,ball_Y,ball_Z);
		ball.draw(gl2,glu);
		gl2.glTranslatef(camera_X, camera_Y, camera_Z);
		//gl2.glTranslatef(-camera_angle_X,-camera_angle_Y,0f);
		//gl2.glTranslated(-2f * Math.sin(camera_angle_X),-2f *Math.sin(camera_angle_Y),-2f *Math.cos(camera_angle_X));
	}
	
	public void drawHUD(GL2 gl2, float barSize){
		hud.draw(gl2, barSize);
		if (check == true){
			hud.drawBallPower(gl2);
		}
	}
	
	
    @Override
	public void display(GLAutoDrawable gLDrawable) {
		// TODO Auto-generated method stub
		final GL2 gl2 = gLDrawable.getGL().getGL2();
        
		setup3D(gl2);
		render3D(gl2);
		
		setup2D(gl2);
		render2D(gl2);
		if (ShootMode == true){
			if(barSize < 300){
				barSize = barSize + 5;
			}
			else{
				barSize = 0;
				barCounter++;
				if (barCounter == 2){
					ShootMode = false;
					barCounter = 0;
				}
			}
		}
		
	}
    
    public void setup3D(final GL2 gl2){
		////////////3D Rendering
		gl2.glEnable(GL.GL_DEPTH_TEST);              // Enables Depth Testing
		gl2.glDepthFunc(GL.GL_LEQUAL);               // The Type Of Depth Testing To Do
		gl2.glMatrixMode(GL2.GL_PROJECTION);
	    gl2.glLoadIdentity();
	    // camera perspective setup
	    float widthHeightRatio = (float) getWidth() / (float) getHeight();
	    glu.gluPerspective(45, widthHeightRatio, 1,1000);
	    camera_lookat_X = (float) (camera_X + (Math.cos(camera_angle_X)));
		camera_lookat_Y = (float) (camera_Y - 3.0 + (Math.sin(camera_angle_Y)));
		camera_lookat_Z = (float) (camera_Z + (Math.sin(camera_angle_X)));
		//System.out.println(camera_lookat_X);
		//System.out.println(camera_lookat_Y);
	    glu.gluLookAt(camera_X,camera_Y - 3.0,camera_Z,
	    		camera_lookat_X, 
	    		camera_lookat_Y, 
	    		camera_lookat_Z,
	    		0,1,0);
    }
    
    public void render3D(final GL2 gl2){
		gl2.glPushMatrix();

	    gl2.glMatrixMode(GL2.GL_MODELVIEW);
	    gl2.glLoadIdentity();
		gl2.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		gl2.glPushMatrix();

		drawCourt(gl2);
		drawBasket(gl2);
		drawBall(gl2);
        
        gl2.glPopMatrix();
    }
    
    public void setup2D(final GL2 gl2){
        /////////////2D Rendering
        gl2.glMatrixMode(GL2.GL_PROJECTION);
        gl2.glLoadIdentity();

        glu.gluOrtho2D(0.0f, windowWidth, windowHeight, 0.0f);

        gl2.glMatrixMode(GL2.GL_MODELVIEW);
        gl2.glLoadIdentity();
        gl2.glTranslatef(0,0, 0.0f);

        gl2.glDisable(GL.GL_DEPTH_TEST);
    }
    
    public void render2D(final GL2 gl2){
        gl2.glPushMatrix();
        
        drawHUD(gl2, barSize);
        if (helpCheck == false){
        	hud.drawHelpPrompt(gl2);
        }
        else if (helpCheck == true){
        	hud.drawHelpMessage(gl2);
        }
        
        gl2.glPopMatrix();
    }
	
	/*public void drawHUD(final GL2 gl2){
		//this should ideally show the score and some directions
		gl2.glBegin(GL2.GL_QUADS);
		gl2.glColor3f(1, 0, 0);
		gl2.glVertex2f(.1f,.1f);
		gl2.glVertex2f(.2f,.1f);
		gl2.glVertex2f(.2f,.2f);
		gl2.glVertex2f(.1f,.2f);
		gl2.glEnd();
	}*/
	
	
	//control lateral (X-Z) movement through environment
	@Override
	public void keyTyped(KeyEvent e) {
		char key = e.getKeyChar();
		
		switch(key){
		case 'w':
			//check for out of bounds and don't update variable if we hit a wall
			if (Math.abs(camera_X + .3f * Math.sin((Math.PI/2) - camera_angle_X)) <= (Court.MAX_X_WIDTH - Court.WALL_BUFFER)){
				camera_X += .3f * Math.sin((Math.PI/2) - camera_angle_X);
			}
			if (Math.abs(camera_Z + .3f * Math.cos((Math.PI/2) - camera_angle_X)) <= (Court.MAX_Z_WIDTH - Court.WALL_BUFFER)){
				camera_Z += .3f * Math.cos((Math.PI/2) - camera_angle_X);
			}
			break;
		case 'a':
			if (Math.abs(camera_X - .3f * Math.cos((Math.PI/2) + camera_angle_X)) <= (Court.MAX_X_WIDTH - Court.WALL_BUFFER)){
				camera_X -= .3f * Math.cos((Math.PI/2) + camera_angle_X);
			}
			
			if (Math.abs(camera_Z - .3f * Math.sin((Math.PI/2) - camera_angle_X)) <= (Court.MAX_Z_WIDTH - Court.WALL_BUFFER)){
				camera_Z -= .3f * Math.sin((Math.PI/2) - camera_angle_X);
			}
			break;
		case 's':
			if (Math.abs(camera_X - .3f * Math.sin((Math.PI/2) - camera_angle_X)) <= (Court.MAX_X_WIDTH - Court.WALL_BUFFER)){
				camera_X -= .3f * Math.sin((Math.PI/2) - camera_angle_X);
			}
			
			if (Math.abs(camera_Z - .3f * Math.cos((Math.PI/2) - camera_angle_X)) <= (Court.MAX_Z_WIDTH - Court.WALL_BUFFER)){
				camera_Z -= .3f * Math.cos((Math.PI/2) - camera_angle_X);
			}
			break;
		case 'd':
			if (Math.abs(camera_X + .3f * Math.cos((Math.PI/2) + camera_angle_X)) <= (Court.MAX_X_WIDTH - Court.WALL_BUFFER)){
				camera_X += .3f * Math.cos((Math.PI/2) + camera_angle_X);
			}
			
			if (Math.abs(camera_Z + .3f * Math.sin((Math.PI/2) - camera_angle_X)) <= (Court.MAX_Z_WIDTH - Court.WALL_BUFFER)){
				camera_Z += .3f * Math.sin((Math.PI/2) - camera_angle_X);
			}
			break;
		}
		//System.out.println("X: " + camera_X + "\tLookat X: " + camera_lookat_X);
		//System.out.println("Y: " + camera_Y + "\tLookat Y: " + camera_lookat_Y);
		//System.out.println("Z: " + camera_Z + "\tLookat Z: " + camera_lookat_Z);
	}
	
	//determine location of click
	@Override
	public void mousePressed(MouseEvent e) {
		mouseX0 = (e.getX()-800*0.5f)*40/800;
		mouseY0 = -(e.getY()-800*0.5f)*40/800;
		dragX = mouseX0;
		dragY = mouseY0;
	}
	
	//change view direction based on the mouse position relative to the place
	//it was first clicked.
	@Override
	public void mouseDragged(MouseEvent e) {
		float XX = (e.getX()-800*0.5f)*40/800;
		float YY = -(e.getY()-800*0.5f)*40/800;
		boolean directionX;
		boolean directionY;
		
		float dragChangeX = Math.abs(dragX - XX);
		float dragChangeY = Math.abs(dragY - YY);
		
		if (dragX <= XX){
			directionX = true;
		}
		else{
			directionX = false;
		}
		if (dragY <= YY){
			directionY = true;
		}
		else{
			directionY = false;
		}
		
		dragX = XX;
		dragY = YY;

		if (directionX){
			camera_angle_X += dragChangeX / 5;
		}
		else if(!directionX){
			camera_angle_X -= dragChangeX / 5;
		}
		//if we're changing the Y-direction, keep the camera pointed in the same direction if
		//we're already looking straight up or down
		if (directionY && (camera_angle_Y < (Math.PI / 2.0f))){
			camera_angle_Y += dragChangeY / 5;
		}
		else if (!directionY && (camera_angle_Y > (-1 * Math.PI / 2.0f))){
			camera_angle_Y -= dragChangeY / 5;
		}
		
		//reset X angle in case we rotate all the way around
		if (camera_angle_X < -2 * Math.PI) {
			camera_angle_X = (float) (-1 * ((-1 * camera_angle_X) % (2 * Math.PI)));
		}
		else if (camera_angle_X > 2 * Math.PI) {
			camera_angle_X %= 2 * Math.PI;
		}
		//System.out.println("X: " + Math.sin(camera_angle_X));
		//System.out.println("Y: " + (Math.sin(camera_angle_Y) - 3.0));
		//System.out.println("Z: " + Math.cos(camera_angle_X));
	}
			
	@Override
	public void dispose(GLAutoDrawable arg0) {
	
	}
	
	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
	
	@Override
	public void mouseMoved(MouseEvent arg0) {
		
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println("Key Pressed: " + e);
		char key = e.getKeyChar();
		
		//if space bar is pressed, start shoot mode
		if (key == ' ' && ShootMode == false){
			ShootMode = true;
			check = false;
		}
		//gets shot power from second press of space bar
		//and resets power bar
		else if (key == ' ' && ShootMode == true){
			ballPower = barSize / 3;
			System.out.println("BallPower: " + ballPower);
			hud.setBall_Power((int) ballPower); 
			ShootMode = false;
			barSize = 0;
			barCounter = 0;
			check = true;
		}
		//displays help text if h is pressed
		else if (key == 'h' && helpCheck == false){
			helpCheck = true;
		}
		//conceal help text when h is pressed again
		else if (key == 'h' && helpCheck == true){
			helpCheck = false;
		}
	}
}