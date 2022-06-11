package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Potion_Red extends Entity{
    
    GamePanel gp;

    public OBJ_Potion_Red(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_consumable;
        name = "Red Potion";
        value = 5;
        down1 = setup("/objects/potion_red", gp.tileSize, gp.tileSize);
        defenseValue = 2;
        description = "[" + name + "]\nHeals your life by " + value + ".";
        price = 25;

    }

    public void use(Entity entity) {

        gp.gameState = gp.dialougeState;
        gp.ui.currentDialouge = "You drink the " + name + "!\n"
                + "Your life has been recovered \nby " + value + ".";
        entity.life += value;
        if(gp.player.life > gp.player.maxLife) {
            gp.player.life = gp.player.maxLife;
        }
        gp.playSE(2);

    }
    
}
