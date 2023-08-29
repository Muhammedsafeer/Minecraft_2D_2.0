package main;

public class GameLoop implements Runnable {

    GamePanel gp;

    Thread gameThread;

    public GameLoop(GamePanel gp) {
        this.gp = gp;
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

    }
}
