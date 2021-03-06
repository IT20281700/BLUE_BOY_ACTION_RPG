package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.GamePanel;
import main.KeyHandler;
import object.OBJ_Axe;
import object.OBJ_Fireball;
import object.OBJ_Key;
import object.OBJ_ManaCrystal;
import object.OBJ_Potion_Red;
import object.OBJ_Shield_Wood;
import object.OBJ_Sword_Normal;

public class Player extends Entity {

	KeyHandler keyH;
	public final int screenX;
	public final int screenY;
	int standCounter = 0;
	public boolean attackCanceled = false;

	public Player(GamePanel gp, KeyHandler keyH) {
		super(gp);

		this.keyH = keyH;

		screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
		screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

		solidArea = new Rectangle2D.Double();
		solidArea.x = 12;
		solidArea.y = 20;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		solidArea.width = 28;
		solidArea.height = 28;

		setDefaultValues();
		getPlayerImage();
		getPlayerAttackImage();
		setItems();
	}

	// Set player's default values
	public void setDefaultValues() {

		// player's default position
		worldX = gp.tileSize * 23;
		worldY = gp.tileSize * 21;
		defaultSpeed = 4;
		speed = defaultSpeed;
		direction = "down";

		// PLAYER STATUS
		level = 1;
		maxLife = 6;
		life = maxLife;
		maxMana = 4;
		mana = maxMana;
		strength = 1; // The more strength he has, the more damage he gives.
		dexterity = 1; // The more dexterity he has, the less damage he receives.
		exp = 0;
		nextLevelExp = 5;
		coin = 500;
		currentWeapon = new OBJ_Sword_Normal(gp);
		currentShield = new OBJ_Shield_Wood(gp);
		projectile = new OBJ_Fireball(gp);
		attack = getAttack(); // The total attack value is decided by strength and weapon.
		defense = getDefense(); // The total defense value is decided by dexterity and shield.

	}

	public void setDefaultPosition() {

		// player's default position
		worldX = gp.tileSize * 23;
		worldY = gp.tileSize * 21;
		direction = "down";

	}

	public void restoreLifeAndMana() {

		life = maxLife;
		mana = maxMana;
		invincible = false;

	}

	public void setItems() {

		inventory.clear();
		inventory.add(currentWeapon);
		inventory.add(currentShield);

	}

	public int getAttack() {
		attackArea = currentWeapon.attackArea;
		return attack = strength * currentWeapon.attackValue;
	}

	public int getDefense() {
		return defense = dexterity * currentShield.defenseValue;
	}

	public void getPlayerImage() {

		up1 = setup("/player/boy_up_1", gp.tileSize, gp.tileSize);
		up2 = setup("/player/boy_up_2", gp.tileSize, gp.tileSize);
		down1 = setup("/player/boy_down_1", gp.tileSize, gp.tileSize);
		down2 = setup("/player/boy_down_2", gp.tileSize, gp.tileSize);
		left1 = setup("/player/boy_left_1", gp.tileSize, gp.tileSize);
		left2 = setup("/player/boy_left_2", gp.tileSize, gp.tileSize);
		right1 = setup("/player/boy_right_1", gp.tileSize, gp.tileSize);
		right2 = setup("/player/boy_right_2", gp.tileSize, gp.tileSize);

	}

	public void getPlayerAttackImage() {

		if (currentWeapon.type == type_sword) {
			attackUp1 = setup("/player/boy_attack_up_1", gp.tileSize, gp.tileSize * 2);
			attackUp2 = setup("/player/boy_attack_up_2", gp.tileSize, gp.tileSize * 2);
			attackDown1 = setup("/player/boy_attack_down_1", gp.tileSize, gp.tileSize * 2);
			attackDown2 = setup("/player/boy_attack_down_2", gp.tileSize, gp.tileSize * 2);
			attackLeft1 = setup("/player/boy_attack_left_1", gp.tileSize * 2, gp.tileSize);
			attackLeft2 = setup("/player/boy_attack_left_2", gp.tileSize * 2, gp.tileSize);
			attackRight1 = setup("/player/boy_attack_right_1", gp.tileSize * 2, gp.tileSize);
			attackRight2 = setup("/player/boy_attack_right_2", gp.tileSize * 2, gp.tileSize);
		}
		if (currentWeapon.type == type_axe) {
			attackUp1 = setup("/player/boy_axe_up_1", gp.tileSize, gp.tileSize * 2);
			attackUp2 = setup("/player/boy_axe_up_2", gp.tileSize, gp.tileSize * 2);
			attackDown1 = setup("/player/boy_axe_down_1", gp.tileSize, gp.tileSize * 2);
			attackDown2 = setup("/player/boy_axe_down_2", gp.tileSize, gp.tileSize * 2);
			attackLeft1 = setup("/player/boy_axe_left_1", gp.tileSize * 2, gp.tileSize);
			attackLeft2 = setup("/player/boy_axe_left_2", gp.tileSize * 2, gp.tileSize);
			attackRight1 = setup("/player/boy_axe_right_1", gp.tileSize * 2, gp.tileSize);
			attackRight2 = setup("/player/boy_axe_right_2", gp.tileSize * 2, gp.tileSize);
		}

	}

