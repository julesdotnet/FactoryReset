package jules.factoryreset.collectible;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import jules.factoryreset.entity.Player;
import jules.factoryreset.main.BackgroundHandler;
import jules.factoryreset.main.GamePanel;
import jules.factoryreset.main.SpriteLoader;
import jules.factoryreset.sfxhandling.SoundPlayer;

public class Battery extends Collectible {
	
	public Battery(int tileX, int tileY) {
		super();
		setSprite(SpriteLoader.loadSprite("collectibles/battery sprite.png"));
		this.tileX = tileX;
		this.tileY = tileY;
		
		setRarity(1/40);
	}

	@Override
	public void onPickup() {
		GamePanel.getInstance().player.setEnergyPoints(GamePanel.getInstance().player.MAX_HEALTH);
		SoundPlayer.playSound("pickup_collectible");
		Player player = (Player) GamePanel.getInstance().player;
		player.heal();
		System.out.println("player intersects battery");
		
		
		isCollected = true;
	}

	@Override
	public void drawTooltip(Graphics g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		
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