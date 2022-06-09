package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity {

	KeyHandler keyH;
	public final int screenX;
	public final int screenY;
	int standCounter = 0;
	
	public Player(GamePanel gp, KeyHandler keyH) {
		super(gp);
		
		this.keyH = keyH;
		
		screenX = gp.screenWidth/2 - (gp.tileSize/2);
		screenY = gp.screenHeight/2 - (gp.tileSize/2);

		solidArea = new Rectangle2D.Double();
		solidArea.x = 12;
		solidArea.y = 20;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		solidArea.width = 28;
		solidArea.height = 28;
		
		setDefaultValues();
		getPlayerImage();
	}

	// Set player's default position
	public void setDefaultValues() {

		worldX = gp.tileSize * 23;
		worldY = gp.tileSize * 21;
		speed = 4;
		direction = "down";
		
		// PLAYER STATUS
		maxLife = 6;
		life = maxLife;

	}

	public void getPlayerImage() {
		
		up1 = setup("/player/boy_up_1");
		up2 = setup("/player/boy_up_2");
		down1 = setup("/player/boy_down_1");
		down2 = setup("/player/boy_down_2");
		left1 = setup("/player/boy_left_1");
		left2 = setup("/player/boy_left_2");
		right1 = setup("/player/boy_right_1");
		right2 = setup("/player/boy_right_2");

	}
	
	public void update() {

		if (keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true
				|| keyH.rightPressed == true) {

			// Player controller
			if (keyH.upPressed == true) {
				direction = "up";
			}
			if (keyH.downPressed == true) {
				direction = "down";
			}
			if (keyH.leftPressed == true) {
				direction = "left";
			}
			if (keyH.rightPressed == true) {
				direction = "right";
			}
			
			// CHECK TILE COLLISION
			collisionOn = false;
			gp.cChecker.checkTile(this);
			
			// CHECK OBJECT COLLISION
			int objIndex = gp.cChecker.checkObject(this, true);
			pickUpObject(objIndex);
			
			// CHECK NPC COLLISION
			int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
			interactNPC(npcIndex);
			
			// CHECK EVENT
			gp.eHandler.checkEvent();
			
			gp.keyH.enterPressed = false;
			
			// IF COLLISION IS FALSE, PLAYER CAN MOVE
			if(collisionOn == false) {
				
				switch (direction) {
				case "up":
					worldY -= speed;
					break;
				case "down":
					worldY += speed;
					break;
				case "left":
					worldX -= speed;
					break;
				case "right":
					worldX += speed;
					break;
				}
				
			}

			spriteCounter++;
			if (spriteCounter > 12) {
				if (spriteNum == 1) {
					spriteNum = 2;
				} else if (spriteNum == 2) {
					spriteNum = 1;
				}
				spriteCounter = 0;
			}

		} else {
			standCounter++;
			
			if(standCounter == 20) {
				spriteNum = 1;
				standCounter = 0;
			}
		}

	}
	
	public void pickUpObject(int i) {
		
		if(i != 999) {
			
			
			
		}
		
	}
	
	public void interactNPC(int i) {
		
		if(i != 999) {
			
			if(gp.keyH.enterPressed) {
				gp.gameState = gp.dialougeState;
				gp.npc[i].speak();
			}
			
		}
		
	}

	public void draw(Graphics2D g2) {

		BufferedImage image = null;

		switch (direction) {
		case "up":
			if (spriteNum == 1) {
				image = up1;
			}
			if (spriteNum == 2) {
				image = up2;
			}
			break;
		case "down":
			if (spriteNum == 1) {
				image = down1;
			}
			if (spriteNum == 2) {
				image = down2;
			}
			break;
		case "left":
			if (spriteNum == 1) {
				image = left1;
			}
			if (spriteNum == 2) {
				image = left2;
			}
			break;
		case "right":
			if (spriteNum == 1) {
				image = right1;
			}
			if (spriteNum == 2) {
				image = right2;
			}
			break;
		}
		
		int x = screenX;
		int y = screenY;
		
		if(screenX > worldX) {
			x = (int) worldX;
		}
		if(screenY > worldY) {
			y = (int) worldY;
		}
		int rightOffset  = gp.screenWidth - screenX;
		if(rightOffset > gp.worldWidth - worldX) {
			x = (int) (gp.screenWidth - (gp.worldWidth - worldX));
		}
		int bottomOffset = gp.screenHeight - screenY;
		if(bottomOffset > gp.worldHeight - worldY) {
			y = (int) (gp.screenHeight - (gp.worldHeight - worldY));
		}

		g2.drawImage(image, x, y, null);

	}

}
