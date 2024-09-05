package jules.factoryreset.entity;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class EntityHandler {
	static ArrayList<Firebot> fireBots = new ArrayList<>(0);

	public static void spawn(Entity entity) {
		if (!entity.spawnable) {
			System.out.println("ERROR: The entity " + entity.getName() + " cannot be spawned!");
			return;
		}

		switch (entity.getName()) {
		case "Firebot":
			fireBots.add(new Firebot((int) entity.getX(), (int) entity.getY(), 50, 50));
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
		}
		
	}
}
