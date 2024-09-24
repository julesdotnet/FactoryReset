package jules.factoryreset.main;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import jules.factoryreset.utils.CollisionHandler;

//TODO: scale player focus movement map border to be inside viewport

public class BackgroundHandler {
	public static int[][] arrayRow = new int[100][100];
	public static BackgroundTile[] backgroundTiles = new BackgroundTile[100];
	static GamePanel gp;
	SpriteLoader spriteLoader;
	public static int mapSizeX;
	public static int mapSizeY;

	private static int offsetX = 0;
	private static int offsetY = 0;

	private static int playerFocusMovementX;
	private static int playerFocusMovementY;

	public BackgroundHandler(GamePanel gp) {
		BackgroundHandler.gp = gp;
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

	public static int getTileSize() {
		return GamePanel.getInstance().getWidth() / 25; 
	}

	private void mapOffsetHandling() {
		if(GamePanel.getInstance().isEscapeMenuVisible) {
			return;
		}
		final int playerSpeed = GamePanel.getInstance().getPlayer().getSpeed();

		if (GamePanel.getInstance().player.isAlive()) {
			String direction = KeyInput.getDirection().toString();

			switch (direction) {
			case "UP":
				if (CollisionHandler.canMoveUp()) {
					if (cameraCanMoveUp() && getPlayerFocusMovementY() <= 0) {
						offsetY -= playerSpeed;
					} else {
						playerFocusMoveUp();
					}
				}
				break;

			case "LEFT":
				if (CollisionHandler.canMoveLeft()) {
					if (cameraCanMoveLeft() && getPlayerFocusMovementX() <= 0) {
						offsetX -= playerSpeed;
					} else {
						playerFocusMoveLeft();
					}
				}
				break;

			case "DOWN":
				if (CollisionHandler.canMoveDown()) {
					if (cameraCanMoveDown() && getPlayerFocusMovementY() >= 0) {
						offsetY += playerSpeed;
					} else { 
						playerFocusMoveDown();
					}
				}
				break;

			case "RIGHT":
				if (CollisionHandler.canMoveRight()) {
					if (cameraCanMoveRight() && getPlayerFocusMovementX() >= 0) {
						offsetX += playerSpeed;
					} else {
						playerFocusMoveRight();
					}
				}
				break;

			case "UP_LEFT":
				if (CollisionHandler.canMoveUp() && cameraCanMoveUp() && getPlayerFocusMovementY() <= 0) {
					offsetY -= playerSpeed / 1.4;
				} else if (CollisionHandler.canMoveUp()) {
					playerFocusMoveUp();
				}
				if (CollisionHandler.canMoveLeft() && cameraCanMoveLeft() && getPlayerFocusMovementX() <= 0) {
					offsetX -= playerSpeed / 1.4;
				} else if (CollisionHandler.canMoveLeft()) {
					playerFocusMoveLeft();
				}
				break;

			case "UP_RIGHT":
				if (CollisionHandler.canMoveUp() && cameraCanMoveUp() && getPlayerFocusMovementY() <= 0) {
					offsetY -= playerSpeed / 1.4;
				} else if (CollisionHandler.canMoveUp()) {
					playerFocusMoveUp();
				}
				if (CollisionHandler.canMoveRight() && cameraCanMoveRight() && getPlayerFocusMovementX() >= 0) {
					offsetX += playerSpeed / 1.4;
				} else if (CollisionHandler.canMoveRight()) {
					playerFocusMoveRight();
				}
				break;

			case "DOWN_LEFT":
				if (CollisionHandler.canMoveDown() && cameraCanMoveDown() && getPlayerFocusMovementY() >= 0) {
					offsetY += playerSpeed / 1.4;
				} else if (CollisionHandler.canMoveDown()) {
					playerFocusMoveDown();
				}
				if (CollisionHandler.canMoveLeft() && cameraCanMoveLeft() && getPlayerFocusMovementX() <= 0) {
					offsetX -= playerSpeed / 1.4;
				} else if (CollisionHandler.canMoveLeft()) {
					playerFocusMoveLeft();
				}
				break;

			case "DOWN_RIGHT":
				if (CollisionHandler.canMoveDown() && cameraCanMoveDown() && getPlayerFocusMovementY() >= 0) {
					offsetY += playerSpeed / 1.4;
				} else if (CollisionHandler.canMoveDown()) {
					playerFocusMoveDown();
				}
				if (CollisionHandler.canMoveRight() && cameraCanMoveRight() && getPlayerFocusMovementX() >= 0) {
					offsetX += playerSpeed / 1.4;
				} else if (CollisionHandler.canMoveRight()) {
					playerFocusMoveRight();
				}
				break;
			}
		}
	}

	public static boolean getTileCollidableAtScreenCoordinates(int screenX, int screenY) {
		int tileSize = getTileSize();

		// Convert screenX and screenY to tileCol and tileRow
		int tileCol = (screenX + offsetX) / tileSize;
		int tileRow = (screenY + offsetY) / tileSize;

		// making sure requested tile is within map bounds
		if (tileCol >= 0 && tileCol < mapSizeX && tileRow >= 0 && tileRow < mapSizeY) {
			int tileIndex = arrayRow[tileCol][tileRow];
			return backgroundTiles[tileIndex].collidable;
		} else 
			return false;
	}

	// functions to determine if camera viewport is on the map like needed
	public static boolean cameraCanMoveUp() {
		return offsetY >= 2;
	}

	public static boolean cameraCanMoveLeft() {
		return offsetX >= 2;
	}

	public static boolean cameraCanMoveDown() {
		return offsetY < getTileSize() * mapSizeY - gp.getHeight() - 2;
	}

	public static boolean cameraCanMoveRight() {
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
		GamePanel.getInstance();
		playerFocusMovementY -= GamePanel.getInstance().getPlayer().getSpeed();
	}

	void playerFocusMoveLeft() {
		playerFocusMovementX -= GamePanel.getInstance().getPlayer().getSpeed();
	}

	void playerFocusMoveDown() {
		playerFocusMovementY += GamePanel.getInstance().getPlayer().getSpeed();
	}

	void playerFocusMoveRight() {
		playerFocusMovementX += GamePanel.getInstance().getPlayer().getSpeed();
	}
}