package utilz;

import com.sun.source.tree.BreakTree;
import main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class LoadSave {
    public static final String Player_Atlas = "Adventurer.png";
    public static final String Level_Atlas = "outside_sprites.png";
    public static final String Level_One_Data = "level_one_data.png";
    public static final String MENU_Buttons = "button_atlas.png";
    public static final String MENU_Background = "menu_background.png";
    public static final String PAUSE_Background = "pause_background.png";
    public static final String SOUNDS_Buttons = "sound_button.png";
    public static final String URM_Buttons = "urm_buttons.png";
    public static final String VOLUME_BUTTONS = "volume_buttons.png";

    public static BufferedImage getSpriteAtlas(String fileName){
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
        try {
            img = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return img;
    }
    public static int[][] getLevelData(){
        int[][] levelData = new int[Game.TILES_HEIGHT][Game.TILES_WIDTH];
        BufferedImage img = getSpriteAtlas(Level_One_Data);
        for(int y = 0; y < img.getHeight(); y++)
            for(int x = 0; x < img.getWidth(); x++){
                Color color = new Color(img.getRGB(x, y));
                int value = color.getRed();
                if(value >= 48)
                    value = 0;
                levelData[y][x] = value;
            }
        return levelData;
    }
}
