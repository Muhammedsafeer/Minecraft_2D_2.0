package main;

import entity.Player;
import world.WorldManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    // SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16px tile texture
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; // 48x48px tile size
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 9;
    public final int screenWidth = maxScreenCol * tileSize; // 768px
    public final int screenHeight = maxScreenRow * tileSize; // 432px

    GameLoop gameLoop = new GameLoop(this);
    KeyHandler keyH = new KeyHandler();
    WorldManager world = new WorldManager(this, keyH);
    MouseHandler mouseH = new MouseHandler();
    Player player = new Player(this, keyH);

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
