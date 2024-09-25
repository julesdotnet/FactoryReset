package jules.factoryreset.weapon;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import jules.factoryreset.entity.EntityHandler;

public class Bullet {
	public double speedX;
	public double speedY;

	private double angleInRadians;
	private int bulletSize;
	public boolean isHostile = false;
	Rectangle bulletHitBox;

	int endX;
	int endY;

	double startX;
	double startY;

	public Bullet(Point start, Point end, int bulletSize, BufferedImage bulletSprite, double bulletSpeed,
			boolean hostile) {

		endX = (int) end.getX();
		endY = (int) end.getY();

		startX = start.getX();
		startY = start.getY();

		// Calculate the angle between start and end points
		double dx = end.getX() - start.getX();
		double dy = end.getY() - start.getY();
		this.angleInRadians = Math.atan2(dy, dx);

		// Calculate the speed components based on the angle
		this.speedX = bulletSpeed * Math.cos(angleInRadians);
		this.speedY = bulletSpeed * Math.sin(angleInRadians);

		this.isHostile = hostile;

		// Bullet size and hitbox initialization
		this.bulletSize = bulletSize;
		this.bulletHitBox = new Rectangle((int) start.getX(), (int) start.getY(), bulletSize, bulletSize);
	}

	public void update() {
		startX += speedX;
		startY += speedY;

		// Update hitbox position with casting to int for rendering
		bulletHitBox.setLocation((int) startX, (int) startY);
	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();

		AffineTransform originalTransform = g2.getTransform();

		g2.setColor(Color.red);
		g2.fillRect((int) startX, (int) startY, bulletSize, bulletSize);

		g2.setTransform(originalTransform);
		g2.dispose();
	}

    private double angleInRadians;
    private int bulletSize;
    Rectangle bulletHitBox;
    
    int offsetX = 0;
    int offsetY = 0;
    
    int endX;
    int endY;
     
    double startX;
    double startY;

    public Bullet(Point start, Point end, int bulletSize, BufferedImage bulletSprite, double bulletSpeed) {
        
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
    		startX += speedX;
    		startY += speedY;
    		
            bulletHitBox.setLocation((int)startX, (int) startY);
            System.out.println(EntityHandler.fireBots.get(0).getHitBox().getX());
    }
 
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        
        AffineTransform originalTransform = g2.getTransform();

        g2.setColor(Color.red);
        g2.fillRect((int) startX, (int) startY, bulletSize, bulletSize);

        // Restore the original transform (optional)
        g2.setTransform(originalTransform);
        g2.dispose();
    }

}
