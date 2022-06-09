package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JPanel;

import entity.Entity;
import entity.Player;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {

    private static final long serialVersionUID = 1L;
    /**
     *
     */

    // SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3;

    public int tileSize = originalTileSize * scale; // 48x48 tile
    public int maxScreenCol = 16;
    public int maxScreenRow = 12;
    public int screenWidth = tileSize * maxScreenCol; // 768px
    public int screenHeight = tileSize * maxScreenRow; // 576px

    // WORLD MAP SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

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
    Thread gameThread;

    // ENTITY AND OBJECT
    public Player player = new Player(this, keyH);
    public Entity obj[] = new Entity[10];
    public Entity npc[] = new Entity[10];
    public Entity monster[] = new Entity[20];
    ArrayList<Entity> entityList = new ArrayList<>();

    // GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialougeState = 3;
    public final int characterState = 4;

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
//		playMusic(0);
        gameState = titleState;

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
                repaint();

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
            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null) {
                    npc[i].update();
                }
            }
            // MONSTER
            for (int i = 0; i < monster.length; i++) {
                if (monster[i] != null) {
                    if(monster[i].alive && monster[i].dying == false) {
                      monster[i].update();  
                    } 
                    if(monster[i].alive == false) {
                        monster[i] = null;
                    }
                }
            }

        }
        if (gameState == pauseState) {
            // nothing
        }

    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // DEBUG
        long drawStart = 0;
        if (keyH.checkDrawTime) {
            drawStart = System.nanoTime();
        }

        // TITILE SCREEN
        if (gameState == titleState) {

            ui.draw(g2);

        } // OTHERS
        else {

            // TILES
            tileManager.draw(g2);

            // ADD ENTITIES TO THE LIST
            // player
            entityList.add(player);

            //npcs
            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null) {
                    entityList.add(npc[i]);
                }
            }

            // objects
            for (int i = 0; i < obj.length; i++) {
                if (obj[i] != null) {
                    entityList.add(obj[i]);
                }
            }

            // monsters
            for (int i = 0; i < monster.length; i++) {
                if (monster[i] != null) {
                    entityList.add(monster[i]);
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
        if (keyH.checkDrawTime) {

            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;
            g2.setColor(Color.white);
            g2.drawString("Draw Time: " + passed, 10, 400);
            System.out.println("Draw Time: " + passed);

        }

        g2.dispose();

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
