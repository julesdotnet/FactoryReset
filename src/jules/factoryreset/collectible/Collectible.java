package jules.factoryreset.collectible;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public abstract class Collectible {

	
	int tileX;
	int tileY;
	int posX = 0;
	int posY = 0;
	int width = 0;
	int height = 0;
	double spawnRarity;
	BufferedImage sprite;
	Rectangle hitBox;
	public boolean isCollected = false;
	public abstract void onPickup();
	protected void setSprite(BufferedImage sprite) {
		this.sprite = sprite;
	}
	
	public BufferedImage getSprite() {
		return sprite;
	}
	
	public void setRarity(double rarity) {
		this.spawnRarity = rarity;
	}
	
	public boolean getCollected() {
		return isCollected;
	}
	public Collectible() {
		hitBox = new Rectangle(posX, posY, width, height);
	}
	
	public Rectangle getHitBox() {
		return hitBox;
	}
	
	public int getTileX() {
		return tileX;
	}
	public int getTileY() {
		return tileY;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public abstract void drawTooltip(Graphics g);
	public abstract void update();
	public abstract void setHitBox();
	public abstract void draw(Graphics g);
}
