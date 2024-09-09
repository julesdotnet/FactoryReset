package jules.factoryreset.entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Queue;

import jules.factoryreset.main.BackgroundHandler;
import jules.factoryreset.utils.Node;

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

    protected int x;
    private int y;
    private int width;
    private int height;

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
        return new Node(x / tileSize - BackgroundHandler.getOffsetX(), y / tileSize - BackgroundHandler.getOffsetY(), 0, 0);
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

    // Abstract methods to be implemented by subclasses
    public abstract void draw(Graphics2D g);
    public abstract void update();
    protected abstract void hitBoxUpdate();
    protected abstract void loadEntitySprites();
}
