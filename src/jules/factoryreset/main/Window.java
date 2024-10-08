package jules.factoryreset.main;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javax.swing.*;
import jules.factoryreset.mainmenu.StartMenu;

public class Window extends JFrame {

    private static final long serialVersionUID = 1L;
    public StartMenu startMenu;
    private boolean fullscreenEnabled = false;

    public Window() { 
        // Initialize GamePanel here
        GamePanel.initInstance(this);

        setTitle("Factory Reset - v0.0.2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setFullScreenMode(false);
        setResizable(false);

        startMenu = new StartMenu(this, GamePanel.getInstance());
        add(startMenu);
 
        setVisible(true);
    }

    public static void main(String[] args) {
        System.gc();
        SwingUtilities.invokeLater(Window::new);
    }

    protected void setFullScreenMode(boolean enabled) {
    	fullscreenEnabled = enabled;
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();

        if (graphicsDevice.isFullScreenSupported() && enabled) {
            setUndecorated(true);
            graphicsDevice.setFullScreenWindow(this);
        } else if (!enabled) {
            // Ensure that GamePanel instance is initialized before getting its preferred size
            GamePanel.getInstance(); // Ensure initialization
            setSize(GamePanel.getInstance().getPreferredSize());
        } else {
            // Fallback if fullscreen mode is not supported
            GamePanel.getInstance(); // Ensure initialization
            setSize(GamePanel.getInstance().getPreferredSize());
        }
    }
    
    public boolean getFullscreenEnabled() {
    	return fullscreenEnabled;
    }
}
