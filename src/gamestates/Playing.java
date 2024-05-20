package gamestates;

import entities.Enemy;
import entities.Skeleton;
import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import main.Game;
import ui.LevelCompletedOverlay;
import ui.PauseOverlay;
import ui.GameOverOverlay;
import utilz.LoadSave;

import java.awt.*;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import static utilz.Constants.Environment.*;

public class Playing extends State implements Statemethods {
    private Player player;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private PauseOverlay pauseOverlay;
    private GameOverOverlay gameOverOverlay;
    private LevelCompletedOverlay levelCompletedOverlay;
    private boolean paused = false;

    private int xLvlOffset;
    private int leftBorder = (int) (0.2 * Game.GAME_WIDTH);
    private int rightBorder = (int) (0.8 * Game.GAME_WIDTH);
    private int maxLvlOffsetX;

    private BufferedImage background, bigClouds, smallClouds;
    private int[] smallCloudsPos;
    private Random rand = new Random();

    private boolean gameOver;
    private boolean levelCompleted;

    public Playing(Game game) {
        super(game);
        initClasses();

        background = LoadSave.getSpriteAtlas(LoadSave.PLAYING_IMG);
        bigClouds = LoadSave.getSpriteAtlas(LoadSave.BIG_CLOUDS);
        smallClouds = LoadSave.getSpriteAtlas(LoadSave.SMALL_CLOUDS);
        smallCloudsPos = new int[8];
        for (int i = 0; i < smallCloudsPos.length; i++) {
            smallCloudsPos[i] = (int) (90 * Game.SCALE) + rand.nextInt((int) (100 * Game.SCALE));
        }

        calcLvlOffset();
        loadStartLevel();
    }

    public void loadNextLevel(){
        resetAll();
        levelManager.loadNextLevel();
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
    }

    private void loadStartLevel() {
        enemyManager.loadEnemies(levelManager.getCurrentLevel());
    }

    private void calcLvlOffset() {
        maxLvlOffsetX = levelManager.getCurrentLevel().getLvlOffset();
    }

    private void initClasses() {
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        player = new Player(200, 200, (int) (150 * Game.SCALE), (int) (150 * Game.SCALE), this);
        player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
        pauseOverlay = new PauseOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);
        levelCompletedOverlay = new LevelCompletedOverlay(this);
    }

    @Override
    public void update() {
        if (paused) {
            pauseOverlay.update();
        } else if (levelCompleted) {
            levelCompletedOverlay.update();
        } else if (!gameOver){
            levelManager.update();
            player.update();
            enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player);
            checkCloseToBorder();
        }
    }

    private void checkCloseToBorder() {
        int playerX = (int)player.getHitBox().x;
        int dif = playerX - xLvlOffset;

        if(dif > rightBorder){
            xLvlOffset += dif - rightBorder;
        }
        else if(dif < leftBorder){
            xLvlOffset += dif - leftBorder;
        }

        if(xLvlOffset > maxLvlOffsetX)
            xLvlOffset = maxLvlOffsetX;
        else if(xLvlOffset < 0)
            xLvlOffset = 0;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(background, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT,null);

        drawClouds(g);

        levelManager.draw(g, xLvlOffset);
        player.render(g, xLvlOffset);
        enemyManager.draw(g, xLvlOffset);

        if (paused){
            g.setColor(new Color(0,0,0,150));
            g.fillRect(0,0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            pauseOverlay.draw(g);
        }
        else if (gameOver)
            gameOverOverlay.draw(g);
        else if(levelCompleted)
            levelCompletedOverlay.draw(g);
    }

    private void drawClouds(Graphics g) {
        for(int i = 0; i < 3; ++i)
            g.drawImage(bigClouds, i * BIG_CLOUDS_WIDTH - (int)(xLvlOffset * 0.3), (int)(204 * Game.SCALE), BIG_CLOUDS_WIDTH, BIG_CLOUDS_HEIGHT,null);

        for(int i = 0; i < smallCloudsPos.length; i++)
            g.drawImage(smallClouds, SMALL_CLOUDS_WIDTH * 4 * i - (int)(xLvlOffset * 0.7), smallCloudsPos[i], SMALL_CLOUDS_WIDTH, SMALL_CLOUDS_HEIGHT, null);
    }

    public void resetAll() {
        gameOver = false;
        paused = false;
        levelCompleted = false;
        player.resetAll();
        enemyManager.resetAllEnemies();
    }

    public void setLevelCompleted(boolean levelCompleted) {
        this.levelCompleted = levelCompleted;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        enemyManager.checkEnemyHit(attackBox);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            player.setAttacking(true);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(!gameOver){
            if(paused)
                pauseOverlay.mousePressed(e);
            else if(levelCompleted)
                levelCompletedOverlay.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(!gameOver){
            if(paused)
                pauseOverlay.mouseReleased(e);
            else if(levelCompleted)
                levelCompletedOverlay.mouseReleased(e);
        }

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(!gameOver){
            if(paused)
                pauseOverlay.mouseMoved(e);
            else if(levelCompleted)
                levelCompletedOverlay.mouseMoved(e);
        }
    }

    public void mouseDragged(MouseEvent e) {
        if (!gameOver)
            if (paused)
                pauseOverlay.mouseDragged(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver)
            gameOverOverlay.keyPressed(e);
        else switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                player.setLeft(true);
                break;
            case KeyEvent.VK_D:
                player.setRight(true);
                break;
            case KeyEvent.VK_SPACE:
                player.setJump(true);
                break;
            case KeyEvent.VK_ESCAPE:
                paused = !paused;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!gameOver) switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                player.setLeft(false);
                break;
            case KeyEvent.VK_D:
                player.setRight(false);
                break;
            case KeyEvent.VK_SPACE:
                player.setJump(false);
                break;
        }
    }

    public void unpauseGame() {
        paused = false;
    }

    public void windowLostFocus(){
        player.resetDirBooleans();
    }

    public void setMaxLvlOffset(int LvlOffset) {
        this.maxLvlOffsetX = LvlOffset;
    }

    public Player getPlayer() {
        return player;
    }

    public EnemyManager getEnemyManager(){
        return enemyManager;
    }

}
