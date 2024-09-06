package jules.factoryreset.utils;

import jules.factoryreset.main.BackgroundHandler;

public class Grid {
    private final int rows;
    private final int cols;
    private final int[][] grid;

    public Grid(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new int[rows][cols];
    }

    public void setCell(int row, int col, int value) {
        grid[row][col] = value;
    }
    
    public void buildGridFromMap() {
    	if(BackgroundHandler.mapSizeX == 0 | BackgroundHandler.mapSizeY == 0) {
    		System.out.println("ERROR: Mapfile is empty and cannot be used to create a grid!");
    		return;
    	}
    	for(int y = 0; y < BackgroundHandler.mapSizeY; y++) {
    		for(int x = 0; x < BackgroundHandler.mapSizeX; x++) {
    			if(!BackgroundHandler.backgroundTiles[BackgroundHandler.arrayRow[x][y]].collidable) {
    				setCell(x, y, 0);
    			} else {
    				setCell(x, y, 1);
    			}
    		}
    	}
    }

    public int getCell(int row, int col) {
        return grid[row][col];
    }

    public int getRows() {
        return rows; 
    }

    public int getCols() {
        return cols;
    }
}
