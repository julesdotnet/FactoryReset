package jules.factoryreset.main;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.*;

import jules.factoryreset.mainmenu.StartMenu;

public class Window extends JFrame {

    private static final long serialVersionUID = 1L;
    
    public GamePanel gp = new GamePanel();
    public StartMenu startMenu;
    
    Window() {
    	startMenu = new StartMenu(this, gp);
        setTitle("Factory Reset - v0.0.2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setFullScreenMode(false);
        setResizable(false);
        add(startMenu);        
        // Now start the game thread
        //gp.startGameThread();

        // Finally, make the window visible
        setVisible(true);
    }
    
    public static void main(String[] args) {
        System.gc();
        SwingUtilities.invokeLater(() -> new Window());
    }
    
    protected void setFullScreenMode(boolean enabled) {
    	// Fullscreen setup
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();

        // Check if fullscreen mode is supported
        if (graphicsDevice.isFullScreenSupported() && enabled) {
            setUndecorated(true); // Remove window borders before making the frame visible
            graphicsDevice.setFullScreenWindow(this); // Enable fullscreen
        } else if(!enabled){
            System.out.println("Fullscreen mode is disabled");
            setSize(gp.getPreferredSize()); // Fallback window size
        }
        else {
        	System.out.println("ERROR: Fullscreen mode is not supported!");
            setSize(gp.getPreferredSize()); // Fallback window size
        }
    }
}
