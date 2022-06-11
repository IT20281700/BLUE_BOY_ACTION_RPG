package tile_interactive;

import java.awt.Color;
import java.awt.Graphics2D;

import entity.Entity;
import main.GamePanel;

public class IT_DryTree extends InteractiveTile {

	GamePanel gp;

	public IT_DryTree(GamePanel gp, int col, int row) {
		super(gp, col, row);
		this.gp = gp;

		this.worldX = gp.tileSize * col;
		this.worldY = gp.tileSize * row;

		down1 = setup("/tiles_interactive/drytree", gp.tileSize, gp.tileSize);
		destructible = true;
		life = 3;
	}

	public boolean isCorrectItem(Entity entity) {
		boolean isCorrectItem = false;

		if (entity.currentWeapon.type == type_axe) {
			isCorrectItem = true;
		}

		return isCorrectItem;
	}

	public void playSE() {
		gp.playSE(11);
	}

	public InteractiveTile getDestroyedForm() {

		InteractiveTile tile = new IT_Trunk(gp, (int) worldX / gp.tileSize, (int) worldY / gp.tileSize);
		return tile;

	}

	public Color getParticleColor() {
		Color color = new Color(65, 50, 30);
		return color;
	}

	public int getParticleSize() {
		int size = 6;
		return size;
	}

	public int getParticleSpeed() {
		int speed = 1;
		return speed;
	}

	public int getParticleMaxLife() {
		int maxLife = 20;
		return maxLife;
	}

	public void draw(Graphics2D g2) {

		double screenX = worldX - gp.player.worldX + gp.player.screenX;
		double screenY = worldY - gp.player.worldY + gp.player.screenY;
		
		int x = (int) screenX;
		int y = (int) screenY;

		if (screenX > worldX) {
			x = (int) worldX;
		}
		if (screenY > worldY) {
			y = (int) worldY;
		}
		int rightOffset = (int) (gp.screenWidth - screenX);
		if (rightOffset > gp.worldWidth - worldX) {
			x = (int) (gp.screenWidth - (gp.worldWidth - worldX));
		}
		int bottomOffset = (int) (gp.screenHeight - screenY);
		if (bottomOffset > gp.worldHeight - worldY) {
			y = (int) (gp.screenHeight - (gp.worldHeight - worldY));
		}

		if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX
				&& worldX - gp.tileSize < gp.player.worldX + gp.player.screenX
				&& worldY + gp.tileSize > gp.player.worldY - gp.player.screenY
				&& worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

			g2.drawImage(down1, (int) x, (int) y, null);

		}
	}
}
