package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JPanel;

import ai.PathFinder;
import entity.Entity;
import entity.Player;
import java.awt.Font;
import tile.TileManager;
import tile_interactive.InteractiveTile;

public class GamePanel extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;
	/**
	 *
	 */

	// SCREEN SETTINGS
	final int originalTileSize = 16; // 16x16 tile
	final int scale = 3;

	public int tileSize = originalTileSize * scale; // 48x48 tile
	public int maxScreenCol = 20;
	public int maxScreenRow = 12;
	public int screenWidth = tileSize * maxScreenCol; // 960px
	public int screenHeight = tileSize * maxScreenRow; // 576px

	// WORLD MAP SETTINGS
	public final int maxWorldCol = 50;
	public final int maxWorldRow = 50;
	public final int worldWidth = tileSize * maxWorldCol;
	public final int worldHeight = tileSize * maxWorldRow;
	public final int maxMap = 10;
	public int currentMap = 0;

	// FOR FULL SCREEN
	int screenWidth2 = screenWidth;
	int screenHeight2 = screenHeight;
	BufferedImage tempScreen;
	Graphics2D g2;
	public boolean fullScreenOn = false;

	// FPS
	int FPS = 60;

	// SYSTEM
	public TileManager tileManager = new TileManager(this);
	public KeyHandler keyH = new KeyHandler(this);
	Sound music = new Sound();
	Sound se = new Sound();
	public CollisionChecker cChecker = new CollisionChecker(this);
	public AssetSetter aSetter = new AssetSetter(this);
	public UI ui = new UI(this);
	public EventHandler eHandler = new EventHandler(this);
	Config config = new Config(this);
	public PathFinder pFinder = new PathFinder(this);
	Thread gameThread;

	// ENTITY AND OBJECT
	public Player player = new Player(this, keyH);
	public Entity obj[][] = new Entity[maxMap][20];
	public Entity npc[][] = new Entity[maxMap][10];
	public Entity monster[][] = new Entity[maxMap][20];
	public InteractiveTile iTile[][] = new InteractiveTile[maxMap][50];
	public Entity projectile[][] = new Entity[maxMap][20];
//	public ArrayList<Entity> projectileList = new ArrayList<>();
	public ArrayList<Entity> particleList = new ArrayList<>();
	ArrayList<Entity> entityList = new ArrayList<>();

	// GAME STATE
	public int gameState;
	public final int titleState = 0;
	public final int playState = 1;
	public final int pauseState = 2;
	public final int dialougeState = 3;
	public final int characterState = 4;
	public final int optionState = 5;
	public final int gameOverState = 6;
	public final int transitionState = 7;
	public final int tradeState = 8;

	public GamePanel() {

		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);

	}

	public void setupGame() {

		aSetter.setObject();
		aSetter.setNPC();
		aSetter.setMonster();
		aSetter.setInteractiveTile();
//		playMusic(0);
		gameState = titleState;

		tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
		g2 = (Graphics2D)tempScreen.getGraphics();
		
		if(fullScreenOn == true) {
			setFullScreen();
		}

	}
	
	public void retry() {
		player.setDefaultPosition();
		player.restoreLifeAndMana();
		aSetter.setNPC();
		aSetter.setMonster();
		playMusic(0);
	}
	
	public void restart() {
		player.setDefaultValues();
		player.setDefaultPosition();
		player.restoreLifeAndMana();
		player.setItems();
		aSetter.setObject();
		aSetter.setNPC();
		aSetter.setMonster();
		aSetter.setInteractiveTile();
	}

