package jules.factoryreset.utils;

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
