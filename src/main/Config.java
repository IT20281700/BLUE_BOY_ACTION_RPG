package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.temporal.ValueRange;

public class Config {

	GamePanel gp;

	public Config(GamePanel gp) {
		this.gp = gp;
	}

	public void saveConfig() {

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("Config.txt"));
			// info
			bw.write("[BLUE BOY CONFIG FILE]");
			bw.newLine();
			
			// Full screen
			if (gp.fullScreenOn == true) {
				bw.write("On");
			}
			if (gp.fullScreenOn == false) {
				bw.write("Off");
			}
			bw.newLine();

			// Music Volume
			bw.write(String.valueOf(gp.music.volumeScale));
			bw.newLine();

			// SE Volume
			bw.write(String.valueOf(gp.se.volumeScale));
			bw.newLine();

			bw.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void loadConfig() {

		try {
			BufferedReader br = new BufferedReader(new FileReader("config.txt"));

			String s = br.readLine();
			s = br.readLine();

			// Full screen
			if (s.equals("On")) {
				gp.fullScreenOn = true;
			}
			if (s.equals("Off")) {
				gp.fullScreenOn = false;
			}

			// Music Volume
			s = br.readLine();
			gp.music.volumeScale = Integer.parseInt(s);

			// SE Volume
			s = br.readLine();
			gp.se.volumeScale = Integer.parseInt(s);
			
			br.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
