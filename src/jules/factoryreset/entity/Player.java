package jules.factoryreset.entity;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import jules.factoryreset.main.*;
import jules.factoryreset.sfxhandling.SoundPlayer;
import jules.factoryreset.weapon.Weapon;
import jules.factoryreset.weapon.WeaponRenderer;

public class Player extends Entity {

	int x;
	int y;
	int width;
	int height;

	// Directions
	public static final int DOWN = 0;
	public static final int LEFT = 1;
	public static final int RIGHT = 2;
	public static final int UP = 3;

	// Energy Levels
	public static final int NO_ENERGY = 0;
	public static final int ENERGY_LVL2 = 1;
	public static final int ENERGY_LVL3 = 2;
	public static final int ENERGY_LVL4 = 3;
	public static final int ENERGY_LVL5 = 4;
	public static final int ENERGY_LVL6 = 5;
	public static final int FULL_ENERGY = 6;

	private int ticks = 0;
	BufferedImage[] batterySprites = new BufferedImage[7];
	BufferedImage currentBatterySprite;
	BackgroundHandler bgHandler;
	GamePanel gp;
	WeaponRenderer weaponRenderer;
	SoundPlayer soundPlayer;	
	Weapon laserGun;

	public Player(int x, int y, int width, int height, GamePanel gp) {
		// setting values
		super(x, y, width, height);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.speed = 4;
		setAlive();
		this.energyPoints = 6;
		soundPlayer = new SoundPlayer();	
		weaponRenderer = new WeaponRenderer(this, gp);
		// loading sprites and setting sprites to be shown on startup
		loadPlayerSprites();
		currentEntitySprite = sprites[DOWN][FULL_ENERGY][1];
		if (energyPoints >= 2) {
			currentBatterySprite = batterySprites[energyPoints - 1];
		} else
			currentBatterySprite = batterySprites[6];

		// mouseListener init
		this.gp = gp;
		// mouseListener = new MouseListener(gp, this);
		
		laserGun = new Weapon(gp, "lasergun", 6, 24, 100, 100, false);
		bgHandler = new BackgroundHandler(gp);
	}

	public void draw(Graphics2D g) {
		Point startPoint = new Point((int)getHitBox().getCenterX() + bgHandler.getPlayerFocusMovementX(),(int) getHitBox().getCenterY() + bgHandler.getPlayerFocusMovementY());
		Point aimPoint = new Point(MouseListener.getMouseX(),MouseListener.getMouseY());
		
		laserGun.drawExistingBullets(g);
		
		if (KeyInput.getDirection().toString() == "UP" | KeyInput.getDirection().toString() == "LEFT"
				| KeyInput.getDirection().toString() == "UP_LEFT" | KeyInput.getDirection().toString() == "DOWN_LEFT"
				&& isAlive()) {
			weaponRenderer.drawWeapon(g, weaponRenderer.getWeaponMap().get("lasergun"), hitBox, weaponRenderer.getWeaponAngle(startPoint, aimPoint),
                    MouseListener.getLeftMouseButtonClicked(), 
                    aimPoint, 
                    weaponScaleX(), weaponScaleY());
		}
		// Draw the player sprite
			g.drawImage(SpriteLoader.spriteAnimationHandling(energyPoints, KeyInput.getDirection().toString(), ANIMATION_STATE,
							sprites, this),
					(int) hitBox.getX()  + bgHandler.getPlayerFocusMovementX(), (int) hitBox.getY()  + bgHandler.getPlayerFocusMovementY(), (int) hitBox.getWidth(), (int) hitBox.getHeight(), null);

		// Draw the battery sprite
		g.drawImage(currentBatterySprite, 7, 50, 140, 40, null);

		// draw weapon in foreground
		if (KeyInput.getDirection().toString() == "DOWN" | KeyInput.getDirection().toString() == "RIGHT"
				| KeyInput.getDirection().toString() == "DOWN_RIGHT" | KeyInput.getDirection().toString() == "UP_RIGHT"
				| KeyInput.getDirection().toString() == "NONE" && isAlive()) {
			weaponRenderer.drawWeapon(g, weaponRenderer.getWeaponMap().get("lasergun"), hitBox, weaponRenderer.getWeaponAngle(startPoint, aimPoint),
                    MouseListener.getLeftMouseButtonClicked(), 
                    aimPoint, 
                    weaponScaleX(), weaponScaleY());
		}
	}

	public void update() {
		playerScaling();
		if (isAlive()) {
			hitBoxUpdate();
			if(MouseListener.getLeftMouseButtonClicked()) {
				laserGun.playerShoot(new Point(MouseListener.getMouseX(), MouseListener.getMouseY()));
			}
			laserGun.updateExistingBullets();
		}

		// deducts energy every couple hundred game ticks
		energyHandling();
	}

	private void playerScaling() {
		x = gp.getWidth() / 2 - (int) hitBox.getWidth() / 2;
		y = gp.getHeight() / 2 - (int) hitBox.getHeight() / 2;
		width = gp.getWidth() / 26;
		height = gp.getHeight() / 7;
	}

