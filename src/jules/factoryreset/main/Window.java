package jules.factoryreset.main;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.List;

import javax.swing.*;

import jules.factoryreset.mainmenu.StartMenu;
import jules.factoryreset.utils.*;

public class Window extends JFrame {

    private static final long serialVersionUID = 1L;
    public StartMenu startMenu;

    public Window() {
        // Initialize GamePanel here
        GamePanel.initInstance(this);

        setTitle("Factory Reset - v0.0.2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setFullScreenMode(false);
        setResizable(false);

        startMenu = new StartMenu(this, GamePanel.getInstance());
        add(startMenu);

        setVisible(true);
    }

    public static void main(String[] args) {
        System.gc();
        SwingUtilities.invokeLater(Window::new);
        
        Grid grid = new Grid(2, 1);
        // Set cells (1 represents an obstacle, 0 represents a free cell)
        grid.setCell(1, 0, 0); // Example: obstacle in the second row

        Node start = new Node(0, 0, 0, 0);
        Node goal = new Node(1, 0, 0, 0);

        AStarPathfinding aStar = new AStarPathfinding();
        List<Node> path = aStar.findPath(grid, start, goal);

        if (!path.isEmpty()) {
            System.out.println("Path found:");
            for (Node node : path) {
                System.out.println("(" + node.row + ", " + node.col + ")");
            }
        } else {
            System.out.println("No path found");
        }
    }

    protected void setFullScreenMode(boolean enabled) {
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();

        if (graphicsDevice.isFullScreenSupported() && enabled) {
            setUndecorated(true);
            graphicsDevice.setFullScreenWindow(this);
        } else if (!enabled) {
            // Ensure that GamePanel instance is initialized before getting its preferred size
            GamePanel.getInstance(); // Ensure initialization
            setSize(GamePanel.getInstance().getPreferredSize());
        } else {
            // Fallback if fullscreen mode is not supported
            GamePanel.getInstance(); // Ensure initialization
            setSize(GamePanel.getInstance().getPreferredSize());
        }
    }
}
