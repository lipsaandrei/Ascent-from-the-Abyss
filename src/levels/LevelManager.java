package levels;

import gamestates.Gamestate;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static main.Game.TILES_SIZE;

public class LevelManager {
    private Game game;
    private BufferedImage[] levelSprite;
    private ArrayList<Level> levels;
    private int lvlindex = 0;

    public LevelManager(Game game) {
        this.game = game;
        //levelSprite = LoadSave.getSpriteAtlas(LoadSave.Level_Atlas);
        importOutsideSprites();
        levels = new ArrayList<>();
        buildAllLevels();
    }

    private void buildAllLevels() {
        BufferedImage[] allLevels = LoadSave.getAllLevels();
        for (BufferedImage img : allLevels) {
            levels.add(new Level(img));
        }
    }

    public void loadNextLevel(){
        lvlindex++;
        if(lvlindex >= levels.size()){
            lvlindex = 0;
            System.out.println("Game completed!");
            Gamestate.state = Gamestate.MENU;
        }

        Level newLevel = levels.get(lvlindex);
        game.getPlaying().getEnemyManager().loadEnemies(newLevel);
        game.getPlaying().getPlayer().loadLvlData(newLevel.getLevelData());
        game.getPlaying().setMaxLvlOffset(newLevel.getLvlOffset());
    }

    private void importOutsideSprites() {
        BufferedImage img = LoadSave.getSpriteAtlas(LoadSave.Level_Atlas);
        levelSprite = new BufferedImage[48];
        for(int j = 0; j < 4; ++j)
            for(int i = 0; i < 12; ++i){
                int index = j * 12 + i;
                levelSprite[index] = img.getSubimage(i * 32, j * 32, 32, 32);
            }
    }

    public void draw(Graphics g, int xLvlOffset) {
        for(int j = 0; j < Game.TILES_HEIGHT; ++j)
            for(int i = 0; i < levels.get(lvlindex).getLevelData()[0].length; ++i){
                int index = levels.get(lvlindex).getSpriteIndex(i, j);
                g.drawImage(levelSprite[index], i * TILES_SIZE - xLvlOffset, j * TILES_SIZE,TILES_SIZE,TILES_SIZE, null);
            }
    }
    public void update(){

    }

    public Level getCurrentLevel() {
        return levels.get(lvlindex);
    }

    public int getAmountOfLevels(){
        return levels.size();
    }
}
