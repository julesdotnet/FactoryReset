package jules.factoryreset.main;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BackgroundHandler {
	private static int[][] arrayRow = new int[100][100];
	BackgroundTile[] backgroundTiles = new BackgroundTile[100];
	GamePanel gp;
	SpriteLoader spriteLoader;

	private static int offsetX = 0;
	private static int offsetY = 0;

	public BackgroundHandler(GamePanel gp) {
		this.gp = gp;
		spriteLoader = new SpriteLoader();
		loadBackgroundTiles();
	}

	public static void loadMapFile(String filePath) {
		try (InputStream inputStream = BackgroundHandler.class.getResourceAsStream(filePath);
				BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {

			if (inputStream == null) {
				System.out.println("ERROR: Map file not found at path: " + filePath);
				return;
			}

			String line;
			int column = 0;

			while ((line = br.readLine()) != null) {
				String[] row = line.split(" ");
				for (int i = 0; i < row.length; i++) {
					arrayRow[i][column] = Integer.parseInt(row[i]);
				}
				column++;
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("ERROR: Map " + filePath + " could not be imported!");
		} catch (NumberFormatException e) {
			e.printStackTrace();
			System.out.println("ERROR: Map file contains invalid characters! Only numbers allowed!");
		} catch (NullPointerException e) {
			e.printStackTrace();
			System.out.println("The map file is null!");
		}
	}

	void drawMap(Graphics2D g) {
		// Handle offsets for map movement
		mapOffsetHandling();

		// Draw each visible tile directly onto the screen
		int tileSize = getTileSize();
		for (int col = 0; col < arrayRow.length; col++) {
			for (int row = 0; row < arrayRow[col].length; row++) {
				// Calculate map tile positions based on offset
				int screenX = (row * tileSize) - offsetX;
				int screenY = (col * tileSize) - offsetY;
				
				if(screenX < gp.getWidth() && screenY < gp.getHeight() && screenX > -tileSize && screenY > -tileSize) {
					g.drawImage(backgroundTiles[arrayRow[row][col]].tileSprite, screenX, screenY, tileSize, tileSize, null);
				}
			}
		}
	}

	void loadBackgroundTiles() {
		try {
			backgroundTiles[0] = new BackgroundTile();
			backgroundTiles[0].tileSprite = SpriteLoader.loadSprite("factoryassets/factory floor.png");

			backgroundTiles[1] = new BackgroundTile();
			backgroundTiles[1].tileSprite = SpriteLoader.loadSprite("factoryassets/wooden-box.png");
			backgroundTiles[1].collidable = true;
			
			backgroundTiles[2] = new BackgroundTile();
			backgroundTiles[2].tileSprite = SpriteLoader.loadSprite("factoryassets/wall.png");
			backgroundTiles[2].collidable = true;
		} catch (NullPointerException e) {
			e.printStackTrace();
			System.out.println("ERROR: Background Tile Object is null!");
		}
	}

	int getTileSize() {
		return gp.getWidth() / 25; // Assuming 25 tiles across the width of the screen
	}

	private void mapOffsetHandling() {
		final int playerSpeed = GamePanel.getPlayer().getSpeed();
		if (GamePanel.player.isAlive()) {
			switch (KeyInput.getDirection().toString()) {
			case "UP":
				offsetY -= playerSpeed;
				break;
			case "LEFT":
				offsetX -= playerSpeed;
				break;
			case "DOWN":
				offsetY += playerSpeed;
				break;
			case "RIGHT":
				offsetX += playerSpeed;
				break;
			case "UP_LEFT":
				offsetX -= playerSpeed;
				offsetY -= playerSpeed;
				break;
			case "UP_RIGHT":
				offsetX += playerSpeed;
				offsetY -= playerSpeed;
				break;
			case "DOWN_LEFT":
				offsetX -= playerSpeed;
				offsetY += playerSpeed;
				break;
			case "DOWN_RIGHT":
				offsetX += playerSpeed;
				offsetY += playerSpeed;
				break;
			}
		}
	}
	
	public static int getOffsetX() {
		return offsetX;
	}
	
	public static int getOffsetY() {
		return offsetY;
	}
}
