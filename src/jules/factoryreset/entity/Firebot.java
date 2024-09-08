package jules.factoryreset.entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.List;

import jules.factoryreset.main.BackgroundHandler;
import jules.factoryreset.main.GamePanel;
import jules.factoryreset.main.SpriteLoader;
import jules.factoryreset.utils.AStarPathfinding;
import jules.factoryreset.utils.Grid;
import jules.factoryreset.utils.Node;

public class Firebot extends Entity {

    private Node goal;
    private boolean pathNeedsRecalculation = true;

    // Track player's previous position to detect movement
    private double previousPlayerX = -1;
    private double previousPlayerY = -1;

    // Distance threshold for recalculating path (e.g., player moves 1 tile)
    private final int recalculationThreshold = BackgroundHandler.getTileSize();

    // Stop threshold: 90 pixels
    private final int stopDistance = 90;

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
    }

    @Override
    public void draw(Graphics2D g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.drawImage(getSprites()[LEFT][10][0], (int) getHitBox().getX(), (int) getHitBox().getY(),
                (int) getHitBox().getWidth(), (int) getHitBox().getHeight(), null);
        g2.dispose();
    }

    @Override
    public void update() {
        // Detect if the player has moved far enough to trigger path recalculation
        if (hasPlayerMoved()) {
            pathNeedsRecalculation = true;
        }

        // Stop if close enough to the player (within 90 pixels on x or y axis)
        if (isCloseToPlayer()) {
            return;  // Don't update path or move if close enough to the player
        }

        if (pathNeedsRecalculation) {
            findPath();
        }

        processMovementQueue();
        hitBoxUpdate();
    }

    @Override
    protected void hitBoxUpdate() {
        getHitBox().setBounds(getX() - BackgroundHandler.getOffsetX(), getY() - BackgroundHandler.getOffsetY(),
                getWidth(), getHeight());
    }

    @Override
    protected void loadEntitySprites() {
        getSprites()[LEFT][10][0] = SpriteLoader.loadSprite("enemies/temp_firebot.png");
        System.out.println(getSprites()[LEFT][10][0] == null ? "Sprite not loaded" : "Sprite loaded");
    }

    public void processMovementQueue() {
        if (getQueuedMovement() == null || getQueuedMovement().isEmpty()) {
            return;
        }

        int[] target = getQueuedMovement().peek();
        if (target == null) {
            return;
        }

        // Move right
        if (target[0] > getX()) {
            setX(Math.min(getX() + getSpeed(), target[0]));
        }
        // Move left
        else if (target[0] < getX()) {
            setX(Math.max(getX() - getSpeed(), target[0]));
        }
        // Move down
        else if (target[1] > getY()) {
            setY(Math.min(getY() + getSpeed(), target[1]));
        }
        // Move up
        else if (target[1] < getY()) {
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

    private void findPath() {
        Grid grid = new Grid(BackgroundHandler.mapSizeX, BackgroundHandler.mapSizeY);
        grid.buildGridFromMap();

        int tileSize = BackgroundHandler.getTileSize();

        // Start is based on the current bot position (not initial start)
        Node start = new Node(getX() / tileSize, getY() / tileSize, 0, 0);

        // Calculate goal based on player position
        goal = new Node(
            (int) GamePanel.getInstance().player.getHitBox().getX() / BackgroundHandler.getTileSize(),
            (int) GamePanel.getInstance().player.getHitBox().getY() / BackgroundHandler.getTileSize(),
            0, 0
        );

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
                System.out.println("Step: (" + node.row + ", " + node.col + ")");
            }
            pathNeedsRecalculation = false;  // Path is found, no need to recalculate until next condition
        } else {
            System.out.println("No path found.");
        }
    }

    private void clearMovementQueue() {
        if (getQueuedMovement() != null) {
            getQueuedMovement().clear();
        }
    }

    // Method to detect if the player has moved enough to trigger a path recalculation
    private boolean hasPlayerMoved() {
        double currentPlayerX = GamePanel.getInstance().player.getHitBox().getX();
        double currentPlayerY = GamePanel.getInstance().player.getHitBox().getY();

        // Calculate the distance the player has moved
        double distanceMovedX = Math.abs(currentPlayerX - previousPlayerX);
        double distanceMovedY = Math.abs(currentPlayerY - previousPlayerY);

        // If player has moved more than the threshold, trigger recalculation
        if (distanceMovedX > recalculationThreshold || distanceMovedY > recalculationThreshold) {
            previousPlayerX = currentPlayerX;
            previousPlayerY = currentPlayerY;
            return true;
        }

        return false;
    }

    // Method to check if the bot is close enough to the player (within 90 pixels)
    private boolean isCloseToPlayer() {
        double playerX = GamePanel.getInstance().player.getHitBox().getCenterX();
        double playerY = GamePanel.getInstance().player.getHitBox().getCenterY();

        double botX = getHitBox().getCenterX();
        double botY = getHitBox().getCenterY();

        // Check if bot is within 90 pixels on either the x-axis or y-axis
        return (Math.abs(botX - playerX) <= stopDistance || Math.abs(botY - playerY) <= stopDistance);
    }
}
