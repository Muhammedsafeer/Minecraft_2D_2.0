package main;

import entity.Player;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    // SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16px tile texture
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; // 48x48px tile size
    final int maxScreenCol = 16;
    final int maxScreenRow = 9;
    final int screenWidth = maxScreenCol * tileSize; // 768px
    final int screenHeight = maxScreenRow * tileSize; // 432px

    GameLoop gameLoop = new GameLoop(this);
    KeyHandler keyH = new KeyHandler();
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
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        player.draw(g2d);

        g2d.dispose();

    }
}