	public void update() {

		if (attacking) {

			attacking();

		} else if (keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true
				|| keyH.rightPressed == true || keyH.enterPressed == true) {

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

			// CHECK MONSTER COLLISION
			int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
			contactMonster(monsterIndex);

			// CHECK INTERACTIVE TILE COLLISION
//			int iTileIndex = gp.cChecker.checkEntity(this, gp.iTile);

			// CHECK EVENT
			gp.eHandler.checkEvent();

			// IF COLLISION IS FALSE, PLAYER CAN MOVE
			if (collisionOn == false && keyH.enterPressed == false) {

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

			if (keyH.enterPressed && attackCanceled == false) {
				gp.playSE(7);
				attacking = true;
				spriteCounter = 0;
			}

			attackCanceled = false;
			gp.keyH.enterPressed = false;

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

			if (standCounter == 20) {
				spriteNum = 1;
				standCounter = 0;
			}
		}

		if (gp.keyH.shotKeyPressed && projectile.alive == false && shotAvailableCounter == 30
				&& projectile.haveResource(this) == true) {

			// SET DEFAULT COORDINATES DIRECTION AND USER
			projectile.set((int) worldX, (int) worldY, direction, true, this);

			// SUBSTRACT THE COST (MANA, AMMO ETC.)
			projectile.substractResource(this);

			// ADD IT TO LIST
			// CHECK VACANCY
			for (int i = 0; i < gp.projectile[1].length; i++) {
				if (gp.projectile[gp.currentMap][i] == null) {
					gp.projectile[gp.currentMap][i] = projectile;
					break;
				}
			}

			shotAvailableCounter = 0;

			gp.playSE(10);

		}

		// This needs to be outside of key if statement!
		if (invincible) {
			invincibleCounter++;
			if (invincibleCounter > 60) {
				invincible = false;
				invincibleCounter = 0;
			}
		}
		if (shotAvailableCounter < 30) {
			shotAvailableCounter++;
		}

		if (life > maxLife) {
			life = maxLife;
		}

		if (mana > maxMana) {
			mana = maxMana;
		}

		// check player's life
		if (life <= 0) {
			gp.stopMusic();
			gp.gameState = gp.gameOverState;
			gp.ui.commandNum = -1;
			gp.playSE(12);
		}

	}

	public void attacking() {

		spriteCounter++;

		if (spriteCounter <= 5) {
			spriteNum = 1;
		}
		if (spriteCounter > 5 && spriteCounter <= 25) {
			spriteNum = 2;

			// Save the current worldX, worldY, solidArea
			int currentWorldX = (int) worldX;
			int currentWorldY = (int) worldY;
			int solidAreaWidth = (int) solidArea.width;
			int solidAreaHeight = (int) solidArea.height;

			// Adjest player's worldX/Y for the attackArea
			switch (direction) {
			case "up":
				worldY -= attackArea.height;
				break;
			case "down":
				worldY += attackArea.height;
				break;
			case "left":
				worldX -= attackArea.width;
				break;
			case "right":
				worldX += attackArea.width;
				break;
			}

			// Attack area becomes Solid area
			solidArea.width = attackArea.width;
			solidArea.height = attackArea.height;
			// Check monster collision with the updated worldX, worldY and solidArea
			int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
			damageMonster(monsterIndex, attack, currentWeapon.knockBackPower);

			int iTileIndex = gp.cChecker.checkEntity(this, gp.iTile);
			damageInteractiveTile(iTileIndex);

			int projectileIndex = gp.cChecker.checkEntity(this, gp.projectile);
			damageProjectile(projectileIndex);

			// After checking collisions, restore the original data
			worldX = currentWorldX;
			worldY = currentWorldY;
			solidArea.width = solidAreaWidth;
			solidArea.height = solidAreaHeight;

		}
		if (spriteCounter > 25) {
			spriteNum = 1;
			spriteCounter = 0;
			attacking = false;
		}

	}

	public void pickUpObject(int i) {

		if (i != 999) {

			// PICKUP ONLY ITEMS
			if (gp.obj[gp.currentMap][i].type == type_pickupOnly) { // FIXED

				gp.obj[gp.currentMap][i].use(this); // FIXED
				gp.obj[gp.currentMap][i] = null; // FIXED

			}
			// OBSTACLE
			else if (gp.obj[gp.currentMap][i].type == type_obstacle) {
				if (keyH.enterPressed == true) {
					attackCanceled = true;
					gp.obj[gp.currentMap][i].interact();
				}
			}
			// INVENTORY ITEMS
			else {
				String text = "";

				if (canobtainItem(gp.obj[gp.currentMap][i]) == true) {
					gp.playSE(1);
					text = "Got a " + gp.obj[gp.currentMap][i].name + "!"; // FIXED

				} else {
					text = "You cannot carry any more!";
				}

				gp.ui.addMessage(text);
				gp.obj[gp.currentMap][i] = null; // FIXED
			}

		}

	}

	public void interactNPC(int i) {

		if (gp.keyH.enterPressed) {

			if (i != 999) {
				attackCanceled = true;
				gp.gameState = gp.dialougeState;
				gp.npc[gp.currentMap][i].speak(); // FIXED
			}

		}

	}

	public void contactMonster(int i) {

		if (i != 999) {

			if (invincible == false && gp.monster[gp.currentMap][i].dying == false) { // FIXED
				gp.playSE(6);

				int damage = gp.monster[gp.currentMap][i].attack - defense; // FIXED
				if (damage < 0) {
					damage = 0;
				}

				life -= damage;
				invincible = true;
			}

		}

	}

	public void damageMonster(int i, int attack, int knockBackPower) {

		if (i != 999) {

			if (gp.monster[gp.currentMap][i].invincible == false) { // FIXED

				gp.playSE(5);

				if (knockBackPower > 0) {
					knockBack(gp.monster[gp.currentMap][i], knockBackPower);
				}

				int damage = attack - gp.monster[gp.currentMap][i].defense; // FIXED
				if (damage < 0) {
					damage = 0;
				}

				gp.monster[gp.currentMap][i].life -= damage; // FIXED
				gp.ui.addMessage(damage + " damage!");

				gp.monster[gp.currentMap][i].invincible = true; // FIXED
				gp.monster[gp.currentMap][i].damageReaction(); // FIXED

				if (gp.monster[gp.currentMap][i].life <= 0) { // FIXED
					gp.monster[gp.currentMap][i].dying = true; // FIXED
					gp.ui.addMessage("Killed the " + gp.monster[gp.currentMap][i].name + "!"); // FIXED
					gp.ui.addMessage("Exp +" + gp.monster[gp.currentMap][i].exp); // FIXED
					exp += gp.monster[gp.currentMap][i].exp; // FIXED
					checkLevelUp();
				}

			}

		}

	}

	public void knockBack(Entity entity, int knockBackPower) {

		entity.direction = direction;
		entity.speed += knockBackPower;
		entity.knockBack = true;

	}

	public void damageInteractiveTile(int i) {

		if (i != 999 && gp.iTile[gp.currentMap][i].destructible == true
				&& gp.iTile[gp.currentMap][i].isCorrectItem(this) == true
				&& gp.iTile[gp.currentMap][i].invincible == false) { // FIXED
			gp.iTile[gp.currentMap][i].playSE();
			gp.iTile[gp.currentMap][i].life--; // FIXED
			gp.iTile[gp.currentMap][i].invincible = true; // FIXED

			// generate particles
			generateParticle(gp.iTile[gp.currentMap][i], gp.iTile[gp.currentMap][i]); // FIXED

			if (gp.iTile[gp.currentMap][i].life == 0) { // FIXED
				gp.iTile[gp.currentMap][i] = gp.iTile[gp.currentMap][i].getDestroyedForm(); // FIXED
			}

		}

	}

	public void damageProjectile(int i) {

		if (i != 999) {
			Entity projectile = gp.projectile[gp.currentMap][i];
			projectile.alive = false;
			generateParticle(projectile, projectile);
		}

	}

	public void checkLevelUp() {

		if (exp >= nextLevelExp) {

			level++;
			nextLevelExp = nextLevelExp * 2;
			maxLife += 2;
			life += 2;
			strength++;
			dexterity++;
			attack = getAttack();
			defense = getDefense();

			gp.playSE(8);
			gp.gameState = gp.dialougeState;
			gp.ui.currentDialouge = "You are level " + level + " now!\n" + "You feel stronger!";

		}

	}

	public void selectItem() {

		int itemIndex = gp.ui.getItemIndexOnSlot(gp.ui.playerSlotCol, gp.ui.playerSlotRow);

		if (itemIndex < inventory.size()) {

			Entity selectedItem = inventory.get(itemIndex);

			if (selectedItem.type == type_sword || selectedItem.type == type_axe) {

				currentWeapon = selectedItem;
				attack = getAttack();
				getPlayerAttackImage();

			}
			if (selectedItem.type == type_shield) {

				currentShield = selectedItem;
				defense = getDefense();

			}
			if (selectedItem.type == type_consumable) {

				if (selectedItem.use(this) == true) {
					if (selectedItem.amount > 1) {
						selectedItem.amount--;
					} else {
						inventory.remove(itemIndex);
					}
				}

			}

		}

	}

	public int searchItemInInventory(String itemName) {
		int itemIndex = 999;

		for (int i = 0; i < inventory.size(); i++) {
			if (inventory.get(i).name.equals(itemName)) {
				itemIndex = i;
				break;
			}
		}

		return itemIndex;
	}

	public boolean canobtainItem(Entity item) {

		boolean canObtain = false;

		// CHECK IF STACKABLE
		if (item.stackable == true) {

			int index = searchItemInInventory(item.name);

			if (index != 999) {
				inventory.get(index).amount++;
				canObtain = true;
			} else { // THIS IS NEW ITEM NEED TO CHECK VACANCY
				if (inventory.size() != maxInventorySize) {
					inventory.add(item);
					canObtain = true;
				}
			}
		} else { // NOT STACKABLE
			if (inventory.size() != maxInventorySize) {
				inventory.add(item);
				canObtain = true;
			}
		}
		return canObtain;
	}

	public void draw(Graphics2D g2) {

		BufferedImage image = null;
		int tempScreenX = screenX;
		int tempScreenY = screenY;

		switch (direction) {
		case "up":
			if (attacking == false) {
				if (spriteNum == 1) {
					image = up1;
				}
				if (spriteNum == 2) {
					image = up2;
				}
			}

			if (attacking) {
				tempScreenY = screenY - gp.tileSize;
				if (spriteNum == 1) {
					image = attackUp1;
				}
				if (spriteNum == 2) {
					image = attackUp2;
				}
			}
			break;
		case "down":
			if (attacking == false) {
				if (spriteNum == 1) {
					image = down1;
				}
				if (spriteNum == 2) {
					image = down2;
				}
			}
			if (attacking) {
				if (spriteNum == 1) {
					image = attackDown1;
				}
				if (spriteNum == 2) {
					image = attackDown2;
				}
			}
			break;
		case "left":
			if (attacking == false) {

				if (spriteNum == 1) {
					image = left1;
				}
				if (spriteNum == 2) {
					image = left2;
				}

			}
			if (attacking) {
				tempScreenX = screenX - gp.tileSize;
				if (spriteNum == 1) {
					image = attackLeft1;
				}
				if (spriteNum == 2) {
					image = attackLeft2;
				}
			}
			break;
		case "right":
			if (attacking == false) {

				if (spriteNum == 1) {
					image = right1;
				}
				if (spriteNum == 2) {
					image = right2;
				}

			}
			if (attacking) {
				if (spriteNum == 1) {
					image = attackRight1;
				}
				if (spriteNum == 2) {
					image = attackRight2;
				}
			}

			break;
		}

		int x = tempScreenX;
		int y = tempScreenY;

		if (tempScreenX > worldX) {
			x = (int) worldX;
		}
		if (tempScreenY > worldY) {
			y = (int) worldY;
		}
		int rightOffset = gp.screenWidth - tempScreenX;
		if (rightOffset > gp.worldWidth - worldX) {
			x = (int) (gp.screenWidth - (gp.worldWidth - worldX));
		}
		int bottomOffset = gp.screenHeight - tempScreenY;
		if (bottomOffset > gp.worldHeight - worldY) {
			y = (int) (gp.screenHeight - (gp.worldHeight - worldY));
		}

		if (invincible == true) {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
		}

		g2.drawImage(image, x, y, null);

		// RESET ALPHA
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

		// DEBUG
//        g2.setFont(new Font("Arial", Font.PLAIN, 26));
//        g2.setColor(Color.white);
//        g2.drawString("Invincible: "+invincibleCounter, 10, 400);
	}

}
