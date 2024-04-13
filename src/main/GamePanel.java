package main;

import inputs.KeyboardInputs;
import inputs.MouseInputs;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static utilz.Constants.PlayerConstants.*;
import static utilz.Constants.Directions.*;

public class GamePanel extends JPanel {
    private MouseInputs mouseInputs;
    private float xBeta = 0, yBeta = 0;
    private BufferedImage img;
    private BufferedImage[][] animations;
    private int animationCounter, animationIndex ,animationSpeed = 15;
    private int playerAction = IDLE;
    private int playerDir = -1;
    private boolean moving = false;

    public GamePanel() {

        mouseInputs = new MouseInputs(this);

        importImg();
        loadAnimation();

        setPanelSize();
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    private void loadAnimation() {
        animations = new BufferedImage[4][4];
        for (int j = 0; j < animations.length; j++)
            for (int i = 0; i < animations[j].length; i++)
                animations[j][i] = img.getSubimage(i * 150, j * 150, 150, 150);
    }

    private void importImg() {
        InputStream is = getClass().getResourceAsStream("/Idle.png");
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
    }

    private void setPanelSize() {
        Dimension screenSize = new Dimension(1280, 800);
        setPreferredSize(screenSize);
    }
    public void setDirection(int direction) {
        this.playerDir = direction;
        moving = true;
    }
    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    private void updateAnimation() {
        animationCounter++;
        if(animationCounter >= animationSpeed){
            animationCounter = 0;
            animationIndex++;
            if(animationIndex >= GetSpriteAmount(playerAction)){
                animationIndex = 0;
            }
        }
    }
    private void setAnimation() {
        if (moving)
            playerAction = RUNNING;
        else
            playerAction = IDLE;
    }
    private void updatePos() {
        if (moving) {
            switch (playerDir) {
                case LEFT:
                    xBeta -= 5;
                    break;
                case UP:
                    yBeta -= 5;
                    break;
                case RIGHT:
                    xBeta += 5;
                    break;
                case DOWN:
                    yBeta += 5;
                    break;
            }
        }
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        updateAnimation();

        g.drawImage(animations[playerAction][animationIndex], (int)xBeta, (int)yBeta, 300, 300,  null);
    }

}
