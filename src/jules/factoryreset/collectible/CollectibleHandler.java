package jules.factoryreset.collectible;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import jules.factoryreset.main.BackgroundHandler;

public class CollectibleHandler {
	static ArrayList<Collectible> allCollectibles;
	Random random = new Random();
	
	public CollectibleHandler() {
		allCollectibles = new ArrayList<>();
	}
	
	public static void spawnCollectibles(Collectible collectible){
		allCollectibles.add(collectible);
	}
	
	public static void updateAll() {
	    if (allCollectibles.size() == 0) {
	        return;
	    }

	    Iterator<Collectible> iterator = allCollectibles.iterator();
	    while (iterator.hasNext()) {
	        Collectible collectible = iterator.next();
	        collectible.update();

	        if (collectible.getCollected()) {
	            iterator.remove();
	        }
	    }
	}
	
	public static void drawCollectibles(Graphics g) {
		int tileSize = BackgroundHandler.getTileSize();
		Graphics2D g2 = (Graphics2D) g.create();
		
		if(allCollectibles.size() == 0) {
			return;
		}
		
		for(Collectible collectible : allCollectibles) {
			g2.drawImage(collectible.getSprite(), collectible.getTileX() * tileSize - BackgroundHandler.getOffsetX(), collectible.getTileY() * tileSize - BackgroundHandler.getOffsetY(), collectible.getWidth(), collectible.getHeight(), null);
		}
	}
}
