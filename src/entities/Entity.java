package entities;

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Entity {
    protected float x, y;
    protected int width, height;
    protected Rectangle2D.Float hitBox;
    protected int state;
    protected int maxHealth;
    protected int currentHealth;

    // AttackBox
    protected Rectangle2D.Float attackBox;

    public Entity(float x, float y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

    }

    protected void drawHitBox(Graphics g, int xLvlOffset){
        g.setColor(Color.BLACK);
        g.drawRect((int)hitBox.x - xLvlOffset, (int)hitBox.y, (int)hitBox.width, (int)hitBox.height);
    }
    protected void initHitBox(int width, int height) {
        hitBox = new Rectangle2D.Float(x, y, (int)(width * Game.SCALE), (int)(height * Game.SCALE));
    }
//    protected void updateHitBox(){
//        hitBox.x = (int) x;
//        hitBox.y = (int) y;
//    }
    public Rectangle2D.Float getHitBox(){
        return hitBox;
    }

    public int getState(){
        return state;
    }
}
