package entities;
import main.Game;

import java.awt.geom.Rectangle2D;

import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.*;
import static utilz.Constants.Directions.*;
import static utilz.Constants.GRAVITY;

public abstract class Enemy extends Entity {
    protected int animationIndex, enemyType;
    protected int animationTick, animationSpeed = 16;
    protected boolean firstUpdate = true;
    protected boolean inAir = false;
    protected float fallSpeed;

    protected float walkSpeed = 0.2f * Game.SCALE;
    protected int walkdir = LEFT;
    protected int tileY;
    protected float attackDistance = Game.TILES_SIZE;
    protected boolean active = true;
    protected boolean attackChecked;

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        initHitBox(width, height);
        maxHealth = GetMaxHealth(enemyType);
        currentHealth = maxHealth;
    }

    protected void updateAnimationTick(){
        animationTick++;
        if(animationTick >= animationSpeed){
            animationTick = 0;
            animationIndex++;
            if(animationIndex >= GetSpriteAmounts(enemyType, state)){
                animationIndex = 0;
                switch (state) {
                    case ATTACK, HIT -> state = IDLE;
                    case DEAD -> active = false;
                }
            }
        }
    }

    protected void checkFirstUpdate(int[][] lvlData){
        if (!IsEntityOnFloor(hitBox, lvlData))
            inAir = true;
        firstUpdate = false;
    }

    protected void updateInAir(int[][] lvlData){
        if(CanMoveHere(hitBox.x, hitBox.y + fallSpeed, hitBox.width, hitBox.height, lvlData)){
            hitBox.y = hitBox.y + fallSpeed;
            fallSpeed += GRAVITY;
        }
        else {
            inAir = false;
            hitBox.y = GetEntityYPosUnderRoofOrAboveFloor(hitBox, fallSpeed);
            tileY = (int)(hitBox.y / Game.TILES_SIZE);
        }
    }

    protected void move(int[][] lvlData){
        float xSpeed = 0;
        if(walkdir == LEFT)
            xSpeed = -walkSpeed;
        else xSpeed = walkSpeed;

        if(CanMoveHere(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height, lvlData))
            if(IsFloor(hitBox, xSpeed, lvlData) && IsFloor(hitBox, xSpeed + (int)(16 * Game.SCALE), lvlData)) {
                hitBox.x += xSpeed;
                return;
            }

        changeWalkDir();
    }

    protected boolean CanSeePlayer(int[][] lvlData, Player player){
        int playerTileY = (int)(player.getHitBox().y / Game.TILES_SIZE);
        if(playerTileY == tileY)
            if(playerInRange(player)){
                if(IsSightClear(lvlData, hitBox, player.hitBox, tileY))
                    return true;
            }
        return false;
    }

    protected void moveTowardsPlayer(Player player){
        if(player.hitBox.x > hitBox.x)
            walkdir = RIGHT;
        else walkdir = LEFT;
    }

    protected boolean playerInRange(Player player) {
        int absValue = (int)Math.abs(player.hitBox.x - hitBox.x);

        return absValue <= attackDistance * 5;
    }

    protected boolean isPlayerCloseForAttack(Player player){
        int absValue = (int)Math.abs(player.hitBox.x - hitBox.x);
        return absValue <= attackDistance - 20;
    }

    protected void newState(int enemyState){
        this.state = enemyState;
        animationTick = 0;
        animationIndex = 0;
    }

    public void hurt(int amount) {
        currentHealth -= amount;
        if (currentHealth <= 0)
            newState(DEAD);
        else
            newState(HIT);
    }

    protected void checkPlayerHit(Rectangle2D.Float attackBox, Player player) {
        if (attackBox.intersects(player.hitBox))
            player.changeHealth(-GetEnemyDmg(enemyType));
        attackChecked = true;

    }

    protected void changeWalkDir(){
        if(walkdir == LEFT)
            walkdir = RIGHT;
        else walkdir = LEFT;
    }

    public void resetEnemy() {
        hitBox.x = x;
        hitBox.y = y;
        firstUpdate = true;
        currentHealth = maxHealth;
        newState(IDLE);
        active = true;
        fallSpeed = 0;
    }

    public int getAnimationIndex() {
        return animationIndex;
    }

    public boolean isActive() {
        return active;
    }
}
