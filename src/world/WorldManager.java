package world;

import main.GamePanel;
import main.KeyHandler;

import java.awt.*;

public class WorldManager extends BlockManager {
    GamePanel gp;
    KeyHandler keyH;

    int[][] world;

    String[] directions = {"up", "down"};
    int lengthX;
    int lengthY;

    public WorldManager(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        world = new int[gp.maxScreenCol][gp.maxScreenRow];

        getBlockSprite();
        generateWorld();
    }

    public void generateWorld() {
        generateLandScapeRight();
        generateLandScapeLeft();

        generateGrassBlocks();
        generateDirtBlocks();
    }

    public void generateLandScapeRight() {

        int x = gp.maxScreenCol / 2;
        int y = (gp.maxScreenRow / 2) + 2;

        int direction = (int) (Math.random() * directions.length);
        lengthX = (int) (Math.random() * 10) + 2;
        lengthY = (int) (Math.random() * 6) + 2;

        world[x][y] = 1;

        while (x + 1 < gp.maxScreenCol) {
            if (lengthX == 0) {
                direction = (int) (Math.random() * directions.length);
                lengthX = (int) (Math.random() * 10) + 2;
                lengthY = (int) (Math.random() * 6) + 2;
            }else {
                if (directions[direction].equals("up") && y + 1 < gp.maxScreenRow && lengthY > 0) {
                    int upOrStraight = (int) (Math.random() * 2);
                    if (upOrStraight == 1) {
                        y += 1;
                    }
                }
                if (directions[direction].equals("down") && y - 1 > 0 && lengthY > 0) {
                    int upOrStraight = (int) (Math.random() * 2);
                    if (upOrStraight == 1) {
                        y -= 1;
                    }
                }
                lengthX -= 1;
            }
            x += 1;
            world[x][y] = 1;
        }

        System.out.println("Right part of the world generated!");
    }
    public void generateLandScapeLeft() {
        int x = (gp.maxScreenCol / 2) - 1;
        int y = (gp.maxScreenRow / 2) + 2;

        int direction = (int) (Math.random() * directions.length);
        lengthX = (int) (Math.random() * 10) + 2;
        lengthY = (int) (Math.random() * 6) + 2;

        world[x][y] = 1;

        while (x > 0) {
            if (lengthX == 0) {
                direction = (int) (Math.random() * directions.length);
                lengthX = (int) (Math.random() * 10) + 2;
                lengthY = (int) (Math.random() * 6) + 2;
            }else {
                if (directions[direction].equals("up") && y + 1 < gp.maxScreenRow && lengthY > 0) {
                    int upOrStraight = (int) (Math.random() * 2);
                    if (upOrStraight == 1) {
                        y += 1;
                    }
                }
                if (directions[direction].equals("down") && y - 1 > 0 && lengthY > 0) {
                    int upOrStraight = (int) (Math.random() * 2);
                    if (upOrStraight == 1) {
                        y -= 1;
                    }
                }
                lengthX -= 1;
            }
            x -= 1;
            world[x][y] = 1;
        }

        System.out.println("Left part of the world generated!");
    }
    public void generateGrassBlocks() {
        int col = 0;
        int row = 0;

        while(col < gp.maxScreenCol) {
            while(row < gp.maxScreenRow) {
                if (world[col][row] == 1) {
                    world[col][row - 1] = 2;
                }
                row++;
            }
            col++;
            row = 0;
        }

        System.out.println("Grass blocks generated!");
    }
    public void generateDirtBlocks() {
        int col = 0;
        int row = 0;

        while(col < gp.maxScreenCol) {
            while(row < gp.maxScreenRow) {
                if (row + 1 < gp.maxScreenRow && world[col][row] == 1) {
                    world[col][row + 1] = 1;
                }
                row++;
            }
            col++;
            row = 0;
        }

        System.out.println("Dirt blocks generated!");
    }

    public void update() {
        if (keyH.resetWorld) {
            world = new int[gp.maxScreenCol][gp.maxScreenRow];
            generateWorld();
            keyH.resetWorld = false;
        }
    }

    public void draw(Graphics2D g2d) {

        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;

        while(col < gp.maxScreenCol) {
            while(row < gp.maxScreenRow) {

                g2d.drawImage(block[world[col][row]].image, x, y, gp.tileSize, gp.tileSize, null);

                row++;
                y += gp.tileSize;
            }
            col++;
            x += gp.tileSize;
            row = 0;
            y = 0;
        }
    }
}
