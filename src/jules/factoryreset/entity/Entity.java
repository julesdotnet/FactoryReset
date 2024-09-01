package jules.factoryreset.entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public abstract class Entity {
	Rectangle hitBox;
	
	public Entity(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        hitBox = new Rectangle(x, y, width, height);
        this.sprites = new BufferedImage[100][7][2];
    }
	
	//general info
	BufferedImage[][][] sprites;
	BufferedImage currentEntitySprite;
	int energyPoints;
	boolean isHostile;
	boolean isActive;
	public int speed;
	private boolean alive;
	
	//coords + hitbox
	int x;
	int y;
	int width;
	int height;
	
	//animation variables
	public int animationTicks;
	public int ANIMATION_STATE;
	
	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int newSpeed) {
		speed = newSpeed;
	}

	public int getX() {
		return x;
	}

	public void setX(int newX) {
		x = newX;
	}

	public int getY() {
		return y;
	}

	public void setY(int newY) {
		y = newY;
	}

	public Rectangle getHitBox() {
		return hitBox;
	}

	public boolean getHostile() {
	    return isHostile;
	}

	public boolean getActive() {
	    return isActive;
	}


	public int getEnergy() {
		// TODO Auto-generated method stub
		return energyPoints;
	}
	
	public boolean isAlive() {
		return alive;
	}
	
	public void kill() {
		alive = false;
	}
	
	public void setAlive() {
		alive = true;
	}
	
	public abstract void draw(Graphics2D g);
	public abstract void update();
	
	abstract void hitBoxUpdate();
}