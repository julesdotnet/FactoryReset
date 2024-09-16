package jules.factoryreset.entity;

import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Set;
import jules.factoryreset.main.BackgroundHandler;
import jules.factoryreset.main.SpriteLoader;

public class Firebot extends Entity {

    private static final Set<Firebot> allFirebots = new HashSet<>();

    public Firebot(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.setHostile(true);
        this.setActive(true);
        this.setEnergyPoints(10);
        this.setHitBox(new Rectangle(x, y, width, height));
        this.setSpawnable(true);
        this.setName("Firebot");
        this.setSpeed(2);

        loadEntitySprites();

        allFirebots.add(this); // Add this instance to the list of all Firebots
    }

    
    private void scaleFirebots() {
        getHitBox().setSize(BackgroundHandler.getTileSize(), BackgroundHandler.getTileSize());
    }

    @Override
    public void update() {
        scaleFirebots();
        hitBoxUpdate();
        
        if(!isAlive()) {
        	return;
        }
        
        
        processMovementQueue();
        if (hasPlayerMovedSignificantly()) {
            pathNeedsRecalculation = true;
        }

        if (pathNeedsRecalculation && getQueuedMovement().isEmpty()) {
            findPath();
        }
    }

    @Override
    protected void hitBoxUpdate() {
        getHitBox().setBounds(x - BackgroundHandler.getOffsetX(), y - BackgroundHandler.getOffsetY(), width, height);
    }

    @Override
    protected void loadEntitySprites() {
        getSprites()[LEFT][10][0] = SpriteLoader.loadSprite("enemies/temp_firebot.png");
        System.out.println(getSprites()[LEFT][10][0] == null ? "Sprite not loaded" : "Sprite loaded");
    }

}
