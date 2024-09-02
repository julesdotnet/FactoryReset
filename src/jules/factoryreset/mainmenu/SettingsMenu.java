package jules.factoryreset.mainmenu;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import jules.factoryreset.main.Window;

public class SettingsMenu extends JPanel {
	private static final long serialVersionUID = 1L;
	
	JLabel title = new JLabel("Settings");
	JTabbedPane categoryPanel = new JTabbedPane();
	MainSettings mainSettings;
	
	SettingsMenu(Window window){
		mainSettings = new MainSettings(window);
		setBackground(Color.black);
		setPreferredSize(window.getPreferredSize());
		setLayout(null);
		
		title.setText("Settings");
		title.setForeground(Color.white);
		title.setFont(new Font("Arial", Font.PLAIN, 60));
		title.setBounds(525, 10, 1000, 100);
		add(title);
		
		try {
		    UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
		    e.printStackTrace();
		}
		SwingUtilities.updateComponentTreeUI(this);
		
		categoryPanel.setBounds(20, 100, 1200, 520);
		categoryPanel.addTab("General", mainSettings);
		
		add(categoryPanel);
	}
	
}
