package jules.factoryreset.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import jules.factoryreset.main.BackgroundHandler;
import jules.factoryreset.main.GamePanel;
import jules.factoryreset.sfxhandling.SoundPlayer;
import jules.factoryreset.utils.AStarPathfinding;
import jules.factoryreset.utils.Grid;
import jules.factoryreset.utils.Node;
import jules.factoryreset.weapon.Weapon;
import jules.factoryreset.weapon.WeaponRenderer;

public abstract class Entity {
    private Rectangle hitBox;
    protected BufferedImage[][][] sprites;
    public BufferedImage currentEntitySprite;
    private int energyPoints;
    private boolean isHostile;
    private boolean isActive;
    private boolean spawnable;
    private String name;
    private int speed;
    private boolean alive;

    private Queue<int[]> queuedMovement;
    private double lastPlayerX, lastPlayerY;
    private Node goal;
    protected boolean pathNeedsRecalculation = true;
    private final int overlapResolutionStep = 5;
    private final int stopDistance = 90;
    
	WeaponRenderer weaponRenderer;
	SoundPlayer soundPlayer;
	Weapon laserGun;
	GamePanel gp;

    protected int x;
    protected int y;
    protected int width;
    protected int height;

    // Directions 
    public static final int DOWN = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int UP = 3;

    // Energy Levels
    public static final int NO_ENERGY = 0;
    public static final int ENERGY_LVL2 = 1;
    public static final int ENERGY_LVL3 = 2;
    public static final int ENERGY_LVL4 = 3;
    public static final int ENERGY_LVL5 = 4;
    public static final int ENERGY_LVL6 = 5;
    public static final int FULL_ENERGY = 6;

    // Animation variables
    public int animationTicks;
    private int animationState;

