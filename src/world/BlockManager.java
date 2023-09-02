package world;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class BlockManager {
    GamePanel gp;
    Block[] block;


    public void getBlockSprite() {

        block = new Block[3];

        try {

            block[0] = new Block();
            block[0].image = ImageIO.read(getClass().getResourceAsStream("/minecraft2d/textures/block/air.png"));

            block[1] = new Block();
            block[1].image = ImageIO.read(getClass().getResourceAsStream("/minecraft2d/textures/block/dirt.png"));

            block[2] = new Block();
            block[2].image = ImageIO.read(getClass().getResourceAsStream("/minecraft2d/textures/block/grass_block.png"));


        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}
