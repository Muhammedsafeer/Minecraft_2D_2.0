package main;

import entity.Player;
import world.WorldManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    // SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16px tile texture
    final int scale = 3;

    public int tileSize = originalTileSize * scale; // 48x48px tile size
    public int maxScreenCol = 16;
    public int maxScreenRow = 9;
    public int screenWidth = maxScreenCol * tileSize; // 768px
    public int screenHeight = maxScreenRow * tileSize; // 432px

    // WORLD SETTINGS
    public final int maxWorldChunks = 3;
    public final int maxWorldCol = maxWorldChunks * 16;
    public final int maxWorldRow = 256;
    public final int worldWidth = maxWorldCol * tileSize;
    public final int worldHeight = maxWorldRow * tileSize;

    GameLoop gameLoop = new GameLoop(this);
    KeyHandler keyH = new KeyHandler(this);
    WorldManager world = new WorldManager(this, keyH);
    MouseHandler mouseH = new MouseHandler();
    public Player player = new Player(this, keyH);

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.addMouseListener(mouseH);
        this.addMouseMotionListener(mouseH);
        this.addMouseWheelListener(mouseH);
        this.setFocusable(true);
    }

    public void zoomInOut(int i) {
        int oldWorldWidth = tileSize * maxWorldCol;
        tileSize += i;
        int newWorldWidth = tileSize * maxWorldCol;

        player.speed = (double)newWorldWidth/600;

        double multiplier = (double) newWorldWidth / oldWorldWidth;

        double newPlayerWorldX = player.worldX * multiplier;
        double newPlayerWorldY = player.worldY * multiplier;

        player.worldX = newPlayerWorldX;
        player.worldY = newPlayerWorldY;
    }


    public void update() {
        player.update();
        world.update();
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        world.draw(g2d);
        player.draw(g2d);

        g2d.dispose();

    }
}
