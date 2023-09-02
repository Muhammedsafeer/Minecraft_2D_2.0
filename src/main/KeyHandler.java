package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gp;

    public boolean upPressed, downPressed, leftPressed, rightPressed;
    public boolean resetWorld;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) {
//            System.out.println("W pressed");
            upPressed = true;
        }
        if (code == KeyEvent.VK_S) {
//            System.out.println("S pressed");
            downPressed = true;
        }
        if (code == KeyEvent.VK_A) {
//            System.out.println("A pressed");
            leftPressed = true;
        }
        if (code == KeyEvent.VK_D) {
//            System.out.println("D pressed");
            rightPressed = true;
        }

        // ZOOM IN AND OUT
        if (code == KeyEvent.VK_EQUALS) {
            gp.zoomInOut(1);
        }
        if (code == KeyEvent.VK_MINUS) {
            gp.zoomInOut(-1);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) {
//            System.out.println("W released");
            upPressed = false;
        }
        if (code == KeyEvent.VK_S) {
//            System.out.println("S released");
            downPressed = false;
        }
        if (code == KeyEvent.VK_A) {
//            System.out.println("A released");
            leftPressed = false;
        }
        if (code == KeyEvent.VK_D) {
//            System.out.println("D released");
            rightPressed = false;
        }

        // Testing
        if (code == KeyEvent.VK_R) {
            resetWorld = true;
        }
    }
}
