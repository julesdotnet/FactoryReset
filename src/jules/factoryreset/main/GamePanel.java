package jules.factoryreset.main;

import java.awt.*;

import java.awt.image.BufferedImage;
import javax.swing.*;
import jules.factoryreset.entity.Entity;
import jules.factoryreset.entity.EntityHandler;
import jules.factoryreset.entity.Firebot;
import jules.factoryreset.entity.Player;

public class GamePanel extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;
	private static GamePanel INSTANCE;

	private Thread gameThread;
	private final DrawHandler drawHandler;
	public MouseListener mouseListener;
	public Entity player = null;
	Window window;
	private boolean hasComputed = false;
	KeyInput ki;

	private EscapeMenu escapeMenu;
	public boolean isEscapeMenuVisible = false;
	private static boolean debugMenuActive;

	private long lastTime = System.nanoTime();
	private long lastFPSUpdateTime = System.nanoTime();
	private int frames = 0;
	int fps = 0;

	private GamePanel(Window window) {
		this.window = window;
		new KeyInput(this);
		drawHandler = new DrawHandler(this);
		setBackground(Color.blue);
		setPreferredSize(new Dimension(1250, 730));
		setDoubleBuffered(true);
		setFocusable(true);
		ki = new KeyInput(this);
		addKeyListener(ki);

		player = new Player(getWidth(), getHeight(), 50, 100, this);
		mouseListener = new MouseListener(this);
		escapeMenu = new EscapeMenu(this, window);
		setCrosshairCursor();
	}

	private void doOnce() {
		if (!hasComputed) {
			EntityHandler.spawn(new Firebot(3, 3, 100, 100, this));
			EntityHandler.spawn(new Firebot(4, 3, 100, 100, this));

			System.out.println("doonce");
			
			hasComputed = true;
		}
	}

	public static void initInstance(Window window) {
		if (INSTANCE == null) {
			INSTANCE = new GamePanel(window);
		}
	}

	public static GamePanel getInstance() {
		if (INSTANCE == null) {
			throw new IllegalStateException("GamePanel has not been initialized yet.");
		}
		return INSTANCE;
	}

	public Entity getPlayer() {
		return INSTANCE != null ? INSTANCE.player : null;
	}

	public void startGameThread() {
		if (gameThread == null) {
			gameThread = new Thread(this);
			gameThread.start();
		}
	}

	private void toggleEscapeMenu() {
		if (escapeMenu != null) {
			escapeMenu.setVisible(isEscapeMenuVisible);
			if (isEscapeMenuVisible) {
				add(escapeMenu);
				revalidate();
				repaint();
			} else {
				remove(escapeMenu);
				revalidate();
				repaint();
			}
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();
		drawHandler.draw(g2d);
	}

	private void update() {
		doOnce();
		if (isEscapeMenuVisible) {
			return;
		}
		if (player != null) {
			player.update();
		}
		EntityHandler.updateAll();
	}

	protected void setCrosshairCursor() {
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		BufferedImage loadedImage = SpriteLoader.loadSprite("guisprites/crosshair.png.png");

		Graphics cursorGraphics = cursorImg.createGraphics();
		cursorGraphics.drawImage(loadedImage, 0, 0, null);
		cursorGraphics.dispose();

		Cursor crosshairCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0),
				"Crosshair Cursor");
		setCursor(crosshairCursor);
	}

	@Override
	public void run() {
		int TARGET_FPS = 60;
		final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;

		while (true) {
			long now = System.nanoTime();
			lastTime = now;

			frames++;
			if (now - lastFPSUpdateTime >= 1000000000) {
				fps = frames;
				frames = 0;
				lastFPSUpdateTime += 1000000000;
			}
			if (!isEscapeMenuVisible) {
				update();
			}
			toggleEscapeMenu();
			repaint();
			try {
				long sleepTime = (lastTime - System.nanoTime() + OPTIMAL_TIME) / 1000000;
				if (sleepTime > 0) {
					Thread.sleep(sleepTime);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static boolean getDebugEnabled() {
		return debugMenuActive;
	}
	
	public static void toggleDebugEnabled() {
		debugMenuActive = !debugMenuActive;
	}
}
