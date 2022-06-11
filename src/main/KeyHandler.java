package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.omg.IOP.Codec;

public class KeyHandler implements KeyListener {

	public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, shotKeyPressed;
	GamePanel gp;

	// DEBUG
	boolean showDebugText = false;

	public KeyHandler(GamePanel gp) {
		this.gp = gp;
	}

	@Override
	public void keyPressed(KeyEvent e) {

		int code = e.getKeyCode();

		// TITLE STATE
		if (gp.gameState == gp.titleState) {
			titleState(code);
		} // PLAY STATE
		else if (gp.gameState == gp.playState) {
			playState(code);
		} // PAUSE STATE
		else if (gp.gameState == gp.pauseState) {
			pauseState(code);
		} // DIALOUGE STATE
		else if (gp.gameState == gp.dialougeState) {
			dialougeState(code);
		} // CHARACTER STATE
		else if (gp.gameState == gp.characterState) {
			characterState(code);
		} // OPTION STATE
		else if (gp.gameState == gp.optionState) {
			optionState(code);
		} // OPTION STATE
		else if (gp.gameState == gp.gameOverState) {
			gameOverState(code);
		} // TRADE STATE
		else if (gp.gameState == gp.tradeState) {
			tradeState(code);
		}

	}

	public void titleState(int code) {
		// CONTROL TITLE MENU
		if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
			gp.ui.commandNum--;
			if (gp.ui.commandNum < 0) {
				gp.ui.commandNum = 2;
			}
		}
		if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
			gp.ui.commandNum++;
			if (gp.ui.commandNum > 2) {
				gp.ui.commandNum = 0;
			}
		}
		if (code == KeyEvent.VK_ENTER) {
			switch (gp.ui.commandNum) {
			case 0:
				gp.gameState = gp.playState;
				gp.playMusic(0);
				break;
			case 1:
				// LOAD GAME
				break;
			case 2:
				System.exit(0);
				break;
			}
		}
	}

	public void playState(int code) {
		// PLAYER CONTROLLER
		if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
			upPressed = true;
		}
		if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
			downPressed = true;
		}
		if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
			leftPressed = true;
		}
		if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
			rightPressed = true;
		}
		if (code == KeyEvent.VK_C) {
			gp.gameState = gp.characterState;
		}
		if (code == KeyEvent.VK_ENTER) {
			enterPressed = true;
		}
		if (code == KeyEvent.VK_F) {
			shotKeyPressed = true;
		}

		// Zoom in Zoom out
