package com.Danki.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.Danki.graficos.Spritesheet;
import com.Danki.graficos.UI;
import com.Danki.main.Game;
import com.Danki.main.Sound;
import com.Danki.world.Camera;
import com.Danki.world.World;

public class Player extends Entity {

	public boolean right, left, up, down;
	public double speed = 1;
	public int right_dir = 0, left_dir = 1, down_dir = 2, up_dir = 3;
	public int dir = down_dir;

	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 3;
	private boolean moved = false;
	private BufferedImage [] rightPlayer;
	private BufferedImage [] leftPlayer;
	private BufferedImage [] upPlayer;
	private BufferedImage [] downPlayer;
	private BufferedImage damagedPlayer;

	public int ammo = 0;
	public boolean hasGun = false;
	public boolean shoot = false, mouseShoot = false;

	public boolean isDamaged = false;
	private int damagedFrames = 0;

	public double maxLife = 100, life = 100;
	public int mx,my;
	
	public boolean jump = false;
	public boolean isJumping = false;
	public boolean jumpUp = false, jumpDown = false;
	public int jumpSpd = 1;
	public int z = 0;
	
	public int jumpFrames = 50, jumpCur = 0;

	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);

		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		upPlayer = new BufferedImage[4];
		downPlayer = new BufferedImage[4];
		damagedPlayer = Game.spritesheet.getSprite(64, 33, 16, 16);

		//MoveDown
		for(int i = 0; i < 4; i++) {
			downPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 0, 16, 16);
		}
		//MoveUp
		for(int i = 0; i < 4; i++) {
			upPlayer[i] = Game.spritesheet.getSprite(0 + (i*16), 16 , 16, 16);
		}
		//MoveLeft
		for(int i = 0; i < 4; i++) {
			leftPlayer[i] = Game.spritesheet.getSprite(0 + (i*16), 32, 16, 16);
		}
		//MoveRight
		for(int i = 0; i < 4; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(64 + (i*16), 16, 16, 16);
		}
	}


	public void tick() {
		if(jump) {
			if(isJumping == false) {
				jump = false;
				isJumping = true;
				jumpUp = true;
			}
		}
		
		if(isJumping == true) {

				if(jumpUp) {
					jumpCur+=jumpSpd;
				}
				else if(jumpDown) {
					jumpCur-=jumpSpd;
					if(jumpCur <= 0) {
						isJumping = false;
						jumpDown = false;
						jumpDown = false;
					}
				}
				z = jumpCur;
				if(jumpCur >= jumpFrames) {
					jumpUp = false;
					jumpDown = true;
					System.out.println("Chegou na altura maxima");
				}
			
		}
		
		moved = false;

		if(right && World.isFree((int)(x+speed),this.getY())) {
			moved = true;
			dir = right_dir;
			x+=speed;
		}

		else if(left && World.isFree((int)(x-speed),this.getY())) {
			moved = true;
			dir = left_dir;
			x-=speed;
		}

		if(up && World.isFree(this.getX(),(int)(y-speed))) {
			moved = true;
			dir = up_dir;
			y-=speed;
		}

		else if(down && World.isFree(this.getX(),(int)(y+speed))) {
			moved = true;
			dir = down_dir;
			y+=speed;
		}

		if(moved) {
			frames++;
			if(frames == maxFrames) {
				frames = 0;
				index++;
				if(index > maxIndex) {
					index = 0;
				}
			}
		}

		checkCollisionBullet();
		checkCollisionLifePack();
		checkCollisionGun();

		if(isDamaged) {
			this.damagedFrames++;
			if(this.damagedFrames == 8) {
				this.damagedFrames = 0;
				isDamaged = false;
			}
		}
		if(shoot && hasGun && ammo > 0) {
			//Criar bala e atirar
			Sound.Clips.Bullet_Shoot.play();
			shoot = false;
			ammo--;
			int dx = 0;
			int dy = 0;
			int px = 0;
			int py = 6;
			if(dir == right_dir) {
				dx = 1;
				px = 15;
				dy = 0;
			}
			else if (dir == left_dir) {
				px = -3;
				dx = -1;
				dy = 0;
			}
			if(dir == up_dir) {
				dy = -1;
				px = 6;
				py = -2;
			}
			else if(dir == down_dir) {
				dy = 1;
				px = 6;
				py = 12;
			}
			BulletShoot bullet = new BulletShoot(this.getX()+px,this.getY()+py, 3, 3, null, dx, dy);
			Game.bullets.add(bullet);
			
		}
		
		if(mouseShoot) {
			mouseShoot = false;
			
			if(hasGun && ammo >0) {
				Sound.Clips.Bullet_Shoot.play();
				ammo--;
				double angle = Math.atan2(my - (this.getY() +8 - Camera.y), mx - (this.getX() +8 - Camera.x));

				double dx = Math.cos(angle);
				double dy = Math.sin(angle);
				int px = 0;
				int py = 6;
				
				if(dir == right_dir) {
					px = 15;
				}
				else if (dir == left_dir) {
					px = -3;
				}
				if(dir == up_dir) {
					px = 6;
					py = -2;
				}
				else if(dir == down_dir) {
					px = 6;
					py = 12;
				}
				
				BulletShoot bullet = new BulletShoot(this.getX()+px,this.getY()+py, 3, 3, null, dx, dy);
				Game.bullets.add(bullet);
			}//if hasGun && ammo
			
		}//mouseShoot

		if(life <= 0 ) {
			//Game over
			life = 0;
			Game.gameState = "GAME_OVER";
		}

		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2), 0, World.WIDTH*16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2), 0, World.HEIGHT*16 - Game.HEIGHT);

	}

	public void checkCollisionGun() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Weapon) {
				if(Entity.isColidding(this, atual)) {
					hasGun = true;
					Game.entities.remove(i);
				}
			}
		}
	}

	public void checkCollisionBullet() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Bullet) {
				if(Entity.isColidding(this, atual)) {
					ammo+=5;
					Game.entities.remove(i);
				}
			}
		}
	}

	public void checkCollisionLifePack() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof LifePack) {
				if(Entity.isColidding(this, atual)) {
					life+=10;
					Game.entities.remove(i);
					if(life >= maxLife) {
						life = maxLife;
					}
				}
			}
		}
	}

	public void render(Graphics g) {
		if(!isDamaged) {

			if(dir == right_dir) {
				g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y - z, null);
				if(hasGun) {
					g.drawImage(Entity.WEAPON_RIGHT, this.getX() - Camera.x+5, this.getY() - Camera.y -1 - z, null);
				}
				//Render Gun right
			}
			else if(dir == left_dir) {
				g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y - z, null);
				if(hasGun) {
					g.drawImage(Entity.WEAPON_LEFT, this.getX() - Camera.x-5, this.getY() - Camera.y -1 - z, null);
				}
				//Render Gun left
			}
			if(dir == down_dir) {
				g.drawImage(downPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y - z, null);
				if(hasGun) {
					g.drawImage(Entity.WAEPON_DOWN, this.getX() - Camera.x, this.getY() - Camera.y +4 - z, null);
				}
				//Render Gun down
			}
			else if(dir == up_dir) {
				g.drawImage(upPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y + z, null);
			}
			//Render Gun up
		}
		else {
			g.drawImage(damagedPlayer, this.getX() - Camera.x, this.getY() - Camera.y - z, null);
		}

	}

}
