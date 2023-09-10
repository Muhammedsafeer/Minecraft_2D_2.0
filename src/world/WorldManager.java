package world;

import main.GamePanel;
import main.KeyHandler;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Objects;

public class WorldManager extends BlockManager {
    private final GamePanel gamePanel;
    private final KeyHandler keyHandler;
    private Chunk[] terrainChunks;
    private Chunk[] previousChunks;

    private int[][] gameWorld;

    private final String[] directions = {"up", "down"};

    public WorldManager(GamePanel gamePanel, KeyHandler keyHandler) {
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;
        terrainChunks = new Chunk[3];
        previousChunks = terrainChunks;
        gameWorld = new int[gamePanel.maxWorldCol][gamePanel.maxWorldRow];

        getBlockSprite();
        generateWorld();

        try {
            for (int i = 0; i < terrainChunks.length; i++) {
                // Create terrain data file
                FileWriter terrainFile = new FileWriter("src\\saves\\chunks\\chunk_" + i + ".txt");
                BufferedWriter terrainWriter = new BufferedWriter(terrainFile);

                int chunkNumber = terrainChunks[i].chunkNumber;
                int rightGrassHeight = terrainChunks[i].rightGrassHeight;
                int leftGrassHeight = terrainChunks[i].leftGrassHeight;
                int[][] terrainBlocks = terrainChunks[i].blocks;

                int x = 0;
                int y = 0;
                while (x < terrainBlocks.length && y < terrainBlocks[x].length) {
                    // Copy terrain data to the file
                    terrainWriter.write(terrainBlocks[x][y] + " ");

                    // Copy terrain data to the game world variable
                    if (chunkNumber == -1) {
                        gameWorld[x][y] = terrainBlocks[x][y];
                    }
                    if (chunkNumber == 0) {
                        gameWorld[x + 16][y] = terrainBlocks[x][y];
                    }
                    if (chunkNumber == 1) {
                        gameWorld[x + 32][y] = terrainBlocks[x][y];
                    }

                    x++;
                    if (x == terrainBlocks.length) {
                        x = 0;
                        y++;
                        terrainWriter.write("\n");
                    }
                }

                // Close the terrain data stream
                terrainWriter.close();

                // Create terrain info file
                terrainFile = new FileWriter("src\\saves\\chunk_" + i + "_info.txt");
                terrainWriter = new BufferedWriter(terrainFile);

                terrainWriter.write("chunk: " + chunkNumber + "\n");
                terrainWriter.write("rightGrassHeight: " + rightGrassHeight + "\n");
                terrainWriter.write("leftGrassHeight: " + leftGrassHeight + "\n");
                terrainWriter.write("status: " + terrainChunks[i].status + "\n");

                // Close the info stream
                terrainWriter.close();
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void generateWorld() {
        terrainChunks[0] = new Chunk();
        terrainChunks[0].chunkNumber = 0;
        terrainChunks[0].status = "not generated";
        terrainChunks[0].leftGrassHeight = 20;
        terrainChunks[0].currentChunkLocation = 0;
        terrainChunks[0].chunkDirection = "right";
        generateTerrainChunkRight(0, terrainChunks[0].leftGrassHeight);

        terrainChunks[1] = new Chunk();
        terrainChunks[1].chunkNumber = 1;
        terrainChunks[1].status = "not generated";
        terrainChunks[1].leftGrassHeight = terrainChunks[0].rightGrassHeight;
        terrainChunks[1].currentChunkLocation = 1;
        terrainChunks[1].chunkDirection = "right";
        generateTerrainChunkRight(1, terrainChunks[1].leftGrassHeight);

        terrainChunks[2] = new Chunk();
        terrainChunks[2].chunkNumber = -1;
        terrainChunks[2].status = "not generated";
        terrainChunks[2].rightGrassHeight = terrainChunks[0].leftGrassHeight;
        terrainChunks[2].currentChunkLocation = -1;
        terrainChunks[2].chunkDirection = "left";
        generateTerrainChunkLeft(2, terrainChunks[2].rightGrassHeight);
    }

    public void generateTerrainChunkRight(int chunkNumber, int y) {
        int x = 0;

        int direction = (int) (Math.random() * directions.length);
        int length = (int) (Math.random() * 10) + 2;

        while (x < gamePanel.maxChunkCol) {
            if (length == 0) {
                direction = (int) (Math.random() * directions.length);
                length = (int) (Math.random() * 10) + 2;
            } else {
                if (directions[direction].equals("up") && y + 1 < gamePanel.maxChunkRow) {
                    int upOrStraight = (int) (Math.random() * 2);
                    if (upOrStraight == 1) {
                        y += 1;
                    }
                }
                if (directions[direction].equals("down") && y - 1 > 0) {
                    int upOrStraight = (int) (Math.random() * 2);
                    if (upOrStraight == 1) {
                        y -= 1;
                    }
                }
                length -= 1;
            }
            terrainChunks[chunkNumber].blocks[x][y] = 1;
            x++;
        }
        terrainChunks[chunkNumber].rightGrassHeight = y;

        // Generate other terrain blocks
        generateGrassBlocks(chunkNumber);
        generateDirtBlocks(chunkNumber);
    }

    public void generateTerrainChunkLeft(int chunkNumber, int y) {
        int x = gamePanel.maxChunkCol - 1;

        int direction = (int) (Math.random() * directions.length);
        int length = (int) (Math.random() * 10) + 2;

        while (x > -1) {
            if (length == 0) {
                direction = (int) (Math.random() * directions.length);
                length = (int) (Math.random() * 10) + 2;
            } else {
                if (directions[direction].equals("up") && y + 1 < gamePanel.maxChunkRow) {
                    int upOrStraight = (int) (Math.random() * 2);
                    if (upOrStraight == 1) {
                        y += 1;
                    }
                }
                if (directions[direction].equals("down") && y - 1 > 0) {
                    int upOrStraight = (int) (Math.random() * 2);
                    if (upOrStraight == 1) {
                        y -= 1;
                    }
                }
                length -= 1;
            }
            terrainChunks[chunkNumber].blocks[x][y] = 1;
            x--;
        }
        terrainChunks[chunkNumber].leftGrassHeight = y;

        // Generate other terrain blocks
        generateGrassBlocks(chunkNumber);
        generateDirtBlocks(chunkNumber);
    }

    public void generateTerrain(String direction) {
        System.out.println("Generating terrain...");
        int[][] previousWorld;
        switch (direction) {
            case "right" -> {
                for (int i = 0; i < terrainChunks.length; i++) {
                    if (terrainChunks[i].currentChunkLocation == gamePanel.maxWorldChunks - 2) {
                        previousChunks = terrainChunks;
                        terrainChunks = new Chunk[terrainChunks.length + 1];
                        System.arraycopy(previousChunks, 0, terrainChunks, 0, previousChunks.length);

                        terrainChunks[terrainChunks.length - 1] = new Chunk();
                        terrainChunks[terrainChunks.length - 1].chunkNumber = gamePanel.maxWorldChunks - 1;
                        terrainChunks[terrainChunks.length - 1].status = "not generated";
                        int lastChunk = 0;
                        for (int j = 0; j < previousChunks.length; j++) {
                            if (Objects.equals(previousChunks[j].chunkDirection, "right")) {
                                lastChunk = j;
                            }
                        }
                        terrainChunks[terrainChunks.length - 1].leftGrassHeight = previousChunks[lastChunk].rightGrassHeight;
                        terrainChunks[terrainChunks.length - 1].currentChunkLocation = gamePanel.maxWorldChunks - 1;
                        terrainChunks[terrainChunks.length - 1].chunkDirection = "right";
                        generateTerrainChunkRight(terrainChunks.length - 1, terrainChunks[terrainChunks.length - 1].leftGrassHeight);

                        gamePanel.maxWorldChunks++;
                        gamePanel.maxWorldCol = gamePanel.maxWorldChunks * 16;
                        previousWorld = gameWorld;
                        gameWorld = new int[gamePanel.maxWorldCol][gamePanel.maxWorldRow];

                        int col = 0;
                        int row = 0;
                        while (col < previousWorld.length && row < previousWorld[col].length) {
                            gameWorld[col][row] = previousWorld[col][row];
                            col++;
                            if (col == previousWorld.length) {
                                col = 0;
                                row++;
                            }
                        }
                        col = 0;
                        row = 0;
                        while (col < terrainChunks[terrainChunks.length - 1].blocks.length && row < terrainChunks[terrainChunks.length - 1].blocks[col].length) {
                            gameWorld[col + (gamePanel.maxWorldChunks - 1) * 16][row] = terrainChunks[terrainChunks.length - 1].blocks[col][row];
                            col++;
                            if (col == terrainChunks[terrainChunks.length - 1].blocks.length) {
                                col = 0;
                                row++;
                            }
                        }

                        // Create a file to save this terrain chunk
                        try {
                            // Create terrain data file
                            FileWriter terrainFile = new FileWriter("src\\saves\\chunks\\chunk_" + (terrainChunks.length - 1) + ".txt");
                            BufferedWriter terrainWriter = new BufferedWriter(terrainFile);

                            int chunkNumber = terrainChunks[terrainChunks.length - 1].chunkNumber;
                            int rightGrassHeight = terrainChunks[terrainChunks.length - 1].rightGrassHeight;
                            int leftGrassHeight = terrainChunks[terrainChunks.length - 1].leftGrassHeight;
                            int[][] terrainBlocks = terrainChunks[terrainChunks.length - 1].blocks;

                            int x = 0;
                            int y = 0;
                            while (x < terrainBlocks.length && y < terrainBlocks[x].length) {
                                // Copy terrain data to the file
                                terrainWriter.write(terrainBlocks[x][y] + " ");

                                x++;
                                if (x == terrainBlocks.length) {
                                    x = 0;
                                    y++;
                                    terrainWriter.write("\n");
                                }
                            }

                            // Close the terrain data stream
                            terrainWriter.close();

                            // Create terrain info file
                            terrainFile = new FileWriter("src\\saves\\chunk_" + (terrainChunks.length - 1) + "_info.txt");
                            terrainWriter = new BufferedWriter(terrainFile);

                            terrainWriter.write("chunk: " + chunkNumber + "\n");
                            terrainWriter.write("rightGrassHeight: " + rightGrassHeight + "\n");
                            terrainWriter.write("leftGrassHeight: " + leftGrassHeight + "\n");
                            terrainWriter.write("status: " + terrainChunks[terrainChunks.length - 1].status + "\n");

                            // Close the info stream
                            terrainWriter.close();
                        } catch (Exception e) {
                            System.err.println("Error: " + e.getMessage());
                        }
                        return;
                    }
                }
            }
            case "left" -> {
                for (int i = 0; i < terrainChunks.length; i++) {
                    if (terrainChunks[i].currentChunkLocation == -1) {
                        previousChunks = terrainChunks;
                        terrainChunks = new Chunk[terrainChunks.length + 1];
                        for (int j = 0; j < previousChunks.length; j++) {
                            terrainChunks[j] = previousChunks[j];
                            terrainChunks[j].currentChunkLocation += 1;
                        }
                        gamePanel.player.worldX += gamePanel.tileSize * gamePanel.maxChunkCol;

                        terrainChunks[terrainChunks.length - 1] = new Chunk();
                        terrainChunks[terrainChunks.length - 1].chunkNumber = -1;
                        terrainChunks[terrainChunks.length - 1].status = "not generated";
                        int lastChunk = 0;
                        for (int j = 0; j < previousChunks.length; j++) {
                            if (Objects.equals(previousChunks[j].chunkDirection, "left")) {
                                lastChunk = j;
                            }
                        }
                        terrainChunks[terrainChunks.length - 1].rightGrassHeight = previousChunks[lastChunk].leftGrassHeight;
                        terrainChunks[terrainChunks.length - 1].currentChunkLocation = -1;
                        terrainChunks[terrainChunks.length - 1].chunkDirection = "left";
                        generateTerrainChunkLeft(terrainChunks.length - 1, terrainChunks[terrainChunks.length - 1].rightGrassHeight);

                        gamePanel.maxWorldChunks++;
                        gamePanel.maxWorldCol = gamePanel.maxWorldChunks * 16;
                        previousWorld = gameWorld;
                        gameWorld = new int[gamePanel.maxWorldCol][gamePanel.maxWorldRow];

                        int col = 0;
                        int row = 0;
                        while (col < terrainChunks[terrainChunks.length - 1].blocks.length && row < terrainChunks[terrainChunks.length - 1].blocks[col].length) {
                            gameWorld[col][row] = terrainChunks[terrainChunks.length - 1].blocks[col][row];
                            col++;
                            if (col == terrainChunks[terrainChunks.length - 1].blocks.length) {
                                col = 0;
                                row++;
                            }
                        }
                        col = 0;
                        row = 0;
                        while (col < previousWorld.length && row < previousWorld[col].length) {
                            gameWorld[col + 16][row] = previousWorld[col][row];
                            col++;
                            if (col == previousWorld.length) {
                                col = 0;
                                row++;
                            }
                        }

                        // Create a file to save this terrain chunk
                        try {
                            // Create terrain data file
                            FileWriter terrainFile = new FileWriter("src\\saves\\chunks\\chunk_" + (terrainChunks.length - 1) + ".txt");
                            BufferedWriter terrainWriter = new BufferedWriter(terrainFile);

                            int chunkNumber = terrainChunks[terrainChunks.length - 1].chunkNumber;
                            int rightGrassHeight = terrainChunks[terrainChunks.length - 1].rightGrassHeight;
                            int leftGrassHeight = terrainChunks[terrainChunks.length - 1].leftGrassHeight;
                            int[][] terrainBlocks = terrainChunks[terrainChunks.length - 1].blocks;

                            int x = 0;
                            int y = 0;
                            while (x < terrainBlocks.length && y < terrainBlocks[x].length) {
                                // Copy terrain data to the file
                                terrainWriter.write(terrainBlocks[x][y] + " ");

                                x++;
                                if (x == terrainBlocks.length) {
                                    x = 0;
                                    y++;
                                    terrainWriter.write("\n");
                                }
                            }

                            // Close the terrain data stream
                            terrainWriter.close();

                            // Create terrain info file
                            terrainFile = new FileWriter("src\\saves\\chunk_" + (terrainChunks.length - 1) + "_info.txt");
                            terrainWriter = new BufferedWriter(terrainFile);

                            terrainWriter.write("chunk: " + chunkNumber + "\n");
                            terrainWriter.write("rightGrassHeight: " + rightGrassHeight + "\n");
                            terrainWriter.write("leftGrassHeight: " + leftGrassHeight + "\n");
                            terrainWriter.write("status: " + terrainChunks[terrainChunks.length - 1].status + "\n");

                            // Close the info stream
                            terrainWriter.close();
                        } catch (Exception e) {
                            System.err.println("Error: " + e.getMessage());
                        }
                        return;
                    }
                }
            }
        }
    }

    public void generateGrassBlocks(int chunkNumber) {
        int col = 0;
        int row = 0;
        while (col < gamePanel.maxChunkCol && row < gamePanel.maxChunkRow) {

            if (terrainChunks[chunkNumber].blocks[col][row] == 1) {
                terrainChunks[chunkNumber].blocks[col][row - 1] = 2;
            }

            col++;
            if (col == gamePanel.maxChunkCol) {
                col = 0;
                row++;
            }
        }
    }

    public void generateDirtBlocks(int chunkNumber) {
        int col = 0;
        int row = 0;
        while (col < gamePanel.maxChunkCol && row < gamePanel.maxChunkRow) {

            if (terrainChunks[chunkNumber].blocks[col][row] == 2) {
                for (int i = 2; i < 5; i++) {
                    System.out.println(row + i);
                    terrainChunks[chunkNumber].blocks[col][row + i] = 1;
                }
            }

            col++;
            if (col == gamePanel.maxChunkCol) {
                col = 0;
                row++;
            }
        }
    }

    public void update() {
        if (keyHandler.resetWorld) {
            gameWorld = new int[gamePanel.maxWorldCol][gamePanel.maxWorldRow];
            generateWorld();
            keyHandler.resetWorld = false;
        }

        double playerX = gamePanel.player.worldX + 40;

        int playerBlockX = (int) playerX / gamePanel.tileSize;

        int playerChunk = playerBlockX / gamePanel.maxChunkCol - 1;

        if (playerChunk == -1) {
            generateTerrain("left");
        }
        if (playerChunk == gamePanel.maxWorldChunks - 2) {
            generateTerrain("right");
        }
    }

    public void draw(Graphics2D g2d) {
        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < gamePanel.maxWorldCol && worldRow < gamePanel.maxWorldRow) {

            int worldX = worldCol * gamePanel.tileSize;
            int worldY = worldRow * gamePanel.tileSize;
            double screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
            double screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;

            if (worldX + (gamePanel.tileSize * 2) > gamePanel.player.worldX - gamePanel.player.screenX &&
                    worldX - (gamePanel.tileSize * 4) < gamePanel.player.worldX + gamePanel.player.screenX &&
                    worldY + (gamePanel.tileSize * 2) > gamePanel.player.worldY - gamePanel.player.screenY &&
                    worldY - (gamePanel.tileSize * 5) < gamePanel.player.worldY + gamePanel.player.screenY) {
                g2d.drawImage(block[gameWorld[worldCol][worldRow]].image, (int) screenX, (int) screenY, gamePanel.tileSize, gamePanel.tileSize, null);
            }

            worldCol++;
            if (worldCol == gp.maxScreenCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
}

