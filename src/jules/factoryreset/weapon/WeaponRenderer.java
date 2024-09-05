package jules.factoryreset.weapon;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import jules.factoryreset.entity.Player;
import jules.factoryreset.main.BackgroundHandler;
import jules.factoryreset.main.GamePanel;
import jules.factoryreset.main.MouseListener;
import jules.factoryreset.main.SpriteLoader;
import jules.factoryreset.sfxhandling.SoundPlayer;

public class WeaponRenderer {
	SoundPlayer soundPlayer;
	static BackgroundHandler bgHandler;
	
	private static int centerX = 0;
	private static int centerY = 0;

	private Map<String, BufferedImage> weaponSprites = new HashMap<>();

	public WeaponRenderer(Player player, GamePanel gp) {
		soundPlayer = new SoundPlayer();
		bgHandler = new BackgroundHandler(gp);
		loadAllWeaponSprites();
	}

	public void drawWeapon(Graphics g, BufferedImage weaponImage, Rectangle playerHitBox, double weaponAngle,
			boolean isLeftMouseClicked, Point mousePosition, int weaponScaleX,
			int weaponScaleY) {

		Graphics2D g2 = (Graphics2D) g.create(); // Create a copy of the Graphics2D context

		AffineTransform originalTransform = g2.getTransform();

		// Get the center of the player (the pivot point for rotation)
		centerX = (int) playerHitBox.getCenterX();
		centerY = (int) playerHitBox.getCenterY();

		double angle = weaponAngle;

		// never rotate before translating
		g2.translate(centerX, centerY);
		g2.rotate(angle); 

		// Solely for debug purposes
		// drawAimLine(g);

		if (Math.abs(angle) < Math.PI / 2) {
			// Normal orientation
			g2.drawImage(weaponImage, 0, -weaponScaleY / 2 + 2, weaponScaleX, weaponScaleY, null);
		} else {
			// Flipped orientation
			g2.drawImage(weaponImage, 0, -weaponScaleY + 55 / 2, weaponScaleX, -weaponScaleY, null);
		}

		g2.setTransform(originalTransform);

		g2.dispose();
	}
	// only for debug purposes, rather slow due to rendering system
	void drawAimLine(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();

		Point playerCenter = new Point((int) GamePanel.getInstance().player.getHitBox().getCenterX(),
				(int) GamePanel.getInstance().player.getHitBox().getCenterY());
		Point mousePosition = new Point(MouseListener.getMouseX(), MouseListener.getMouseY());

		g2.setColor(Color.yellow);
		g2.drawLine(playerCenter.x, playerCenter.y - 2, mousePosition.x, mousePosition.y);

		g2.dispose();
	}

	// calculates weapon holding angle based on holding point and aim point
	public double getWeaponAngle(Point start, Point end) {
		int dx = (int) (end.getX() - start.getX());
		int dy = (int) (end.getY() - start.getY());
		return Math.atan2(dy, dx);
	}

	// returns the sprite hashmap
	public Map<String, BufferedImage> getWeaponMap() {
		return weaponSprites;
	}

	// loads all player weapon sprites
	void loadAllWeaponSprites() {
		weaponSprites.put("lasergun", SpriteLoader.loadSprite("weaponsprites/lasergun-facing-right.png"));
	}
	
	public static Point playerBulletOrigin() {
		return new Point(centerX , centerY);
	}
}
