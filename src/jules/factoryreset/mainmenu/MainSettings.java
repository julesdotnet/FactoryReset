package jules.factoryreset.mainmenu;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import jules.factoryreset.main.Window;

public class MainSettings extends JPanel {
	private static final long serialVersionUID = 1L;
	
	JLabel title = new JLabel("Settings");
	JTabbedPane categoryPanel = new JTabbedPane();
	
	MainSettings(Window window){
		setBackground(Color.red);
		setPreferredSize(window.getPreferredSize());
		setLayout(null);
		
		title.setText(" Main Settings");
		title.setForeground(Color.white);
		title.setFont(new Font("Arial", Font.PLAIN, 60));
		title.setBounds(405, 10, 1000, 100);
		add(title);
	}
	
}
