package jules.factoryreset.collectible;

import java.awt.Dimension;
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
	BufferedImage sprite;
	Rectangle hitBox;
	public abstract void onPickup();
	public abstract void spawningBehavior();
	protected void setSprite(BufferedImage sprite) {
		this.sprite = sprite;
	}
	
	public BufferedImage getSprite() {
		return sprite;
	}
	
	public Collectible() {
		hitBox = new Rectangle(posX, posY, width, height);
	}
	
	public abstract void drawTooltip(Graphics g);
	public abstract void update();
	public abstract void setHitBox();
	public abstract void draw(Graphics g);
}
