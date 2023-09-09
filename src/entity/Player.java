package entity;

import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Player extends Entity {

    public GamePanel gamePanel;
    public  KeyHandler keyHandler;

    public final int screenX;
    public int screenY;

    public Player(GamePanel gamePanel, KeyHandler keyHandler) {
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;

        screenX = gamePanel.screenWidth / 2 - 41;
        screenY = 432 / 2 - 50;

        setDefaults();
        loadPlayerSprite();
    }

    private void setDefaults() {
        worldX = 0 * gamePanel.tileSize;
        worldY = 0 * gamePanel.tileSize;
//        speed = 4;
        speed = gamePanel.worldWidth/ 600;
    }

    private void loadPlayerSprite() {
        InputStream inputStream = getClass().getResourceAsStream("/minecraft2d/textures/entity/player/steve.png");
        try {
            sprite = ImageIO.read(inputStream);
            loadAnimationFrames();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadAnimationFrames() {
        animations = new BufferedImage[4][6];
        for (int row = 0; row < animations.length; row++) {
            for (int col = 0; col < animations[row].length; col++) {
                animations[row][col] = sprite.getSubimage(col * 32, row * 64, 32, 64);
            }
        }
    }

    private void updateAnimationTick() {
        switch (animationCode) {
            case 0 -> maxAnimationIndex = 5;
            case 1 -> maxAnimationIndex = 6;
            case 2 -> maxAnimationIndex = 5;
            case 3 -> maxAnimationIndex = 6;
        }
        animationTick++;
        if (animationTick >= animationSpeed) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= maxAnimationIndex) {
                animationIndex = 0;
            }
        }
    }

    public void srollScreen(int i) {

    }

    public void update() {
        updateAnimationTick();
        directionX = "";
        directionY = "";

        if ("idle_left".equals(currentAction)) {
            animationCode = 0;
        }
        if ("move_left".equals(currentAction)) {
            animationCode = 1;
        }
        if ("idle_right".equals(currentAction)) {
            animationCode = 2;
        }
        if ("move_right".equals(currentAction)) {
            animationCode = 3;
        }

        if (keyHandler.upPressed) {
            directionY = "up";
        }
        //////
        if (keyHandler.downPressed) {
            worldY += speed;
        }
        //////
        if (!"up".equals(directionY)) {
            directionY = "down";
        }
        if (keyHandler.leftPressed) {
            currentAction = "move_right";
            mainAction = "right";
            directionX = "left";
        }
        if (keyHandler.rightPressed) {
            currentAction = "move_left";
            mainAction = "left";
            directionX = "right";
        }

        if (!keyHandler.leftPressed && !keyHandler.rightPressed && "right".equals(mainAction)) {
            currentAction = "idle_right";
        }
        if (!keyHandler.leftPressed && !keyHandler.rightPressed && "left".equals(mainAction)) {
            currentAction = "idle_left";
        }

        switch (directionX) {
            case "left": worldX -= speed; break;
            case "right": worldX += speed; break;
        }
        switch (directionY) {
            case "up": worldY -= speed; break;
//            case "down": worldY += speed; break;
        }
    }

    public void draw(Graphics2D g2d) {
        g2d.drawImage(animations[animationCode][animationIndex], screenX, screenY, (int) ((int) gamePanel.tileSize * 1.73), (int) ((int)gamePanel.tileSize * 3.46), null);
    }
}
