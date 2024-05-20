package entities;

import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static utilz.Constants.EnemyConstants.ATTACK;
import static utilz.Constants.GRAVITY;
import static utilz.HelpMethods.*;
import static utilz.Constants.PlayerConstants.*;

public class Player extends Entity {

    private BufferedImage[][] animations;
    private int animationCounter, animationIndex ,animationSpeed = 15;
    private boolean left, up, right, down, jump;
    private boolean moving = false, attacking = false;
    private float playerSpeed = 1.0f * Game.SCALE;
    private int[][] lvlData;
    private float xDrawOffset = 40 * Game.SCALE, yDrawOffset = 35 * Game.SCALE;

    //Jumping and Gravity
    private float airSpeed = 0f;
    private float jumpSpeed = -2.25f * Game.SCALE;
    private float fallSpeedAfterCollsion = 0.5f * Game.SCALE;
    private boolean inAir = false;

    // StatusBarUI
    private BufferedImage statusBarImg;

    private int statusBarWidth = (int) (192 * Game.SCALE);
    private int statusBarHeight = (int) (58 * Game.SCALE);
    private int statusBarX = (int) (10 * Game.SCALE);
    private int statusBarY = (int) (10 * Game.SCALE);

    private int healthBarWidth = (int) (150 * Game.SCALE);
    private int healthBarHeight = (int) (4 * Game.SCALE);
    private int healthBarXStart = (int) (34 * Game.SCALE);
    private int healthBarYStart = (int) (14 * Game.SCALE);

    private int healthWidth = healthBarWidth;

    private int flipX = 0;
    private int flipW = 1;

    private boolean attackChecked;
    private Playing playing;

    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y, width, height);
        this.playing = playing;
        this.state = IDLE;
        this.maxHealth = 30;
        this.currentHealth = maxHealth;
        loadAnimation();
        initHitBox(20,28);
        initAttackBox();
    }

    public void setSpawn(Point spawn){
        this.x = spawn.x;
        this.y = spawn.y;
        hitBox.x = x;
        hitBox.y = y;
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int) (25 * Game.SCALE), (int) (28 * Game.SCALE));
    }

    public void update(){
        updateHealthBar();

        if (currentHealth <= 0) {
            playing.setGameOver(true);
            return;
        }

        updateAttackBox();

        updatePos();
        if (attacking)
            checkAttack();
        updateAnimation();
        setAnimation();
    }

    private void checkAttack() {
        if (attackChecked || animationIndex != 1)
            return;
        attackChecked = true;
        playing.checkEnemyHit(attackBox);

    }

    private void updateAttackBox() {
        if (right)
            attackBox.x = hitBox.x + hitBox.width + (int) (Game.SCALE * 1);
        else if (left)
            attackBox.x = hitBox.x - attackBox.width - (int) (Game.SCALE * 1);

        attackBox.y = hitBox.y;
    }

    private void updateHealthBar() {
        healthWidth = (int) ((currentHealth / (float) maxHealth) * healthBarWidth);
    }

    public void render(Graphics g, int xLvlOffset){
        g.drawImage(animations[state][animationIndex], (int)(hitBox.x - xDrawOffset) - xLvlOffset + flipX, (int)(hitBox.y - yDrawOffset), (int)(100 * Game.SCALE) * flipW, (int)(100 * Game.SCALE),  null);
        //drawHitBox(g, xLvlOffset);
        //drawAttackBox(g, xLvlOffset);
        drawUI(g);
    }

    private void drawAttackBox(Graphics g, int lvlOffsetX) {
        g.setColor(Color.red);
        g.drawRect((int) attackBox.x - lvlOffsetX, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);

    }

    private void drawUI(Graphics g) {
        g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
        g.setColor(Color.red);
        g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
    }

    private void updateAnimation() {
        animationCounter++;
        if(animationCounter >= animationSpeed){
            animationCounter = 0;
            animationIndex++;
            if(animationIndex >= GetSpriteAmount(state)){
                animationIndex = 0;
                attacking = false;
                attackChecked = false;
            }
        }
    }
    private void setAnimation() {
        int startAnimation = state;

        if (moving)
            state = RUNNING;
        else
            state = IDLE;

        if(inAir){
            if(airSpeed < 0)
                state = JUMP;
            else state = FALL;
        }

        if(attacking) {
            state = ATTACK_1;
            if (startAnimation != ATTACK) {
                animationIndex = 1;
                animationCounter = 0;
                return;
            }
        }
        if(startAnimation != state){
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
        if(!inAir)
            if((!left && !right) || (left && right)){
                return;
        }
        float xSpeed = 0;

        if(left) {
            xSpeed -= playerSpeed;
            flipX = (int)(100 * Game.SCALE);
            flipW = -1;
        }
        if(right ){
            xSpeed += playerSpeed;
            flipX = 0;
            flipW = 1;
        }
        if(!inAir){
            if(!IsEntityOnFloor(hitBox, lvlData)){
                inAir = true;
            }
        }

        if(inAir){
            if(CanMoveHere(hitBox.x, hitBox.y + airSpeed, hitBox.width, hitBox.height, lvlData)){
                hitBox.y += airSpeed;
                airSpeed += GRAVITY;
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

    public void changeHealth(int value) {
        currentHealth += value;

        if (currentHealth <= 0)
            currentHealth = 0;
        else if (currentHealth >= maxHealth)
            currentHealth = maxHealth;
    }

    private void loadAnimation() {
        BufferedImage img = LoadSave.getSpriteAtlas(LoadSave.Player_Atlas);
        animations = new BufferedImage[11][8];
        for (int j = 0; j < animations.length; j++)
            for (int i = 0; i < animations[j].length; i++)
                animations[j][i] = img.getSubimage(i * 150, j * 150, 150, 150);
        statusBarImg = LoadSave.getSpriteAtlas(LoadSave.STATUS_BAR);
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

    public void resetAll() {
        resetDirBooleans();
        inAir = false;
        attacking = false;
        moving = false;
        state = IDLE;
        currentHealth = maxHealth;

        hitBox.x = x;
        hitBox.y = y;

        if (!IsEntityOnFloor(hitBox, lvlData))
            inAir = true;
    }
}
