package main;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setTitle("Minecraft 2D");

        GamePanel gamePanel = new GamePanel();
        frame.add(gamePanel);

        frame.pack();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        GameLoop gameLoop = new GameLoop(gamePanel);
        gameLoop.startGameThread();
    }
}