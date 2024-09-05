package jules.factoryreset.weapon;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import jules.factoryreset.entity.CollisionHandler;
import jules.factoryreset.main.BackgroundHandler;
import jules.factoryreset.main.GamePanel;

public class Bullet {
	private Point start;
	private double speedX;
	private double speedY;
    private double angleInRadians;
    private int bulletSize;
    Rectangle bulletHitBox;
    
    int endX;
    int endY;
    
    double startX;
    double startY;

    public Bullet(Point start, Point end, int bulletSize, BufferedImage bulletSprite, double bulletSpeed) {
        // Initialize start position
        this.start = start;
        
        endX = (int)end.getX();
        endY = (int)end.getY();
        
        startX = start.getX();
        startY = start.getY();
        
        // Calculate the angle between start and end points
        double dx = end.getX() - start.getX();
        double dy = end.getY() - start.getY();
        this.angleInRadians = Math.atan2(dy, dx);

        // Calculate the speed components based on the angle
        this.speedX = bulletSpeed * Math.cos(angleInRadians);
        this.speedY = bulletSpeed * Math.sin(angleInRadians);
        
        // Bullet size and hitbox initialization
        this.bulletSize = bulletSize;
        this.bulletHitBox = new Rectangle((int)start.getX(), (int)start.getY(), bulletSize, bulletSize);
    }

    public void update() {
    	
        // Update bullet's position based on the speed
    	//if(start.x < endX && start.y < endY) {
    		startX += speedX;
    		startY += speedY;
    		
    		// Update hitbox position with casting to int for rendering
            bulletHitBox.setLocation((int)startX, (int) startY);
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        
        // Save the original transform (optional, depends on how you are managing transforms)
        AffineTransform originalTransform = g2.getTransform();

        // Draw the bullet centered at its current location, casting to int for pixel positioning
        g2.setColor(Color.red);
        g2.fillRect((int) startX, (int) startY, bulletSize, bulletSize);

        // Restore the original transform (optional)
        g2.setTransform(originalTransform);
        g2.dispose();
    }
}
