package world;

import main.GamePanel;
import main.KeyHandler;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class WorldManager extends BlockManager {
    GamePanel gp;
    KeyHandler keyH;
    Chunk[] chunk;
    Chunk[] chunk2;

    String printMsg;

    int[][] world;
    int[][] world2;

    String[] directions = {"up", "down"};
    int lengthX;
    int lengthY;

    public WorldManager(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
        chunk = new Chunk[3];
        chunk2 = chunk;
        world = new int[gp.maxWorldCol][gp.maxWorldRow];

        getBlockSprite();
        generateWorld();

        try {
            for (int i = 0; i < chunk.length; i++) {
                // Create file
                FileWriter fstream = new FileWriter("src\\saves\\chunks\\chunk_" + i + ".txt");
                BufferedWriter out = new BufferedWriter(fstream);

                int chunkCount = chunk[i].chunk;
                int rightGrassY = chunk[i].rightGrassY;
                int leftGrassY = chunk[i].leftGrassY;
                int[][] blocks = chunk[i].blocks;

                int x = 0;
                int y = 0;
                while (x < blocks.length && y < blocks[x].length) {
                    // Copy it to the file
                    out.write(blocks[x][y] + " ");

                    // Copy it to the world variable
                    if (chunkCount == -1) {
                        world[x][y] = blocks[x][y];
                    }
                    if (chunkCount == 0) {
                        world[x + 16][y] = blocks[x][y];
                    }
                    if (chunkCount == 1) {
                        world[x + 32][y] = blocks[x][y];
                    }

                    x++;
                    if (x == blocks.length) {
                        x = 0;
                        y++;
                        out.write("\n");
                    }
                }

                // Close the output stream
                out.close();

                // Create file for chunk info
                fstream = new FileWriter("src\\saves\\chunk_" + i + "_info.txt");
                out = new BufferedWriter(fstream);

                out.write("chunk: " + chunkCount + "\n");
                out.write("rightGrassY: " + rightGrassY + "\n");
                out.write("leftGrassY: " + leftGrassY + "\n");
                out.write("status: " + chunk[i].status + "\n");

                // Close the output stream
                out.close();
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }


    }

    public void generateWorld() {

        chunk[0] = new Chunk();
        chunk[0].chunk = 0;
        chunk[0].status = "not generated";
        chunk[0].leftGrassY = 20;
        chunk[0].currentChunkLocation = 0;
        chunk[0].chunkDirection = "right";
        generateChunkRight(0, chunk[0].leftGrassY);

        chunk[1] = new Chunk();
        chunk[1].chunk = 1;
        chunk[1].status = "not generated";
        chunk[1].leftGrassY = chunk[0].rightGrassY;
        chunk[1].currentChunkLocation = 1;
        chunk[1].chunkDirection = "right";
        generateChunkRight(1, chunk[1].leftGrassY);

        chunk[2] = new Chunk();
        chunk[2].chunk = -1;
        chunk[2].status = "not generated";
        chunk[2].rightGrassY = chunk[0].leftGrassY;
        chunk[2].currentChunkLocation = -1;
        chunk[2].chunkDirection = "left";
        generateChunkLeft(2, chunk[2].rightGrassY);

    }
    public void generateChunkRight(int chunkCount, int y) {
        int x = 0;

        int direction = (int) (Math.random() * directions.length);
        int length = (int) (Math.random() * 10) + 2;

//        chunk[chunkCount].blocks[x][y] = 2;

        while (x < gp.maxChunkCol) {
            if (length == 0) {
                direction = (int) (Math.random() * directions.length);
                length = (int) (Math.random() * 10) + 2;
            }else {
                if (directions[direction].equals("up") && y + 1 < gp.maxChunkRow) {
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
            chunk[chunkCount].blocks[x][y] = 1;
            x++;
        }
        chunk[chunkCount].rightGrassY = y;

        // Generate other blocks
        generateGrassBlocks(chunkCount);
        generateDirtBlocks(chunkCount);
    }
    public void generateChunkLeft(int chunkCount, int y) {
        int x = gp.maxChunkCol - 1;

        int direction = (int) (Math.random() * directions.length);
        int length = (int) (Math.random() * 10) + 2;

//        chunk[chunkCount].blocks[x][y] = 2;

        while (x > -1) {
            if (length == 0) {
                direction = (int) (Math.random() * directions.length);
                length = (int) (Math.random() * 10) + 2;
            }else {
                if (directions[direction].equals("up") && y + 1 < gp.maxChunkRow) {
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
            chunk[chunkCount].blocks[x][y] = 1;
            x--;
        }
        chunk[chunkCount].leftGrassY = y;

        // Generate other blocks
        generateGrassBlocks(chunkCount);
        generateDirtBlocks(chunkCount);
    }


    public void generateChunk(String direction) {
        System.out.println("Generating chunk...");
        switch (direction) {
            case "right":
                for (int i = 0; i < chunk.length; i++) {
                    if (chunk[i].currentChunkLocation == gp.maxWorldChunks - 2) {
                        chunk2 = chunk;
                        chunk = new Chunk[chunk.length + 1];
                        for (int j = 0; j < chunk2.length; j++) {
                            chunk[j] = chunk2[j];
                        }

                        chunk[chunk.length - 1] = new Chunk();
                        chunk[chunk.length - 1].chunk = gp.maxWorldChunks - 1;
                        chunk[chunk.length - 1].status = "not generated";
                        int lastChunk = 0;
                        for (int j = 0; j < chunk2.length; j++) {
                            if (chunk2[j].chunkDirection == "right") {
                                lastChunk = j;
                            }
                        }
                        chunk[chunk.length - 1].leftGrassY = chunk2[lastChunk].rightGrassY;
                        chunk[chunk.length - 1].currentChunkLocation = gp.maxWorldChunks - 1;
                        chunk[chunk.length - 1].chunkDirection = "right";
                        generateChunkRight(chunk.length - 1, chunk[chunk.length - 1].leftGrassY);

                        gp.maxWorldChunks++;
                        gp.maxWorldCol = gp.maxWorldChunks * 16;
                        world2 = world;
                        world = new int[gp.maxWorldCol][gp.maxWorldRow];

                        int col = 0;
                        int row = 0;
                        while (col < world2.length && row < world2[col].length) {
                            world[col][row] = world2[col][row];
                            col++;
                            if (col == world2.length) {
                                col = 0;
                                row++;
                            }
                        }
                        col = 0;
                        row = 0;
                        while (col < chunk[chunk.length - 1].blocks.length && row < chunk[chunk.length - 1].blocks[col].length) {
                            world[col + (gp.maxWorldChunks - 1) * 16][row] = chunk[chunk.length - 1].blocks[col][row];
                            col++;
                            if (col == chunk[chunk.length - 1].blocks.length) {
                                col = 0;
                                row++;
                            }
                        }

                        // Create a file to save this chunk
                        try {
                            // Create file
                            FileWriter fstream = new FileWriter("src\\saves\\chunks\\chunk_" + (chunk.length - 1) + ".txt");
                            BufferedWriter out = new BufferedWriter(fstream);

                            int chunkCount = chunk[chunk.length - 1].chunk;
                            int rightGrassY = chunk[chunk.length - 1].rightGrassY;
                            int leftGrassY = chunk[chunk.length - 1].leftGrassY;
                            int[][] blocks = chunk[chunk.length - 1].blocks;


                            int x = 0;
                            int y = 0;
                            while (x < blocks.length && y < blocks[x].length) {
                                // Copy it to the file
                                out.write(blocks[x][y] + " ");



                                x++;
                                if (x == blocks.length) {
                                    x = 0;
                                    y++;
                                    out.write("\n");
                                }
                            }

                            // Close the output stream
                            out.close();

                            // Create file for chunk info
                            fstream = new FileWriter("src\\saves\\chunk_" + (chunk.length - 1) + "_info.txt");
                            out = new BufferedWriter(fstream);

                            out.write("chunk: " + chunkCount + "\n");
                            out.write("rightGrassY: " + rightGrassY + "\n");
                            out.write("leftGrassY: " + leftGrassY + "\n");
                            out.write("status: " + chunk[chunk.length - 1].status + "\n");

                            // Close the output stream
                            out.close();
                        } catch (Exception e) {
                            System.err.println("Error: " + e.getMessage());
                        }
                        return;
                    }
                }
                break;
            case "left":
                for (int i = 0; i < chunk.length; i++) {
                    if (chunk[i].currentChunkLocation == -1) {
                        chunk2 = chunk;
                        chunk = new Chunk[chunk.length + 1];
                        for (int j = 0; j < chunk2.length; j++) {
                            chunk[j] = chunk2[j];
                            chunk[j].currentChunkLocation += 1;
                        }
                        gp.player.worldX += gp.tileSize * gp.maxChunkCol;

                        chunk[chunk.length - 1] = new Chunk();
                        chunk[chunk.length - 1].chunk = -1;
                        chunk[chunk.length - 1].status = "not generated";
                        int lastChunk = 0;
                        for (int j = 0; j < chunk2.length; j++) {
                            if (chunk2[j].chunkDirection == "left") {
                                lastChunk = j;
                            }
                        }
                        chunk[chunk.length - 1].rightGrassY = chunk2[lastChunk].leftGrassY;
                        chunk[chunk.length - 1].currentChunkLocation = -1;
                        chunk[chunk.length - 1].chunkDirection = "left";
                        generateChunkLeft(chunk.length - 1, chunk[chunk.length - 1].rightGrassY);

                        gp.maxWorldChunks++;
                        gp.maxWorldCol = gp.maxWorldChunks * 16;
                        world2 = world;
                        world = new int[gp.maxWorldCol][gp.maxWorldRow];

                        int col = 0;
                        int row = 0;
                        while (col < chunk[chunk.length - 1].blocks.length && row < chunk[chunk.length - 1].blocks[col].length) {
                            world[col][row] = chunk[chunk.length - 1].blocks[col][row];
                            col++;
                            if (col == chunk[chunk.length - 1].blocks.length) {
                                col = 0;
                                row++;
                            }
                        }
                        col = 0;
                        row = 0;
                        while (col < world2.length && row < world2[col].length) {
                            world[col + 16][row] = world2[col][row];
                            col++;
                            if (col == world2.length) {
                                col = 0;
                                row++;
                            }
                        }

                        // Create a file to save this chunk
                        try {
                            // Create file
                            FileWriter fstream = new FileWriter("src\\saves\\chunks\\chunk_" + (chunk.length - 1) + ".txt");
                            BufferedWriter out = new BufferedWriter(fstream);

                            int chunkCount = chunk[chunk.length - 1].chunk;
                            int rightGrassY = chunk[chunk.length - 1].rightGrassY;
                            int leftGrassY = chunk[chunk.length - 1].leftGrassY;
                            int[][] blocks = chunk[chunk.length - 1].blocks;


                            int x = 0;
                            int y = 0;
                            while (x < blocks.length && y < blocks[x].length) {
                                // Copy it to the file
                                out.write(blocks[x][y] + " ");



                                x++;
                                if (x == blocks.length) {
                                    x = 0;
                                    y++;
                                    out.write("\n");
                                }
                            }

                            // Close the output stream
                            out.close();

                            // Create file for chunk info
                            fstream = new FileWriter("src\\saves\\chunk_" + (chunk.length - 1) + "_info.txt");
                            out = new BufferedWriter(fstream);

                            out.write("chunk: " + chunkCount + "\n");
                            out.write("rightGrassY: " + rightGrassY + "\n");
                            out.write("leftGrassY: " + leftGrassY + "\n");
                            out.write("status: " + chunk[chunk.length - 1].status + "\n");

                            // Close the output stream
                            out.close();
                        } catch (Exception e) {
                            System.err.println("Error: " + e.getMessage());
                        }
                        return;
                    }
                }
                break;
        }
    }

    public void generateGrassBlocks(int chunkCount) {
        int col = 0;
        int row = 0;
        while (col < gp.maxChunkCol && row < gp.maxChunkRow) {

            if (chunk[chunkCount].blocks[col][row] == 1) {
                chunk[chunkCount].blocks[col][row - 1] = 2;
            }

            col++;
            if (col == gp.maxChunkCol) {
                col = 0;
                row++;
            }
        }
    }
    public void generateDirtBlocks(int chunkCount) {
        int col = 0;
        int row = 0;
        while (col < gp.maxChunkCol && row < gp.maxChunkRow) {

            if (chunk[chunkCount].blocks[col][row] == 2) {
                for (int i = 2; i < 5; i++) {
                    System.out.println(row + i);
                    chunk[chunkCount].blocks[col][row + i] = 1;
                }
            }

            col++;
            if (col == gp.maxChunkCol) {
                col = 0;
                row++;
            }
        }
    }


    public void update() {
        if (keyH.resetWorld) {
            world = new int[gp.maxWorldCol][gp.maxWorldRow];
            generateWorld();
            keyH.resetWorld = false;
        }

        double playerX = gp.player.worldX + 40;
        double playerY = gp.player.worldY + 120;
//        System.out.println(playerX + ", " + playerY);

        int playerBlockX = (int) playerX / gp.tileSize;
//        double playerBlockY = playerY / gp.tileSize + 2.5;
//        System.out.println(playerBlockX);

        int playerChunk = playerBlockX / gp.maxChunkCol - 1;
//        System.out.println(playerChunk);

        if (playerChunk == -1) {
            generateChunk("left");
        }
        if (playerChunk == gp.maxWorldChunks - 2) {
            generateChunk("right");
        }
    }
    public void draw(Graphics2D g2d) {

        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            double screenX = worldX - gp.player.worldX + gp.player.screenX;
            double screenY = worldY - gp.player.worldY + gp.player.screenY;

            if (worldX + (gp.tileSize * 2) > gp.player.worldX - gp.player.screenX &&
                worldX - (gp.tileSize * 4) < gp.player.worldX + gp.player.screenX &&
                worldY + (gp.tileSize * 2) > gp.player.worldY - gp.player.screenY &&
                worldY - (gp.tileSize * 5) < gp.player.worldY + gp.player.screenY) {
                g2d.drawImage(block[world[worldCol][worldRow]].image, (int)screenX, (int)screenY, gp.tileSize, gp.tileSize, null);
            }

            worldCol++;
            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
}
