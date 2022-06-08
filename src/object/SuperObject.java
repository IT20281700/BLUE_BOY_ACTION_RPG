package object;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import main.GamePanel;
import main.UtilityTool;

public class SuperObject {

	public BufferedImage image;
	public String name;
	public boolean collision = false;
	public double worldX, worldY;
	public Rectangle2D.Double solidArea = new Rectangle2D.Double(0, 0, 48, 48);
	public double solidAreaDefaultX = 0;
	public double solidAreaDefaultY = 0;
	UtilityTool uTool = new UtilityTool();

	public void draw(Graphics2D g2, GamePanel gp) {

		double screenX = worldX - gp.player.worldX + gp.player.screenX;
		double screenY = worldY - gp.player.worldY + gp.player.screenY;

		if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX
				&& worldX - gp.tileSize < gp.player.worldX + gp.player.screenX
				&& worldY + gp.tileSize > gp.player.worldY - gp.player.screenY
				&& worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

			g2.drawImage(image, (int) screenX, (int) screenY, gp.tileSize, gp.tileSize, null);

		}

	}

}
