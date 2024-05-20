package ui;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.UrmButtons.URM_SIZE;

public class LevelCompletedOverlay {
    private Playing playing;
    private UrmButton menu, nextLevel, retry;
    private BufferedImage img;
    private int bgX, bgY , bgW, bgH;

    public LevelCompletedOverlay(Playing playing) {
        this.playing = playing;
        initImg();
        initButtons();
    }

    private void initButtons() {
        int menuX = (int)(330 * Game.SCALE);
        int nextX = (int)(445 * Game.SCALE);
        int y = (int)(200 * Game.SCALE);
        nextLevel = new UrmButton(nextX, y, URM_SIZE, URM_SIZE, 0);
        menu = new UrmButton(menuX, y, URM_SIZE, URM_SIZE, 2);
    }

    private void initImg() {
        img = LoadSave.getSpriteAtlas(LoadSave.LEVEL_COMPLETED);    // get the image
        bgW = (int)(img.getWidth() * Game.SCALE);       // scale the image
        bgH = (int)(img.getHeight() * Game.SCALE);  // scale the image
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;    // center the image
        bgY = (int)(75 * Game.SCALE); // 75 is the y position of the level completed image
    }

    public void update() {
        nextLevel.update();
        menu.update();
    }

    private boolean IsIn(UrmButton b, MouseEvent e){
        return b.getBounds().contains(e.getX(), e.getY());
    }

    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        g.drawImage(img, bgX, bgY, bgW, bgH, null);
        nextLevel.draw(g);
        menu.draw(g);
    }

    public void mouseMoved(MouseEvent e) {
        nextLevel.setMouseOver(false);
        menu.setMouseOver(false);

        if(IsIn(menu, e))
            menu.setMouseOver(true);
        else if(IsIn(nextLevel, e))
            nextLevel.setMouseOver(true);
    }

    public void mouseReleased(MouseEvent e) {
        if(IsIn(menu, e)) {
            if (menu.isMousePressed()){
                playing.resetAll();
                Gamestate.state = Gamestate.MENU;
            }
        }
        else if(IsIn(nextLevel, e))
            if (nextLevel.isMousePressed())
                playing.loadNextLevel();
        menu.resetBools();
        nextLevel.resetBools();
    }

    public void mousePressed(MouseEvent e) {
        if(IsIn(menu, e))
            menu.setMousePressed(true);
        else if(IsIn(nextLevel, e))
            nextLevel.setMousePressed(true);
    }
}
