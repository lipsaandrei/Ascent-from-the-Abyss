package entities;

import gamestates.Playing;
import levels.Level;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.EnemyConstants.*;

public class EnemyManager {
    private Playing playing;
    private BufferedImage[][] skeletonArr;
    private ArrayList<Skeleton> skeletons = new ArrayList<>();

    public EnemyManager(Playing playing){
        this.playing = playing;
        loadEnemyImg();
    }

    public void loadEnemies(Level level) {
        skeletons = level.getSkeletons();
        //System.out.println("no of skeletons: " + skeletons.size());
    }

    public void update(int [][] lvlData, Player player){
        boolean isAnyActive = false;
        for(Skeleton skeleton : skeletons)
            if(skeleton.isActive()){
                skeleton.update(lvlData, player);
                isAnyActive = true;
            }
        if(!isAnyActive){
            playing.setLevelCompleted(true);
        }
    }

    public void draw(Graphics g, int xLvlOffset){
        drawSkeletons(g, xLvlOffset);

    }

    private void drawSkeletons(Graphics g, int xLvlOffset) {
        for(Skeleton skeleton : skeletons){
            if(skeleton.isActive()){
                g.drawImage(skeletonArr[skeleton.getState()][skeleton.getAnimationIndex()], (int) skeleton.getHitBox().x - xLvlOffset - SKELETON_DRAW_X + skeleton.flipX(), (int) skeleton.getHitBox().y - SKELETON_DRAW_Y, SKELETON_WIDTH * skeleton.flipW(), SKELETON_HEIGHT, null);
                //skeleton.drawHitBox(g, xLvlOffset);
                //skeleton.drawAttackBox(g, xLvlOffset);
            }
        }
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        for (Skeleton s : skeletons){
            if (s.isActive())
                if (attackBox.intersects(s.getHitBox())) {
                    s.hurt(10);
                    return;
                }
        }
    }

    private void loadEnemyImg() {
        skeletonArr = new BufferedImage[5][13];
        BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.SKELETON_SPRITE);
        for(int j = 0; j < skeletonArr.length; j++){
            for (int i = 0; i < skeletonArr[j].length; i++) {
                skeletonArr[j][i] = temp.getSubimage(i * SKELETON_WIDTH_DEFAULT, j * SKELETON_HEIGHT_DEFAULT, SKELETON_WIDTH_DEFAULT, SKELETON_HEIGHT_DEFAULT);
            }
        }
    }

    public void resetAllEnemies() {
        for (Skeleton s : skeletons)
            s.resetEnemy();
    }
}
