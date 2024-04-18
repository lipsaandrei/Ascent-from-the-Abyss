package main;

import com.sun.management.GarbageCollectionNotificationInfo;
import entities.Player;
import levels.LevelManager;

import java.awt.*;

public class Game implements Runnable {
    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private Player player;
    private LevelManager levelManager;

    public static int TILES_DEFAULT_SIZE = 32;
    public static float SCALE = 2f;
    public static int TILES_WIDTH = 26;
    public static int TILES_HEIGHT = 14;
    public static int TILES_SIZE = (int)(TILES_DEFAULT_SIZE * SCALE);
    public static int GAME_WIDTH = TILES_SIZE * TILES_WIDTH;
    public static int GAME_HEIGHT = TILES_SIZE * TILES_HEIGHT;

    private final int frame_per_second = 120;
    private final int ups = 200;
    public Game(){
        initClasses();
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();

        startGameLoop();
    }

    private void initClasses() {
        levelManager = new LevelManager(this);
        player = new Player(200, 200, (int)(150 * SCALE), (int)(150 * SCALE));
        player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
    }

    private void startGameLoop(){
        gameThread = new Thread(this);
        gameThread.start();
    }
    public void update(){
        levelManager.update();
        player.update();
    }
    public void render(Graphics g){
        Toolkit.getDefaultToolkit().sync();
        levelManager.draw(g);
        player.render(g);
    }

    @Override
    public void run() {
        //gameThread = new Thread(this);
        double time_per_frame = 1_000_000_000.0 / frame_per_second;
        double time_per_ups = 1_000_000_000.0 / ups;

        long previosTime = System.nanoTime();

        int frames = 0;
        int updates = 0;
        long framesCounter = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;
        while(true){
            long currentTime = System.nanoTime();

            deltaU += (currentTime - previosTime) / time_per_ups;
            deltaF += (currentTime - previosTime) / time_per_frame;
            previosTime = currentTime;
            if(deltaU >= 1){
                update();
                updates++;
                deltaU --;
            }
            if(deltaF >= 1){
                gamePanel.repaint();
                frames++;
                deltaF--;
            }

            if(System.currentTimeMillis() - framesCounter >= 1000){
                framesCounter = System.currentTimeMillis();
                System.out.println("Frames: " + frames + " | Updates: " + updates);
                frames = 0;
                updates = 0;
            }
        }
    }
    public void windowLostFocus(){
        player.resetDirBooleans();
    }

    public Player getPlayer() {
        return player;
    }
}
