package jules.factoryreset.utils;

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
        if (!GamePanel.getInstance().player.isAlive()) {
            return;
        }

        double speed = GamePanel.getInstance().player.getSpeed();
        String direction = KeyInput.getDirection().toString();
        Rectangle hitBox = GamePanel.getInstance().player.getHitBox();

        switch (direction) {
        //cardinals
            case "UP":
                for (int i = (int) hitBox.getX(); i < hitBox.getX() + hitBox.getWidth(); i++) {
                    if (BackgroundHandler.getTileCollidableAtScreenCoordinates(i, (int) (hitBox.getY() - 2 * speed))) {
                        setCanMoveUp(false);
                        break;
                    } else setCanMoveUp(true);
                }
                break; 

            case "LEFT":
                for (int i = (int) hitBox.getY(); i < hitBox.getY() + hitBox.getHeight(); i++) {
                    if (BackgroundHandler.getTileCollidableAtScreenCoordinates((int) (hitBox.getX() - 2 *speed), i)) {
                        setCanMoveLeft(false);
                        break;
                    } else setCanMoveLeft(true);
                }
                break;

            case "DOWN":
                for (int i = (int) hitBox.getX(); i < hitBox.getX() + hitBox.getWidth(); i++) {
                    if (BackgroundHandler.getTileCollidableAtScreenCoordinates(i, (int) (hitBox.getY() + hitBox.getHeight() + 2 * speed))) {
                        setCanMoveDown(false);
                        break;
                    } else setCanMoveDown(true);
                } 
                break;

            case "RIGHT":
                for (int i = (int) hitBox.getY(); i < hitBox.getY() + hitBox.getHeight(); i++) {
                    if (BackgroundHandler.getTileCollidableAtScreenCoordinates((int) (hitBox.getX() + hitBox.getWidth() +  2 * speed), i)) {
                        setCanMoveRight(false);
                        break;
                    } else setCanMoveRight(true);
                }
                break;
                
               //diagonals
            case "UP_LEFT":
            	for (int i = (int) hitBox.getX(); i < hitBox.getX() + hitBox.getWidth(); i++) {
                    if (bgHandler.getTileCollidableAtScreenCoordinates(i, (int) (hitBox.getY() - 2 * speed))) {
                        setCanMoveUp(false);
                        break;
                    } else setCanMoveUp(true);
                }
            	for (int i = (int) hitBox.getY(); i < hitBox.getY() + hitBox.getHeight(); i++) {
                    if (bgHandler.getTileCollidableAtScreenCoordinates((int) (hitBox.getX() - 2 * speed), i)) {
                        setCanMoveLeft(false);
                        break;
                    } else setCanMoveLeft(true);
                }
            	break;
            case "UP_RIGHT":
            	for (int i = (int) hitBox.getY(); i < hitBox.getY() + hitBox.getHeight(); i++) {
                    if (bgHandler.getTileCollidableAtScreenCoordinates((int) (hitBox.getX() + hitBox.getWidth() + 2 * speed), i)) {
                        setCanMoveRight(false);
                        break;
                    } else setCanMoveRight(true);
                }
            	
            	for (int i = (int) hitBox.getX(); i < hitBox.getX() + hitBox.getWidth(); i++) {
                    if (bgHandler.getTileCollidableAtScreenCoordinates(i, (int) (hitBox.getY() - 2 * speed))) {
                        setCanMoveUp(false);
                        break;
                    } else setCanMoveUp(true);
                }
            	break;
            case "DOWN_LEFT":
            	for (int i = (int) hitBox.getY(); i < hitBox.getY() + hitBox.getHeight(); i++) {
                    if (bgHandler.getTileCollidableAtScreenCoordinates((int) (hitBox.getX() -  2 * speed), i)) {
                        setCanMoveLeft(false);
                        break;
                    } else setCanMoveLeft(true);
                }
            	for (int i = (int) hitBox.getX(); i < hitBox.getX() + hitBox.getWidth(); i++) {
                    if (bgHandler.getTileCollidableAtScreenCoordinates(i, (int) (hitBox.getY() + hitBox.getHeight() + 2 *speed))) {
                        setCanMoveDown(false);
                        break;
                    } else setCanMoveDown(true);
                } 
            	break;
            case "DOWN_RIGHT":
            	for (int i = (int) hitBox.getY(); i < hitBox.getY() + hitBox.getHeight(); i++) {
                    if (BackgroundHandler.getTileCollidableAtScreenCoordinates((int) (hitBox.getX() + hitBox.getWidth() + 2 * speed), i)) {
                        setCanMoveRight(false);
                        break;
                    } else setCanMoveRight(true);
                }
            	
            	for (int i = (int) hitBox.getX(); i < hitBox.getX() + hitBox.getWidth(); i++) {
                    if (BackgroundHandler.getTileCollidableAtScreenCoordinates(i, (int) (hitBox.getY() + hitBox.getHeight() + 2 * speed))) {
                        setCanMoveDown(false);
                        break;
                    } else setCanMoveDown(true);
                } 
               break;
        }
    }
    
    public static boolean getMovable(String direction) {
		switch (direction) {
		case "UP":
			return canMoveUp();
		case "UP_LEFT":
			return canMoveUp() && canMoveLeft();

		case "UP_RIGHT":
			return canMoveUp() && canMoveRight();
			
		case "DOWN":
			return canMoveDown();
			
		case "DOWN_LEFT":
			return canMoveDown() && canMoveLeft();
			
		case "DOWN_RIGHT":
			return canMoveDown() && canMoveRight();
			
		case "RIGHT":
			return canMoveRight();
			
		case "LEFT":
			return canMoveLeft();
			
		default:
			return true;
		}
    }
}