//	Zoom function
//	public void zoomInOut(int i) {
//		
//		int oldWorldWidth = tileSize * maxWorldCol; // 2400
//		tileSize += i;
//		int newWorldWidth = tileSize * maxWorldCol; // 2350
//		
//		player.speed = (double)newWorldWidth/600;
//		
//		double multiplier = (double)newWorldWidth/oldWorldWidth; // 0.97
//		
//		double newPlayerWorldX = player.worldX * multiplier;
//		double newPlayerWorldY = player.worldY * multiplier;
//		
//		// zoom of objects
//		for(int j = 0; j < obj.length; j++) {
//			
//			if(obj[j] != null) {
//				
//				double newObjectWorldX = obj[j].worldX * multiplier;
//				double newObjectWorldY = obj[j].worldY * multiplier;
//				
//				obj[j].worldX = newObjectWorldX;
//				obj[j].worldY = newObjectWorldY;
//				
//			}
//			
//		}
//		
//		// zoom of player
//		player.worldX = newPlayerWorldX;
//		player.worldY = newPlayerWorldY;
//		
//		// zoom of player solid area
//		double newPlayerSolidX = player.solidArea.x * multiplier;
//		double newPlayerSolidY = player.solidArea.y * multiplier;
//		double newPlayerSolidWidth = player.solidArea.width * multiplier;
//		double newPlayerSolidHeight = player.solidArea.height * multiplier;
//		
//		player.solidArea.x = newPlayerSolidX;
//		player.solidArea.y = newPlayerSolidY;
//		player.solidArea.width = newPlayerSolidWidth;
//		player.solidArea.height = newPlayerSolidHeight;
//		
//	}
	
	public void setFullScreen() {
		
		// GET LOCAL SCREEN DEVICE
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		gd.setFullScreenWindow(Main.window);
		
		// GET SCREEN WIDTH AND HEIGT
		screenWidth2 = Main.window.getWidth();
		screenHeight2 = Main.window.getHeight();
		
	}
	
	public void startGameThread() {

		gameThread = new Thread(this);
		gameThread.start();

	}

	@Override
	public void run() {

		double drawInterval = 1000000000 / FPS; // 0.01666 seconds
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		int drawCount = 0;

		while (gameThread != null) {

			currentTime = System.nanoTime();

			delta += (currentTime - lastTime) / drawInterval;
			timer += (currentTime - lastTime);
			lastTime = currentTime;

			if (delta >= 1) {

				// 1 UPDATE: update the information such as character positions
				update();
				// 2 DRAW: draw the screen with the updated information
				drawToTempScreen(); // draw everything to buffered image
				drawToScreen(); // draw image to screen

				delta--;
				drawCount++;

			}

			if (timer >= 1000000000) {
				System.out.println("FPS: " + drawCount);
				drawCount = 0;
				timer = 0;
			}

		}

	}

	public void update() {

		if (gameState == playState) {
			// PLAYER
			player.update();
			// NPC
			for (int i = 0; i < npc[1].length; i++) {
				if (npc[currentMap][i] != null) {
					npc[currentMap][i].update();
				}
			}
			// MONSTER
			for (int i = 0; i < monster[1].length; i++) {
				if (monster[currentMap][i] != null) {
					if (monster[currentMap][i].alive && monster[currentMap][i].dying == false) {
						monster[currentMap][i].update();
					}
					if (monster[currentMap][i].alive == false) {
						monster[currentMap][i].checkDrop();
						monster[currentMap][i] = null;
					}
				}
			}
			// PROJECTTILE
			for (int i = 0; i < projectile[1].length; i++) {
				if (projectile[currentMap][i] != null) {
					if (projectile[currentMap][i].alive) {
						projectile[currentMap][i].update();
					}
					if (projectile[currentMap][i].alive == false) {
						projectile[currentMap][i] = null;
					}
				}
			}

			// PARTICLES
			for (int i = 0; i < particleList.size(); i++) {
				if (particleList.get(i) != null) {
					if (particleList.get(i).alive) {
						particleList.get(i).update();
					}
					if (particleList.get(i).alive == false) {
						particleList.remove(i);
					}
				}
			}

			// INTERACTIVE TILES
			for (int i = 0; i < iTile[1].length; i++) {
				if (iTile[currentMap][i] != null) {
					iTile[currentMap][i].update();
				}
			}

		}
		if (gameState == pauseState) {
			// nothing
		}

	}

	public void drawToTempScreen() {
		// DEBUG
		long drawStart = 0;
		if (keyH.showDebugText) {
			drawStart = System.nanoTime();
		}

		// TITILE SCREEN
		if (gameState == titleState) {

			ui.draw(g2);

		} // OTHERS
		else {

			// TILES
			tileManager.draw(g2);

			// INTERACTIVE TILES
			for (int i = 0; i < iTile[1].length; i++) {
				if (iTile[currentMap][i] != null) {
					iTile[currentMap][i].draw(g2);
				}
			}

			// ADD ENTITIES TO THE LIST
			// player
			entityList.add(player);

			// npcs
			for (int i = 0; i < npc[1].length; i++) {
				if (npc[currentMap][i] != null) {
					entityList.add(npc[currentMap][i]);
				}
			}

			// objects
			for (int i = 0; i < obj[1].length; i++) {
				if (obj[currentMap][i] != null) {
					entityList.add(obj[currentMap][i]);
				}
			}

			// monsters
			for (int i = 0; i < monster[1].length; i++) {
				if (monster[currentMap][i] != null) {
					entityList.add(monster[currentMap][i]);
				}
			}

			// projectitle
			for (int i = 0; i < projectile[1].length; i++) {
				if (projectile[currentMap][i] != null) {
					entityList.add(projectile[currentMap][i]);
				}
			}
			
			// particles
            for (int i = 0; i < particleList.size(); i++) {
                if (particleList.get(i) != null) {
                    entityList.add(particleList.get(i));
                }
            }

			// SORT
			Collections.sort(entityList, new Comparator<Entity>() {

				@Override
				public int compare(Entity e1, Entity e2) {

					int result = Integer.compare((int) e1.worldY, (int) e2.worldY);
					return result;
				}

			});

			// DRAW ENTITIES
			for (int i = 0; i < entityList.size(); i++) {
				entityList.get(i).draw(g2);
			}
			// EMPTY LIST
			entityList.clear();

			// UI
			ui.draw(g2);

		}

		// DEBUG
		if (keyH.showDebugText) {

			long drawEnd = System.nanoTime();
			long passed = drawEnd - drawStart;

			g2.setFont(new Font("Arial", Font.PLAIN, 20));
			g2.setColor(Color.white);
			int x = 10;
			int y = 400;
			int lineHeight = 20;

			g2.drawString("WorldX " + (int) player.worldX, x, y);
			y += lineHeight;
			g2.drawString("WorldY " + (int) player.worldY, x, y);
			y += lineHeight;
			g2.drawString("Col " + (int) (player.worldX + player.solidArea.x) / tileSize, x, y);
			y += lineHeight;
			g2.drawString("Row " + (int) (player.worldY + player.solidArea.y) / tileSize, x, y);
			y += lineHeight;

			g2.drawString("Draw Time: " + passed, x, y);
			y += lineHeight;
			System.out.println("Draw Time: " + passed);

		}
	}

	public void drawToScreen() {
		
		Graphics g = getGraphics();
		g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
		g.dispose();
		
		
	}

	// MUSIC PLAYER
	public void playMusic(int i) {

		music.setFile(i);
		music.play();
		music.loop();

	}

	public void stopMusic() {

		music.stop();

	}

	public void playSE(int i) {

		se.setFile(i);
		se.play();

	}

}
