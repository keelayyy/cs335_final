package game;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.Animator;

public class GameWindow extends JFrame {
	
	public static void main(String[] args) {
		//initialize and display game window
		GameWindow window = new GameWindow();
        window.setVisible(true);
	}
	
    public GameWindow() {
    	super("The White Point Guard Special - A Free Throw Simulator");

        GLCapabilities capabilities = createGLCapabilities();
	    JoglEventListener jgl = new JoglEventListener(600,600,capabilities);
    	
    	this.getContentPane().add(jgl, BorderLayout.CENTER);
    	this.setSize(800,800);
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        });
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
	    jgl.requestFocus();
    }
	
	private static GLCapabilities createGLCapabilities() {
	    GLCapabilities capabilities = new GLCapabilities(null);
	    capabilities.setRedBits(8);
	    capabilities.setBlueBits(8);
	    capabilities.setGreenBits(8);
	    capabilities.setAlphaBits(8);
	    return capabilities;
	}
	
	//auto-generated ID (not used, I just don't like eclipse warnings)
	private static final long serialVersionUID = 1L;

}
