package jules.factoryreset.main;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.*;

import jules.factoryreset.entity.Entity;
import jules.factoryreset.entity.Player;

public class GamePanel extends JPanel implements Runnable {

    private static final long serialVersionUID = 1L;

    // Variables for game loop
    private Thread gameThread;
    private final DrawHandler drawHandler;
    public MouseListener mouseListener;
    public static Entity player  = null;

    // FPS variables
    private long lastTime = System.nanoTime();
    private long lastFPSUpdateTime = System.nanoTime();
    private int frames = 0;
    int fps = 0;
    

    public GamePanel() {
        new KeyInput(this);
        drawHandler = new DrawHandler(this);
        setBackground(Color.blue);
        setPreferredSize(new Dimension(1250, 730));
        setDoubleBuffered(true);
        setFocusable(true);
       
        player = new Player(getWidth(), getHeight(), 50, 100, this);
        mouseListener = new MouseListener(this);
        
        setCrosshairCursor();
    }
    
    public static Entity getPlayer() {
    	return player;
    }

    public void startGameThread() {
        if (gameThread == null) {
            gameThread = new Thread(this);
            gameThread.start();
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        drawHandler.draw(g2d);
    }
    
    Runtime runTime = Runtime.getRuntime();
    
    // UPDATING ALL GAME INFORMATION
    private void update() {
    	
    	player.setX(getWidth() / 2);
        player.update();
    }
    
 // Method to set the cursor invisible
    protected void setCrosshairCursor() {
        // Create a blank image
        BufferedImage cursorImg = new BufferedImage( 16, 16, BufferedImage.TYPE_INT_ARGB);
        BufferedImage loadedImage = SpriteLoader.loadSprite("guisprites/crosshair.png.png");
        
        Graphics cursorGraphics = cursorImg.createGraphics();
        cursorGraphics.drawImage(loadedImage, 0, 0, null);
        
        cursorGraphics.dispose();

        // Create an invisible cursor using the blank image
        Cursor crosshairCursor = Toolkit.getDefaultToolkit().createCustomCursor(
            cursorImg, new Point(0, 0), "Crosshair Cursor");

        // Set the invisible cursor on this JPanel (or JFrame)
        setCursor(crosshairCursor);
    }
    
    
    @Override
    public void run() {
        final int TARGET_FPS = 60;
        final long OPTIMAL_TIME = 1000000000 / TARGET_FPS; // Nanoseconds per frame

        while (true) {
            long now = System.nanoTime();
            //long updateLength = now - lastTime;
            lastTime = now;

            // Update FPS counter
            frames++;
            if (now - lastFPSUpdateTime >= 1000000000) { // If 1 second has passed
                fps = frames;
                frames = 0;
                lastFPSUpdateTime += 1000000000;
            }

            update();
            repaint(0, 0, this.getWidth(), this.getHeight());

            try {
                long sleepTime = (lastTime - System.nanoTime() + OPTIMAL_TIME) / 1000000;
                if (sleepTime > 0) {
                    Thread.sleep(sleepTime);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
