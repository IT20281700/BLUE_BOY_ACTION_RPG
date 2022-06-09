package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_ManaCrystal extends Entity {

    public OBJ_ManaCrystal(GamePanel gp) {
        super(gp);
    
    name = "Mana Crystal";
    image = setup("/objects/manacrystal_full", gp.tileSize, gp.tileSize);
    image2 = setup("/objects/manacrystal_blank", gp.tileSize, gp.tileSize);
    
    down1 = setup("/objects/manacrystal_full", gp.tileSize, gp.tileSize);
    
    }
 
}
