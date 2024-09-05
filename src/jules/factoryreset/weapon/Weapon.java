package jules.factoryreset.weapon;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import jules.factoryreset.main.BackgroundHandler;
import jules.factoryreset.main.GamePanel;
import jules.factoryreset.sfxhandling.SoundPlayer;

public class Weapon {
	String name;
	int ammoCapacity;
	int fireRateTicks;
	int reloadTicks;
	int damage;
	boolean doesAreaDamage;

	int currentAmmo;
	String firingSound;
	String reloadSound;
	public List<BufferedImage> spriteList;
	public List<Bullet> magazine;
	private static int bulletSize = 6;

	private int shootCooldownCounter;

	public static Weapon laserGun;
	GamePanel gp;

	public Weapon(GamePanel gp, String name, int ammoCapacity, int fireRateTicks, int ReloadTicks, int damage,
			boolean doesAreaDamage) {
		this.name = name;
		this.ammoCapacity = ammoCapacity;
		this.fireRateTicks = fireRateTicks;
		this.damage = damage;
		this.doesAreaDamage = doesAreaDamage;

		spriteList = new ArrayList<>();
		magazine = new ArrayList<>();

		this.shootCooldownCounter = fireRateTicks;

		this.gp = gp;
	}

	void setFiringSound(String firingSound) {
		this.firingSound = firingSound;
	}

	String getFiringSound() {
		return firingSound;
	}

	void decrementCurrentAmmo() {
		currentAmmo--;
	}

	int getCurrentAmmo() {
		return currentAmmo;
	}

	String getWeaponName() {
		return name;
	}

	void addWeaponSprite(BufferedImage images) {
		spriteList.add(images);
	}

	public void shoot(Point startPoint, Point aimPoint) {
		if (shootCooldownCounter == 0) {
			SoundPlayer.playSound("laserRayShot");
			magazine.add(new Bullet(startPoint, aimPoint, 4, null, 12));
		}
	}

	public void playerShoot(Point aimPoint) {
		if (shootCooldownCounter == 0) {
			Point startPoint = WeaponRenderer.playerBulletOrigin();

			SoundPlayer.playSound("laserRayShot");
			magazine.add(new Bullet(startPoint, aimPoint, 4, null, 12));
			shootCooldownCounter = fireRateTicks; // Reset firing rate cooldown
		}
	}

	public void updateExistingBullets() {
		// Update all active bullets
		for (int i = 0; i < magazine.size(); i++) {
			if (magazine.get(i) != null) {
				magazine.get(i).update();

				if (BackgroundHandler.getTileCollidableAtScreenCoordinates((int) magazine.get(i).startX,
						(int) magazine.get(i).startY)) {
					magazine.remove(i);				}
			}
		}
		// Handle shooting cooldown
		if (shootCooldownCounter > 0) {
			shootCooldownCounter--;
		}
	}

	public void drawExistingBullets(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setColor(Color.red);
		for (int i = 0; i < magazine.size(); i++) {
			int bulletX = (int) magazine.get(i).bulletHitBox.getX();
			int bulletY = (int) magazine.get(i).bulletHitBox.getY();

			if (bulletX < gp.getWidth() | bulletY < gp.getHeight() | bulletX > -bulletSize | bulletY > -bulletSize) {
				g2.fillRect(bulletX, bulletY, bulletSize, bulletSize);
			}
		}
	}
}
