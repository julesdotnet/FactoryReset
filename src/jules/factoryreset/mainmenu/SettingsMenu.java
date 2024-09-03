package jules.factoryreset.mainmenu;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import jules.factoryreset.main.Window;

public class SettingsMenu extends JPanel {
	private static final long serialVersionUID = 1L;
	
	JLabel title = new JLabel("Settings");
	
	JButton openGraphics = new JButton("Graphics");
	JButton openAudio = new JButton("Audio");
	JButton openMainMenu = new JButton("Back");
	
	GraphicSettings mainSettings;
	
	SettingsMenu(Window window){
		mainSettings = new GraphicSettings(window);
		setBackground(Color.black);
		setPreferredSize(window.getPreferredSize());
		setLayout(null);
		
		title.setText("Settings");
		title.setForeground(Color.white);
		title.setFont(new Font("Arial", Font.PLAIN, 60));
		title.setBounds(525, 10, 1000, 100);
		add(title);
		
		openGraphics.setBackground(Color.decode("#5941A9"));
		openGraphics.setFont(new Font("Arial", Font.PLAIN, 20));
		openGraphics.setBounds(325, 200, 200, 50);
		
		openGraphics.addActionListener(e -> {
			
		});
		add(openGraphics);
		
		openMainMenu.setBackground(Color.decode("#5941A9"));
		openMainMenu.setFont(new Font("Arial", Font.PLAIN, 20));
		openMainMenu.setBounds(10, 630, 200, 50);
		
		openMainMenu.addActionListener(e -> {
			window.remove(this);                 
		    window.add(window.startMenu);        
		    window.revalidate();                 
		    window.repaint();       
		});
		add(openMainMenu);
		
	}
	
}
