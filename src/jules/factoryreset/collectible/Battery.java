package jules.factoryreset.collectible;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import jules.factoryreset.entity.Player;
import jules.factoryreset.main.BackgroundHandler;
import jules.factoryreset.main.GamePanel;
import jules.factoryreset.main.SpriteLoader;

public class Battery extends Collectible {
	
	public Battery(int tileX, int tileY) {
		super();
		setSprite(SpriteLoader.loadSprite("collectibles/battery sprite.png"));
		this.tileX = tileX;
		this.tileY = tileY;
	}

	@Override
	public void onPickup() {
		GamePanel.getInstance().player.setEnergyPoints(GamePanel.getInstance().player.MAX_HEALTH);
		Player player = (Player) GamePanel.getInstance().player;
		player.heal();
		System.out.println("player intersects battery");
		
	}

	@Override
	public void spawningBehavior() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawTooltip(Graphics g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		update();
		
		g2.drawImage(getSprite(), (int) hitBox.getX(), (int) hitBox.getY(), (int) hitBox.getWidth(), (int) hitBox.getHeight(), null);
		
	}

	@Override
	public void update() {
		setHitBox();
		
		Rectangle playerHitBox = GamePanel.getInstance().player.getHitBox();
		
		if(hitBox.intersects(playerHitBox)) {
			onPickup();
		}
	}

	@Override
	public void setHitBox() {
		int tileSize = BackgroundHandler.getTileSize();
		int offsetX = BackgroundHandler.getOffsetX();
		int offsetY = BackgroundHandler.getOffsetY();
		
		posX = tileX * tileSize - offsetX;
		posY = tileY * tileSize - offsetY;
		
		width = tileSize;
		height = (int) width / 2;
		
		hitBox.setLocation(posX, posY);
		hitBox.setSize(width, height);
	}
}