	private void energyHandling() {

		if (isAlive()) {
			ticks++;
			if (ticks >= 400) {
				energyPoints--;
				ticks = 0;

				if (energyPoints == 0) {
					kill();
				}
				switch (energyPoints) {
				case 6:
					currentBatterySprite = batterySprites[5];
					break;

				case 5:
					currentBatterySprite = batterySprites[4];
					break;

				case 4:
					currentBatterySprite = batterySprites[3];
					break;

				case 3:
					currentBatterySprite = batterySprites[2];
					break;

				case 2:
					currentBatterySprite = batterySprites[1];
					break;

				case 1:
					currentBatterySprite = batterySprites[0];
					break;

				case 0:
					currentBatterySprite = batterySprites[6];
					break;
				}
			}
		}
	}
	
	public int weaponScaleX() {
		return gp.getWidth() / 24;
	}

	public int weaponScaleY() {
		return gp.getWidth() / 60;
	}

	@Override
	void hitBoxUpdate() {
		hitBox.x = x;
		hitBox.y = y;
		hitBox.width = width;
		hitBox.height = height;
	}

	private void loadPlayerSprites() {
		sprites[DOWN][ENERGY_LVL2][0] = SpriteLoader.loadSprite("player/bot-facing-down-energy-lvl2-1.png");
		sprites[DOWN][ENERGY_LVL2][1] = SpriteLoader.loadSprite("player/bot-facing-down-energy-lvl2-2.png");
		sprites[DOWN][ENERGY_LVL3][0] = SpriteLoader.loadSprite("player/bot-facing-down-energy-lvl3-1.png");
		sprites[DOWN][ENERGY_LVL3][1] = SpriteLoader.loadSprite("player/bot-facing-down-energy-lvl3-2.png");
		sprites[DOWN][ENERGY_LVL4][0] = SpriteLoader.loadSprite("player/bot-facing-down-energy-lvl4-1.png");
		sprites[DOWN][ENERGY_LVL4][1] = SpriteLoader.loadSprite("player/bot-facing-down-energy-lvl4-2.png");
		sprites[DOWN][ENERGY_LVL5][0] = SpriteLoader.loadSprite("player/bot-facing-down-energy-lvl5-1.png");
		sprites[DOWN][ENERGY_LVL5][1] = SpriteLoader.loadSprite("player/bot-facing-down-energy-lvl5-2.png");
		sprites[DOWN][ENERGY_LVL6][0] = SpriteLoader.loadSprite("player/bot-facing-down-energy-lvl6-1.png");
		sprites[DOWN][ENERGY_LVL6][1] = SpriteLoader.loadSprite("player/bot-facing-down-energy-lvl6-2.png");
		sprites[DOWN][FULL_ENERGY][0] = SpriteLoader.loadSprite("player/bot-facing-down-full-energy-1.png");
		sprites[DOWN][FULL_ENERGY][1] = SpriteLoader.loadSprite("player/bot-facing-down-full-energy-2.png");
		sprites[DOWN][NO_ENERGY][0] = SpriteLoader.loadSprite("player/bot-facing-down-no-energy.png");
		sprites[DOWN][NO_ENERGY][1] = sprites[DOWN][NO_ENERGY][0]; // Assuming no animation for no energy

		// LEFT direction sprites
		sprites[LEFT][ENERGY_LVL2][0] = SpriteLoader.loadSprite("player/bot-facing-left-energy-lvl2 to lvl5-1.png");
		sprites[LEFT][ENERGY_LVL2][1] = SpriteLoader.loadSprite("player/bot-facing-left-energy-lvl2 to lvl5-2.png");
		sprites[LEFT][ENERGY_LVL3][0] = sprites[LEFT][ENERGY_LVL2][0]; // Reusing sprites for energy levels 2 to 5
		sprites[LEFT][ENERGY_LVL3][1] = sprites[LEFT][ENERGY_LVL2][1];
		sprites[LEFT][ENERGY_LVL4][0] = sprites[LEFT][ENERGY_LVL2][0];
		sprites[LEFT][ENERGY_LVL4][1] = sprites[LEFT][ENERGY_LVL2][1];
		sprites[LEFT][ENERGY_LVL5][0] = sprites[LEFT][ENERGY_LVL2][0];
		sprites[LEFT][ENERGY_LVL5][1] = sprites[LEFT][ENERGY_LVL2][1];
		sprites[LEFT][ENERGY_LVL6][0] = SpriteLoader.loadSprite("player/bot-facing-left-energy-lvl6-1.png");
		sprites[LEFT][ENERGY_LVL6][1] = SpriteLoader.loadSprite("player/bot-facing-left-energy-lvl6-2.png");
		sprites[LEFT][FULL_ENERGY][0] = SpriteLoader.loadSprite("player/bot-facing-left-full-energy-1.png");
		sprites[LEFT][FULL_ENERGY][1] = SpriteLoader.loadSprite("player/bot-facing-left-full-energy-2.png");
		sprites[LEFT][NO_ENERGY][0] = SpriteLoader.loadSprite("player/bot-facing-left-no-energy.png");
		sprites[LEFT][NO_ENERGY][1] = sprites[LEFT][NO_ENERGY][0]; // No animation for no energy

		// RIGHT direction sprites
		sprites[RIGHT][ENERGY_LVL2][0] = SpriteLoader.loadSprite("player/bot-facing-right-energy-lvl2-1.png");
		sprites[RIGHT][ENERGY_LVL2][1] = SpriteLoader.loadSprite("player/bot-facing-right-energy-lvl2-2.png");
		sprites[RIGHT][ENERGY_LVL3][0] = SpriteLoader.loadSprite("player/bot-facing-right-energy-lvl3-1.png");
		sprites[RIGHT][ENERGY_LVL3][1] = SpriteLoader.loadSprite("player/bot-facing-right-energy-lvl3-2.png");
		sprites[RIGHT][ENERGY_LVL4][0] = SpriteLoader.loadSprite("player/bot-facing-right-energy-lvl4-1.png");
		sprites[RIGHT][ENERGY_LVL4][1] = SpriteLoader.loadSprite("player/bot-facing-right-energy-lvl4-2.png");
		sprites[RIGHT][ENERGY_LVL5][0] = SpriteLoader.loadSprite("player/bot-facing-right-energy-lvl5-1.png");
		sprites[RIGHT][ENERGY_LVL5][1] = SpriteLoader.loadSprite("player/bot-facing-right-energy-lvl5-2.png");
		sprites[RIGHT][ENERGY_LVL6][0] = SpriteLoader.loadSprite("player/bot-facing-right-energy-lvl6-1.png");
		sprites[RIGHT][ENERGY_LVL6][1] = SpriteLoader.loadSprite("player/bot-facing-right-energy-lvl6-2.png");
		sprites[RIGHT][FULL_ENERGY][0] = SpriteLoader.loadSprite("player/bot-facing-right-full-energy-1.png");
		sprites[RIGHT][FULL_ENERGY][1] = SpriteLoader.loadSprite("player/bot-facing-right-full-energy-2.png");
		sprites[RIGHT][NO_ENERGY][0] = SpriteLoader.loadSprite("player/bot-facing-right-no-energy.png");
		sprites[RIGHT][NO_ENERGY][1] = sprites[RIGHT][NO_ENERGY][0]; // No animation for no energy

		// UP direction sprites
		sprites[UP][ENERGY_LVL2][0] = SpriteLoader.loadSprite("player/bot-facing-up-any-energy.png");
		sprites[UP][ENERGY_LVL2][1] = sprites[UP][ENERGY_LVL2][0]; // Assuming no animation for the UP direction
		sprites[UP][ENERGY_LVL3][0] = sprites[UP][ENERGY_LVL2][0]; // Reusing the same sprite for all energy levels
		sprites[UP][ENERGY_LVL3][1] = sprites[UP][ENERGY_LVL2][0];
		sprites[UP][ENERGY_LVL4][0] = sprites[UP][ENERGY_LVL2][0];
		sprites[UP][ENERGY_LVL4][1] = sprites[UP][ENERGY_LVL2][0];
		sprites[UP][ENERGY_LVL5][0] = sprites[UP][ENERGY_LVL2][0];
		sprites[UP][ENERGY_LVL5][1] = sprites[UP][ENERGY_LVL2][0];
		sprites[UP][ENERGY_LVL6][0] = sprites[UP][ENERGY_LVL2][0];
		sprites[UP][ENERGY_LVL6][1] = sprites[UP][ENERGY_LVL2][0];
		sprites[UP][FULL_ENERGY][0] = sprites[UP][ENERGY_LVL2][0];
		sprites[UP][FULL_ENERGY][1] = sprites[UP][ENERGY_LVL2][0];
		sprites[UP][NO_ENERGY][0] = sprites[UP][ENERGY_LVL2][0];
		sprites[UP][NO_ENERGY][1] = sprites[UP][ENERGY_LVL2][0];

		// battery sprites
		batterySprites[0] = SpriteLoader.loadSprite("guisprites/energy-lvl1-bar.png");
		batterySprites[1] = SpriteLoader.loadSprite("guisprites/energy-lvl2-bar.png");
		batterySprites[2] = SpriteLoader.loadSprite("guisprites/energy-lvl3-bar.png");
		batterySprites[3] = SpriteLoader.loadSprite("guisprites/energy-lvl4-bar.png");
		batterySprites[4] = SpriteLoader.loadSprite("guisprites/energy-lvl5-bar.png");
		batterySprites[5] = SpriteLoader.loadSprite("guisprites/full-energy-bar.png");
		batterySprites[6] = SpriteLoader.loadSprite("guisprites/no-energy-bar.png");

	}
}
