package com.Danki.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.Danki.main.Game;
import com.Danki.world.Camera;

public class Entity {

	public static BufferedImage LIFEPACK_EN = Game.spritesheet.getSprite(6*16, 0, 16,16);
	public static BufferedImage WEPON_EN = Game.spritesheet.getSprite(7*16, 0, 16,16);
	public static BufferedImage BULLET_EN = Game.spritesheet.getSprite(8*16, 0, 16,16);
	public static BufferedImage ENEMY_EN = Game.spritesheet.getSprite(9*16, 0, 16,16);
	public static BufferedImage ENEMY_FEEDBACK = Game.spritesheet.getSprite(144, 16, 16, 16);
	public static BufferedImage WEAPON_LEFT = Game.spritesheet.getSprite(96, 32, 16, 16);
	public static BufferedImage WEAPON_RIGHT = 	Game.spritesheet.getSprite(80, 32, 16, 16);
	public static BufferedImage WAEPON_DOWN = Game.spritesheet.getSprite(112, 32, 16, 16);

	protected double x;
	protected double y;
	protected double z;
	protected int width;
	protected int height;

	private BufferedImage sprite;

	private int maskx, masky, mWidth, mHeight;

	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;

		this.maskx = 0;
		this.masky = 0;
		this.mWidth = width;
		this.mHeight = height;

	}

	public void setMask(int maskx, int masky, int mWidth, int mHeight) {
		this.maskx = maskx;
		this.masky = masky;
		this.mWidth = mWidth;
		this.mHeight = mHeight;
	}

	public int getX() {
		return (int)this.x;
	}

	public int getY() {
		return (int)this.y;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public void setX(int newX) {
		this.x = newX;
	}

	public void setY(int newY) {
		this.y = newY;
	}

	public void tick() {

	}

	public static boolean isColidding(Entity e1, Entity e2) {
		Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskx, e1.getY() + e1.masky, e1.mWidth, e1.mHeight);
		Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskx, e2.getY() + e2.masky, e2.mWidth, e2.mHeight);
		if(e1Mask.intersects(e2Mask) && e1.z == e2.z) {
			return true;
		}
		return false;
	}

	public void render(Graphics g) {
		g.drawImage(this.sprite,this.getX() - Camera.x,this.getY() - Camera.y,null);
	}

}
