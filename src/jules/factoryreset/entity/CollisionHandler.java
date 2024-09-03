package jules.factoryreset.entity;

import java.awt.Rectangle;
import jules.factoryreset.main.BackgroundHandler;
import jules.factoryreset.main.GamePanel;
import jules.factoryreset.main.KeyInput;

public class CollisionHandler {
    // Private boolean variables to control movement
    private static boolean canMoveUp = true;
    private static boolean canMoveLeft = true;
    private static boolean canMoveDown = true;
    private static boolean canMoveRight = true;

    // Public getters for the boolean variables
    public static boolean canMoveUp() {
        return canMoveUp;
    }

    public static boolean canMoveLeft() {
        return canMoveLeft;
    }

    public static boolean canMoveDown() {
        return canMoveDown;
    }

    public static boolean canMoveRight() {
        return canMoveRight;
    }

    // Private setters for the boolean variables
    private static void setCanMoveUp(boolean canMove) {
        canMoveUp = canMove;
    }

    private static void setCanMoveLeft(boolean canMove) {
        canMoveLeft = canMove;
    }

    private static void setCanMoveDown(boolean canMove) {
        canMoveDown = canMove;
    }

    private static void setCanMoveRight(boolean canMove) {
        canMoveRight = canMove;
    }

    // Method to handle player collision detection
    public static void handlePlayerCollision(BackgroundHandler bgHandler) {
        if (!GamePanel.player.isAlive()) {
            return;
        }

        double speed = GamePanel.player.getSpeed();
        String direction = KeyInput.getDirection().toString();
        Rectangle hitBox = GamePanel.player.getHitBox();

        // Reset movement flags
        setCanMoveUp(true);
        setCanMoveLeft(true);
        setCanMoveDown(true);
        setCanMoveRight(true);

        switch (direction) {
            case "UP":
                for (int i = (int) hitBox.getX(); i < hitBox.getX() + hitBox.getWidth(); i++) {
                    if (bgHandler.getTileCollidableAtScreenCoordinates(i, (int) (hitBox.getY() - speed))) {
                        setCanMoveUp(false);
                        break;
                    } else setCanMoveUp(true);
                }
                break;

            case "LEFT":
                for (int i = (int) hitBox.getY(); i < hitBox.getY() + hitBox.getHeight(); i++) {
                    if (bgHandler.getTileCollidableAtScreenCoordinates((int) (hitBox.getX() - speed), i)) {
                        System.out.println("left: collision detected");
                        setCanMoveLeft(false);
                        break;
                    } else setCanMoveLeft(true);
                }
                break;

            case "DOWN":
                for (int i = (int) hitBox.getX(); i < hitBox.getX() + hitBox.getWidth(); i++) {
                    if (bgHandler.getTileCollidableAtScreenCoordinates(i, (int) (hitBox.getY() + hitBox.getHeight() + speed))) {
                        System.out.println("downward: collision detected");
                        setCanMoveDown(false);
                        break;
                    }
                }
                break;

            case "RIGHT":
                for (int i = (int) hitBox.getY(); i < hitBox.getY() + hitBox.getHeight(); i++) {
                    if (bgHandler.getTileCollidableAtScreenCoordinates((int) (hitBox.getX() + hitBox.getWidth() + speed), i)) {
                        System.out.println("right: collision detected");
                        setCanMoveRight(false);
                        break;
                    }
                }
                break;

            case "UP_LEFT":
            case "UP_RIGHT":
            case "DOWN_LEFT":
            case "DOWN_RIGHT":
                int xDirection = 0;
                int yDirection = 0;

                // Determine the direction of movement based on the case
                switch (KeyInput.getDirection().toString()) {
                    case "UP_LEFT":
                        xDirection = -(int) speed;
                        yDirection = -(int) speed;
                        break;
                    case "UP_RIGHT":
                        xDirection = (int) speed;
                        yDirection = -(int) speed;
                        break;
                    case "DOWN_LEFT":
                        xDirection = -(int) speed;
                        yDirection = (int) speed;
                        break;
                    case "DOWN_RIGHT":
                        xDirection = (int) speed;
                        yDirection = (int) speed;
                        break;
                }

                // Check horizontal movement (LEFT/RIGHT) while considering diagonal direction
                for (int i = (int) hitBox.getY(); i < hitBox.getY() + hitBox.getHeight(); i++) {
                    if (bgHandler.getTileCollidableAtScreenCoordinates((int) hitBox.getX() + xDirection, i)) {
                        System.out.println("diagonal: horizontal collision detected");
                        if (xDirection < 0) {
                            setCanMoveLeft(false);
                        } else {
                            setCanMoveRight(false);
                        }
                        break;
                    }
                }

                // Check vertical movement (UP/DOWN) while considering diagonal direction
                for (int i = (int) hitBox.getX(); i < hitBox.getX() + hitBox.getWidth(); i++) {
                    if (bgHandler.getTileCollidableAtScreenCoordinates(i, (int) hitBox.getY() + yDirection)) {
                        System.out.println("diagonal: vertical collision detected");
                        if (yDirection < 0) {
                            setCanMoveUp(false);
                        } else {
                            setCanMoveDown(false);
                        }
                        break;
                    }
                }
                break;
        }
    }
}
