package entities;

import main.Game;
import utilz.LoadSave;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static utilz.HelpMethods.*;
import static utilz.Constants.PlayerConstants.*;

public class Player extends Entity {
    private BufferedImage[][] animations;
    private int animationCounter, animationIndex ,animationSpeed = 15;
    private int playerAction = IDLE;
    private boolean left, up, right, down, jump;
    private boolean moving = false, attacking = false;
    private float playerSpeed = 1.0f * Game.SCALE;
    private int[][] lvlData;
    private float xDrawOffset = 40 * Game.SCALE, yDrawOffset = 35 * Game.SCALE;

    //Jumping and Gravity
    private float airSpeed = 0f;
    private float gravity = 0.04f * Game.SCALE;
    private float jumpSpeed = -2.25f * Game.SCALE;
    private float fallSpeedAfterCollsion = 0.5f * Game.SCALE;
    private boolean inAir = false;

    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);
        loadAnimation();
        initHitBox(x, y, (int)(20 * Game.SCALE), (int)(28 * Game.SCALE));
    }
    public void update(){
        updatePos();
        updateAnimation();
        setAnimation();

    }
    public void render(Graphics g){
        g.drawImage(animations[playerAction][animationIndex], (int)(hitBox.x - xDrawOffset), (int)(hitBox.y - yDrawOffset), (int)(100 * Game.SCALE), (int)(100 * Game.SCALE),  null);
        //drawHitBox(g);
    }


    private void updateAnimation() {
        animationCounter++;
        if(animationCounter >= animationSpeed){
            animationCounter = 0;
            animationIndex++;
            if(animationIndex >= GetSpriteAmount(playerAction)){
                animationIndex = 0;
                attacking = false;
            }
        }
    }
    private void setAnimation() {
        int startAnimation = playerAction;

        if (moving)
            playerAction = RUNNING;
        else
            playerAction = IDLE;

        if(inAir){
            if(airSpeed < 0)
                playerAction = JUMP;
            else playerAction = FALL;
        }

        if(attacking)
            playerAction = ATTACK_1;
        if(startAnimation != playerAction){
            resetAnimationTick();
        }
    }

    private void resetAnimationTick() {
        animationCounter = 0;
        animationIndex = 0;
    }

    private void updatePos() {
        moving = false;

        if(jump)
            jump();
        if(!left && !right && !inAir){
            return;
        }
        float xSpeed = 0;

        if(left)
            xSpeed -= playerSpeed;
        if(right )
            xSpeed += playerSpeed;
        if(!inAir){
            if(!IsEntityOnFloor(hitBox, lvlData)){
                inAir = true;
            }
        }

        if(inAir){
            if(CanMoveHere(hitBox.x, hitBox.y + airSpeed, hitBox.width, hitBox.height, lvlData)){
                hitBox.y += airSpeed;
                airSpeed += gravity;
                updateXPos(xSpeed);
            }
            else{
                hitBox.y = GetEntityYPosUnderRoofOrAboveFloor(hitBox, airSpeed);
                if(airSpeed > 0){
                    resetAirSpeed();
                }
                else{
                    airSpeed = fallSpeedAfterCollsion;
                }
                updateXPos(xSpeed);
            }
        }
        else {
            updateXPos(xSpeed);
        }
        moving = true;
    }

    private void jump() {
        if(inAir)
            return;
        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void resetAirSpeed() {
        inAir = false;
        airSpeed = 0;
    }

    private void updateXPos(float xSpeed) {
        if(CanMoveHere(hitBox.x+xSpeed, hitBox.y, hitBox.width, hitBox.height, lvlData)){
            hitBox.x += xSpeed;
        }
        else {
            hitBox.x = GetEntityXPosNextToWall(hitBox, xSpeed);
        }
    }

    private void loadAnimation() {
        BufferedImage img = LoadSave.getSpriteAtlas(LoadSave.Player_Atlas);
        animations = new BufferedImage[11][8];
        for (int j = 0; j < animations.length; j++)
            for (int i = 0; i < animations[j].length; i++)
                animations[j][i] = img.getSubimage(i * 150, j * 150, 150, 150);
    }

    public void loadLvlData(int[][] lvlData){
        this.lvlData = lvlData;
        if(!IsEntityOnFloor(hitBox, lvlData))
            inAir = true;
    }

    public void resetDirBooleans(){
        left = false;
        up = false;
        right = false;
        down = false;
    }

    public void setAttacking(boolean attacking){
        this.attacking = attacking;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isUp() {
        return up;
    }

    public boolean isRight() {
        return right;
    }

    public boolean isDown() {
        return down;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void setJump(boolean jump){
        this.jump = jump;
    }
}
