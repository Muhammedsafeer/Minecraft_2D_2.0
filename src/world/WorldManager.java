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

        world = new int[gp.maxWorldCol][gp.maxWorldRow];

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

        int x = gp.maxWorldCol / 2;
        int y = gp.maxScreenRow + 64;

        int direction = (int) (Math.random() * directions.length);
        lengthX = (int) (Math.random() * 10) + 2;
        lengthY = (int) (Math.random() * 6) + 2;

        world[x][y] = 1;

        while (x + 1 < gp.maxWorldCol) {
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
        int x = (gp.maxWorldCol / 2) - 1;
        int y = gp.maxScreenRow + 64;

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
                if (directions[direction].equals("up") && y + 1 < gp.maxWorldRow && lengthY > 0) {
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

        while(col < gp.maxWorldCol) {
            while(row < gp.maxWorldRow) {
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

        while(col < gp.maxWorldCol) {
            while(row < gp.maxWorldRow) {
                if (row + 1 < gp.maxWorldRow && world[col][row] == 1) {
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
            world = new int[gp.maxWorldCol][gp.maxWorldRow];
            generateWorld();
            keyH.resetWorld = false;
        }
    }
    public void getCoordinatesX(int x) {

    }
    public void getCoordinatesY(int y) {

    }

    public void draw(Graphics2D g2d) {

        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < gp.maxScreenCol && worldRow < gp.maxWorldRow) {

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            double screenX = worldX - gp.player.worldX + gp.player.screenX;
            double screenY = worldY - gp.player.worldY + gp.player.screenY;

            if (worldX + (gp.tileSize * 2) > gp.player.worldX - gp.player.screenX &&
                worldX - (gp.tileSize * 2) < gp.player.worldX + gp.player.screenX &&
                worldY + (gp.tileSize * 2) > gp.player.worldY - gp.player.screenY &&
                worldY - (gp.tileSize * 4) < gp.player.worldY + gp.player.screenY) {
                g2d.drawImage(block[world[worldCol][worldRow]].image, (int)screenX, (int)screenY, gp.tileSize, gp.tileSize, null);
            }

            worldCol++;
            if (worldCol == gp.maxScreenCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
}
