package jules.factoryreset.main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import jules.factoryreset.entity.Player;

public class MouseListener {

	private static int mouseX = 0;
	private static int mouseY = 0;

	GamePanel gp;
	Player player;
	private static boolean leftMouseButtonClicked = false;

	public MouseListener(GamePanel gp) {

		this.gp = gp;
		// Add a MouseMotionListener to track mouse movement
		gp.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				mouseX = e.getX(); // Get X position of the mouse
				mouseY = e.getY(); // Get Y position of the mouse
			}
		});

		gp.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					leftMouseButtonClicked = true;
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					leftMouseButtonClicked = false;
				}
			}

		});

	}

	public static int getMouseX() {
		return mouseX + 13;
	}

	public static int getMouseY() {
		return mouseY + 13;
	}

	public static boolean getLeftMouseButtonClicked() {
		return leftMouseButtonClicked;
	}
}