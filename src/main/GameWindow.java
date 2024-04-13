package main;

import javax.swing.*;

public class GameWindow {
    private JFrame frame;
    public GameWindow(GamePanel gamePanel) {
        frame = new JFrame();
        //frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gamePanel);

        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        frame.pack();
        frame.setVisible(true);
    }
}
