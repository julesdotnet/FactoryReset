package jules.factoryreset.entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import jules.factoryreset.main.BackgroundHandler;
import jules.factoryreset.main.SpriteLoader;

public class Firebot extends Entity {
	private int x;
	private int y;
	private int width;
	private int height;

	public Firebot(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.isHostile = true;
		this.isActive = true;
		this.energyPoints = 10;
		this.hitBox = new Rectangle(x, y, width, height);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.spawnable = true;
		this.name = "Firebot";
		
		loadEntitySprites();
	}

	@Override
	public void draw(Graphics2D g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.drawImage(sprites[LEFT][10][0], (int)hitBox.getX(), (int)hitBox.getY(), (int)hitBox.getWidth(), (int)hitBox.getHeight(), null);
		
	}

	@Override
	public void update() {
		hitBoxUpdate();
		System.out.println("firebot update");
		
	}

	@Override
	void hitBoxUpdate() {
		hitBox.setBounds(x - BackgroundHandler.getOffsetX(), y - BackgroundHandler.getOffsetY(), width, height);
		
	}

	@Override
	void loadEntitySprites() {
		//direction, energylvl, animation state
		sprites[LEFT][10][0] = SpriteLoader.loadSprite("enemies/temp_firebot.png");
		
		System.out.println(sprites[LEFT][10][0] == null);
		
		
	}
	
}
