package jules.factoryreset.main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JButton;
import javax.swing.JPanel;

import jules.factoryreset.mainmenu.StartMenu;

public class EscapeMenu extends JPanel {

	private static final long serialVersionUID = 1L;
	private float opacity = 0.5f;
	private JButton leaveGame = new JButton("Leave game");
	private JButton setFullscreen = new JButton("Enable Fullscreen Mode");
	StartMenu startMenu;

	protected EscapeMenu(GamePanel gp, Window window) {
		startMenu = new StartMenu(window, gp);
		setPreferredSize(gp.getPreferredSize());
		setBackground(Color.gray);
		setOpaque(false);
		setOpacity(opacity);

		leaveGame.setBackground(Color.red);
		leaveGame.setFont(new Font("Arial", Font.PLAIN, 40));
		leaveGame.setForeground(Color.white);
		leaveGame.addActionListener(e -> {
			gp.remove(this);
			gp.revalidate();
			GamePanel.getInstance().isEscapeMenuVisible = false;
			window.remove(gp);
			window.revalidate();
			window.add(startMenu);
		});
		add(leaveGame);

		// Assuming window is your JFrame or other top-level container
		setFullscreen.setBackground(Color.gray);
		setFullscreen.setFont(new Font("Arial", Font.PLAIN, 30));
		setFullscreen.setForeground(Color.white);
		setFullscreen.addActionListener(e -> {
		    GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		    if (gd.isFullScreenSupported()) {
		        if (gd.getFullScreenWindow() == window) {
		            // Exiting fullscreen mode
		            gd.setFullScreenWindow(null);
		            window.dispose();
		            window.setUndecorated(false); 
		            window.setVisible(true);
		            setFullscreen.setText("Enable FullScreen");
		            
		            this.setPreferredSize(GamePanel.getInstance().getSize());
		        } else {
		            // Entering fullscreen mode
		        	window.dispose();
		            window.setUndecorated(true);
		            window.setVisible(false);
		            gd.setFullScreenWindow(window);
		            setFullscreen.setText("Disable FullScreen");
		            this.setPreferredSize(GamePanel.getInstance().getSize());
		        }
		        window.revalidate();
		    }
		});

		add(setFullscreen);

	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
		g2d.setColor(getBackground());
		g2d.fillRect(0, 0, getWidth(), getHeight());
		g2d.dispose();

		super.paintComponent(g); // Continue with the normal painting
	}

	public void setOpacity(float opacity) {
		this.opacity = opacity;
		repaint(); // Repaint the panel to apply the new opacity
	}
}
