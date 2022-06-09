package entity;

import java.util.Random;

import main.GamePanel;

public class NPC_OldMan extends Entity{
	
	public NPC_OldMan(GamePanel gp) {
		super(gp);
		
		direction = "down";
		speed = 1;
		
		getImage();
		setDialouge();
		
		solidArea.x = 12;
		solidArea.y = 20;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		solidArea.width = 28;
		solidArea.height = 28;
	}
	
	public void getImage() {
		
		up1 = setup("/npc/oldman_up_1");
		up2 = setup("/npc/oldman_up_2");
		down1 = setup("/npc/oldman_down_1");
		down2 = setup("/npc/oldman_down_2");
		left1 = setup("/npc/oldman_left_1");
		left2 = setup("/npc/oldman_left_2");
		right1 = setup("/npc/oldman_right_1");
		right2 = setup("/npc/oldman_right_2");

	}
	
	public void setDialouge() {
		
		dialouges[0] = "Hello, Chamod.";
		dialouges[1] = "So you've come to this \nisland to find the treasure?";
		dialouges[2] = "I used to be a great wizard \nbut now... I'm a bit too \nold for taking an adventure.";
		dialouges[3] = "Well, good luck on you.";
		
	}
	
	public void setAction() {
		
		actionLockCounter ++;
		
		if(actionLockCounter == 120) {
		
			Random random = new Random();
			int i = random.nextInt(100)+1; // pickup a number from 1 to 100
			
			if(i <= 25) {
				direction = "up";
			}
			if(i > 25 && i <= 50) {
				direction = "down";
			}
			if(i > 50 && i <= 75) {
				direction = "left";
			}
			if(i > 75 && i <= 100) {
				direction = "right";
			}
			
			actionLockCounter = 0;
			
		}
		
	}
	
	public void speak() {
		
		// Do this character specific stuff
		
		super.speak();
		
	}

}








