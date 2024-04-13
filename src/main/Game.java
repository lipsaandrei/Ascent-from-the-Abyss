package main;

public class Game implements Runnable {
    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int frame_per_second = 120;
    public Game(){
        gamePanel = new GamePanel();
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();
        startGameLoop();
    }

    private void startGameLoop(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        //gameThread = new Thread(this);
        double time_per_frame = 1_000_000_000.0 / frame_per_second;
        long lastFrame = System.nanoTime();
        long now = System.nanoTime();
        int frames = 0;
        long framesCounter = System.currentTimeMillis();
        while(true){
            now = System.nanoTime();
            if(now - lastFrame >= time_per_frame){
                gamePanel.repaint();
                lastFrame = now;
                frames++;
            }
            if(System.currentTimeMillis() - framesCounter >= 1000){
                framesCounter = System.currentTimeMillis();
                System.out.println("Frames: " + frames);
                frames = 0;
            }
        }
    }
}
