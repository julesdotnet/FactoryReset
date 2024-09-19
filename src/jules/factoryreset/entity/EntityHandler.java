package jules.factoryreset.entity;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import jules.factoryreset.main.BackgroundHandler;
import jules.factoryreset.main.GamePanel;

public class EntityHandler {
	static GamePanel gp;
	
	public EntityHandler(GamePanel gp) {
		EntityHandler.gp = gp;
	}
	public static ArrayList<Firebot> fireBots = new ArrayList<>(0);

	public static void spawn(Entity entity) {
		if (!entity.isSpawnable()) {
			System.out.println("ERROR: The entity " + entity.getName() + " cannot be spawned!");
			return; 
		} 
 
		switch (entity.getName()) {
		case "Firebot":
			fireBots.add(new Firebot((int) entity.getX() * BackgroundHandler.getTileSize(), (int) entity.getY() * BackgroundHandler.getTileSize(), 50, 50, gp));
			break;
		}
	}

	public static void updateAll() {
		for (Firebot fireBot : fireBots) {
			if (fireBot != null) {
				fireBot.update();
			}
		} 
		
	}
	
	public void drawAll(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		for(Firebot fireBot : fireBots) {
			fireBot.draw(g2);
			fireBot.heldWeapon.drawExistingBullets(g2);
		}
		
	}
}
