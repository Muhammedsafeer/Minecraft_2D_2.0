package main;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    // SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16px tile texture
    final int scale = 3;

    final int tileSize = originalTileSize * scale; // 48x48px tile size
    final int maxScreenCol = 16;
    final int maxScreenRow = 9;
    final int screenWidth = maxScreenCol * tileSize; // 768px
    final int screenHeight = maxScreenRow * tileSize; // 432px

    GameLoop gameLoop = new GameLoop(this);


    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
    }


}
