package jules.factoryreset.main;

import java.awt.*;

import jules.factoryreset.collectible.Battery;
import jules.factoryreset.entity.EntityHandler;

public class DrawHandler {
	
	GamePanel gp;
	BackgroundHandler bgHandler;
	EntityHandler entityHandler;
	Battery testBattery = new Battery(3, 5);
	
	protected DrawHandler(GamePanel gp) {
		this.gp = gp;
		bgHandler = new BackgroundHandler(gp);
        BackgroundHandler.loadMapFile("/mapfiles/map1.txt");
        entityHandler = new EntityHandler(gp);
	}

    public void draw(Graphics2D g) {
    	Graphics2D g2d = (Graphics2D) g;
    	//render settings
    	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        
        //drawing everything there is to draw, this draw method is getting called in the main draw method in GamePanel
        
        //drawing background first
        bgHandler.drawMap(g2d);
        
        entityHandler.drawAll(g2d);
        
        //drawing foreground
        GamePanel.getInstance().player.draw(g2d);
        
        testBattery.draw(g2d);
        
        //FPS counter
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString("FPS: " + gp.fps, 4, 20);
        
        g2d.setColor(Color.RED);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString("Press M to toggle debug", 200, 20);

        
        g2d.dispose();
    }
}
