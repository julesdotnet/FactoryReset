package jules.factoryreset.entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.List;

import jules.factoryreset.main.BackgroundHandler;
import jules.factoryreset.main.SpriteLoader;
import jules.factoryreset.utils.AStarPathfinding;
import jules.factoryreset.utils.Grid;
import jules.factoryreset.utils.Node;

public class Firebot extends Entity {

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
        g2.drawImage(getSprites()[LEFT][10][0], (int) getHitBox().getX(), (int) getHitBox().getY(), (int) getHitBox().getWidth(), (int) getHitBox().getHeight(), null);
        g2.dispose();
    }

    @Override
    public void update() {
        findPath();
        processMovementQueue();
    }

    @Override
	protected
    void hitBoxUpdate() {
        getHitBox().setBounds(getX() - BackgroundHandler.getOffsetX(), getY() - BackgroundHandler.getOffsetY(), getWidth(), getHeight());
    }

    @Override
	protected
    void loadEntitySprites() {
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

        boolean moved = false;

        // Move right
        if (target[0] > getX()) {
            setX(Math.min(getX() + getSpeed(), target[0]));
            moved = true;
        }
        // Move left
        if (target[0] < getX()) {
            setX(Math.max(getX() - getSpeed(), target[0]));
            moved = true;
        }
        // Move down
        if (target[1] > getY()) {
            setY(Math.min(getY() + getSpeed(), target[1]));
            moved = true;
        }
        // Move up
        if (target[1] < getY()) {
            setY(Math.max(getY() - getSpeed(), target[1]));
            moved = true;
        }

        if (!moved || (getX() == target[0] && getY() == target[1])) {
            getQueuedMovement().poll();
        }
        hitBoxUpdate();
    }

    private void findPath() {
        Grid grid = new Grid(BackgroundHandler.mapSizeX, BackgroundHandler.mapSizeY);
        grid.buildGridFromMap();

        int tileSize = BackgroundHandler.getTileSize();

        Node start = positionToNode();
        setX(start.row * tileSize);
        setY(start.col * tileSize);
        Node goal = new Node(9, 9, 0, 0);

        AStarPathfinding pathfinding = new AStarPathfinding();
        List<Node> path = pathfinding.findPath(grid, start, goal);

        if (!path.isEmpty()) {
            System.out.println("Path found:");
            for (Node node : path) {
                queueMovement(node.row * tileSize, node.col * tileSize);
                System.out.println("Step: (" + node.row + ", " + node.col + ")");
            }
        } else {
            System.out.println("No path found.");
        }
    }
}