    public Entity(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.hitBox = new Rectangle(x, y, width, height);
        this.sprites = new BufferedImage[100][100][2];
        this.queuedMovement = new LinkedList<>();
        this.alive = true;  // Assume the entity is alive by default
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
        updateHitBox();
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
        updateHitBox();
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
        updateHitBox();
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
        updateHitBox();
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public void setHitBox(Rectangle hitBox) {
        this.hitBox = hitBox;
    }

    public BufferedImage[][][] getSprites() {
        return sprites;
    }

    public void setSprites(BufferedImage[][][] sprites) {
        this.sprites = sprites;
    }

    public int getEnergyPoints() {
        return energyPoints;
    }

    public void setEnergyPoints(int energyPoints) {
        this.energyPoints = energyPoints;
    }

    public boolean isHostile() {
        return isHostile;
    }

    public void setHostile(boolean hostile) {
        this.isHostile = hostile;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public boolean isSpawnable() {
        return spawnable;
    }

    public void setSpawnable(boolean spawnable) {
        this.spawnable = spawnable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAlive() {
        return alive;
    }

    public void kill() {
        this.alive = false;
    }

    public void revive() {
        this.alive = true;
    }

    public Queue<int[]> getQueuedMovement() {
        return queuedMovement;
    }

    public void queueMovement(int targetX, int targetY) {
        queuedMovement.add(new int[]{targetX, targetY});
    }

    public Node positionToNode() {
        int tileSize = BackgroundHandler.getTileSize();
        System.out.println("true x: " + (x - BackgroundHandler.getOffsetX()));
        System.out.println("true y: " + (y - BackgroundHandler.getOffsetY()));
        return new Node(Math.abs(x  - BackgroundHandler.getOffsetX()) / tileSize,Math.abs (y - BackgroundHandler.getOffsetY()) / tileSize, 0, 0);
    }

    public int getAnimationState() {
        return animationState;
    }

    public void setAnimationState(int animationState) {
        this.animationState = animationState;
    }

    private void updateHitBox() {
        this.hitBox.setBounds(x, y, width, height);
    }
    
    protected void drawPathLines(Graphics g, Queue<int[]> queuedMovement) {
    	if(!isAlive()) {
    		return;
    	}
        Graphics2D g2 = (Graphics2D) g.create();

        Queue<int[]> usedQueue = new LinkedList<>(queuedMovement);

        g2.setColor(Color.RED);

        int[] lastPoint = null;

        while (!usedQueue.isEmpty()) {
            int[] currentPoint = usedQueue.poll();

            if (lastPoint != null) {
                g2.drawLine(
                    lastPoint[0] - BackgroundHandler.getOffsetX() + BackgroundHandler.getTileSize() / 2,
                    lastPoint[1] - BackgroundHandler.getOffsetY() + BackgroundHandler.getTileSize() / 2,
                    currentPoint[0] - BackgroundHandler.getOffsetX() +  BackgroundHandler.getTileSize() / 2,
                    currentPoint[1] - BackgroundHandler.getOffsetY()  + BackgroundHandler.getTileSize() / 2
                );
            }

            lastPoint = currentPoint;
        }

        g2.dispose();  // Properly dispose of the Graphics2D context
    }
    
    public void draw(Graphics2D g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.drawImage(getSprites()[LEFT][10][0], (int) getHitBox().getX(), (int) getHitBox().getY(),
                (int) getHitBox().getWidth(), (int) getHitBox().getHeight(), null);

        if (GamePanel.getDebugEnabled()) {
        	if(isAlive()) {
            g2.setColor(Color.orange);
        	} else {
        		g2.setColor(Color.blue);
        	}
            g2.draw(getHitBox());
            drawPathLines(g, getQueuedMovement());
        }

        g2.dispose();
    }
    
    public void processMovementQueue() {
        if (getQueuedMovement() == null || getQueuedMovement().isEmpty()) {
            return;
        }

        int[] target = getQueuedMovement().peek();     
        if (target == null) {
            return;
        }

        // Move towards the target
        if (target[0] > getX()) {
            setX(Math.min(getX() + getSpeed(), target[0]));
        } else if (target[0] < getX()) {
            setX(Math.max(getX() - getSpeed(), target[0]));
        }   

        if (target[1] > getY()) {
            setY(Math.min(getY() + getSpeed(), target[1]));
        } else if (target[1] < getY()) {
            setY(Math.max(getY() - getSpeed(), target[1]));
        }

        // Remove target if reached
        if (getX() == target[0] && getY() == target[1]) {
            getQueuedMovement().poll();

            // If there's no more movement in the queue, recalculate path
            if (getQueuedMovement().isEmpty()) {
                pathNeedsRecalculation = true;
            }
        }

        hitBoxUpdate();
    }

    protected void findPath() {
        int playerX = (int) GamePanel.getInstance().player.getHitBox().getX();
        int playerY = (int) GamePanel.getInstance().player.getHitBox().getY(); 
        Grid grid = new Grid(BackgroundHandler.mapSizeX, BackgroundHandler.mapSizeY);
        grid.buildGridFromMap();

        int tileSize = BackgroundHandler.getTileSize();

        // Start is based on the current bot position
        Node start = new Node(getX() / tileSize, getY() / tileSize, 0, 0);

        // Calculate goal based on player position
        goal = new Node((int) playerX / BackgroundHandler.getTileSize(),
                (int) playerY / BackgroundHandler.getTileSize(), 0, 0);

        // If start and goal are the same, no need to pathfind
        if (start.equals(goal)) {
            pathNeedsRecalculation = false;
            return;
        }

        AStarPathfinding pathfinding = new AStarPathfinding();
        List<Node> path = pathfinding.findPath(grid, start, goal);

        if (!path.isEmpty()) {
            System.out.println("Path found:");
            clearMovementQueue();
            for (Node node : path) {
                queueMovement(node.row * tileSize, node.col * tileSize);
            }
            pathNeedsRecalculation = false;
        } 
    }
    
    @SuppressWarnings(value = { "unused" })
    protected void resolveOverlap(Firebot other) {
        Rectangle thisBox = getHitBox();
        Rectangle otherBox = other.getHitBox();
        other.pathNeedsRecalculation = true;

        // Resolve overlap by moving this Firebot away from the other
        if (thisBox.intersects(otherBox)) {
            // Move horizontally
            if (thisBox.getCenterX() < otherBox.getCenterX()) {
                setX(getX() - overlapResolutionStep);
            } else {
                setX(getX() + overlapResolutionStep);
            }

            // Move vertically
            if (thisBox.getCenterY() < otherBox.getCenterY()) {
                setY(getY() - overlapResolutionStep);
            } else {
                setY(getY() + overlapResolutionStep);
            }
        }
    }

    private void clearMovementQueue() {
        if (getQueuedMovement() != null) {
            getQueuedMovement().clear();
        }
    }

    protected boolean hasPlayerMovedSignificantly() {
        double playerX = GamePanel.getInstance().player.getHitBox().getX();
        double playerY = GamePanel.getInstance().player.getHitBox().getY();

        // Check if the player has moved more than a certain distance (stopDistance)
        boolean hasMoved = Math.hypot(playerX - lastPlayerX, playerY - lastPlayerY) > stopDistance;

        // Update last known position of the player
        lastPlayerX = playerX;
        lastPlayerY = playerY;

        return hasMoved;
    }

    public abstract void update();
    protected abstract void hitBoxUpdate();
    protected abstract void loadEntitySprites();
}
