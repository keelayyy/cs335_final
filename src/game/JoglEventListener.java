package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.vecmath.Vector3f;

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
    
    private boolean powerCheck = false;
    private boolean helpCheck = false;
    
    //size of power bar and power bar counter to count twice
    private float barSize = 0;
    private float barCounter = 0;
    private float ballPower = 0;
    
    //true if currently displaying a shot, false otherwise
    private boolean shootingMode = false;
    private int shootFrame = 0;
    private boolean reshot = false;
    private boolean miss = false;
    
    //true if asking for replay, false otherwise
    private boolean askForReplay = false;
    
    //Text renderer for ballPower
    private TextRenderer textRenderer;
    
    //declare court, basket, and ball objects
    Court court = null;
    Ball ball = null;
    Basket basket = null;
    HUD hud = null;
    BallPath path = null;
	
	private float camera_angle_X = 0;
	private float camera_angle_Y = 0.0f;
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
		if (!shootingMode){
			shootFrame = 0;
			path = null;
			gl2.glPushMatrix();
			Vector3f vec = new Vector3f((float)(camera_lookat_X + (5* (Math.cos(camera_angle_X)))), 
					(float)(camera_lookat_Y + (5* (Math.sin(camera_angle_Y)))), 
					(float)(camera_lookat_Z + (5* (Math.sin(camera_angle_X)))));
			//check for ball going through walls or the floor
			if (vec.x - ball.getRadius() < -1 * Court.MAX_X_WIDTH){
				vec.x = -1 * Court.MAX_X_WIDTH + ball.getRadius();
			}
			if (vec.x + ball.getRadius() > Court.MAX_X_WIDTH){
				vec.x = Court.MAX_X_WIDTH - ball.getRadius();
			}
			if (vec.y - ball.getRadius() < Court.MIN_Y_HEIGHT){
				vec.y = Court.MIN_Y_HEIGHT + ball.getRadius();
			}
			if (vec.z - ball.getRadius() < -1 * Court.MAX_Z_WIDTH){
				vec.z = -1 * Court.MAX_Z_WIDTH + ball.getRadius();
			}
			if (vec.z + ball.getRadius() > Court.MAX_Z_WIDTH){
				vec.z = Court.MAX_Z_WIDTH - ball.getRadius();
			}
			ball.draw(gl2,glu,vec);
			gl2.glTranslatef(-camera_lookat_X, -camera_lookat_Y, -camera_lookat_Z);
			gl2.glPopMatrix();
		}
		else {
			path.showPath(gl2, glu, ball, shootFrame);
			shootingMode = true;
		}
	}
	
	public void drawHUD(GL2 gl2, float barSize){
		hud.draw(gl2, barSize);
		if (powerCheck == true){
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
		if (shootingMode){
			miss = !path.isScored();
			if (shootFrame < path.getNumSteps() - 1){
				if (reshot){
					shootFrame = 0;
					playSound(new File("src/sounds/please_no_more.wav"));
				}
				else if (path.isScored() && path.getScoredLocation() == shootFrame){
					playSound(new File("src/sounds/bill_ted_excellent.wav"));
				}
				else {
					if (miss && shootFrame < 1){
						playSound(new File("src/sounds/my_wheaties.wav"));
					}
					miss = false;
				}
				shootFrame++;
			}
			else {
				askForReplay = true;
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
    
    public void playSound(File file){
		
		try {
			AudioInputStream stream;
			//AudioFormat format;
			//DataLine.Info info;
			Clip clip;
			
			stream = AudioSystem.getAudioInputStream(file);
			//format = stream.getFormat();
			//info = new DataLine.Info(Clip.class, format);
			clip = AudioSystem.getClip();
			clip.open(stream);
			clip.start();
			/*Thread.sleep(clip.getMicrosecondLength());
			clip.close();*/
			
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} /*catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	
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
		char key = e.getKeyChar();
		
		//if space bar is pressed, start shoot mode
		if (key == ' ' && ShootMode == false){
			ShootMode = true;
			powerCheck = false;
			shootingMode = false;
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
			Vector3f position = new Vector3f(new float[]{camera_X,camera_Y,camera_Z});
//			if (position.x < ball.getPosition().x && position.z < ball.getPosition().z){
//				position.x *= -1;
//				position.z *= -1;
//			}
//			else if  (position.x > ball.getPosition().x && position.z < ball.getPosition().z){
//				position.z *= -1;
//			}
//			else if  (position.x < ball.getPosition().x && position.z > ball.getPosition().z){
//				position.x *= -1;
//			}
			if (position.x < 0) position.x -= ball.getPosition().x; else position.x += ball.getPosition().x;
			if (position.z < 0) position.z -= ball.getPosition().z; else position.z += ball.getPosition().z;
			position.y += ball.getPosition().y;
//			position.z += Math.abs(ball.getPosition().z);
			System.out.println("X: " + position.x);
		    System.out.println("Y: " + position.y);
		    System.out.println("Z: " + position.z);
		    System.out.println("Xd: " + ball.getPosition().x);
		    System.out.println("Yd: " + ball.getPosition().y);
		    System.out.println("Zd: " + ball.getPosition().z);
			path = new BallPath(ball.getPosition(),position, ballPower, camera_angle_X, camera_angle_Y, court, basket, ball);
			shootingMode = true;
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