package jules.factoryreset.main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyInput  implements KeyListener {
	private static boolean escapeMenuRequested = false;

    public static enum Direction {UP, LEFT, DOWN, RIGHT, UP_LEFT, DOWN_LEFT, DOWN_RIGHT, UP_RIGHT, NONE};
    private static Direction currentDirection = Direction.NONE;

    private boolean wPressed = false;
    private boolean aPressed = false;
    private boolean sPressed = false;
    private boolean dPressed = false;

    public KeyInput(JPanel panel) {
        bindKey(panel, "W_PRESSED", KeyStroke.getKeyStroke("W"), true);
        bindKey(panel, "A_PRESSED", KeyStroke.getKeyStroke("A"), true);
        bindKey(panel, "S_PRESSED", KeyStroke.getKeyStroke("S"), true);
        bindKey(panel, "D_PRESSED", KeyStroke.getKeyStroke("D"), true);
        
        bindKey(panel, "W_RELEASED", KeyStroke.getKeyStroke("released W"), false);
        bindKey(panel, "A_RELEASED", KeyStroke.getKeyStroke("released A"), false);
        bindKey(panel, "S_RELEASED", KeyStroke.getKeyStroke("released S"), false);
        bindKey(panel, "D_RELEASED", KeyStroke.getKeyStroke("released D"), false);
        
        bindKey(panel, "ESC_RELEASED", KeyStroke.getKeyStroke("released ESCAPE"), false);
        bindKey(panel, "M_RELEASED", KeyStroke.getKeyStroke("released M"), false);
        
    }

    private void bindKey(JPanel panel, String name, KeyStroke keyStroke, boolean pressed) {
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, name);
        panel.getActionMap().put(name, new AbstractAction() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public void actionPerformed(ActionEvent e) {
                switch (name) {
                    case "W_PRESSED":
                    case "W_RELEASED":
                        wPressed = pressed;
                        break;
                    case "A_PRESSED":
                    case "A_RELEASED":
                        aPressed = pressed;
                        break;
                    case "S_PRESSED":
                    case "S_RELEASED":
                        sPressed = pressed;
                        break;
                    case "D_PRESSED":
                    case "D_RELEASED":
                        dPressed = pressed;
                        break;
                       
                    case "ESC_PRESSED":
                    case "ESC_RELEASED":
                        escapeMenuRequested = pressed;
                        GamePanel.getInstance().isEscapeMenuVisible = !GamePanel.getInstance().isEscapeMenuVisible;
                        break;
                    
                    case "M_RELEASED":
                    	GamePanel.toggleDebugEnabled();
                    	break;
                }
                updateDirection();
            }
        });
    }

    private void updateDirection() {
        if (wPressed && aPressed) {
            currentDirection = Direction.UP_LEFT;
        } else if (wPressed && dPressed) {
            currentDirection = Direction.UP_RIGHT;
        } else if (sPressed && aPressed) {
            currentDirection = Direction.DOWN_LEFT;
        } else if (sPressed && dPressed) {
            currentDirection = Direction.DOWN_RIGHT;
        } else if (wPressed) {
            currentDirection = Direction.UP;
        } else if (aPressed) {
            currentDirection = Direction.LEFT;
        } else if (sPressed) {
            currentDirection = Direction.DOWN;
        } else if (dPressed) {
            currentDirection = Direction.RIGHT;
        } else {
            currentDirection = Direction.NONE;
        }
    }

    public static Direction getDirection() {
    		return currentDirection;
    }
    public static boolean getEscapeMenuRequested() {
    	return escapeMenuRequested;
    }

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		System.out.println("hi");
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.out.println("escape released!");
			
		} 
		
	}
}
