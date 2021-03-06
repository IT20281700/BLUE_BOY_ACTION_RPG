package main;

import entity.Entity;

public class EventHandler {

	GamePanel gp;
	EventRect eventRect[][][];

	int previousEventX, previousEventY;
	boolean canTouchEvent = true;
	int tempMap, tempCol, tempRow;

	public EventHandler(GamePanel gp) {
		this.gp = gp;

		eventRect = new EventRect[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];

		int map = 0;
		int col = 0;
		int row = 0;
		while (map < gp.maxMap && col < gp.maxWorldCol && row < gp.maxWorldRow) {

			eventRect[map][col][row] = new EventRect();
			eventRect[map][col][row].x = 23;
			eventRect[map][col][row].y = 23;
			eventRect[map][col][row].width = 2;
			eventRect[map][col][row].height = 2;
			eventRect[map][col][row].eventRectDefaultX = eventRect[map][col][row].x;
			eventRect[map][col][row].eventRectDefaultY = eventRect[map][col][row].y;

			col++;

			if (col == gp.maxWorldCol) {
				col = 0;
				row++;

				if (row == gp.maxWorldRow) {
					row = 0;
					map++;
				}

			}

		}

	}

	public void checkEvent() {

		// Check if the player character is more than 1 tile away from the last event
		int xDistance = (int) Math.abs(gp.player.worldX - previousEventX);
		int yDistance = (int) Math.abs(gp.player.worldY - previousEventY);
		int distance = Math.max(xDistance, yDistance);
		if (distance > gp.tileSize) {

			canTouchEvent = true;

		}

		if (canTouchEvent) {
			
			// DAMAGE PITS
			if (hit(0, 27, 16, "right") == true) {
				damagePit(gp.dialougeState);
			}
			// HEALING POOLS
			else if (hit(0, 23, 12, "up") == true) {
				healingPool(gp.dialougeState);
			}
			// TELEPORT TO MERCHANT's HUT
			// teleport go
			else if(hit(0, 10, 39, "any") == true) {
				teleport(1, 12, 13);
			}
			// teleport return
			else if(hit(1, 12, 13, "any") == true) {
				teleport(0, 10, 39);
			}
			// SPEAK WITH MERCHANT
			else if (hit(1, 12, 9, "up")) {
				speak(gp.npc[1][0]);
			}

		}

	}

	public boolean hit(int map, int col, int row, String reqDirection) {

		boolean hit = false;

		if (map == gp.currentMap) {

			gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
			gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
			eventRect[map][col][row].x = col * gp.tileSize + eventRect[map][col][row].x;
			eventRect[map][col][row].y = row * gp.tileSize + eventRect[map][col][row].y;

			if (gp.player.solidArea.intersects(eventRect[map][col][row])
					&& eventRect[map][col][row].eventDone == false) {
				if (gp.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")) {
					hit = true;

					previousEventX = (int) gp.player.worldX;
					previousEventY = (int) gp.player.worldY;
				}
			}

			gp.player.solidArea.x = gp.player.solidAreaDefaultX;
			gp.player.solidArea.y = gp.player.solidAreaDefaultY;
			eventRect[map][col][row].x = eventRect[map][col][row].eventRectDefaultX;
			eventRect[map][col][row].y = eventRect[map][col][row].eventRectDefaultY;

		}

		return hit;

	}

	public void damagePit(int gameState) {

		gp.gameState = gameState;
		gp.playSE(6);
		gp.ui.currentDialouge = "You fall into a pit!";
		gp.player.life -= 1;
//		eventRect[col][row].eventDone = true;
		canTouchEvent = false;

	}

	public void healingPool(int gameState) {

		if (gp.keyH.enterPressed == true) {
			gp.gameState = gameState;
			gp.player.attackCanceled = true;
			gp.playSE(2);
			gp.ui.currentDialouge = "You drink the water. \nYour life has been recovered.";
			gp.player.life = gp.player.maxLife;
			// eventRect[col][row].eventDone = true;
			canTouchEvent = false;
			gp.aSetter.setMonster();
			
			// upgrade monster defence
			if(gp.monster[1] != null) {
				for(int i = 0; i < gp.monster[gp.currentMap].length; i++) {
					if(gp.monster[gp.currentMap][i] != null) {
						gp.monster[gp.currentMap][i].defense += 2;
					}
				}
				System.out.println("Mon Defense: " +gp.monster[gp.currentMap][0].defense);
			}
		}

	}

	public void teleport(int map, int col, int row) {
		
		gp.gameState = gp.transitionState;
		tempMap = map;
		tempCol = col;
		tempRow = row;
		canTouchEvent = false;
		gp.playSE(13);
		
	}
	
	public void speak(Entity entity) {
		
		if(gp.keyH.enterPressed == true) {
			gp.gameState = gp.dialougeState;
			gp.player.attackCanceled = true;
			entity.speak();
		}
		
	}

}
