package entities;

import main.Game;

import static utilz.Constants.Directions.LEFT;
import static utilz.Constants.Directions.RIGHT;
import static utilz.Constants.EnemyConstants.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Skeleton extends Enemy {

    private int attackBoxOffsetX;

    public Skeleton(float x, float y) {
        super(x, y, SKELETON_WIDTH, SKELETON_HEIGHT, SKELETON);
        initHitBox(15,25);
        initAttackBox();
        state = 3;
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int) (20 * Game.SCALE), (int) (20 * Game.SCALE));
        attackBoxOffsetX = (int) (Game.SCALE);
    }

    public void update(int[][] lvlData, Player player) {
        updateBehavior(lvlData, player);
        updateAnimationTick();
        updateAttackBox();
    }

    private void updateAttackBox() {
        if (walkdir == RIGHT)
            attackBox.x = hitBox.x + hitBox.width + (int) (Game.SCALE * 1);
        else if (walkdir == LEFT)
            attackBox.x = hitBox.x - attackBox.width - (int) (Game.SCALE * 1);

        attackBox.y = hitBox.y;
    }

    private void updateBehavior(int[][] lvlData, Player player) {
        if (firstUpdate)
            checkFirstUpdate(lvlData);
        if (inAir)
            updateInAir(lvlData);
        else {
            switch (state) {
                case IDLE:
                    newState(RUN);
                    break;
                case RUN:
                    if (CanSeePlayer(lvlData, player)){
                        moveTowardsPlayer(player);
                        if (isPlayerCloseForAttack(player))
                            newState(ATTACK);
                    }

                    move(lvlData);
                    break;
                case ATTACK:
                    if (animationIndex == 0 || animationIndex == 5)
                        attackChecked = false;

                    // Changed the name for checkEnemyHit to checkPlayerHit
                    if ((animationIndex == 4 || animationIndex == 8) && !attackChecked)
                        checkPlayerHit(attackBox, player);

                    break;
                case HIT:
                    break;
            }
        }
    }

    public void drawAttackBox(Graphics g, int xLvlOffset) {
        g.setColor(Color.red);
        g.drawRect((int) (attackBox.x - xLvlOffset), (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }

    public int flipX() {
        if (walkdir == LEFT)
            return width;
        else
            return 0;
    }

    public int flipW() {
        if (walkdir == LEFT)
            return -1;
        else
            return 1;

    }
}
