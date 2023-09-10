package world;

import main.GamePanel;

public class Chunk {
    GamePanel gp;

    int chunkNumber;
    String status;
    int[][] blocks = new int[16][256];
    int rightGrassHeight;
    int leftGrassHeight;
    int currentChunkLocation;
    String chunkDirection;
}
