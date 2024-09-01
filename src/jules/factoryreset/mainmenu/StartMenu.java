package jules.factoryreset.mainmenu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.*;

import jules.factoryreset.main.GamePanel;
import jules.factoryreset.main.Window;

public class StartMenu extends JPanel{
	private static final long serialVersionUID = 1L;
	
	JLabel titleLabel = new JLabel();
	JButton playButton = new JButton();
	JButton quitButton = new JButton();
	
	public StartMenu( Window window, GamePanel gp) {
		setSize(gp.getPreferredSize());
		setBackground(Color.BLACK);
		setLayout(null);
		
		titleLabel.setBounds((int) (getPreferredSize().getWidth() / 2 - 290), 50, 600, 80); // Adjust width/height accordingly
		titleLabel.setForeground(Color.white);
		titleLabel.setText("Factory Reset v0.0.1");
		titleLabel.setFont(new Font("Arial", Font.PLAIN, 60));
		add(titleLabel);
		
		//play button
		playButton.setSize(new Dimension(400, 80));
		playButton.setLocation(getWidth() / 2 - 200, 200);
		playButton.setText("Play");
		playButton.setBackground(Color.green);
		
		playButton.addActionListener(e -> {
			window.remove(this);
			window.add(gp);
			gp.startGameThread();
			gp.repaint();
			window.revalidate();
		});
		
		add(playButton);
		
		//adding button the close the game
		quitButton.setText("Quit game");
		quitButton.setSize(new Dimension(200, 50));
		quitButton.setLocation(10, getHeight() - 97);
		quitButton.setBackground(Color.yellow);
		
		quitButton.addActionListener(e -> {
			System.out.println("Closing game");
			System.exit(0);
		});
		
		add(quitButton);
		
	}

}