//			if (code == KeyEvent.VK_UP) {
//				gp.zoomInOut(1);
//			}
//			if (code == KeyEvent.VK_DOWN) {
//				gp.zoomInOut(-1);
//			}
		// PAUSE MENU
		if (code == KeyEvent.VK_P) {
			gp.gameState = gp.pauseState;
		}

		// OPTION MENU
		if (code == KeyEvent.VK_ESCAPE) {
			gp.gameState = gp.optionState;
		}

		// DEBUG
		if (code == KeyEvent.VK_T) {
			if (showDebugText == false) {
				showDebugText = true;
			} else if (showDebugText == true) {
				showDebugText = false;
			}
		}
		if (code == KeyEvent.VK_R) {
			switch (gp.currentMap) {
			case 0:
				gp.tileManager.loadMap("/maps/worldV4.txt", gp.currentMap);
				break;
			case 1:
				gp.tileManager.loadMap("/maps/interior01.txt", gp.currentMap);
				break;
			}
		}
	}

	public void pauseState(int code) {
		if (code == KeyEvent.VK_P) {
			gp.gameState = gp.playState;
		}
	}

	public void dialougeState(int code) {
		if (code == KeyEvent.VK_ENTER) {
			gp.gameState = gp.playState;
		}
	}

	public void characterState(int code) {
		if (code == KeyEvent.VK_C) {
			gp.gameState = gp.playState;
		}
		if (code == KeyEvent.VK_ENTER) {
			gp.player.selectItem();
		}
		playerInventory(code);
	}

	public void optionState(int code) {

		if (code == KeyEvent.VK_ESCAPE) {
			gp.gameState = gp.playState;
		}
		if (code == KeyEvent.VK_ENTER) {
			enterPressed = true;
		}

		int maxCommandNum = 0;

		switch (gp.ui.subState) {
		case 0:
			maxCommandNum = 5;
			break;
		case 3:
			maxCommandNum = 1;
			break;
		}

		// option changer
		if (code == KeyEvent.VK_W) {
			gp.ui.commandNum--;
			gp.playSE(9);
			if (gp.ui.commandNum < 0) {
				gp.ui.commandNum = maxCommandNum;
			}
		}
		if (code == KeyEvent.VK_S) {
			gp.ui.commandNum++;
			gp.playSE(9);
			if (gp.ui.commandNum > maxCommandNum) {
				gp.ui.commandNum = 0;
			}
		}

		// volume control
		if (code == KeyEvent.VK_A) {
			if (gp.ui.subState == 0) {
				if (gp.ui.commandNum == 1 && gp.music.volumeScale > 0) {
					gp.music.volumeScale--;
					gp.music.checkVolume();
					gp.playSE(9);
				}

				if (gp.ui.commandNum == 2 && gp.se.volumeScale > 0) {
					gp.se.volumeScale--;
					gp.playSE(9);
				}
			}
		}

		if (code == KeyEvent.VK_D) {
			if (gp.ui.subState == 0) {
				if (gp.ui.commandNum == 1 && gp.music.volumeScale < 5) {
					gp.music.volumeScale++;
					gp.music.checkVolume();
					gp.playSE(9);
				}

				if (gp.ui.commandNum == 2 && gp.se.volumeScale < 5) {
					gp.se.volumeScale++;
					gp.playSE(9);
				}
			}
		}
	}

	public void gameOverState(int code) {

		if (code == KeyEvent.VK_W) {
			gp.ui.commandNum--;
			if (gp.ui.commandNum < 0) {
				gp.ui.commandNum = 1;
			}
			gp.playSE(9);
		}

		if (code == KeyEvent.VK_S) {
			gp.ui.commandNum++;
			if (gp.ui.commandNum > 1) {
				gp.ui.commandNum = 0;
			}
			gp.playSE(9);
		}

		if (code == KeyEvent.VK_ENTER) {
			if (gp.ui.commandNum == 0) {
				gp.gameState = gp.playState;
				gp.retry();
				gp.ui.commandNum = 0;
			} else if (gp.ui.commandNum == 1) {
				gp.gameState = gp.titleState;
				gp.restart();
				gp.ui.commandNum = 0;
			}
		}

	}

	public void tradeState(int code) {

		if (code == KeyEvent.VK_ENTER) {
			enterPressed = true;
		}

		if (gp.ui.subState == 0) {
			if (code == KeyEvent.VK_W) {
				gp.ui.commandNum--;
				if (gp.ui.commandNum < 0) {
					gp.ui.commandNum = 2;
				}
				gp.playSE(9);
			}
			if (code == KeyEvent.VK_S) {
				gp.ui.commandNum++;
				if (gp.ui.commandNum > 2) {
					gp.ui.commandNum = 0;
				}
				gp.playSE(9);
			}
		}
		
		if(gp.ui.subState == 1) {
			npcInventory(code);
			if(code == KeyEvent.VK_ESCAPE) {
				gp.ui.subState = 0;
			}
		}
		
		if(gp.ui.subState == 2) {
			playerInventory(code);
			if(code == KeyEvent.VK_ESCAPE) {
				gp.ui.subState = 0;
			}
		}
	}

	public void playerInventory(int code) {

		if (code == KeyEvent.VK_W) {
			if (gp.ui.playerSlotRow != 0) {
				gp.ui.playerSlotRow--;
				gp.playSE(9);
			}
		}
		if (code == KeyEvent.VK_A) {
			if (gp.ui.playerSlotCol != 0) {
				gp.ui.playerSlotCol--;
				gp.playSE(9);
			}
		}
		if (code == KeyEvent.VK_S) {
			if (gp.ui.playerSlotRow != 3) {
				gp.ui.playerSlotRow++;
				gp.playSE(9);
			}
		}
		if (code == KeyEvent.VK_D) {
			if (gp.ui.playerSlotCol != 4) {
				gp.ui.playerSlotCol++;
				gp.playSE(9);
			}
		}

	}
	
	public void npcInventory(int code) {

		if (code == KeyEvent.VK_W) {
			if (gp.ui.npcSlotRow != 0) {
				gp.ui.npcSlotRow--;
				gp.playSE(9);
			}
		}
		if (code == KeyEvent.VK_A) {
			if (gp.ui.npcSlotCol != 0) {
				gp.ui.npcSlotCol--;
				gp.playSE(9);
			}
		}
		if (code == KeyEvent.VK_S) {
			if (gp.ui.npcSlotRow != 3) {
				gp.ui.npcSlotRow++;
				gp.playSE(9);
			}
		}
		if (code == KeyEvent.VK_D) {
			if (gp.ui.npcSlotCol != 4) {
				gp.ui.npcSlotCol++;
				gp.playSE(9);
			}
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {

		int code = e.getKeyCode();

		if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
			upPressed = false;
		}
		if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
			downPressed = false;
		}
		if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
			leftPressed = false;
		}
		if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
			rightPressed = false;
		}
		if (code == KeyEvent.VK_F) {
			shotKeyPressed = false;
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

}
