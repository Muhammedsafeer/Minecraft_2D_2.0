package entity;

import java.awt.image.BufferedImage;

public class Entity {

    public double worldX, worldY;
    public double speed;
    public BufferedImage sprite;
    public BufferedImage[][] animations;
    public String direction;
    public String currentAction = "idle_left";
    public String mainAction = "idle_left";
    public String directionX = "right";
    public String directionY = "down";
    public int animationTick, animationIndex, animationSpeed = 30, animationCode;
    public int maxAnimationIndex;

    // Constructor
    public Entity() {
        // Initialize default values here if needed
    }
}
