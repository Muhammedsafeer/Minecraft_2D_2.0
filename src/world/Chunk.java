package world;

import main.GamePanel;

public class Chunk {
    GamePanel gp;

    int chunk;
    String status;
    int[][] blocks = new int[16][256];
    int rightGrassY;
    int leftGrassY;
    int currentChunkLocation;
    String chunkDirection;
}
