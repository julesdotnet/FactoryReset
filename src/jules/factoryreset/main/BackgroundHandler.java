package jules.factoryreset.main;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import jules.factoryreset.entity.CollisionHandler;

public class BackgroundHandler {
	private static int[][] arrayRow = new int[100][100];
	BackgroundTile[] backgroundTiles = new BackgroundTile[100];
	GamePanel gp;
	SpriteLoader spriteLoader;
	private static int mapSizeX;
	private static int mapSizeY;

	private static int offsetX = 0;
	private static int offsetY = 0;

	private static int playerFocusMovementX;
	private static int playerFocusMovementY;

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
				mapSizeX = row.length;
				for (int i = 0; i < row.length; i++) {
					arrayRow[i][column] = Integer.parseInt(row[i]);
				}
				column++;
			}
			mapSizeY = column;
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
		CollisionHandler.handlePlayerCollision(this);

		// Draw each visible tile directly onto the screen
		int tileSize = getTileSize();
		for (int col = 0; col < mapSizeY; col++) {
			for (int row = 0; row < mapSizeX; row++) {
				// Calculate map tile positions based on offset
				int screenX = (row * tileSize) - offsetX;
				int screenY = (col * tileSize) - offsetY;

				if (screenX < gp.getWidth() && screenY < gp.getHeight() && screenX > -tileSize && screenY > -tileSize) {
					g.drawImage(backgroundTiles[arrayRow[row][col]].tileSprite, screenX, screenY, tileSize, tileSize,
							null);
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
		        if ((cameraCanMoveUp() ^ getPlayerFocusMovementY() > 0) && CollisionHandler.canMoveUp()) {
		            offsetY -= playerSpeed;
		        } else if(CollisionHandler.canMoveUp()) {
		            playerFocusMoveUp();
		        }
		        break;
		    case "LEFT":
		        if (cameraCanMoveLeft() ^ getPlayerFocusMovementX() > 0 && (CollisionHandler.canMoveLeft())) {
		            offsetX -= playerSpeed;
		        } else if(CollisionHandler.canMoveLeft()) {
		            playerFocusMoveLeft();
		        }
		        break;
		    case "DOWN":
		        if (cameraCanMoveDown() ^ getPlayerFocusMovementY() < 0 && (CollisionHandler.canMoveDown())) {
		            offsetY += playerSpeed;
		        } else if(CollisionHandler.canMoveDown()) {
		            playerFocusMoveDown();
		        }
		        break;
		    case "RIGHT":
		        if (cameraCanMoveRight() ^ getPlayerFocusMovementX() < 0 && (CollisionHandler.canMoveRight())) {
		            offsetX += playerSpeed;
		        } else if(CollisionHandler.canMoveRight()) {
		            playerFocusMoveRight();
		        }
		        break;
		    case "UP_LEFT":
		        if (cameraCanMoveUp() ^ getPlayerFocusMovementY() > 0 && (CollisionHandler.canMoveUp())) {
		            offsetY -= playerSpeed;
		        } else if(CollisionHandler.canMoveUp()){
		            playerFocusMoveUp();
		        }
		        if (cameraCanMoveLeft() ^ getPlayerFocusMovementX() > 0 && (CollisionHandler.canMoveLeft())) {
		            offsetX -= playerSpeed;
		        } else if(CollisionHandler.canMoveLeft()){
		            playerFocusMoveLeft();
		        }
		        break;
		    case "UP_RIGHT":
		        if (cameraCanMoveUp() ^ getPlayerFocusMovementY() > 0  && (CollisionHandler.canMoveUp())) {
		            offsetY -= playerSpeed;
		        } else if (CollisionHandler.canMoveUp()){
		            playerFocusMoveUp();
		        }
		        if (cameraCanMoveRight() ^ getPlayerFocusMovementX() < 0  && (CollisionHandler.canMoveRight())) {
		            offsetX += playerSpeed;
		        } else if (CollisionHandler.canMoveRight()) {
		            playerFocusMoveRight();
		        }
		        break;
		    case "DOWN_LEFT":
		        if (cameraCanMoveDown() ^ getPlayerFocusMovementY() < 0 && (CollisionHandler.canMoveDown())) {
		            offsetY += playerSpeed;
		        } else  if (CollisionHandler.canMoveDown()){
		            playerFocusMoveDown();
		        }
		        if (cameraCanMoveLeft() ^ getPlayerFocusMovementX() > 0 && (CollisionHandler.canMoveLeft())) {
		            offsetX -= playerSpeed;
		        } else  if (CollisionHandler.canMoveLeft()) {
		            playerFocusMoveLeft();
		        }
		        break;
		    case "DOWN_RIGHT":
		        if (cameraCanMoveDown() ^ getPlayerFocusMovementY() < 0 && (CollisionHandler.canMoveDown())) {
		            offsetY += playerSpeed;
		        } else  if (CollisionHandler.canMoveDown()) {
		            playerFocusMoveDown();
		        }
		        if (cameraCanMoveRight() ^ getPlayerFocusMovementX() < 0 && (CollisionHandler.canMoveRight())) {
		            offsetX += playerSpeed;
		        } else  if (CollisionHandler.canMoveRight()) {
		            playerFocusMoveRight();
		        }
		        break;
		}

		}
	}
	
	public boolean getTileCollidableAtScreenCoordinates(int screenX, int screenY) {
	    int tileSize = getTileSize();

	    // Convert screenX and screenY to tileCol and tileRow
	    int tileCol = (screenX + offsetX) / tileSize;
	    int tileRow = (screenY + offsetY) / tileSize;

	    //making sure requested tile is within map bounds
	    if (tileCol >= 0 && tileCol < mapSizeX && tileRow >= 0 && tileRow < mapSizeY) {
	        int tileIndex = arrayRow[tileCol][tileRow];
	        return backgroundTiles[tileIndex].collidable;
	    } else return false;
	}


	// functions to determine if camera viewport is on the map like needed
	public static boolean cameraCanMoveUp() {
		return offsetY >= 2;
	}

	public static boolean cameraCanMoveLeft() {
		return offsetX >= 2;
	}

	public boolean cameraCanMoveDown() {
		return offsetY < getTileSize() * mapSizeY - gp.getHeight() - 2;
	}

	public boolean cameraCanMoveRight() {
		return offsetX < getTileSize() * mapSizeX - gp.getWidth() - 2;
	}

	public int getPlayerFocusMovementX() {
		return playerFocusMovementX;
	}

	public int getPlayerFocusMovementY() {
		return playerFocusMovementY;
	}

	// returns the offset of all tiles relative to player, variables change with
	// keyboard input
	public static int getOffsetX() {
		return offsetX;
	}

	public static int getOffsetY() {
		return offsetY;
	}

	void playerFocusMoveUp() {
		playerFocusMovementY -= GamePanel.getPlayer().getSpeed();
	}

	void playerFocusMoveLeft() {
		playerFocusMovementX -= GamePanel.getPlayer().getSpeed();
	}

	void playerFocusMoveDown() {
		playerFocusMovementY += GamePanel.getPlayer().getSpeed();
	}

	void playerFocusMoveRight() {
		playerFocusMovementX += GamePanel.getPlayer().getSpeed();
	}
}
