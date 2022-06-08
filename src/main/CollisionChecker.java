package main;

import entity.Entity;

public class CollisionChecker {

	GamePanel gp;

	public CollisionChecker(GamePanel gp) {
		this.gp = gp;
	}

	public void checkTile(Entity entity) {

		double entityLeftWorldX = entity.worldX + entity.solidArea.x;
		double entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
		double entityTopWorldY = entity.worldY + entity.solidArea.y;
		double entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

		double entityLeftCol = entityLeftWorldX / gp.tileSize;
		double entityRightCol = entityRightWorldX / gp.tileSize;
		double entityTopRow = entityTopWorldY / gp.tileSize;
		double entityBottomRow = entityBottomWorldY / gp.tileSize;

		double tileNum1, tileNum2;

		switch (entity.direction) {
		case "up":
			entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
			tileNum1 = gp.tileManager.mapTileNum[(int) entityLeftCol][(int) entityTopRow];
			tileNum2 = gp.tileManager.mapTileNum[(int) entityRightCol][(int) entityTopRow];
			if (gp.tileManager.tile[(int) tileNum1].collision == true
					|| gp.tileManager.tile[(int) tileNum2].collision == true) {
				entity.collisionOn = true;
			}
			break;
		case "down":
			entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
			tileNum1 = gp.tileManager.mapTileNum[(int) entityLeftCol][(int) entityBottomRow];
			tileNum2 = gp.tileManager.mapTileNum[(int) entityRightCol][(int) entityBottomRow];
			if (gp.tileManager.tile[(int) tileNum1].collision == true
					|| gp.tileManager.tile[(int) tileNum2].collision == true) {
				entity.collisionOn = true;
			}
			break;
		case "left":
			entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
			tileNum1 = gp.tileManager.mapTileNum[(int) entityLeftCol][(int) entityTopRow];
			tileNum2 = gp.tileManager.mapTileNum[(int) entityLeftCol][(int) entityBottomRow];
			if (gp.tileManager.tile[(int) tileNum1].collision == true
					|| gp.tileManager.tile[(int) tileNum2].collision == true) {
				entity.collisionOn = true;
			}
			break;
		case "right":
			entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
			tileNum1 = gp.tileManager.mapTileNum[(int) entityRightCol][(int) entityTopRow];
			tileNum2 = gp.tileManager.mapTileNum[(int) entityRightCol][(int) entityBottomRow];
			if (gp.tileManager.tile[(int) tileNum1].collision == true
					|| gp.tileManager.tile[(int) tileNum2].collision == true) {
				entity.collisionOn = true;
			}
			break;
		}

	}

	public int checkObject(Entity entity, boolean player) {

		int index = 999;

		for (int i = 0; i < gp.obj.length; i++) {

			if (gp.obj[i] != null) {

				// Get entity's solid area position
				entity.solidArea.x = entity.worldX + entity.solidArea.x;
				entity.solidArea.y = entity.worldY + entity.solidArea.y;

				// Get the object's solid area position
				gp.obj[i].solidArea.x = gp.obj[i].worldX + gp.obj[i].solidArea.x;
				gp.obj[i].solidArea.y = gp.obj[i].worldY + gp.obj[i].solidArea.y;

				switch (entity.direction) {
				case "up":
					entity.solidArea.y -= entity.speed;
					if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
						if (gp.obj[i].collision == true) {
							entity.collisionOn = true;
						}
						if (player == true) {
							index = i;
						}
					}
					break;
				case "down":
					entity.solidArea.y += entity.speed;
					if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
						if (gp.obj[i].collision == true) {
							entity.collisionOn = true;
						}
						if (player == true) {
							index = i;
						}
					}
					break;
				case "left":
					entity.solidArea.x -= entity.speed;
					if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
						if (gp.obj[i].collision == true) {
							entity.collisionOn = true;
						}
						if (player == true) {
							index = i;
						}
					}
					break;
				case "right":
					entity.solidArea.x += entity.speed;
					if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
						if (gp.obj[i].collision == true) {
							entity.collisionOn = true;
						}
						if (player == true) {
							index = i;
						}
					}
					break;
				}
				entity.solidArea.x = entity.solidAreaDefaultX;
				entity.solidArea.y = entity.solidAreaDefaultY;
				gp.obj[i].solidArea.x = gp.obj[i].solidAreaDefaultX;
				gp.obj[i].solidArea.y = gp.obj[i].solidAreaDefaultY;

			}

		}

		return index;

	}

	// NPC OR MONSTER
	public int checkEntity(Entity entity, Entity[] target) {

		int index = 999;

		for (int i = 0; i < target.length; i++) {

			if (target[i] != null) {

				// Get entity's solid area position
				entity.solidArea.x = entity.worldX + entity.solidArea.x;
				entity.solidArea.y = entity.worldY + entity.solidArea.y;

				// Get the object's solid area position
				target[i].solidArea.x = target[i].worldX + target[i].solidArea.x;
				target[i].solidArea.y = target[i].worldY + target[i].solidArea.y;

				switch (entity.direction) {
				case "up":
					entity.solidArea.y -= entity.speed;
					if (entity.solidArea.intersects(target[i].solidArea)) {
						entity.collisionOn = true;
						index = i;
					}
					break;
				case "down":
					entity.solidArea.y += entity.speed;
					if (entity.solidArea.intersects(target[i].solidArea)) {
						entity.collisionOn = true;
						index = i;
					}
					break;
				case "left":
					entity.solidArea.x -= entity.speed;
					if (entity.solidArea.intersects(target[i].solidArea)) {
						entity.collisionOn = true;
						index = i;
					}
					break;
				case "right":
					entity.solidArea.x += entity.speed;
					if (entity.solidArea.intersects(target[i].solidArea)) {
						entity.collisionOn = true;
						index = i;
					}
					break;
				}
				entity.solidArea.x = entity.solidAreaDefaultX;
				entity.solidArea.y = entity.solidAreaDefaultY;
				target[i].solidArea.x = target[i].solidAreaDefaultX;
				target[i].solidArea.y = target[i].solidAreaDefaultY;

			}

		}

		return index;

	}
	
	public void checkPlayer(Entity entity) {
		
		// Get entity's solid area position
		entity.solidArea.x = entity.worldX + entity.solidArea.x;
		entity.solidArea.y = entity.worldY + entity.solidArea.y;

		// Get the object's solid area position
		gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
		gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;

		switch (entity.direction) {
		case "up":
			entity.solidArea.y -= entity.speed;
			if (entity.solidArea.intersects(gp.player.solidArea)) {
				entity.collisionOn = true;
			}
			break;
		case "down":
			entity.solidArea.y += entity.speed;
			if (entity.solidArea.intersects(gp.player.solidArea)) {
				entity.collisionOn = true;
			}
			break;
		case "left":
			entity.solidArea.x -= entity.speed;
			if (entity.solidArea.intersects(gp.player.solidArea)) {
				entity.collisionOn = true;
			}
			break;
		case "right":
			entity.solidArea.x += entity.speed;
			if (entity.solidArea.intersects(gp.player.solidArea)) {
				entity.collisionOn = true;
			}
			break;
		}
		entity.solidArea.x = entity.solidAreaDefaultX;
		entity.solidArea.y = entity.solidAreaDefaultY;
		gp.player.solidArea.x = gp.player.solidAreaDefaultX;
		gp.player.solidArea.y = gp.player.solidAreaDefaultY;
		
	}

}
