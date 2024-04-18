package ui;

import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import static utilz.Constants.UI.PauseButtons.SOUND_SIZE_DEFAULT;

public class SoundButton extends PauseButton {
    private BufferedImage[][] soundsImgs;
    private boolean mousePressed, mouseOver;
    private boolean muted;
    private int rowIndex, colIndex ;

    public SoundButton(int x, int y, int width, int height) {
        super(x, y, width, height);

        loadSoundImgs();
    }

    private void loadSoundImgs() {
        BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.SOUNDS_Buttons);
        soundsImgs = new BufferedImage[2][3];
        for(int j = 0; j < soundsImgs.length; j++) {
            for(int i = 0; i < soundsImgs[j].length; i++) {
                soundsImgs[j][i] = temp.getSubimage(i*SOUND_SIZE_DEFAULT, j*SOUND_SIZE_DEFAULT, SOUND_SIZE_DEFAULT, SOUND_SIZE_DEFAULT);
            }
        }
    }

    public void update(){
        if(muted)
            rowIndex = 1;
        else rowIndex = 0;

        colIndex = 0;
        if(mouseOver)
            colIndex = 1;
        if(mousePressed)
            colIndex = 2;
    }

    public void draw(Graphics g){
        g.drawImage(soundsImgs[rowIndex][colIndex], x, y,width,height, null);
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public void resetBools(){
        mousePressed = false;
        mouseOver = false;
    }
}
