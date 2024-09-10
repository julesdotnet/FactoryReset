package jules.factoryreset.main;

import java.awt.image.*;
import java.io.*;

import javax.imageio.*;

import jules.factoryreset.entity.*;

public class SpriteLoader {

	public static BufferedImage loadSprite(String path) {
		BufferedImage sprite = null;

		try (InputStream inputStream = SpriteLoader.class.getClassLoader().getResourceAsStream(path)){

			sprite = ImageIO.read(inputStream);

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("ERROR: Image could not be loaded from path: " + path);
		} catch (NullPointerException e) {
			e.printStackTrace();
			System.out.println("ERROR: File is null!");
		}
		return sprite;
	}

	public static final int DOWN = 0; 
	public static final int LEFT = 1;
	public static final int RIGHT = 2;
	public static final int UP = 3;

	public static BufferedImage spriteAnimationHandling(int energyPoints, String currentDirection, int animationState,
			BufferedImage[][][] entitySprites, Entity entity) {

		if (entity.isAlive() && !GamePanel.getInstance().isEscapeMenuVisible) {
			int directionInt = 0;
			if(!currentDirection.equals("NONE")) {
				entity.animationTicks++;
			}

			// switching between animation frames
			if (entity.animationTicks >= 12) {
				if (entity.getAnimationState() == 0) {
					setNewAnimationState(1, entity); 
				} else if (entity.getAnimationState() == 1) {
					setNewAnimationState(0, entity);
				} 
				entity.animationTicks = 0;
			}

			// determining correct sprite direction
			switch (currentDirection) {
			case "UP":
				directionInt = UP;
				break;

			case "LEFT":
			case "UP_LEFT":
			case "DOWN_LEFT":
				directionInt = LEFT;
				break;

			case "DOWN":
				directionInt = DOWN;
				break;

			case "RIGHT":
			case "UP_RIGHT":
			case "DOWN_RIGHT":
				directionInt = RIGHT;
				break;
			}

			return entitySprites[directionInt][energyPoints][animationState];
		} else if(GamePanel.getInstance().isEscapeMenuVisible) {
			return entitySprites[DOWN][energyPoints][0];
		} else	return entitySprites[DOWN][0][0];
	}

	private static void setNewAnimationState(int newAnimationState, Entity entity) {
		entity.setAnimationState(newAnimationState);
	}
}