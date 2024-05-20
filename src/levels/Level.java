package levels;

import entities.Skeleton;
import main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import static utilz.HelpMethods.*;

public class Level {
    private int[][] LevelData;
    private BufferedImage img;
    private ArrayList<Skeleton> skeletons;
    private int lvlTilesWide;
    private int maxTilesOffset;
    private int maxLvlOffsetX;
    private Point playerSpawn;

    public Level(BufferedImage img) {
        this.img = img;
        createLevelData();
        createEnemies();
        calcLvlOffset();
        calcPlayerSpawn();
    }

    private void calcPlayerSpawn() {
        playerSpawn = GetPlayerPoint(img);
    }

    private void calcLvlOffset() {
        lvlTilesWide = img.getWidth();
        maxTilesOffset = lvlTilesWide - Game.TILES_WIDTH;
        maxLvlOffsetX = Game.TILES_SIZE * maxTilesOffset;
    }

    private void createEnemies() {
        skeletons = GetSkeletons(img);
    }

    private void createLevelData() {
        LevelData = GetLevelData(img);
    }

    public int getSpriteIndex(int x, int y) {
        return LevelData[y][x];
    }

    public int[][] getLevelData() {
        return LevelData;
    }

    public int getLvlOffset(){
        return maxLvlOffsetX;
    }

    public ArrayList<Skeleton> getSkeletons() {
        return skeletons;
    }

    public Point getPlayerSpawn() {
        return playerSpawn;
    }
}
