package jules.factoryreset.utils;

import java.util.*;

public class AStarPathfinding {

    private static final int[] ROW_DIRECTIONS = {-1, 1, 0, 0};
    private static final int[] COL_DIRECTIONS = {0, 0, -1, 1};

    public List<Node> findPath(Grid grid, Node start, Node goal) {
        PriorityQueue<Node> openList = new PriorityQueue<>();
        Set<Node> closedList = new HashSet<>();

        openList.add(start);

        while (!openList.isEmpty()) {
            Node current = openList.poll();

            if (current.row == goal.row && current.col == goal.col) {
                return reconstructPath(current);
            }

            closedList.add(current);

            for (int i = 0; i < ROW_DIRECTIONS.length; i++) {
                int newRow = current.row + ROW_DIRECTIONS[i];
                int newCol = current.col + COL_DIRECTIONS[i];

                if (isValid(grid, newRow, newCol) && !closedList.contains(new Node(newRow, newCol, 0, 0))) {
                    int gCost = current.gCost + 1; // Assumes uniform cost
                    int hCost = heuristic(newRow, newCol, goal.row, goal.col);

                    Node neighbor = new Node(newRow, newCol, gCost, hCost);
                    neighbor.parent = current;

                    if (!openList.contains(neighbor)) { 
                        openList.add(neighbor); 
                    }
                }
            }
        }

        return Collections.emptyList(); // No path found
    }

    private boolean isValid(Grid grid, int row, int col) {
        return row >= 0 && col >= 0 && row < grid.getRows() && col < grid.getCols() && grid.getCell(row, col) != 1;
    }

    private int heuristic(int row1, int col1, int row2, int col2) {
        return Math.abs(row1 - row2) + Math.abs(col1 - col2);
    }

    private List<Node> reconstructPath(Node goal) {
        List<Node> path = new ArrayList<>();
        Node current = goal;
        while (current != null) {
            path.add(current);
            current = current.parent;
        }
        Collections.reverse(path);
        return path;
    }
}