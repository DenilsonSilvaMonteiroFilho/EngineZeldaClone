package com.Danki.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.Danki.world.World;

public class Menu {
	
	public String[] options = {"Novo Jogo", "Carregar Jogo", "Sair"};
	public int currentOption = 0;
	public int maxOption = options.length - 1;
	public boolean up,down,enter;
	
	public static boolean pause = false;
	public static boolean saveExists = false;
	public static boolean saveGame = false;
	
	public void tick() {
		File file = new File ("save.txt");
		if(file.exists()) {
			saveExists = true;
		}
		else {
			saveExists = false;
		}
		if(up) {
			up = false;
			currentOption--;
			if(currentOption < 0) {
				currentOption = maxOption;
			}
		}
		if(down) {
			down = false;
			currentOption++;
			if(currentOption > maxOption) {
				currentOption = 0;
			}
		}
		if(enter) {
			//Sound.Clips.music.play();
			enter = false;
			if(options[currentOption] == "Novo Jogo" || options[currentOption] == "Continuar") {
				Game.gameState = "NORMAL";
				pause = false;
				file = new File ("save.txt");
				file.delete();
			}
			else if (options[currentOption] == "Carregar Jogo") {
				file = new File("save.txt");
				if(file.exists()) {
					String save = loadGame(10);
					applySave(save);
				}
			}
			else if (options[currentOption] == "Sair") {
				System.exit(1);
			}
		}
		
	}
	
	public static void applySave(String str) {
		String[] spl = str.split("/");
		for(int i = 0; i < spl.length; i++) {
			String[] spl2 = spl[i].split(":");
			switch(spl2[0]) 
			{
				case "level":
					World.restartGame("level"+spl2[1]+".png");
					Game.gameState = "NORMAL";
					pause = false;
					break;
				case "vida":
					Game.player.life = Integer.parseInt(spl2[1]);
					break;
			}
		}
	}
	
	public static String loadGame(int encode) {
		String line = "";
		File file = new File("save.txt");
		if(file.exists()) {
			try {
				String singleLine = null;
				BufferedReader reader = new BufferedReader(new FileReader("save.txt"));
				try {
					while((singleLine = reader.readLine()) != null) {
						String[] transition = singleLine.split(":");
						char[] val = transition[1].toCharArray();
						transition[1] = "";
						for(int i = 0; i < val.length; i++) {
							val[i]-=encode;
							transition[1]+=val[i];
						}
						line+=transition[0];
						line+=":";
						line+=transition[1];
						line+="/";
					}
				}catch(IOException e) {}
			}catch(FileNotFoundException e) {}
		}
		return line;
	}
	
	public static void saveGame(String[] val1, int[] val2, int encode) {
		BufferedWriter write = null;
		try {
			write = new BufferedWriter(new FileWriter("save.txt"));
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i < val1.length; i++) {
			String current = val1[i];
			current+=":";
			char[]value = Integer.toString(val2[i]).toCharArray();
			for(int n = 0; n < value.length; n++) {
				value[n]+=encode;
				current+=value[n];
			}
			try {
				write.write(current);
				if(i < val1.length) {
					write.newLine();
				}
			}catch(IOException e) {
				
			}
		}
		try {
			write.flush();
			write.close();
		}catch(IOException e) {
			
		}
	}
	
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0,0,0,100));
		//g.setColor(Color.black);
		g.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		g.setColor(Color.red);
		g.setFont(new Font("arial",Font.BOLD,36));
		g.drawString("Menu",(Game.WIDTH*Game.SCALE)/2 - 52, (Game.HEIGHT*Game.SCALE) / 2 - 120);
		
		//Opcao do menu
		
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD,24));
		
		if(pause == true) {
			g.drawString("Continuar",(Game.WIDTH*Game.SCALE)/2 - 65, 160);
		}
		else {
			g.drawString("Novo Jogo",(Game.WIDTH*Game.SCALE)/2 - 65, 160);
		}
		
		g.drawString("Carregar Jogo",(Game.WIDTH*Game.SCALE)/2 - 90, 190);
		
		g.drawString("Sair",(Game.WIDTH*Game.SCALE)/2 - 32, 220);
		
		
		
		if(options[currentOption] == "Novo Jogo") {
			g.drawString(">",(Game.WIDTH*Game.SCALE) / 2 - 90, 160);
		}
		else if(options[currentOption] == "Carregar Jogo") {
			g.drawString(">",(Game.WIDTH*Game.SCALE) / 2 - 120, 190);
		}
		else if(options[currentOption] == "Sair") {
			g.drawString(">",(Game.WIDTH*Game.SCALE) / 2 - 60, 220);
		}
	}
}
