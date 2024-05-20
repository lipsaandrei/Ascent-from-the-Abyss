package utilz;

import com.sun.source.tree.BreakTree;
import entities.Skeleton;
import main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import static utilz.Constants.EnemyConstants.SKELETON;

public class LoadSave {
    public static final String Player_Atlas = "Adventurer.png";
    public static final String Level_Atlas = "outside_sprites.png";
    public static final String MENU_Buttons = "button_atlas.png";
    public static final String MENU_Background = "menu_background.png";
    public static final String PAUSE_Background = "pause_background.png";
    public static final String SOUNDS_Buttons = "sound_button.png";
    public static final String URM_Buttons = "urm_buttons.png";
    public static final String VOLUME_BUTTONS = "volume_buttons.png";
    public static final String BACKGROUND_MENU = "summer.png";
    public static final String PLAYING_IMG = "playing_bg_img.png";
    public static final String SMALL_CLOUDS = "small_clouds.png";
    public static final String BIG_CLOUDS = "big_clouds.png";
    public static final String SKELETON_SPRITE = "Skeleton enemy.png";
    public static final String STATUS_BAR = "health_power_bar.png";
    public static final String LEVEL_COMPLETED = "completed_sprite.png";

    public static BufferedImage[] getAllLevels(){
        URL url = LoadSave.class.getResource("/lvls");
        File file = null;

        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        File[] files = file.listFiles();
        File[] fileSorted = new File[files.length];
        for(int i = 0; i < files.length; i++){
            for (int j = 0; j < files.length; j++){
                if(files[j].getName().equals("" + (i + 1) + ".png"))
                    fileSorted[i] = files[j];
            }
        }

        BufferedImage[] imgs = new BufferedImage[fileSorted.length];

        for(int i = 0; i < imgs.length; i++){
            try {
                imgs[i] = ImageIO.read(fileSorted[i]);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return imgs;
    }

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

}
