package jules.factoryreset.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import jules.factoryreset.main.BackgroundHandler;
import jules.factoryreset.main.GamePanel;
import jules.factoryreset.main.KeyInput;
import jules.factoryreset.main.SpriteLoader;
import jules.factoryreset.utils.AStarPathfinding;
import jules.factoryreset.utils.Grid;
import jules.factoryreset.utils.Node;

public class Firebot extends Entity {

	private static final Set<Firebot> allFirebots = new HashSet<>();

	private Node goal;
	private boolean pathNeedsRecalculation = true;

	private final int stopDistance = 90;
	private final int overlapResolutionStep = 5; // Step size for resolving overlaps

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

	@Override
	public void draw(Graphics2D g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.drawImage(getSprites()[LEFT][10][0], (int) getHitBox().getX(), (int) getHitBox().getY(),
				(int) getHitBox().getWidth(), (int) getHitBox().getHeight(), null);

		if (GamePanel.getDebugEnabled()) {
			g2.setColor(Color.orange);
			g2.draw(getHitBox());
			drawPathLines(g, getQueuedMovement());
		}

		g2.dispose();
	}

	private void drawPathLines(Graphics g, Queue<int[]> queuedMovement) {
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
	
	private void scaleFirebots() {
		getHitBox().setSize(BackgroundHandler.getTileSize(), BackgroundHandler.getTileSize());
	}

	@Override
	public void update() {
		scaleFirebots();
		if (hasPlayerMoved()) {
			pathNeedsRecalculation = true;
		}
		// TODO: add proper playermoved statement
		if (isCloseToPlayer() && !hasPlayerMoved()) {
			return;
		}

		if (pathNeedsRecalculation) {
			findPath();
		}

		processMovementQueue();
		hitBoxUpdate();

		System.out.println(positionToNode().col);
	}

	@Override
	protected void hitBoxUpdate() {
		getHitBox().setBounds(x, y, width, height);

	}

	@SuppressWarnings(value = { "unused" })
	private void resolveOverlap(Firebot other) {
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

	private void findPath() {
		Grid grid = new Grid(BackgroundHandler.mapSizeX, BackgroundHandler.mapSizeY);
		grid.buildGridFromMap();

		int tileSize = BackgroundHandler.getTileSize();

		// Start is based on the current bot position
		Node start = new Node(getX() / tileSize, getY() / tileSize, 0, 0);

		// Calculate goal based on player position
		goal = new Node((int) GamePanel.getInstance().player.getHitBox().getX() / BackgroundHandler.getTileSize(),
				(int) GamePanel.getInstance().player.getHitBox().getY() / BackgroundHandler.getTileSize(), 0, 0);

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
			pathNeedsRecalculation = false; // Path is found, no need to recalculate until next condition
		} else {
			System.out.println("No path found.");
		}
	}

	private void clearMovementQueue() {
		if (getQueuedMovement() != null) {
			getQueuedMovement().clear();
		}
	}

	private boolean hasPlayerMoved() {
		if (KeyInput.getDirection().toString().equals("NONE")) {
			return false;
		} else
			return true;
	}

	private boolean isCloseToPlayer() {
		double playerX = GamePanel.getInstance().player.getHitBox().getCenterX();
		double playerY = GamePanel.getInstance().player.getHitBox().getCenterY();

		double botX = getHitBox().getCenterX();
		double botY = getHitBox().getCenterY();

		return (Math.abs(botX - playerX) <= stopDistance || Math.abs(botY - playerY) <= stopDistance);
	}
}
